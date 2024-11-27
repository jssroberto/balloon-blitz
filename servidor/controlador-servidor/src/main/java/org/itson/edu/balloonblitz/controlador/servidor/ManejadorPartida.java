package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoPartida;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.*;
import org.itson.edu.balloonblitz.modelo.servidor.ControladorStreams;
import org.itson.edu.balloonblitz.modelo.servidor.EventoObserver;
import org.itson.edu.balloonblitz.modelo.servidor.Servidor;
import org.tinylog.Logger;

import java.util.Map;

/**
 * Clase que representa el manejador de la partida
 *
 * @author elimo
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ManejadorPartida implements EventoObserver {

    private final ControladorStreams streamsJugador1; // Streams del jugador 1.
    private final ControladorStreams streamsJugador2; // Streams del jugador 2.
    private final Jugador jugador1; // Instancia del jugador 1.
    private final Jugador jugador2; // Instancia del jugador 2.
    private Tablero tableroJugador1; // Tablero del jugador 1.
    private Tablero tableroJugador2; // Tablero del jugador 2.
    private Tablero tableroRival; // Tablero del jugador 2.
    private final ManejadorTurno turno; // Gestor de turnos.
    private final Partida partida; // Representación de la partida.
    int contadorEnvio = 0;
    private final ExecutorService executorService = Executors.newFixedThreadPool(500);
    private ScheduledExecutorService temporizadorActual = null; // Referencia al temporizador activo.
    private final AtomicBoolean detener = new AtomicBoolean(false);

    /**
     * Constructor para inicializar la partida con los jugadores y sus streams.
     *
     *
     * public int iniciarTemporizador(int tiempo) { if(tiempo>0){
     * turno.iniciarTemporizador(tiempo); }else{ return 0; } return 0; }
     *
     * /**
     * Constructor para inicializar la partida con los jugadores y sus streams.
     *
     * @param jugadores Mapa que asocia streams con jugadores.
     */
    public ManejadorPartida(Map<ControladorStreams, Jugador> jugadores) {
        this.partida = new Partida();
        this.turno = new ManejadorTurno();
        this.partida.setTableroJugador1(new Tablero());
        this.partida.setTableroJugador2(new Tablero());
        // Asignar jugadores y sus streams.
        ControladorStreams[] streamsArray = jugadores.keySet().toArray(new ControladorStreams[0]);
        Jugador[] jugadoresArray = jugadores.values().toArray(new Jugador[0]);

        //aquí hay declacraciones repetidas
        this.streamsJugador1 = streamsArray[0];
        this.streamsJugador2 = streamsArray[1];
        this.partida.setJugador1(jugadoresArray[0]);
        this.partida.setJugador2(jugadoresArray[1]);
        this.jugador1 = partida.getJugador1();
        this.jugador2 = partida.getJugador2();
        this.tableroJugador1 = partida.getTableroJugador1();
        this.tableroJugador2 = partida.getTableroJugador2();

        verificarYCrearPartida();
    }

    /**
     * Maneja los eventos enviados por los clientes.
     *
     * @param evento Evento enviado por el cliente.
     * @param entrada Stream de entrada del cliente.
     */
    @Override
    public void manejarEvento(Evento evento, ObjectInputStream entrada) {
        Evento eventoProcesado = procesarEvento(evento, entrada);

        if (eventoProcesado != null) {

            enviarEventoAJugador(streamsJugador1.getSalida(), eventoProcesado);
            enviarEventoAJugador(streamsJugador2.getSalida(), eventoProcesado);
            mandarTurno();
            manejarTurnos();
        }
    }

    /**
     * Enviar un evento a un jugador específico.
     *
     * @param salida Stream de salida del jugador.
     * @param evento Evento a enviar.
     */
    public void enviarEventoAJugador(ObjectOutputStream salida, Evento evento) {
        if (evento != null) {
            Servidor.getInstance().mandarDatosCliente(salida, evento);
        }
    }

    /**
     * Verifica si la partida está lista y la inicializa si los jugadores están
     * configurados.
     */
    private void verificarYCrearPartida() {
        synchronized (this) {
            if (jugador1 != null && jugador2 != null) {
                partida.setJugador1(jugador1);
                partida.setJugador2(jugador2);
                enviarEventoAJugador(streamsJugador1.getSalida(), new ResultadoEvento(true));
                enviarEventoAJugador(streamsJugador2.getSalida(), new ResultadoEvento(true));
                crearPartida();
            }
        }
    }

    /**
     * Inicializa los recursos de la partida.
     */
    public void crearPartida() {
        partida.setTableroJugador1(new Tablero());
        partida.setTableroJugador2(new Tablero());
        partida.setEstadoPartida(EstadoPartida.ACTIVA);
        jugador1.setTurno(true);
        jugador2.setTurno(true);
    }

    /**
     * Procesa un evento recibido y lo asocia con el jugador correspondiente.
     *
     * @param evento Evento recibido.
     * @param entrada Stream del jugador emisor.
     * @return Evento procesado o null si hay un error.
     */
    public Evento procesarEvento(Evento evento, ObjectInputStream entrada) {
        Jugador emisor = obtenerEmisor(entrada);
        evento.setEmisor(emisor);
        TipoEvento tipoEvento = evento.getTipoEvento();

        if (tipoEvento == TipoEvento.POSICION_NAVES) {
            mandarJugador(entrada);
            PosicionNavesEvento posicion = (PosicionNavesEvento) evento;
            if (emisor.equals(jugador1)) {
                tableroJugador1 = posicion.getTablero();
            } else {
                tableroJugador2 = posicion.getTablero();
            }
            contadorEnvio++;
            if (!detener.get()) {
                mandadoPorDosJugadores();
            }
            if (contadorEnvio == 2) {
                manejarTurnos();
            }
            return null;
        } else if (tipoEvento == TipoEvento.DISPARO) {
            detenerTemporizadorActivo();
            tableroRival = obtenerTableroRival(emisor);
            Jugador jugadorRival = obtenerJugadorRival(emisor);
            //Error de referencia, la primera entra porque no ha sido modificado
            ManejadorDisparo manejadorDisparo = new ManejadorDisparo((DisparoEvento) evento, tableroRival, jugadorRival);
            ResultadoDisparoEvento resultado = manejadorDisparo.procesar();
            resultado.setEmisor(emisor);
            return resultado;
        } else if (tipoEvento == TipoEvento.RESULTADO) {
            contadorEnvio++;
            if (contadorEnvio == 2) {
                manejarTurnos();
                contadorEnvio=0;
            }
            return null;
        } else {
            Logger.error("No se reconoció el tipo de evento");
            return null;
        }
    }

    public void mandarJugador(ObjectInputStream entrada) {
        if (obtenerEmisor(entrada).equals(jugador1)) {
            EnvioJugadorEvento evento2 = new EnvioJugadorEvento();
            evento2.setEmisor(jugador2);
            enviarEventoAJugador(streamsJugador1.getSalida(), evento2);
        } else {
            EnvioJugadorEvento evento1 = new EnvioJugadorEvento();
            evento1.setEmisor(jugador1);
            enviarEventoAJugador(streamsJugador2.getSalida(), evento1);
        }
    }

    public void mandadoPorDosJugadores() {
        if (contadorEnvio == 2) {
            detenerTemporizadorActivo();
            mandarTurno();
            manejarTurnos();
            contadorEnvio = 0;
        }
    }

    private int iniciarTemporizadorActivo(int segundos) {
        detenerTemporizadorActivo(); // Asegúrate de limpiar temporizadores anteriores.

        temporizadorActual = Executors.newSingleThreadScheduledExecutor();
        detener.set(false); // Reinicia la señal de detención.

        try {
            // Inicia el temporizador.
            temporizadorActual.schedule(() -> {
                if (!detener.get()) {
                    System.out.println("El tiempo ha expirado.");
                    enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(0));
                    enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(0));
                }
            }, segundos, TimeUnit.SECONDS);

            return 0; // Devuelve 0 si todo está correcto.
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Devuelve -1 si ocurre algún error.
        }
    }

    private void detenerTemporizadorActivo() {
        detener.set(true); // Señaliza que el temporizador debe detenerse.
        if (temporizadorActual != null && !temporizadorActual.isShutdown()) {
            temporizadorActual.shutdownNow(); // Detiene inmediatamente el temporizador.
            temporizadorActual = null;
        }
        System.out.println("El temporizador se ha detenido.");
    }

    /**
     * Maneja los turnos de los jugadores.
     */
    private synchronized void manejarTurnos() {
        if (jugador1.isTurno() && !jugador2.isTurno()) {
            manejarTurnoJugador1();
        } else if (jugador2.isTurno() && !jugador1.isTurno()) {
            manejarTurnoJugador2();
        } else if (jugador2.isTurno() && jugador1.isTurno()) {
            manejarTurnoJugador();
        }
    }

    /**
     * Maneja el turno del jugador actual.
     *
     */
    private void manejarTurnoJugador1() {
        enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
        enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(30));
        jugador1.setTurno(false);
        jugador2.setTurno(true);
        iniciarTemporizadorActivo(30);

    }

    /**
     * Maneja el turno del jugador actual.
     */
    private void manejarTurnoJugador2() {
        enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(30));
        enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
        jugador1.setTurno(true);
        jugador2.setTurno(false);
        iniciarTemporizadorActivo(30);
    }

    /**
     * Maneja el tiempo para posicionar naves cuando todavia no se han
     * establecido el turno de disparo
     */
    private void manejarTurnoJugador() {
        enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(10));
        enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(10));
        partida.getJugador2().setTurno(false);
        partida.getJugador1().setTurno(true);
        iniciarTemporizadorActivo(10);
    }

    /**
     * Obtiene el tablero del jugador rival.
     *
     * @param jugador Jugador actual.
     * @return Tablero del jugador rival.
     */
    private Tablero obtenerTableroRival(Jugador jugador) {
        return jugador.equals(jugador1) ? tableroJugador2 : tableroJugador1;
    }

    /**
     * Obtiene al jugador rival.
     *
     * @param jugador Jugador actual.
     * @return Instancia del jugador rival.
     */
    private Jugador obtenerJugadorRival(Jugador jugador) {
        return jugador.equals(jugador1) ? partida.getJugador2() : partida.getJugador1();
    }

    /**
     * Identifica al jugador que emitió el evento.
     *
     * @param entrada Stream de entrada del cliente.
     * @return Instancia del jugador emisor o null si no se encuentra.
     */
    private Jugador obtenerEmisor(ObjectInputStream entrada) {
        if (entrada.equals(streamsJugador1.getEntrada())) {
            return jugador1;
        } else if (entrada.equals(streamsJugador2.getEntrada())) {
            return jugador2;
        }
        return null;
    }

    public void mandarTurno() {
        if (jugador1.isTurno()) {
            enviarEventoAJugador(streamsJugador1.getSalida(), new ResultadoEvento(false));
            enviarEventoAJugador(streamsJugador2.getSalida(), new ResultadoEvento(true));

        } else {
            enviarEventoAJugador(streamsJugador1.getSalida(), new ResultadoEvento(true));
            enviarEventoAJugador(streamsJugador2.getSalida(), new ResultadoEvento(false));
        }
    }
}

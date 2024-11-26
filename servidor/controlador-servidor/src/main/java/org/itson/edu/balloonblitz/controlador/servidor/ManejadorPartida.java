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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

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
    boolean activo;
    int contadorJugada = 1;

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

        this.streamsJugador1 = streamsArray[0];
        this.streamsJugador2 = streamsArray[1];
        partida.setJugador1(jugadoresArray[0]);
        partida.setJugador2(jugadoresArray[1]);
        this.jugador1 = partida.getJugador1();
        this.jugador2 = jugadoresArray[1];
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
        if (emisor == null) {
            Logger.error("No se pudo identificar el emisor del evento");
            return null;
        }

        evento.setEmisor(emisor);
        TipoEvento tipoEvento = evento.getTipoEvento();

        if (tipoEvento == TipoEvento.POSICION_NAVES) {
            PosicionNavesEvento posicion = (PosicionNavesEvento) evento;
            if (entrada.equals(streamsJugador1.getEntrada())) {
                tableroJugador1 = posicion.getTablero();
            } else if (entrada.equals(streamsJugador2.getEntrada())) {
                tableroJugador2 = posicion.getTablero();
            }

            contadorEnvio++;
            if (contadorEnvio == 2) {
                detenerTemporizadorActivo();

                mandarTurno();
                manejarTurnos();
                contadorEnvio = 0;
            }
            return null;

        } else if (tipoEvento == TipoEvento.DISPARO) {
            detenerTemporizadorActivo();
            tableroRival = obtenerTableroRival(emisor);
            Jugador jugadorRival = obtenerJugadorRival(emisor);
            ManejadorDisparo manejadorDisparo = new ManejadorDisparo((DisparoEvento) evento, tableroRival, jugadorRival);
            contadorJugada++;
            return manejadorDisparo.procesar();

        } else if (tipoEvento == TipoEvento.RESULTADO) {
            contadorEnvio++;
            if (contadorEnvio == 2) {
                detenerTemporizadorActivo();
                mandarTurno();
                manejarTurnos();
                contadorEnvio = 0;
            }
            return null;
        } else {
            Logger.error("No se reconoció el tipo de evento");
            return null;
        }
    }

    private int iniciarTemporizadorActivo(int segundos) {
        detenerTemporizadorActivo(); // Asegúrate de limpiar temporizadores anteriores.
        temporizadorActual = Executors.newSingleThreadScheduledExecutor();
        try {
            activo = true;
            CountDownLatch latch = new CountDownLatch(1);
            temporizadorActual.schedule(() -> {
                System.out.println("El tiempo ha expirado.");
                latch.countDown(); // Libera el hilo principal
            }, segundos, TimeUnit.SECONDS);

            // Espera a que el latch se libere o el tiempo expire
            latch.await(); // Bloquea hasta que el tiempo expira
            activo = false;// Devuelve 0 cuando el tiempo expira
            return 0;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Manejo adecuado de interrupciones
            return -1; // Devuelve -1 si ocurre una interrupción
        } finally {
            temporizadorActual.shutdown(); // Detenemos el scheduler
        }

    }

    /**
     * Detiene cualquier temporizador activo.
     */
    private void detenerTemporizadorActivo() {
        activo = true;
        if (temporizadorActual != null && !temporizadorActual.isShutdown()) {
            temporizadorActual.shutdownNow();
            temporizadorActual = null;
            enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(0));
            enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(0));
        }
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
            System.out.println("los dos tienen turno");
            manejarTurnoJugador();
        }
    }

    /**
     * Maneja el turno del jugador actual.
     *
     * @param streamsJugador Streams del jugador actual.
     * @param jugadorActual Jugador actual.
     * @param jugadorSiguiente Jugador siguiente.
     */
    private void manejarTurnoJugador1() {
        executorService.submit(() -> {
            activo = true;
            EnvioJugadorEvento evento = new EnvioJugadorEvento();
            evento.setEmisor(jugador1);
            EnvioJugadorEvento evento1 = new EnvioJugadorEvento();
            evento1.setEmisor(jugador2);
            enviarEventoAJugador(streamsJugador1.getSalida(), evento1);
            enviarEventoAJugador(streamsJugador2.getSalida(), evento);
            enviarEventoAJugador(streamsJugador1.getSalida(), new ResultadoEvento(true));
            enviarEventoAJugador(streamsJugador2.getSalida(), new ResultadoEvento(false));
            enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
            enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(30));
            jugador1.setTurno(false);
            jugador2.setTurno(true);
            if (iniciarTemporizadorActivo(30) == 0) {
                activo = false;
                enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(0));
                enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(0));
                mandarTurno();
            }
        });

    }

    /**
     * Maneja el turno del jugador actual.
     *
     * @param streamsJugador Streams del jugador actual.
     * @param jugadorActual Jugador actual.
     * @param jugadorSiguiente Jugador siguiente.
     */
    private void manejarTurnoJugador2() {
        executorService.submit(() -> {
            activo = true;
            enviarEventoAJugador(streamsJugador2.getSalida(), new ResultadoEvento(true));
            enviarEventoAJugador(streamsJugador1.getSalida(), new ResultadoEvento(false));
            enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(30));
            enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
            jugador1.setTurno(true);
            jugador2.setTurno(false);
            if (iniciarTemporizadorActivo(30) == 0) {
                activo = false;
                enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(0));
                enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(0));
                mandarTurno();
            }
        });
    }

    /**
     * Maneja el tiempo para posicionar naves cuando todavia no se han
     * establecido el turno de disparo
     */
    private void manejarTurnoJugador() {
        executorService.submit(() -> {
            activo = true;
            enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(2));
            System.out.println("enviado jugador 1");
            enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(2));
            System.out.println("enviado jugador 2");
            partida.getJugador2().setTurno(false);
            partida.getJugador1().setTurno(true);
            if (iniciarTemporizadorActivo(2) == 0) {
                activo = false;
                enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(0));
                enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(0));
                mandarTurno();
            }
        });
    }

    /**
     * Obtiene el tablero del jugador rival.
     *
     * @param jugador Jugador actual.
     * @return Tablero del jugador rival.
     */
    private Tablero obtenerTableroRival(Jugador jugador) {
        return jugador.equals(jugador1) ? partida.getTableroJugador2() : partida.getTableroJugador1();
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

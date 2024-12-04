package org.itson.edu.balloonblitz.modelo.servidor;

import org.itson.edu.balloonblitz.conexion.servidor.ControladorStreams;
import org.itson.edu.balloonblitz.conexion.servidor.EventoObserver;
import org.itson.edu.balloonblitz.conexion.servidor.Servidor;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoNave;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoPartida;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.*;
import org.tinylog.Logger;

import java.util.Map;

/**
 * Clase que representa el manejador de la partida
 *
 * @author elimo
 */
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.itson.edu.balloonblitz.entidades.Coordenada;

public class ManejadorPartida implements EventoObserver {

    private final ControladorStreams streamsJugador1; // Streams del jugador 1.
    private final ControladorStreams streamsJugador2; // Streams del jugador 2.
    private final Partida partida; // Representación de la partida.
    private int contadorEnvio;
    private ScheduledExecutorService temporizadorActual; // Referencia al temporizador activo.
    private final AtomicBoolean detener;
    private int contadorNavesHundidasJugador2 = 0;
    private int contadorNavesHundidasJugador1 = 0;
    private boolean victoria;

    /**
     * Constructor para inicializar la partida con los jugadores y sus streams.
     *
     * @param jugadores Mapa que asocia streams con jugadores.
     */
    public ManejadorPartida(Map<ControladorStreams, Jugador> jugadores) {
        this.partida = new Partida();
        // Asignar jugadores y sus streams.
        ControladorStreams[] streamsArray = jugadores.keySet().toArray(new ControladorStreams[0]);
        Jugador[] jugadoresArray = jugadores.values().toArray(new Jugador[0]);

        //aquí hay declacraciones repetidas
        this.streamsJugador1 = streamsArray[0];
        this.streamsJugador2 = streamsArray[1];

        this.partida.setJugador1(jugadoresArray[0]);
        this.partida.setJugador2(jugadoresArray[1]);

        detener = new AtomicBoolean(false);
        contadorEnvio = 0;
        verificarYCrearPartida();
    }

    /**
     * Maneja los eventos enviados por los clientes.
     *
     * @param evento  Evento enviado por el cliente.
     * @param entrada Stream de entrada del cliente.
     */
    @Override
    public void manejarEvento(Evento evento, ObjectInputStream entrada) {
        Evento eventoProcesado = procesarEvento(evento, entrada);

        if (eventoProcesado != null) {
            enviarEventoAJugador(streamsJugador1.getSalida(), eventoProcesado);
            enviarEventoAJugador(streamsJugador2.getSalida(), eventoProcesado);
            if (!victoria) {
                manejarTurnos();
                mandarTurno();
            }
        } else if (evento.getTipoEvento() == TipoEvento.DESCONEXION) {
            manejarDesconexion(obtenerControlador(entrada));
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
            if (partida.getJugador1() != null && partida.getJugador2() != null) {
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
        partida.setEstadoPartida(EstadoPartida.ACTIVA);
        partida.getJugador1().setTurno(true);
        partida.getJugador2().setTurno(true);
    }

    /**
     * Procesa un evento recibido y lo asocia con el jugador correspondiente.
     *
     * @param evento  Evento recibido.
     * @param entrada Stream del jugador emisor.
     * @return Evento procesado o null si hay un error.
     */
    public Evento procesarEvento(Evento evento, ObjectInputStream entrada) {
        Jugador emisor = obtenerEmisor(entrada);
        evento.setEmisor(emisor);
        TipoEvento tipoEvento = evento.getTipoEvento();

        return switch (tipoEvento) {
            case POSICION_NAVES -> procesarPosicionNaves((PosicionNavesEvento) evento, emisor);
            case DISPARO -> procesarDisparo((DisparoEvento) evento, emisor);
            case RESULTADO -> procesarResultado();
            case DESCONEXION -> null;
            default -> {
                Logger.error("Evento no soportado: {}", tipoEvento);
                yield null;
            }
        };
    }

    private Evento procesarResultado() {
        contadorEnvio++;
        if (contadorEnvio == 2) {
            manejarTurnos();
            contadorEnvio = 0;
        }
        return null;
    }

    private ResultadoDisparoEvento procesarDisparo(DisparoEvento evento, Jugador emisor) {
        if (evento.getCoordenada().columna() == -1) {
            contadorEnvio++;
            if (contadorEnvio == 2) {
                cambiarTurnos();
                manejarTurnos();
                mandarTurno();
                contadorEnvio = 0;
            }
            return null;
        } else {
            detenerTemporizadorActivo();
            Tablero tableroRival = obtenerTableroRival(emisor);
            Jugador jugadorRival = obtenerJugadorRival(emisor);
            ManejadorDisparo manejadorDisparo = new ManejadorDisparo(evento, tableroRival, jugadorRival);
            return procesarResultadoDisparo(evento, emisor, manejadorDisparo, jugadorRival);
        }
    }

    private ResultadoDisparoEvento procesarResultadoDisparo(DisparoEvento evento, Jugador emisor, ManejadorDisparo manejadorDisparo, Jugador jugadorRival) {
        ResultadoDisparoEvento resultado = manejadorDisparo.procesar();
        resultado.setEmisor(emisor);
        resultado.setCoordenada(evento.getCoordenada());

        Coordenada coordenadaDisparo = new Coordenada(evento.getCoordenada().fila(), evento.getCoordenada().columna());
        if (resultado.getTablero().getCasilla(coordenadaDisparo).getNave().isEmpty()) {
            cambiarTurnos();
        }
        if (resultado.getTablero().getCasilla(resultado.getCoordenada()).getNave().isPresent()) {
            victoria = verificarVictoria(resultado.getTablero().getCasilla(resultado.getCoordenada()).getNave().get(), jugadorRival);

        }
        return resultado;
    }

    private Evento procesarPosicionNaves(PosicionNavesEvento evento, Jugador emisor) {
        assert emisor != null;
        if (emisor.equals(partida.getJugador1())) {
            procesarPosicionJugador1(evento);
        } else {
            procesarPosicionJugador2(evento);
        }
        if (contadorEnvio == 2) {
            if (!detener.get()) {
                intercambiarJugadoresEntreClientes();
            }
        }
        return null;
    }

    private void intercambiarJugadoresEntreClientes() {
        EnvioJugadorEvento evento2 = new EnvioJugadorEvento();
        enviarEventoAJugador(streamsJugador1.getSalida(), new ResultadoEvento(true));
        enviarEventoAJugador(streamsJugador2.getSalida(), new ResultadoEvento(true));
        evento2.setEmisor(partida.getJugador2());
        enviarEventoAJugador(streamsJugador1.getSalida(), evento2);
        EnvioJugadorEvento evento1 = new EnvioJugadorEvento();
        evento1.setEmisor(partida.getJugador1());
        enviarEventoAJugador(streamsJugador2.getSalida(), evento1);
        mandadoPorDosJugadores();
    }

    public void mandadoPorDosJugadores() {
        if (contadorEnvio == 2) {
            detenerTemporizadorActivo();
            manejarTurnos();
            mandarTurno();
            contadorEnvio = 0;
        }
    }


    private void procesarPosicionJugador1(PosicionNavesEvento evento) {
        partida.setTableroJugador1(evento.getTablero());
        partida.getJugador1().setNaves(
                ManejadorPosicionNaves.inicializarNaves(partida.getTableroJugador1())
        );
        contadorEnvio++;
        if (contadorEnvio != 2) {
            enviarEventoAJugador(streamsJugador1.getSalida(), new ResultadoEvento(false));
        }
    }

    private void procesarPosicionJugador2(PosicionNavesEvento evento) {
        partida.setTableroJugador2(evento.getTablero());
        partida.getJugador2().setNaves(
                ManejadorPosicionNaves.inicializarNaves(partida.getTableroJugador2())
        );
        contadorEnvio++;
        if (contadorEnvio != 2) {
            enviarEventoAJugador(streamsJugador2.getSalida(), new ResultadoEvento(false));
        }
    }

    private void iniciarTemporizadorActivo(int segundos) {
        detenerTemporizadorActivo();

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

        } catch (Exception e) {
            Logger.error("Error al iniciar el temporizador activo: {}", e.getMessage());
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
        if (partida.getJugador1().isTurno() && !partida.getJugador2().isTurno()) {
            manejarTurnoJugador1();
        } else if (partida.getJugador2().isTurno() && !partida.getJugador1().isTurno()) {
            manejarTurnoJugador2();
            //TODO cambiar nombre evento
        } else if (partida.getJugador2().isTurno() && partida.getJugador1().isTurno()) {
            manejarTurnoJugador();
        }
    }

    /**
     * Maneja el turno del jugador actual.
     */
    private void manejarTurnoJugador1() {
        enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
        enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(30));
        iniciarTemporizadorActivo(30);

    }

    /**
     * Maneja el turno del jugador actual.
     */
    private void manejarTurnoJugador2() {
        enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(30));
        enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
        iniciarTemporizadorActivo(30);
    }

    /**
     * Maneja el tiempo para posicionar naves cuando todavia no se han
     * establecido el turno de disparo
     */
    private void manejarTurnoJugador() {
        enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
        enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(30));
        partida.getJugador2().setTurno(false);
        partida.getJugador1().setTurno(true);
        iniciarTemporizadorActivo(30);
    }

    /**
     * Obtiene el tablero del jugador rival.
     *
     * @param jugador Jugador actual.
     * @return Tablero del jugador rival.
     */
    private Tablero obtenerTableroRival(Jugador jugador) {
        return jugador.equals(partida.getJugador1()) ? partida.getTableroJugador2() : partida.getTableroJugador1();
    }

    /**
     * Obtiene al jugador rival.
     *
     * @param jugador Jugador actual.
     * @return Instancia del jugador rival.
     */
    private Jugador obtenerJugadorRival(Jugador jugador) {
        return jugador.equals(partida.getJugador1()) ? partida.getJugador2() : partida.getJugador1();
    }

    /**
     * Identifica al jugador que emitió el evento.
     *
     * @param entrada Stream de entrada del cliente.
     * @return Instancia del jugador emisor o null si no se encuentra.
     */
    private Jugador obtenerEmisor(ObjectInputStream entrada) {
        if (entrada.equals(streamsJugador1.getEntrada())) {
            return partida.getJugador1();
        } else if (entrada.equals(streamsJugador2.getEntrada())) {
            return partida.getJugador2();
        }
        return null;
    }

    //cambiar turnos
    public void mandarTurno() {
        if (partida.getJugador1().isTurno()) {
            enviarEventoAJugador(streamsJugador1.getSalida(), new ResultadoEvento(true));
            enviarEventoAJugador(streamsJugador2.getSalida(), new ResultadoEvento(false));

        } else {
            enviarEventoAJugador(streamsJugador1.getSalida(), new ResultadoEvento(false));
            enviarEventoAJugador(streamsJugador2.getSalida(), new ResultadoEvento(true));
        }
    }

    private void cambiarTurnos() {
        if (partida.getJugador1().isTurno()) {
            partida.getJugador1().setTurno(false);
            partida.getJugador2().setTurno(true);
        } else {
            partida.getJugador1().setTurno(true);
            partida.getJugador2().setTurno(false);
        }
    }

    private boolean verificarVictoria(Nave nave, Jugador jugador) {
        if (partida.getJugador1().equals(jugador)) {
            if (nave.getEstadoNave().equals(EstadoNave.HUNDIDA)) {
                contadorNavesHundidasJugador2++;
            }
            if (contadorNavesHundidasJugador2 == 11) {
                enviarEventoAJugador(streamsJugador2.getSalida(), new VictoriaEvento(true));
                enviarEventoAJugador(streamsJugador1.getSalida(), new VictoriaEvento(false));
                return true;
            }
            return false;
        } else {
            if (nave.getEstadoNave().equals(EstadoNave.HUNDIDA)) {
                contadorNavesHundidasJugador1++;
            }
            if (contadorNavesHundidasJugador1 == 11) {
                enviarEventoAJugador(streamsJugador2.getSalida(), new VictoriaEvento(false));
                enviarEventoAJugador(streamsJugador1.getSalida(), new VictoriaEvento(true));
                return true;
            }
            return false;
        }
    }

    public void manejarDesconexion(ControladorStreams controlador) {
        if (controlador.equals(streamsJugador1)) {
            enviarEventoAJugador(streamsJugador2.getSalida(), new DesconexionEvento());
        } else {
            enviarEventoAJugador(streamsJugador1.getSalida(), new DesconexionEvento());
        }
    }

    private ControladorStreams obtenerControlador(ObjectInputStream entrada) {
        if (entrada.equals(streamsJugador1.getEntrada())) {
            return streamsJugador1;
        } else if (entrada.equals(streamsJugador2.getEntrada())) {
            return streamsJugador2;
        }
        return null;
    }
}

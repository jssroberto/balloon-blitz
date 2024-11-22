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

public class ManejadorPartida implements EventoObserver {

    private final ControladorStreams streamsJugador1; // Streams del jugador 1.
    private final ControladorStreams streamsJugador2; // Streams del jugador 2.
    private final Jugador jugador1; // Instancia del jugador 1.
    private final Jugador jugador2; // Instancia del jugador 2.
    private final ManejadorTurno turno; // Gestor de turnos.
    private final Partida partida; // Representación de la partida.

    /**
     * Constructor para inicializar la partida con los jugadores y sus streams.
     *
     * @param jugadores Mapa que asocia streams con jugadores.
     */
    public ManejadorPartida(Map<ControladorStreams, Jugador> jugadores) {
        this.partida = new Partida();
        this.turno = new ManejadorTurno();

        // Asignar jugadores y sus streams.
        ControladorStreams[] streamsArray = jugadores.keySet().toArray(new ControladorStreams[0]);
        Jugador[] jugadoresArray = jugadores.values().toArray(new Jugador[0]);

        this.streamsJugador1 = streamsArray[1];
        this.streamsJugador2 = streamsArray[0];
        this.jugador1 = jugadoresArray[0];
        this.jugador2 = jugadoresArray[1];

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
        }

        manejarTurnos();
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
            ManejadorPosicionNaves manejadorPosicion = new ManejadorPosicionNaves((PosicionNavesEvento) evento);
            return manejadorPosicion.procesarEvento();
        } else if (tipoEvento == TipoEvento.DISPARO) {
            Tablero tableroRival = obtenerTableroRival(emisor);
            Jugador jugadorRival = obtenerJugadorRival(emisor);
            ManejadorDisparo manejadorDisparo = new ManejadorDisparo((DisparoEvento) evento, tableroRival, jugadorRival);
            return manejadorDisparo.procesarEvento();
        } else if (tipoEvento == TipoEvento.RESULTADO) {
            System.out.println("entrando a turno");
            manejarTurnos();
            return null;
        } else {
            Logger.error("No se reconoció el tipo de evento");
            return null;
        }
    }

    /**
     * Maneja los turnos de los jugadores.
     */
    private void manejarTurnos() {
        if (jugador1.isTurno() && !jugador2.isTurno()) {
            manejarTurnoJugador(streamsJugador1, jugador1, jugador2);
        } else if (jugador2.isTurno() && !jugador1.isTurno()) {
            manejarTurnoJugador(streamsJugador2, jugador2, jugador1);
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
    private void manejarTurnoJugador(ControladorStreams streamsJugador, Jugador jugadorActual, Jugador jugadorSiguiente) {
        enviarEventoAJugador(streamsJugador.getSalida(), new TimeOutEvento(30));
        if (turno.iniciarTemporizador(30) == 0) {
            enviarEventoAJugador(streamsJugador.getSalida(), new TimeOutEvento(0));
            jugadorActual.setTurno(false);
            jugadorSiguiente.setTurno(true);
        }
    }

    /**
     * Maneja el tiempo para posicionar naves cuando todavia no se han
     * establecido el turno de disparo
     */
    private void manejarTurnoJugador() {
        Evento evento1 = new EnvioJugadorEvento();
        evento1.setEmisor(jugador1);
        enviarEventoAJugador(streamsJugador1.getSalida(), evento1);
        Evento evento2 = new EnvioJugadorEvento();
        evento2.setEmisor(jugador2);
        enviarEventoAJugador(streamsJugador2.getSalida(), evento2);

        iniciarTiempo();
    }

    public void iniciarTiempo() {
        enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
        System.out.println("enviado jugador 1");
        enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(30));
        System.out.println("enviado jugador 2");
        if (turno.iniciarTemporizador(30) == 0) {
            enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(0));
            enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(0));
            partida.getJugador2().setTurno(false);
            partida.getJugador1().setTurno(true);
        }
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
        return jugador.equals(jugador1) ? jugador2 : jugador1;
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
}

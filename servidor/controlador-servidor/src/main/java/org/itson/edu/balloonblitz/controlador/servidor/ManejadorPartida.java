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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.*;

/**
 * Clase que representa el manejador de la partida
 *
 * @author elimo
 */
public class ManejadorPartida implements EventoObserver {

    private final ControladorStreams streamsJugador1;
    private final ControladorStreams streamsJugador2;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private ManejadorTurno turno;
    private final Servidor servidor;
    private final Partida partida;

    public ManejadorPartida(List<ControladorStreams> jugadores) {
        turno = new ManejadorTurno();
        partida = new Partida();
        servidor = Servidor.getInstance();
        streamsJugador1 = jugadores.get(0);
        streamsJugador2 = jugadores.get(1);
        iniciarStreams();

    }

    private void iniciarStreams() {
        executorService.submit(() -> servidor.recibirDatosCiente(streamsJugador1.getEntrada()));
        executorService.submit(() -> servidor.recibirDatosCiente(streamsJugador2.getEntrada()));
        executorService.submit(() -> servidor.mandarDatosCliente(streamsJugador1.getSalida(), null));
        executorService.submit(() -> servidor.mandarDatosCliente(streamsJugador2.getSalida(), null));
        enviarEventoAJugador(streamsJugador2.getSalida(), new ResultadoEvento(true));
        enviarEventoAJugador(streamsJugador1.getSalida(), new ResultadoEvento(true));

    }

    /**
     * Metodo suscriptor que maneja el evento obtenido del servidor
     *
     * @param evento Evento enviado por el cliente
     * @param entrada Stream de entrada del cliente
     */
    @Override
    public void manejarEvento(Evento evento, ObjectInputStream entrada) {
        Evento eventoProcesado = procesarEvento(evento, entrada);
        enviarEventoAJugador(streamsJugador1.getSalida(), eventoProcesado);
        enviarEventoAJugador(streamsJugador2.getSalida(), eventoProcesado);
        manejarTurnos();
    }

    /**
     * Manda a enviar el resultado de los eventos a los jugadores
     *
     * @param evento Resultado de un evento
     */
    public void enviarEventoAJugador(ObjectOutputStream salida, Evento evento) {
        if (evento != null) {
            servidor.mandarDatosCliente(salida, evento);
        }
    }

    private void verificarYCrearPartida() {
        synchronized (this) {
            if (partida.getJugador1() != null && partida.getJugador2() != null) {
                partida.getJugador1().setTurno(true);
                partida.getJugador2().setTurno(true);
                crearPartida();
            }
        }
    }

    public void crearPartida() {
        partida.setTableroJugador1(new Tablero());
        partida.setTableroJugador2(new Tablero());
        partida.setEstadoPartida(EstadoPartida.ACTIVA);
        manejarTurnos();

    }

    public Evento procesarEvento(Evento evento, ObjectInputStream entrada) {
        TipoEvento tipoEvento = evento.getTipoEvento();

        if (tipoEvento == TipoEvento.ENVIO_JUGADOR) {
            if (entrada.equals(streamsJugador1.getEntrada())) {
                partida.setJugador1(evento.getEmisor());
            } else if (entrada.equals(streamsJugador2.getEntrada())) {
                partida.setJugador2(evento.getEmisor());
            }
            verificarYCrearPartida();
            return null;
        } else {
            Jugador emisor = obtenerEmisor(entrada);
            if (emisor == null) {
                Logger.error("No se pudo identificar el emisor del evento");
                return null;
            }
            evento.setEmisor(emisor);

            if (tipoEvento == TipoEvento.POSICION_NAVES) {
                ManejadorPosicionNaves manejadorPosicion = new ManejadorPosicionNaves((PosicionNavesEvento) evento);
                return manejadorPosicion.procesarEvento();

            } else if (tipoEvento == TipoEvento.DISPARO) {
                //Obtiene el jugador rival y su tablero correspondientes al servidor
                Tablero tableroRival = obtenerTableroRival(evento.getEmisor());
                Jugador jugadorRival = obtenerJugadorRival(evento.getEmisor());
                ManejadorDisparo manejadorDisparo = new ManejadorDisparo((DisparoEvento) evento, tableroRival, jugadorRival);
                return manejadorDisparo.procesarEvento();
            } else {
                Logger.error("No se reconoció el tipo de evento");
                return null;
            }
        }

    }

    /**
     * Metodo que maneja los turnos de los jugadores
     */
    private void manejarTurnos() {
        if (partida.getJugador1().isTurno() && !partida.getJugador2().isTurno()) {
            manejarTurnoJugador(streamsJugador1, partida.getJugador1(), partida.getJugador2());
        } else if (partida.getJugador2().isTurno() && !partida.getJugador1().isTurno()) {
            manejarTurnoJugador(streamsJugador2, partida.getJugador2(), partida.getJugador1());
        } else if (partida.getJugador2().isTurno() && partida.getJugador1().isTurno()) {
            manejarTurnoJugador();
        }
    }

    /**
     * Metodo que maneja el turno de un jugador
     *
     * @param streamsJugador Streams del jugador
     * @param jugadorActual Jugador actual
     * @param jugadorSiguiente Jugador siguiente
     */
    private void manejarTurnoJugador(ControladorStreams streamsJugador, Jugador jugadorActual, Jugador jugadorSiguiente) {
        enviarEventoAJugador(streamsJugador.getSalida(), new TimeOutEvento(30));
        if (turno.iniciarTemporizador(30) == 0) {
            enviarEventoAJugador(streamsJugador.getSalida(), new TimeOutEvento(0));
            jugadorActual.setTurno(false);
            jugadorSiguiente.setTurno(true);
        }
    }

    private void manejarTurnoJugador() {
        enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
        enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(30));
        if (turno.iniciarTemporizador(30) == 0) {
            enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(0));
            enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(0));
            partida.getJugador1().setTurno(true);
            partida.getJugador2().setTurno(false);
        }
    }

    private Jugador obtenerJugadorRival(Jugador jugador) {
        return jugador.equals(partida.getJugador1()) ? partida.getJugador2() : partida.getJugador1();
    }

    private Tablero obtenerTableroRival(Jugador jugador) {
        return jugador.equals(partida.getJugador1()) ? partida.getTableroJugador2() : partida.getTableroJugador1();
    }

    private Jugador obtenerEmisor(ObjectInputStream entrada) {
        return entrada.equals(streamsJugador1.getEntrada()) ? partida.getJugador1()
                : entrada.equals(streamsJugador2.getEntrada()) ? partida.getJugador2() : null;
    }
}

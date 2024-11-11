package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoPartida;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.PosicionNaves;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;
import org.itson.edu.balloonblitz.modelo.servidor.ControladorStreams;
import org.itson.edu.balloonblitz.modelo.servidor.EventoObserver;
import org.itson.edu.balloonblitz.modelo.servidor.Servidor;

/**
 * Clase que representa el manejador de la partida
 *
 * @author elimo
 */
public class ManejadorPartida extends Thread implements EventoObserver {

    private final ControladorStreams streamsJugador1;
    private final ControladorStreams streamsJugador2;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final Servidor servidor;
    private Jugador jugador1;
    private ManejadorTurno turno;
    private Jugador jugador2;
    private Partida partida;
    private Tablero tableroJugador1;
    private Tablero tableroJugador2;

    /**
     * Constructor que agrega los I/O de los jugadores
     *
     * @param jugadores
     */
    public ManejadorPartida(List<ControladorStreams> jugadores) {
        servidor = Servidor.getInstance();
        streamsJugador1 = jugadores.get(0);
        streamsJugador2 = jugadores.get(1);
      
    }

    /**
     * Manda a manejar el evento enviado por el jugador 1
     */
    public void obtenerEventoJugador1() {
        servidor.recibirDatosCiente(streamsJugador1.getEntrada());
    }

    /**
     * Manda a manejar el evento enviado por el jugador2
     */
    public void obtenerEventoJugador2() {
        servidor.recibirDatosCiente(streamsJugador2.getEntrada());
    }

    /**
     * Manda a enviar el resultado de los eventos a los jugadores
     *
     * @param salida1 Output del jugador 1
     * @param evento Resultado de un evento
     */
    public void enviarEventoAJugador1(ObjectOutputStream salida1, Evento evento) {
        servidor.mandarDatosCliente(salida1, evento);
    }

    public void enviarEventoAJugador2(ObjectOutputStream salida2, Evento evento) {
        servidor.mandarDatosCliente(salida2, evento);
    }

    /**
     * Metodo suscriptor que maneja el evento obtenido del servidor
     *
     * @param evento Evento enviado por el cliente
     * @param entrada
     * @return
     */
    @Override
    public Evento manejarEvento(Evento evento, ObjectInputStream entrada) {
        if (entrada.equals(streamsJugador1.getEntrada())) {
            if (evento.getTipoEvento() == TipoEvento.ENVIO_JUGADOR) {
                jugador1 = evento.getEmisor();
            } else {
                evento = manejarPartida(evento, entrada);
                enviarEventoAJugador2(streamsJugador2.getSalida(), evento);
            }
        } else if (entrada.equals(streamsJugador2.getEntrada())) {
            if (evento.getTipoEvento() == TipoEvento.ENVIO_JUGADOR) {
                jugador2 = evento.getEmisor();
                if(jugador1!=null){
                    crearPartida();
                }
            } else {
                evento = manejarPartida(evento, entrada);
                enviarEventoAJugador1(streamsJugador1.getSalida(), evento);
            }
        }

        return null;
    }

    @Override
    public void run() {
        obtenerEventoJugador1();
        obtenerEventoJugador2();
    }

    public void crearPartida() {
        partida = new Partida(jugador1, jugador2);
        partida.setTableroJugador1(tableroJugador1);
        partida.setTableroJugador2(tableroJugador2);
        partida.setEstadoPartida(EstadoPartida.ACTIVA);
        enviarEventoAJugador1(streamsJugador1.getSalida(), new TimeOutEvento(1));
        enviarEventoAJugador2(streamsJugador2.getSalida(), new TimeOutEvento(1));

    }

    public Evento manejarPartida(Evento evento, ObjectInputStream entrada) {

        
        if (entrada.equals(streamsJugador1.getEntrada())) {
            evento.setEmisor(jugador1);
        } else if (entrada.equals(streamsJugador2.getEntrada())) {
            evento.setEmisor(jugador2);
        }

        if (evento.getTipoEvento() == TipoEvento.POSICION_NAVES) {
            ManejadorPosicionNaves manejadorPosicion = new ManejadorPosicionNaves(evento);
            return manejadorPosicion.obtenerEvento();

        } else if (evento.getTipoEvento() == TipoEvento.DISPARO) {

            ManejadorDisparo manejadorDisparo = new ManejadorDisparo(evento);
            return manejadorDisparo.obtenerEvento();
        }
        return null;
    }

}

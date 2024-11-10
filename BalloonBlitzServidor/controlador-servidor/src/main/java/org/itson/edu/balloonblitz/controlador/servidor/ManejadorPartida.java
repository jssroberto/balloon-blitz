package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.eventos.EnviarJugador;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.modelo.servidor.ControladorStreams;
import org.itson.edu.balloonblitz.modelo.servidor.EventoObserver;
import org.itson.edu.balloonblitz.modelo.servidor.Servidor;

/**
 * Clase que representa el manejador de la partida
 *
 * @author elimo
 */
public class ManejadorPartida extends Thread implements EventoObserver {
    
    ControladorStreams streamsJugador1;
    ControladorStreams streamsJugador2;
    Servidor servidor;
    Jugador jugador1;
    Jugador jugador2;
    Partida partida;
    Tablero tableroJugador1;
    Tablero tableroJugador2;

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
     * Manda a enviar el evento enviado por el jugador 1
     */
    public void obtenerEventoJugador1() {
        servidor.recibirDatosCiente(streamsJugador1.getEntrada());
    }

    /**
     * Manda a enviar el evento enviado por el jugador2
     */
    public void obtenerEventoJugador2() {
        servidor.recibirDatosCiente(streamsJugador2.getEntrada());
    }

    /**
     * Manda a enviar el resultado de los eventos a los jugadores
     *
     * @param salida1 Output del jugador 1
     * @param salida2 Output del jugador 2
     * @param evento Resultado de un evento
     */
    public void enviarEventoAJugadores(ObjectOutputStream salida1, ObjectOutputStream salida2, Evento evento) {
        servidor.mandarDatosCliente(salida1, evento);
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
            if (evento instanceof EnviarJugador) {
                jugador1 = (Jugador) evento.manejarEvento();
            }
        } else if (entrada.equals(streamsJugador2.getEntrada())) {
            if (evento instanceof EnviarJugador) {
                jugador2 = (Jugador) evento.manejarEvento();
            }
        }
        evento = manejarPartida(evento, entrada);
        enviarEventoAJugadores(streamsJugador1.getSalida(), streamsJugador2.getSalida(), evento.manejarEvento());
        
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
    }
    
    public Evento manejarPartida(Evento evento, ObjectInputStream entrada) {
        if (entrada.equals(streamsJugador1.getEntrada())) {
            evento.setEmisor(jugador1);
        } else if (entrada.equals(streamsJugador2.getEntrada())) {
            evento.setEmisor(jugador2);
        }
        evento.setPartida(partida);
        evento = evento.manejarEvento();
        manejarEvento(evento, entrada);
        return null;
        
    }
    
}

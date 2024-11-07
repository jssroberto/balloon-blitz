package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;

/**
 *
 * @author elimo
 */
public class ManejadorPartida extends Thread {

    Partida partida;
    ControladorStreams streamsJugador1;
    ControladorStreams streamsJugador2;
    Jugador jugador1;
    Jugador jugador2;

    public ManejadorPartida(List<ControladorStreams> jugadores) {
        streamsJugador1 = jugadores.get(0);
        streamsJugador1 = jugadores.get(1);
        try {
            if (streamsJugador1.getEntrada().readObject() instanceof Jugador && streamsJugador2.getEntrada().readObject() instanceof Jugador) {
                jugador1 = (Jugador) streamsJugador1.getEntrada().readObject();
                jugador2 = (Jugador) streamsJugador2.getEntrada().readObject();
                partida = new Partida(jugador1, jugador2);

            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ManejadorPartida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void empezarPartida() {
        System.out.println("empezaste la partida bien machin");
        System.out.println("jugadores:" + jugador1.getNombre() + " " + jugador2.getNombre());
    }

}

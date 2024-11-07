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
        streamsJugador2 = jugadores.get(1);
        if (streamsJugador1.getObjetoRecibido() instanceof Jugador && streamsJugador2.getObjetoRecibido()  instanceof Jugador) {
            jugador1 = (Jugador) streamsJugador1.getObjetoRecibido();
            jugador2 = (Jugador) streamsJugador2.getObjetoRecibido();
           
        }
    }

    public void empezarPartida() {
        System.out.println("empezaste la partida bien machin");
        System.out.println("jugadores:" + jugador1.getNombre() + " " + jugador2.getNombre());
    }

}

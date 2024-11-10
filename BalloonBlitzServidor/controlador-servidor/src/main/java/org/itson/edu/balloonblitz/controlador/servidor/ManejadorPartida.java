
package org.itson.edu.balloonblitz.controlador.servidor;

import java.util.List;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.modelo.servidor.ControladorStreams;

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
    }

  
    

}

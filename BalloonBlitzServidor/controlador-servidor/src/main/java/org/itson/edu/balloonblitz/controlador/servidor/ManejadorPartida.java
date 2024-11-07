
package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.ObjectOutputStream;
import java.util.List;
import org.itson.edu.balloonblitz.entidades.Partida;

/**
 *
 * @author elimo
 */
public class ManejadorPartida extends Thread{
    
    Partida partida;
    

    public ManejadorPartida(List<ObjectOutputStream> jugadores) {
        partida = new Partida(jugadores.getFirst(), jugadores.getLast());
    }
    
    
}

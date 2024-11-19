/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.esperarJugador;

import org.itson.edu.balloonblitz.personalizar.*;
import org.itson.edu.balloonblitz.entidades.Jugador;

/**
 *
 * @author elimo
 */
public class ControladorEmparejamiento {

    private final ControladorJugador controladorJugador;
    private final ModeloResultadoEmparejamiento controladorEmparejamiento;
    private static ControladorEmparejamiento instancia;

    public ControladorEmparejamiento() {
        controladorJugador = ControladorJugador.getInstancia();
        controladorEmparejamiento = ModeloResultadoEmparejamiento.getInstancia();
    }

    public Jugador obtenerJugador() {
        return controladorJugador.obtenerJugador();
    }

    public void setJugador(Jugador jugador) {
        controladorJugador.setJugador(jugador);
    }
    
    public boolean isValido() {
        return controladorEmparejamiento.isValido();
    }

    public void setValido(boolean valido) {
        controladorEmparejamiento.setValido(valido);
    }
    
    public static ControladorEmparejamiento getInstancia() {
        if (instancia == null) {
            instancia = new ControladorEmparejamiento();
        }
        return instancia;
    }

}

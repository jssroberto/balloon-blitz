/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.personalizar;

import org.itson.edu.balloonblitz.entidades.Jugador;

/**
 *
 * @author elimo
 */
public class ControladorJugador {

    private static ControladorJugador instancia;
    private final ModeloJugador modeloJugador;

    public ControladorJugador() {
        modeloJugador = ModeloJugador.getInstancia();
        
    }

    public Jugador obtenerJugador() {
        return modeloJugador.getJugador();
    }

    public void setJugador(Jugador jugador) {
        modeloJugador.setJugador(jugador);
    }
    
    public static ControladorJugador getInstancia() {
        if (instancia == null) {
            instancia = new ControladorJugador();
        }
        return instancia;
    }

}

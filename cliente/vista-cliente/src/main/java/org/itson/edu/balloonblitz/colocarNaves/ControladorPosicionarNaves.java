/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.colocarNaves;

import org.itson.edu.balloonblitz.personalizar.*;
import org.itson.edu.balloonblitz.entidades.Jugador;

/**
 *
 * @author elimo
 */
public class ControladorPosicionarNaves {

    private static ControladorPosicionarNaves instancia;
    private final ModeloJugador modeloJugador;

    public ControladorPosicionarNaves() {
        modeloJugador = ModeloJugador.getInstancia();
        
    }

    public Jugador obtenerJugador() {
        return modeloJugador.getJugador();
    }

    public void setJugador(Jugador jugador) {
        modeloJugador.setJugador(jugador);
    }
    
    public static ControladorPosicionarNaves getInstancia() {
        if (instancia == null) {
            instancia = new ControladorPosicionarNaves();
        }
        return instancia;
    }

}

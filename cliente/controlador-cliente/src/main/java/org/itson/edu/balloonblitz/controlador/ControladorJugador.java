/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.modelo.ClienteControlador;
import org.itson.edu.balloonblitz.modelo.ObservadorJugador;

/**
 *
 * @author elimo
 */
public class ControladorJugador implements ObservadorJugador {

    Jugador jugador;

    public ControladorJugador() {

    }

    @Override
    public void manejarEvento(EnvioJugadorEvento evento) {
        jugador = evento.getEmisor();
    }
}

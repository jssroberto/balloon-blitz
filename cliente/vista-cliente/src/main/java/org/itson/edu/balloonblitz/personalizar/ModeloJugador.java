/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.personalizar;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.modelo.ObservadorJugador;

/**
 *
 * @author elimo
 */
public class ModeloJugador implements ObservadorJugador {

    private static ModeloJugador instancia;
    private Jugador jugador;

    public ModeloJugador() {
    }

    public static ModeloJugador getInstancia() {
        if (instancia == null) {
            instancia = new ModeloJugador();
        }
        return instancia;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    @Override
    public void manejarEvento(EnvioJugadorEvento evento) {
        jugador = evento.getEmisor();
    }
}

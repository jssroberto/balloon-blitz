/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.eventos;

import java.io.Serializable;
import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.Tablero;

/**
 *
 * @author elimo
 */
public class PosicionarNaves implements Serializable, Evento {

    Partida partida;
    Tablero tablero;
    Jugador jugadorActual;
    Casilla casilla;

    public PosicionarNaves() {
        if (jugadorActual.equals(partida.getJugador1())) {
            tablero = partida.getTableroJugador1();
        } else {
            tablero = partida.getTableroJugador2();
        }
        
    }

    @Override
    public Evento manejarEvento() {
        return this;
    }

    @Override
    public void setEmisor(Jugador salida) {
        salida = jugadorActual;
    }

    @Override
    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    @Override
    public Partida getPartida() {
        return partida;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public void setJugadorActual(Jugador jugadorActual) {
        this.jugadorActual = jugadorActual;
    }

    public Casilla getCasilla() {
        return casilla;
    }

    public void setCasilla(Casilla casilla) {
        this.casilla = casilla;
    }

}

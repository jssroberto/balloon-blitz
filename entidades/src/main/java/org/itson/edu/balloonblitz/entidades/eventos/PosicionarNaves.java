/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.eventos;

import java.io.Serializable;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.Tablero;

/**
 *
 * @author elimo
 */
public class PosicionarNaves implements Serializable, Evento {

    Partida partida;
    Tablero tableroJugador1;
    Tablero tableroJugador2;

    public PosicionarNaves(Partida partida) {
        this.partida = partida;
        this.tableroJugador1 = partida.getTableroJugador1();
        this.tableroJugador2 = partida.getTableroJugador2();
    }

    @Override
    public Evento manejarEvento() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setEmisor(Jugador salida) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public Partida getPartida() {
        return partida;
    }

}

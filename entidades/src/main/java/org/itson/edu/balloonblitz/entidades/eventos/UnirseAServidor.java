/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;

/**
 *
 * @author elimo
 */
public class UnirseAServidor implements Evento {

    public UnirseAServidor() {
    }

    @Override
    public Evento manejarEvento() {
        System.out.println("Se ha conectado al cliente con éxito");
        return this;
    }

    @Override
    public void setEmisor(Jugador salida) {

    }

    @Override
    public void setPartida(Partida partida) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

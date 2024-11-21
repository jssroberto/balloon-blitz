/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.partida;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoDisparoEvento;

/**
 *
 * @author elimo
 */
public class ControladorPartida implements IControladorPartida{
    private Jugador jugador;
    private Tablero tablero;
    private Tablero tableroOponente;

    public ControladorPartida() {
        this.tablero = jugador.getTableroPropio();
    }

    public void procesarDisparo(ResultadoDisparoEvento evento) {
        Jugador jugador = evento.getEmisor();
        if (jugador.equals(this.jugador)) {
            tableroOponente = evento.getTablero();
        } else {
            tablero = evento.getTablero();
        }

    }

    public void actualizarTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public void actualizarTableroOponente(Tablero tablero) {
        this.tableroOponente = tablero;
    }

    @Override
    public void enviarEvento(Evento evento) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }


}

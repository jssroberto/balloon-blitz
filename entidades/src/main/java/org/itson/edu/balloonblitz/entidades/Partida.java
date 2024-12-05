/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;

import java.util.List;

/**
 * Representa una partida entre dos jugadores en el juego de Batalla Naval.
 * Contiene la información de los jugadores, los disparos registrados, el estado
 * de la partida y los tableros de ambos jugadores.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class Partida implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Jugador jugador1;
    private Jugador jugador2;
    private Tablero tableroJugador1;
    private Tablero tableroJugador2;

    /**
     * Constructor vacío de la clase Partida
     */
    public Partida() {
    }

    /**
     * Obtiene el primer jugador de la partida.
     *
     * @return El primer jugador.
     */
    public Jugador getJugador1() {
        return jugador1;
    }

    /**
     * Establece el primer jugador de la partida.
     *
     * @param jugador1 El primer jugador de la partida.
     */
    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    /**
     * Obtiene el segundo jugador de la partida.
     *
     * @return El segundo jugador.
     */
    public Jugador getJugador2() {
        return jugador2;
    }

    /**
     * Establece el segundo jugador de la partida.
     *
     * @param jugador2 El segundo jugador de la partida.
     */
    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }

    /**
     * Obtiene el tablero del primer jugador.
     *
     * @return El tablero del primer jugador.
     */
    public Tablero getTableroJugador1() {
        return tableroJugador1;
    }

    /**
     * Establece el tablero del primer jugador.
     *
     * @param tableroJugador1 El tablero del primer jugador.
     */
    public void setTableroJugador1(Tablero tableroJugador1) {
        this.tableroJugador1 = tableroJugador1;
    }

    /**
     * Obtiene el tablero del segundo jugador.
     *
     * @return El tablero del segundo jugador.
     */
    public Tablero getTableroJugador2() {
        return tableroJugador2;
    }

    /**
     * Establece el tablero del segundo jugador.
     *
     * @param tableroJugador2 El tablero del segundo jugador.
     */
    public void setTableroJugador2(Tablero tableroJugador2) {
        this.tableroJugador2 = tableroJugador2;
    }
}


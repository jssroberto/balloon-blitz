/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;

import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoPartida;

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
    private List<Disparo> registroDisparos;
    private Jugador jugador1;
    private Jugador jugador2;
    private EstadoPartida estadoPartida;
    private Tablero tableroJugador1;
    private Tablero tableroJugador2;

    /**
     * Constructor vacío de la clase Partida, que inicializa el estado de la partida en ESPERA.
     */
    public Partida() {
        this.estadoPartida = EstadoPartida.ESPERA;
    }

    /**
     * Constructor que recibe dos jugadores y configura el estado de la partida en ESPERA.
     *
     * @param jugador1 El primer jugador de la partida.
     * @param jugador2 El segundo jugador de la partida.
     */
    public Partida(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.estadoPartida = EstadoPartida.ESPERA;
    }

    /**
     * Obtiene el registro de los disparos realizados durante la partida.
     *
     * @return Una lista con los disparos registrados en la partida.
     */
    public List<Disparo> getRegistroDisparos() {
        return registroDisparos;
    }

    /**
     * Establece el registro de los disparos realizados durante la partida.
     *
     * @param registroDisparos Una lista con los disparos realizados.
     */
    public void setRegistroDisparos(List<Disparo> registroDisparos) {
        this.registroDisparos = registroDisparos;
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
     * Obtiene el estado actual de la partida.
     *
     * @return El estado de la partida.
     */
    public EstadoPartida getEstadoPartida() {
        return estadoPartida;
    }

    /**
     * Establece el estado de la partida.
     *
     * @param estadoPartida El nuevo estado de la partida.
     */
    public void setEstadoPartida(EstadoPartida estadoPartida) {
        this.estadoPartida = estadoPartida;
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


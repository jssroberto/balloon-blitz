/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import java.io.Serializable;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoPartida;
import java.util.List;

/**
 *
 * @author elimo
 */
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Disparo> registroDisparos;
    private Jugador jugador1;
    private Jugador jugador2;
    private EstadoPartida estadoPartida;

    public Partida(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.estadoPartida = EstadoPartida.ESPERA;
    }

    public List<Disparo> getRegistroDisparos() {
        return registroDisparos;
    }

    public void setRegistroDisparos(List<Disparo> registroDisparos) {
        this.registroDisparos = registroDisparos;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public void setJugador1(Jugador jugador1) {
        this.jugador1 = jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public void setJugador2(Jugador jugador2) {
        this.jugador2 = jugador2;
    }

    public EstadoPartida getEstadoPartida() {
        return estadoPartida;
    }

    public void setEstadoPartida(EstadoPartida estadoPartida) {
        this.estadoPartida = estadoPartida;
    }

}

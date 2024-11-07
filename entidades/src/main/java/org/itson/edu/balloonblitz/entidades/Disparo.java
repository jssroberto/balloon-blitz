/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import java.io.Serializable;

/**
 *
 * @author elimo
 */
public class Disparo implements Serializable{

    private Casilla casilla;
    private Jugador jugador;

    public Disparo(Casilla casilla, Jugador jugador) {
        this.casilla = casilla;
        this.jugador = jugador;
    }

}

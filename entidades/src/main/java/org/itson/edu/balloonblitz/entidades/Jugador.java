/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author elimo
 */
public class Jugador implements Serializable {

    private String nombre;
    private String fotoPerfil;
    private Tablero tableroPropio;
    private Tablero tableroContrincante;
    private List<Nave> naves;
    private int navesRestantes;

}

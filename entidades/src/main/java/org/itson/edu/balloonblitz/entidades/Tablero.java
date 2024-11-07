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
public class Tablero implements Serializable {

    private static final long serialVersionUID = 1L;
    private Casilla[][] matriz;

    public Tablero() {
        matriz = new Casilla[10][10];
        inicializarMatriz();
    }

    private void inicializarMatriz() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matriz[i][j] = new Casilla(new Coordenada(i, j));
            }
        }
    }

}

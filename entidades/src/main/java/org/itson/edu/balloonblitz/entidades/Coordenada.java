/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author elimo
 */
public record Coordenada(int fila, int columna) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}

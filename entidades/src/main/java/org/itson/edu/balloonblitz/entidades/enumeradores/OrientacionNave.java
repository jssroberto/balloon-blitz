/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.enumeradores;

/**
 *
 * @author rover
 */
public enum OrientacionNave {
    VERTICAL, HORIZONTAL;
    
    // Método para invertir la orientación
    public OrientacionNave invertir() {
        return this == VERTICAL ? HORIZONTAL : VERTICAL;
    }
}

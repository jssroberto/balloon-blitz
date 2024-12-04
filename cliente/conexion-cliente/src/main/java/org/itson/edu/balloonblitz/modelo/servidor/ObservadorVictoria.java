/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.edu.balloonblitz.modelo.servidor;

import org.itson.edu.balloonblitz.entidades.eventos.VictoriaEvento;

/**
 *
 * @author elimo
 */
public interface ObservadorVictoria {
    
    void manejarEvento(VictoriaEvento evento);
    
}

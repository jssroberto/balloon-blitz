/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.edu.balloonblitz.modelo;

import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;

/**
 *
 * @author elimo
 */
public interface ObservadorTiempo {
    
    void manejarEvento(TimeOutEvento evento);
    
}

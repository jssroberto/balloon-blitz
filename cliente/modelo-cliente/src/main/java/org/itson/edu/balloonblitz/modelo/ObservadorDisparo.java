/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.edu.balloonblitz.modelo;

import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;

/**
 *
 * @author elimo
 */
public interface ObservadorDisparo {
    
   void manejarDisparo(DisparoEvento evento);
    
}
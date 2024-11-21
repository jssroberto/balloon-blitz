/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.partida;

import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoDisparoEvento;
import org.itson.edu.balloonblitz.modelo.ObservadorDisparo;

/**
 *
 * @author elimo
 */
public class ModeloPartida implements ObservadorDisparo{

    public ModeloPartida() {
    }
    
    @Override
    public void manejarEvento(ResultadoDisparoEvento evento) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void enviarEvento(Evento evento) {

    }



}

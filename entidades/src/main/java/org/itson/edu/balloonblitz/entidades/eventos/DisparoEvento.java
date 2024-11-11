/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.eventos;

import java.io.Serializable;
import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

/**
 *
 * @author elimo
 */
public class DisparoEvento extends Evento implements Serializable {
    
    Coordenada coordenada;

    public DisparoEvento(Coordenada coordenada) {
        super(TipoEvento.DISPARO);
        this.coordenada = coordenada;
    }


    @Override
    public void setEmisor(Jugador salida) {
    }


}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.eventos;

import java.io.Serializable;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

/**
 *
 * @author elimo
 */
public class PosicionarNaves extends Evento implements Serializable {

    public PosicionarNaves() {
        super(TipoEvento.POSICION_NAVES);
    }


    @Override
    public void setEmisor(Jugador salida) {

    }

}

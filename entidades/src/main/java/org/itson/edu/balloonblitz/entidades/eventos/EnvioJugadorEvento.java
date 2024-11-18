/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

import java.io.Serial;

/**
 *
 * @author elimo
 */
public class EnvioJugadorEvento extends Evento {

    @Serial
    private static final long serialVersionUID = 1L;

    public EnvioJugadorEvento() {
        super(TipoEvento.ENVIO_JUGADOR);
    }

}

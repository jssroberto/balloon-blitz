/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.eventos.conexion;

import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;

/**
 *
 * @author elimo
 */
public class EnviarJugador extends Evento {

    private static final long serialVersionUID = 1L;

    public EnviarJugador() {
        super(TipoEvento.ENVIO_JUGADOR);
    }

}

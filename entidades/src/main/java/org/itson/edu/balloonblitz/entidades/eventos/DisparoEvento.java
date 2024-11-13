package org.itson.edu.balloonblitz.entidades.eventos;

import java.io.Serializable;

import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

/**
 *
 * @author elimo
 */
public class DisparoEvento extends Evento implements Serializable {
    
    private final org.itson.edu.balloonblitz.entidades.Disparo disparo;

    public DisparoEvento(org.itson.edu.balloonblitz.entidades.Disparo disparo) {
        super(TipoEvento.DISPARO);
        this.disparo = disparo;
    }

    public org.itson.edu.balloonblitz.entidades.Disparo getDisparo() {
        return disparo;
    }   
}

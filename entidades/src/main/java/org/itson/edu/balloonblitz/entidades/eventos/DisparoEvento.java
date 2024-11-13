package org.itson.edu.balloonblitz.entidades.eventos;

import java.io.Serializable;

import org.itson.edu.balloonblitz.entidades.Disparo;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

/**
 *
 * @author elimo
 */
public class DisparoEvento extends Evento implements Serializable {
    
    private final Disparo disparo;

    public DisparoEvento(Disparo disparo) {
        super(TipoEvento.DISPARO);
        this.disparo = disparo;
    }

    public Disparo getDisparo() {
        return disparo;
    }   
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.eventos;

import java.io.Serial;
import java.io.Serializable;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

/**
 *
 * @author elimo
 */
public abstract class Evento implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;
    protected Jugador emisor;
    protected final TipoEvento tipoEvento;

    public Evento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public void setEmisor(Jugador emisor) {
        this.emisor = emisor;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public Jugador getEmisor() {
        return emisor;
    }

}

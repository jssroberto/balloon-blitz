/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

/**
 *
 * @author elimo
 */
public final class TimeOutEvento extends Evento {

    private final int tiempoRestante;  // Puede ser en minutos u otro tipo que necesites

    public TimeOutEvento(int tiempoRestante) {
        super(TipoEvento.TIMEOUT);
        this.tiempoRestante = tiempoRestante;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    @Override
    public TipoEvento getTipoEvento() {
        return TipoEvento.TIMEOUT;
    }

}

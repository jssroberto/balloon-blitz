package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

public class VictoriaEvento extends Evento {

    private boolean victoria;

    public VictoriaEvento(boolean victoria) {
        super(TipoEvento.VICTORIA);
        this.victoria = victoria;
    }

    public boolean isVictoria() {
        return victoria;
    }

}

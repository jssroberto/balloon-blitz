package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

public class VictoriaEvento extends Evento {

    private final Jugador jugador;

    public VictoriaEvento(Jugador jugador) {
        super(TipoEvento.VICTORIA);
        this.jugador = jugador;
    }

    public Jugador getJugador() {
        return jugador;
    }
}

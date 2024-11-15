package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

public class ResultadoEvento extends Evento {

    private final Casilla casiila;
    //TODO idk if this is necessary
    private final Partida partida;
    private final boolean acertado;

    public ResultadoEvento(Casilla casiila, Partida partida, boolean acertado) {
        super(TipoEvento.RESULTADO);
        this.casiila = casiila;
        this.acertado = acertado;
        this.partida = partida;
    }

    public Casilla getCasiila() {
        return casiila;
    }

    public Coordenada getCoordenada() {
        return casiila.getCoordenada();
    }

    public boolean isAcertado() {
        return acertado;
    }

    public Partida getPartida() {
        return partida;
    }
}

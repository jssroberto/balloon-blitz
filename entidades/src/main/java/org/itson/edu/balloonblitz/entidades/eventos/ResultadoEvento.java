package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

public class ResultadoEvento extends Evento {

    private final Casilla casiila;

    public ResultadoEvento(Casilla casiila) {
        super(TipoEvento.RESULTADO);
        this.casiila = casiila;
    }

    public Casilla getCasiila() {
        return casiila;
    }

    public Coordenada getCoordenada(){
        return casiila.getCoordenada();
    }
}

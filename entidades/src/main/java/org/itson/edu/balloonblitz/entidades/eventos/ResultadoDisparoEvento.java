package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

import java.util.List;
import java.util.Optional;

public class ResultadoDisparoEvento extends Evento {

    private final List<Casilla> casillas;

    public ResultadoDisparoEvento(List<Casilla> casillas) {
        super(TipoEvento.RESULTADO_DISPARO);
        this.casillas = casillas;
    }

    public Optional<List<Casilla>> getCasillas() {
        return Optional.ofNullable(casillas);
    }
}
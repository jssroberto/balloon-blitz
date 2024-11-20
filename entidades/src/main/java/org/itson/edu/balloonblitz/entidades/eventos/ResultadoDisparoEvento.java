package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

import java.util.List;
import java.util.Optional;

public class ResultadoDisparoEvento extends Evento {

    private final Tablero tablero;

    public ResultadoDisparoEvento(Tablero tablero) {
        super(TipoEvento.RESULTADO_DISPARO);
        this.tablero = tablero;
    }

    public Tablero getTablero() {
        return tablero;
    }
}
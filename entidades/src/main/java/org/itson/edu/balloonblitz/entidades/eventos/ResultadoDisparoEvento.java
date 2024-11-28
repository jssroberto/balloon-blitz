package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

import java.util.List;
import java.util.Optional;

public class ResultadoDisparoEvento extends Evento {

    private final Tablero tablero;
    private final List<Nave> naves;
    private Coordenada coordenada;

    public ResultadoDisparoEvento(Tablero tablero, List<Nave> naves) {
        super(TipoEvento.RESULTADO_DISPARO);
        this.tablero = tablero;
        this.naves = naves;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public List<Nave> getNaves() {
        return naves;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }
}

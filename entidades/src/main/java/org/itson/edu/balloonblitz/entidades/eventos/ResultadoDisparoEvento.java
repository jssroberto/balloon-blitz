package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

import java.util.List;
import java.util.Optional;

public class ResultadoDisparoEvento extends Evento {

    private final boolean valido;
    private final boolean hundido;
    private Casilla casilla;

    public ResultadoDisparoEvento(boolean hundido, boolean valido, Casilla casilla) {
        super(TipoEvento.RESULTADO_DISPARO);
        this.casilla = casilla;
        this.valido = valido;
        this.hundido = hundido;
    }

    public boolean isValido() {
        return valido;
    }

    public boolean isHundido() {
        return hundido;
    }

    public Casilla getCasilla() {
        return casilla;
    }

    
    
    
}

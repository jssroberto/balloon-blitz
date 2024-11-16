package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

import java.util.List;

public class ResultadoEvento extends Evento {

    private final List<Casilla> casillas;
    private final boolean acertado;

    public ResultadoEvento(List<Casilla> casillas, boolean acertado) {
        super(TipoEvento.RESULTADO);
        this.casillas = casillas;
        this.acertado = acertado;
    }

    public List<Casilla> getCasillas() {
        return casillas;
    }

    public boolean isAcertado() {
        return acertado;
    }
}
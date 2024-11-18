package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

public class ResultadoEvento extends Evento {
    private final boolean resultado;

    public ResultadoEvento(boolean resultado) {
        super(TipoEvento.RESULTADO);
        this.resultado = resultado;
    }

    public boolean isValid() {
        return resultado;
    }
}

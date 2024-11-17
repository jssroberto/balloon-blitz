package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

public class ResultadoPosicionNavesEvento extends Evento {
    private final boolean resultado;

    public ResultadoPosicionNavesEvento(boolean resultado) {
        super(TipoEvento.RESULTADO_POSICION_NAVES);
        this.resultado = resultado;
    }

    public boolean isValid() {
        return resultado;
    }
}

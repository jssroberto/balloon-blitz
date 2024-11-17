package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

/**
 *
 * @author elimo
 */
public class PosicionNavesEvento extends Evento {

    private final Tablero tablero;
    
    public PosicionNavesEvento(Tablero tablero) {
        super(TipoEvento.POSICION_NAVES);
        this.tablero = tablero;
    }

    public Tablero getTablero() {
        return tablero;
    }

}

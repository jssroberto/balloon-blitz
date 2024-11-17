package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;


/**
 *
 * @author elimo
 */
public class DisparoEvento extends Evento {
    
    private final Coordenada coordenda;

    public DisparoEvento(Coordenada coordenda) {
        super(TipoEvento.DISPARO);
        this.coordenda = coordenda;
    }

    public Coordenada getCoordenada() {
        return coordenda;
    }
}

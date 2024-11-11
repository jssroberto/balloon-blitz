package org.itson.edu.balloonblitz.entidades.eventos;

import java.io.Serializable;
import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

/**
 *
 * @author elimo
 */
public class DisparoEvento extends Evento implements Serializable {
    
    private final Coordenada coordenada;

    public DisparoEvento(Coordenada coordenada) {
        super(TipoEvento.DISPARO);
        this.coordenada = coordenada;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }
}

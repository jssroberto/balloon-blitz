package org.itson.edu.balloonblitz.entidades.eventos;

import java.io.Serializable;
import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.enumeradores.OrientacionNave;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;

/**
 *
 * @author elimo
 */
public class PosicionNavesEvento extends Evento implements Serializable {

    private final TipoNave tipoNave;
    private final Coordenada coordenadaCabecera;
    private final OrientacionNave orientacionNave;
    
    public PosicionNavesEvento(TipoNave tipoNave, Coordenada coordenadaCabecera, OrientacionNave orientacionNave) {
        super(TipoEvento.POSICION_NAVES);
        this.tipoNave = tipoNave;
        this.orientacionNave = orientacionNave;
        this.coordenadaCabecera = coordenadaCabecera;
    }

    public TipoNave getTipoNave() {
        return tipoNave;
    }

    public Coordenada getCoordenadaCabecera() {
        return coordenadaCabecera;
    }

    public OrientacionNave getOrientacionNave() {
        return orientacionNave;
    }
}

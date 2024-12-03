package org.itson.edu.balloonblitz.entidades.eventos;

import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

public class DesconexionEvento extends Evento {
    public DesconexionEvento() {
        super(TipoEvento.DESCONEXION);
    }
}

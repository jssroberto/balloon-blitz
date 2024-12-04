package org.itson.edu.balloonblitz.modelo.servidor;

import org.itson.edu.balloonblitz.entidades.eventos.DesconexionEvento;

public interface ObservadorDesconexion {

    void manejarEvento(DesconexionEvento evento);
}

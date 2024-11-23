/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.personalizar;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;
import org.itson.edu.balloonblitz.FramePrincipal;

/**
 *
 * @author elimo
 */
public class ControllerPersonalizar implements ActionHandlerPersonalizacion {

    private final PersonalizarPanel view;
    private final ModelPersonalizar model;
    ConexionCliente conexion;

    
    public ControllerPersonalizar(PersonalizarPanel view, ModelPersonalizar model) {
        this.view = view;
        this.model = model;
        this.view.setActionHandler(this);
        this.model.addObserver(view);
    }

    @Override
    public void cambiarPanelYEnviarJugador(){
        model.cambiarPanel();
    }

    @Override
    public void enviarJugador(Jugador jugador) {
        Evento evento = new EnvioJugadorEvento();
        evento.setEmisor(jugador);
        ConexionCliente.getInstancia().enviarMensaje(evento);
    }

}

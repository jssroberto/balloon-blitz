/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.emparejamiento;

import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;
import org.itson.edu.balloonblitz.modelo.ObservadorResultado;

/**
 * @author elimo
 */
public class ControllerEmparejamiento implements ActionHandlerEmparejamiento, ObservadorResultado {

    private final EmparejamientoPanel view;
    private final ModelEmparejamiento model;

    // Constructor privado para evitar que se instancien múltiples objetos
    public ControllerEmparejamiento(EmparejamientoPanel view, ModelEmparejamiento model) {
        this.view = view;
        this.model = model;
        this.view.setActionHandler(this);
        this.model.addObserver(view);
        buscarPartida();
    }

    @Override
    public void manejarEvento(ResultadoEvento evento) {
        model.setPartidaEncontrada();
    }

    public void enviarEvento(Evento evento) {
        ConexionCliente.getInstancia().enviarMensaje(evento);
    }

    private void buscarPartida() {
        model.buscarPartida();
    }

    @Override
    public void confirmarUnion() {
        enviarEvento(new ResultadoEvento(true));
    }

}

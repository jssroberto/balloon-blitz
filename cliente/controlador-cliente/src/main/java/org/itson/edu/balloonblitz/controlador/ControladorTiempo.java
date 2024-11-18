/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;
import org.itson.edu.balloonblitz.modelo.ClienteControlador;
import org.itson.edu.balloonblitz.modelo.ObservadorTiempo;

/**
 *
 * @author elimo
 */
public class ControladorTiempo implements ObservadorTiempo {

    ClienteControlador cliente;

    public ControladorTiempo() {
        cliente = new ClienteControlador();
    }

    public void enviarEvento(Evento evento) {
        cliente.enviarMensaje(evento);
    }

    @Override
    public void manejarDisparo(TimeOutEvento evento) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}

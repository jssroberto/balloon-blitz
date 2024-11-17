/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.modelo.ClienteControlador;

/**
 *
 * @author elimo
 */
public class ControladorPartida {

    ClienteControlador cliente;

    public ControladorPartida() {
        cliente = new ClienteControlador();
    }

    public void enviarEvento(Evento evento) {
        cliente.enviarMensaje(evento);
    }

}

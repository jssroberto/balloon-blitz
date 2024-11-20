/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.colocarNaves;

import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;

/**
 *
 * @author elimo
 */
public class ControladorEnvio {

    ConexionCliente cliente;

    public ControladorEnvio() {
        cliente = ConexionCliente.getInstancia();
    }

    public synchronized void enviarEvento(Evento evento) {
    cliente.enviarMensaje(evento);
}


}

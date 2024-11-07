/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

import org.itson.edu.balloonblitz.entidades.Jugador;

/**
 *
 * @author elimo
 */
public class pruebacliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Jugador jugador = new Jugador();

        jugador.setFotoPerfil("rrrr");
        jugador.setNombre("rrrrr");

        ClienteControlador cliente = new ClienteControlador("localhost", 12345);
        cliente.start();
        cliente.enviarMensaje(jugador);

    }

}

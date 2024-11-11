/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.conexion.EnviarJugador;

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

        jugador.setFotoPerfil("eeee");
        jugador.setNombre("eeeee");

        Evento jugador2 = new EnviarJugador();
        jugador2.setEmisor(jugador);
        jugador2.getEmisor();
        
        ClienteControlador cliente = new ClienteControlador("localhost", 1234);
        cliente.start();
        cliente.enviarMensaje(jugador2);
        

    }

}

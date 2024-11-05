/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;

/**
 *
 * @author elimo
 */
public class pruebasservidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      try {
            Servidor servidor = Servidor.obtenerInstancia();
            servidor.iniciarSocket(12345);  // Escucha en el puerto 12345
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
    
}

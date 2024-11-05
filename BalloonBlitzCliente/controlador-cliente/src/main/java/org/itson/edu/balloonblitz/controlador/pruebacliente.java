/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

/**
 *
 * @author elimo
 */
public class pruebacliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Cliente cliente = new Cliente("localhost", 12345);
        cliente.enviarMensaje("Hola, servidor!", "Cliente");
    }

}

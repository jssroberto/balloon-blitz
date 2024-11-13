/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

import java.util.Scanner;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.*;

/**
 * @author elimo
 */
public class pruebacliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ClienteControlador cliente = new ClienteControlador("localhost", 1234);
        cliente.start();

        System.out.println("Ingrese un número: ");
        int numero = scanner.nextInt();
        if (numero == 6) {
            // Inicializar el objeto Jugador y asignarle valores
            Jugador jugador = new Jugador.Builder().fotoPerfil("nnn").nombre("nnnn").build();

            // Crear un evento de tipo EnviarJugador y asignarle el emisor
            Evento jugador2 = new EnvioJugadorEvento();
            jugador2.setEmisor(jugador);

            // Imprimir el emisor para verificar antes de enviar
            System.out.println("Emisor antes de enviar: " + jugador2.getEmisor().getNombre());

            // Inicializar el cliente y enviar el mensaje al servidor
            // Este método inicia la conexión del cliente con el servidor
            cliente.enviarMensaje(jugador2); // Enviar el evento al servidor


            System.out.println("Evento enviado al servidor.");
        } else {
            System.out.println("Número ingresado no es 6. No se enviará ningún mensaje.");
        }

        scanner.close();
    }

}

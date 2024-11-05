/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author elimo
 */
public class Servidor {

    private static Servidor instancia;
    private ServerSocket serverSocket;

    private Servidor() {

    }

    public static synchronized Servidor obtenerInstancia() {
        if (instancia == null) {
            instancia = new Servidor();
        }
        return instancia;
    }

    public void iniciarSocket(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
        System.out.println("Servidor iniciado en el puerto " + puerto);

        while (true) {
            Socket socketCliente = serverSocket.accept();
            System.out.println("Cliente conectado: " + socketCliente.getInetAddress().getHostAddress());

            ClienteControlador controladorCliente = new ClienteControlador(socketCliente);
            controladorCliente.start();  // Inicia el hilo para manejar la comunicación
        }
    }

    public static void main(String[] args) {
        try {
            Servidor servidor = Servidor.obtenerInstancia();
            servidor.iniciarSocket(12345);  // Escucha en el puerto 12345
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
        }
    }
}



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
            
            ServidorControlador controladorCliente = new ServidorControlador(socketCliente);
            controladorCliente.start();  // Inicia el hilo para manejar la comunicación
            controladorCliente.unirseALobby();
        }
    }

}



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;

/**
 *
 * @author elimo
 */
public class ClienteControlador extends Thread {

    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private static Set<ObjectOutputStream> clientes = new HashSet<>();
    private boolean conectado;

    public ClienteControlador(Socket socket) {
        this.socket = socket;
        try {
            entrada = new ObjectInputStream(socket.getInputStream());
            salida = new ObjectOutputStream(socket.getOutputStream());
            conectado = true;

            synchronized (clientes) {
                clientes.add(salida);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (conectado) {

                Evento mensajeRecibido = (Evento) entrada.readObject();
                procesarMensaje(mensajeRecibido);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            desconectar();
        }
    }

    private void procesarMensaje(Evento mensajeRecibido) {
        System.out.println("Mensaje recibido: " + mensajeRecibido);

        enviarEventoAMisClientes(mensajeRecibido);
    }

    private void enviarEventoAMisClientes(Evento evento) {
        synchronized (clientes) {
            for (ObjectOutputStream cliente : clientes) {
                try {
                    cliente.writeObject(evento);
                    cliente.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void desconectar() {
        conectado = false;
        try {

            synchronized (clientes) {
                clientes.remove(salida);
            }
            entrada.close();
            salida.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

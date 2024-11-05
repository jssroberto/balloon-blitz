/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;

/**
 *
 * @author elimo
 */
public class Cliente extends Thread {

    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private boolean conectado;

    public Cliente(String host, int puerto) {
        try {
            socket = new Socket(host, puerto);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            conectado = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (conectado) {
            
            Evento mensajeRecibido;
            try {
                mensajeRecibido = (Evento) entrada.readObject();
                procesarMensaje(mensajeRecibido);
            } catch (IOException | ClassNotFoundException ex) {
                ex.getMessage();
            }
            
        }
        desconectar();
    }

    private void procesarMensaje(Evento mensajeRecibido) {
       
        System.out.println("Mensaje recibido: " + mensajeRecibido);
    }

    public void enviarMensaje(Evento evento) {
        try {
            salida.writeObject(evento);
            salida.flush(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Evento obtenerEvento() {
        
        //TO DO
        return new Evento("Mensaje de prueba"); 
    }

    private void desconectar() {
        try {
            conectado = false; 
            entrada.close();
            salida.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

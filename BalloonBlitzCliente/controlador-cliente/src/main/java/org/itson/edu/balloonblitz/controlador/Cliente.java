/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.itson.edu.balloonblitz.entidades.Mensaje;

/**
 *
 * @author elimo
 */
public class Cliente {
    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private Mensaje mensaje;

    public Cliente(String host, int puerto) {
        try {
            socket = new Socket(host, puerto);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje(String contenido, String remitente) {
        try {
            mensaje = new Mensaje(contenido, remitente);
            salida.writeObject(mensaje);

            Mensaje respuesta = (Mensaje) entrada.readObject();
            System.out.println("Servidor responde: " + respuesta.getContenido() + ", Enviado por: " + respuesta.getRemitente());

            salida.close();
            entrada.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Cliente cliente = new Cliente("localhost", 12345);
        cliente.enviarMensaje("Hola, servidor!", "Cliente");
    }
    
}

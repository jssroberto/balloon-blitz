
package org.itson.edu.balloonblitz.controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author elimo
 * @param <T>
 */
public class Cliente<T> extends Thread {

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
            
            T mensajeRecibido;
            try {
                mensajeRecibido = (T) entrada.readObject();
                procesarMensaje(mensajeRecibido);
            } catch (IOException | ClassNotFoundException ex) {
                ex.getMessage();
            }
            
        }
        desconectar();
    }

    private void procesarMensaje(T mensajeRecibido) {
       
        System.out.println("Mensaje recibido: " + mensajeRecibido);
    }

    public void enviarMensaje(T evento) {
        try {
            salida.writeObject(evento);
            salida.flush(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private T obtenerEvento() {
        
        //TO DO
        //return new Evento("Mensaje de prueba"); 
        return null;
        
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author elimo
 */
public class ClienteControlador extends Thread{
    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private Mensaje mensajeCliente;
    
    
    public ClienteControlador(Socket socket) {
        this.socket = socket;
        try {
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            mensajeCliente =  (Mensaje) entrada.readObject();
            System.out.println("Cliente dice: " + mensajeCliente.getContenido() + ", Enviado: " + mensajeCliente.getRemitente());

            // Responde al cliente con un nuevo objeto
            Mensaje respuesta = new Mensaje("Hola, cliente!", "Eliana");
            salida.writeObject(respuesta);

          
            entrada.close();
            salida.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    
}

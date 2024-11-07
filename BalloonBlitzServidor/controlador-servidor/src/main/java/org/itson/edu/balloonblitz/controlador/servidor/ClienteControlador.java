
package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author elimo
 * @param <T>
 */
public class ClienteControlador<T> extends Thread {

    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private boolean conectado;

    public ClienteControlador(Socket socket) {
        this.socket = socket;
        try {
            entrada = new ObjectInputStream(socket.getInputStream());
            salida = new ObjectOutputStream(socket.getOutputStream());
            conectado = true;

            Lobby lobby = Lobby.getInstance();
            lobby.agregarCliente(salida);

        } catch (IOException e) {
            e.printStackTrace();
            desconectar(); // Desconectar si ocurre una excepción durante la inicialización
        }
    }

    @Override
    public void run() {
        try {
            while (conectado) {
                T mensajeRecibido = (T) entrada.readObject();
                procesarMensaje(mensajeRecibido);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            desconectar();
        }
    }

    private void procesarMensaje(T mensajeRecibido) {
        System.out.println("Mensaje recibido: " + mensajeRecibido);

    }

    private void desconectar() {
        conectado = false;
        try {

            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

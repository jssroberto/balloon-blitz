package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 *
 * @author elimo
 * @param <T>
 */
public class ServidorControlador<T> extends Thread {

    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private boolean conectado;
    private T mensajeRecibido;
    private ControladorStreams streams;

    public ServidorControlador() {
    }

    public ServidorControlador(Socket socket) {
        this.socket = socket;
        try {
            entrada = new ObjectInputStream(socket.getInputStream());
            salida = new ObjectOutputStream(socket.getOutputStream());
            streams = new ControladorStreams(salida, entrada);
            conectado = true;

        } catch (IOException e) {
            e.printStackTrace();
            desconectar(); // Desconectar si ocurre una excepción durante la inicialización
        }
    }

    @Override
    public void run() {
        try {
            while (conectado) {

                mensajeRecibido = (T) entrada.readObject();
                streams.setObjetoRecibido(mensajeRecibido);
                System.out.println(mensajeRecibido);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            desconectar();
        }
    }

    public void unirseALobby() {
        Lobby lobby = Lobby.getInstance();
        lobby.agregarCliente(streams);
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

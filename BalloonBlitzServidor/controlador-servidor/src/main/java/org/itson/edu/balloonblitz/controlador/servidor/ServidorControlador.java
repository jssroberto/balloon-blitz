package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.itson.edu.balloonblitz.entidades.Jugador;

/**
 *
 * @author elimo
 * @param <T>
 */
public class ServidorControlador{

    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private boolean conectado;
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

    public Jugador recibirJugador() {
        try {
            Jugador jugador = (Jugador) entrada.readObject();
            return jugador;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ServidorControlador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
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

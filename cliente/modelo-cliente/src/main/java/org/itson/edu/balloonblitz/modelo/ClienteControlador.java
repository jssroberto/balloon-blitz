package org.itson.edu.balloonblitz.modelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento.ENVIO_JUGADOR;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoDisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;

/**
 * @author elimo
 */
public class ClienteControlador {

    private static ClienteControlador instancia;

    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private ObservadorDisparo observadorDisparo;
    private ObservadorTiempo observadorTiempo;
    private ObservadorResultado observadorResultado;
    private ObservadorJugador observadorJugador;
    private boolean conectado;
    Evento mensajeRecibido;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public ClienteControlador() {
        executorService.submit(() -> enviarMensaje(null));
        executorService.submit(() -> recibirEvento());
    }

    public static synchronized ClienteControlador getInstancia(String host, int puerto) {
        if (instancia == null) {
            instancia = new ClienteControlador(host, puerto);
        }
        return instancia;
    }

    public void setObservadorConexion(ObservadorTiempo observadorTiempo) {
        this.observadorTiempo = observadorTiempo;
    }

    public void setObservadorDisparo(ObservadorResultado observadorResultado) {
        this.observadorResultado = observadorResultado;
    }

    public void setObservadorDisparo(ObservadorJugador observadorJugador) {
        this.observadorJugador = observadorJugador;
    }

    private ClienteControlador(String host, int puerto) {
        try {
            socket = new Socket(host, puerto);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            salida.flush();
            conectado = true;
        } catch (IOException e) {
        }
    }

    public void recibirEvento() {
        while (conectado) {
            try {
                mensajeRecibido = (Evento) entrada.readObject();
                procesarMensaje(mensajeRecibido);
            } catch (IOException | ClassNotFoundException ex) {
                ex.getMessage();
            }
        }
        desconectar();
    }

    public void enviarMensaje(Evento evento) {
        try {
            if (evento != null) {
                salida.writeObject(evento);
                salida.flush();
            }
        } catch (IOException e) {
        }
    }

    private void procesarMensaje(Evento evento) {
        switch (mensajeRecibido.getTipoEvento()) {
            case TIMEOUT ->
                observadorTiempo.manejarEvento((TimeOutEvento) evento);
            case ENVIO_JUGADOR ->
                observadorJugador.manejarEvento((EnvioJugadorEvento) evento);
            case RESULTADO ->
                observadorResultado.manejarEvento((ResultadoEvento) evento);
            case RESULTADO_DISPARO ->
                observadorDisparo.manejarEvento((ResultadoDisparoEvento) evento);
            default ->
                System.out.println("Tipo de evento no reconocido: " + mensajeRecibido.getTipoEvento());
        }
    }

    private void desconectar() {
        try {
            conectado = false;
            entrada.close();
            salida.close();
            socket.close();
        } catch (IOException e) {
        }
    }
}

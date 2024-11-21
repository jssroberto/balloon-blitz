package org.itson.edu.balloonblitz.modelo;

import org.itson.edu.balloonblitz.entidades.eventos.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author elimo
 */
public class ConexionCliente {

    private static ConexionCliente instancia;

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

    TimeOutEvento timeOutEvento;

    private ConexionCliente() {
        try {
            socket = new Socket("localhost", 1234);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            salida.flush();
            conectado = true;
        } catch (IOException e) {
        }
    }

    public static synchronized ConexionCliente getInstancia() {
        if (instancia == null) {
            instancia = new ConexionCliente();
            instancia.iniciarStreams();
        }
        return instancia;
    }

    private void iniciarStreams() {
        executorService.submit(() -> recibirEvento());
        executorService.submit(() -> enviarMensaje(null));
    }

    public void setObservadorTiempo(ObservadorTiempo observadorTiempo) {
        this.observadorTiempo = observadorTiempo;
    }

    public void eliminarObservadorTiempo() {
        this.observadorTiempo = null;
    }

    public void setObservadorResultado(ObservadorResultado observadorResultado) {
        this.observadorResultado = observadorResultado;
    }

    public void setObservadorDisparo(ObservadorDisparo observadorDisparo) {
        this.observadorDisparo = observadorDisparo;
    }

    public void setObservadorJugador(ObservadorJugador observadorJugador) {
        this.observadorJugador = observadorJugador;
    }

    public void recibirEvento() {
        while (conectado) {
            try {
                mensajeRecibido = (Evento) entrada.readObject();
                System.out.println("prendido");
                if (mensajeRecibido != null) {
                    System.out.println(mensajeRecibido.getTipoEvento());
                    procesarMensaje(mensajeRecibido);
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
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
        switch (evento.getTipoEvento()) {
            case TIMEOUT:
                if (observadorTiempo != null) {
                    observadorTiempo.manejarEvento((TimeOutEvento) evento);
                }
            case ENVIO_JUGADOR:
                observadorJugador.manejarEvento((EnvioJugadorEvento) evento);
            case RESULTADO:
                System.out.println("entró");
                if (observadorResultado != null) {
                    System.out.println("volvio a entrar");
                    observadorResultado.manejarEvento((ResultadoEvento) evento);
                }
                break;
            case RESULTADO_DISPARO:
                observadorDisparo.manejarEvento((ResultadoDisparoEvento) evento);
            default:
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

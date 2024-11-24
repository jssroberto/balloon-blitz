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

    public void setObservadorResultado(ObservadorResultado observadorResultado) {
        this.observadorResultado = observadorResultado;
    }

    public void setObservadorDisparo(ObservadorDisparo observadorDisparo) {
        this.observadorDisparo = observadorDisparo;
    }

    public void setObservadorJugador(ObservadorJugador observadorJugador) {
        this.observadorJugador = observadorJugador;
    }

    public void eliminarTodosLosObservadores() {
        this.observadorTiempo = null;
        this.observadorResultado = null;
        this.observadorDisparo = null;
        this.observadorJugador = null;
    }

    public void recibirEvento() {
        while (conectado) {
            try {
                mensajeRecibido = (Evento) entrada.readObject();
                System.out.println("evento recien llegado " + mensajeRecibido.getTipoEvento());
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
                System.out.println("enviando evento" + evento.getTipoEvento());
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
                    System.out.println("entrando al timeout");
                    TimeOutEvento time = (TimeOutEvento) evento;
                    System.out.println(time.getTiempoRestante());
                    observadorTiempo.manejarEvento((TimeOutEvento) evento);
                } else {
                    System.out.println("observador tiempo nulo");
                }
                break;
            case ENVIO_JUGADOR:
                if (observadorJugador != null) {
                    observadorJugador.manejarEvento((EnvioJugadorEvento) evento);
                } else {
                    System.out.println("observador jugador nulo");
                }
                break;
            case RESULTADO:
                if (observadorResultado != null) {
                    observadorResultado.manejarEvento((ResultadoEvento) evento);
                } else {
                    System.out.println("observador resultado nulo");
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

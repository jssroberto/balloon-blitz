package org.itson.edu.balloonblitz.controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;

/**
 * @author elimo
 */
public class ClienteControlador extends Thread {

    private static ClienteControlador instancia;

    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private TimeOutEvento timeOut;
    private boolean conectado;
    Evento mensajeRecibido;
    int tiempoRestante;

    public ClienteControlador(String host, int puerto) {
        try {
            socket = new Socket(host, puerto);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            salida.flush();
            conectado = true;
        } catch (IOException e) {
        }
    }

    public static synchronized ClienteControlador getInstancia(String host, int puerto) {
        if (instancia == null) {
            instancia = new ClienteControlador(host, puerto);
        }
        return instancia;
    }

    @Override
    public void run() {
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

    private void procesarMensaje(Evento mensajeRecibido) {
        switch (mensajeRecibido.getTipoEvento()) {
            case TIMEOUT -> manejarTimeOut((TimeOutEvento) mensajeRecibido);
            case ENVIO_JUGADOR -> manejarEnvioJugador(mensajeRecibido);
            default -> System.out.println("Tipo de evento no reconocido: " + mensajeRecibido.getTipoEvento());
        }
    }

    private void manejarTimeOut(TimeOutEvento timeOut) {
        tiempoRestante = timeOut.getTiempoRestante();
        if (tiempoRestante > 0) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                if (tiempoRestante > 0) {
                    System.out.println("El temporizador está corriendo. Tiempo restante: " + tiempoRestante + " minutos.");
                    tiempoRestante--;
                } else {
                    System.out.println("El tiempo ha expirado. Has perdido tu turno.");
                    scheduler.shutdown();
                }
            }, 0, 1, TimeUnit.SECONDS);
        } else {
            System.out.println("El tiempo ha expirado. Has perdido tu turno.");
        }
    }

    private void manejarEnvioJugador(Evento mensajeRecibido) {
        System.out.println(mensajeRecibido.getEmisor());
    }

    public void enviarMensaje(Evento evento) {
        try {
            System.out.println(evento.getTipoEvento());
            salida.writeObject(evento);
            salida.flush();
        } catch (IOException e) {
        }
    }

    private Evento obtenerEvento() {
        return mensajeRecibido;
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

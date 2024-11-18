package org.itson.edu.balloonblitz.modelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import static org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento.ENVIO_JUGADOR;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;

import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;

/**
 * @author elimo
 */
public class ClienteControlador extends Thread {

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
    int tiempoRestante;

    public ClienteControlador() {
    }

    public static synchronized ClienteControlador getInstancia(String host, int puerto) {
        if (instancia == null) {
            instancia = new ClienteControlador(host, puerto);
        }
        return instancia;
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

    private void procesarMensaje(Evento evento) {
        switch (mensajeRecibido.getTipoEvento()) {
            case TIMEOUT ->
                observadorTiempo.manejarDisparo((TimeOutEvento)evento);
            case ENVIO_JUGADOR ->
                observadorJugador.manejarDisparo((EnvioJugadorEvento)evento);
            case RESULTADO ->
               observadorResultado.manejarDisparo((ResultadoEvento)evento);
            case RESULTADO_DISPARO ->
                 observadorDisparo.manejarDisparo((DisparoEvento)evento);
            default ->
                System.out.println("Tipo de evento no reconocido: " + mensajeRecibido.getTipoEvento());
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

    
    public void setObservadorConexion(ObservadorTiempo observadorTiempo) {
        this.observadorTiempo = observadorTiempo;
    }

    
    public void setObservadorDisparo(ObservadorResultado observadorResultado) {
        this.observadorResultado = observadorResultado;
    }
    public void setObservadorDisparo(ObservadorJugador observadorJugador) {
        this.observadorJugador = observadorJugador;
    }


    public void enviarMensaje(Evento evento) {
        try {
            System.out.println(evento.getTipoEvento());
            salida.writeObject(evento);
            salida.flush();
        } catch (IOException e) {
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

package org.itson.edu.balloonblitz.controlador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;

/**
 *
 * @author elimo
 *
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
            e.printStackTrace();
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

    private void procesarMensaje(Evento mensajeRecibido) {

        if (mensajeRecibido.getTipoEvento() == TipoEvento.TIMEOUT) {
            TimeOutEvento timeOut = (TimeOutEvento) mensajeRecibido;
            tiempoRestante = timeOut.getTiempoRestante();

            if (tiempoRestante > 0) {
                // Crear un temporizador que se ejecute cada segundo
                new Thread(() -> {
                    while (tiempoRestante > 0) {
                        System.out.println("El temporizador está corriendo. Tiempo restante: " + tiempoRestante + " minutos.");
                        try {
                            // Esperar 1 segundo antes de actualizar el tiempo
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        tiempoRestante--; // Disminuir el tiempo restante
                    }

                }).start();  // Iniciar el hilo en segundo plano
            } else {
                System.out.println("El tiempo ha expirado. Has perdido tu turno.");
            }
        } else if (mensajeRecibido.getTipoEvento() == TipoEvento.ENVIO_JUGADOR) {
            System.out.println(mensajeRecibido.getEmisor());
        }
    }

    public void enviarMensaje(Evento evento) {
        try {
            System.out.println(evento.getTipoEvento());
            salida.writeObject(evento);
            salida.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

}

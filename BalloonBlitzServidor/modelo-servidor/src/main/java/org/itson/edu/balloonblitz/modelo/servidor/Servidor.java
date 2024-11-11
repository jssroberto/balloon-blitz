/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.modelo.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.PosicionarNaves;
import org.itson.edu.balloonblitz.entidades.eventos.conexion.EnviarJugador;

/**
 * Clase que representa el servidor
 *
 * @author elimo
 */
public final class Servidor {

    private static Servidor instancia;
    private ConexionObserver observadorConexion;
    private EventoObserver observadorEventos;
    private ControladorStreams streams;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private ServerSocket serverSocket;

    /**
     * Constructor que inicializa el puerto del socket y llama al hilo de
     * aceptacion de clientes
     */
    public Servidor() {

        try {
            serverSocket = new ServerSocket(1234);

            iniciarHiloDeAceptacion();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Singleton del servidor
     *
     * @return Nueva instancia si no se ha creado o la instancia existente si ya
     * se creó
     */
    public static Servidor getInstance() {
        if (instancia == null) {
            instancia = new Servidor();
        }
        return instancia;
    }

    /**
     * Metodo que establece el observador de la conexion
     *
     * @param observadorConexion Observador de la conexion
     */
    public void setObservadorConexion(ConexionObserver observadorConexion) {
        this.observadorConexion = observadorConexion;
    }

    /**
     * Metodo que establece el observador de eventos
     *
     * @param observadorEventos Observador de los eventos
     */
    public void setObservadorEventos(EventoObserver observadorEventos) {
        this.observadorEventos = observadorEventos;
    }

    /**
     * Metodo que crea un hilo para la recepcion continua de clientes
     */
    private void iniciarHiloDeAceptacion() {
        executorService.submit(() -> {
            aceptarClientes(); // Puedes decidir si quieres reiniciar el hilo o detenerlo completamente
        });
    }

    /**
     * Metodo que acepta sockets de clientes continuamente, de igual forma crea
     * hilos para recepcion y envio de datos
     *
     * @throws IOException Si algo salio mal durante la aceptación de sockets
     */
    public void aceptarClientes() {
        while (true) {
            try {
                // Esperamos por una nueva conexión
                Socket socketCliente = serverSocket.accept();

                // Crear flujos de entrada y salida para el cliente
                streams = new ControladorStreams(
                        new ObjectOutputStream(socketCliente.getOutputStream()),
                        new ObjectInputStream(socketCliente.getInputStream())
                );
                executorService.submit(() -> recibirDatosCiente(streams.getEntrada()));
                executorService.submit(() -> mandarDatosCliente(streams.getSalida(), new EnviarJugador()));

                System.out.println("Cliente conectado con éxito: " + socketCliente.getInetAddress());

                // Notificar al observador de conexión
                if (observadorConexion != null) {
                    observadorConexion.clienteConectado(streams);
                }

            } catch (IOException e) {
                System.err.println("Error aceptando clientes: " + e.getMessage());

                // Verificamos si el ServerSocket está cerrado
                if (serverSocket.isClosed()) {
                    System.err.println("El ServerSocket está cerrado, no se pueden aceptar más conexiones.");
                    break;  // Romper el bucle si el ServerSocket se cierra
                }

                // Si no es un error fatal, continuamos aceptando conexiones
                continue;
            }
        }
    }

    /**
     * Metodo que recibe eventos de los clientes y manda al observador de
     * eventos para su manejo
     *
     * @param entrada Input del usuario a recibir datos
     */
    public void recibirDatosCiente(ObjectInputStream entrada) {

        try {
            Evento evento = (Evento) entrada.readObject(); // Esto bloqueará hasta que llegue un objeto.
            if (observadorEventos != null) {
                observadorEventos.manejarEvento(evento, entrada);
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Error al recibir el evento: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    /**
     * Metodo que envia eventos a socket proporcionado
     *
     * @param salida ObjectOutputStream del jugador a enviar
     * @param evento Evento a enviar
     */
    public void mandarDatosCliente(ObjectOutputStream salida, Evento evento) {
        try {
            salida.writeObject(evento);
            salida.flush();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

package org.itson.edu.balloonblitz.modelo.servidor;

import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.tinylog.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

/**
 * Clase que representa el servidor
 *
 * @author elimo
 */
public final class Servidor {

    private static Servidor instancia;
    private ConexionObserver observadorConexion;
    private EventoObserver observadorEventos;
    private JugadorObserver observadorJugador;
    private final ExecutorService executorService = Executors.newFixedThreadPool(500);
    private ServerSocket serverSocket;

    /**
     * Constructor que inicializa el puerto del socket y llama al hilo de
     * aceptación de clientes
     */
    public Servidor() {
        try {
            serverSocket = new ServerSocket(1234);
            iniciarHiloDeAceptacion();
        } catch (IOException ex) {
            Logger.error("Error al crear el ServerSocket: {}", ex.getMessage());
        }
    }

    /**
     * Singleton del servidor
     *
     * @return Nueva instancia si no se ha creado o la instancia existente si ya
     * se creó
     */
    public static synchronized Servidor getInstance() {
        if (instancia == null) {
            instancia = new Servidor();
        }
        return instancia;
    }

    /**
     * Método que establece el observador de la conexión
     *
     * @param observadorConexion Observador de la conexión
     */
    public void setObservadorConexion(ConexionObserver observadorConexion) {
        this.observadorConexion = observadorConexion;
    }

    /**
     * Método que establece el observador de eventos
     *
     * @param observadorEventos Observador de los eventos
     */
    public void setObservadorEventos(EventoObserver observadorEventos) {
        this.observadorEventos = observadorEventos;
    }

    /**
     * Método que establece el observador de jugadores
     *
     * @param observadorJugador Observador de los jugadores
     */
    public void setObservadorJugadores(JugadorObserver observadorJugador) {
        this.observadorJugador = observadorJugador;
    }

    /**
     * Método que crea un hilo para la recepción continua de clientes
     */
    private void iniciarHiloDeAceptacion() {
        // Puedes decidir si quieres reiniciar el hilo o detenerlo completamente
        executorService.submit(this::aceptarClientes);
    }

    /**
     * Método que acepta sockets de clientes continuamente, de igual forma crea
     * hilos para recepción y envío de datos
     */
    public void aceptarClientes() {
        while (true) {
            try {
                // Esperamos por una nueva conexión
                Socket socketCliente = serverSocket.accept();

                // Crear flujos de entrada y salida para el cliente
                ControladorStreams streams = new ControladorStreams(
                        new ObjectOutputStream(socketCliente.getOutputStream()),
                        new ObjectInputStream(socketCliente.getInputStream())
                );
                Logger.info("Cliente conectado con éxito: {}", socketCliente.getInetAddress());

                // Notificar al observador de conexión
                if (observadorConexion != null) {
                    observadorConexion.clienteConectado(streams);
                }

            } catch (IOException e) {
                Logger.error("Error al aceptar clientes: {}", e.getMessage());

                // Verificamos si el ServerSocket está cerrado
                if (serverSocket.isClosed()) {
                    Logger.error("El ServerSocket está cerrado, no se pueden aceptar más conexiones.");
                    break;  // Romper el bucle si el ServerSocket se cierra
                }
            }
        }
    }

    /**
     * Método que recibe eventos de los clientes y manda al observador de
     * eventos para su manejo
     *
     * @param entrada Input del usuario a recibir datos
     */
    public void recibirDatosCiente(ObjectInputStream entrada) {
        while (true) { // Bucle infinito para mantener la escucha
            try {
                // Leer el objeto enviado por el cliente
                Evento evento = (Evento) entrada.readObject(); // Esto bloqueará hasta que llegue un objeto

                System.out.println(evento.getTipoEvento());
                System.out.println(evento.getEmisor());
                if (evento.getTipoEvento() == TipoEvento.ENVIO_JUGADOR) {
                    if (observadorJugador != null) {
                        observadorJugador.agregarJugador(evento, entrada);
                    }
                } else {
                    if (observadorEventos != null) {
                        observadorEventos.manejarEvento(evento, entrada);
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                // Detecta la desconexión y realiza las acciones correspondientes
                System.err.println("Cliente desconectado: " + entrada);
                if (observadorJugador != null) {
                    observadorJugador.eliminarCliente(entrada);  // Elimina el cliente
                }
                break; // Salir del bucle para evitar más intentos de lectura
            }
        }
    }

    /**
     * Método que envía eventos a socket proporcionado
     *
     * @param salida ObjectOutputStream del jugador a enviar
     * @param evento Evento a enviar
     */
    public synchronized void mandarDatosCliente(ObjectOutputStream salida, Evento evento) {
        try {
           
                System.out.println("enviando desde servidor "+evento.getTipoEvento());
                salida.writeObject(evento);
                salida.flush();
            
        } catch (IOException ex) {
            Logger.error("Error al enviar datos al cliente: {}", ex.getMessage());
        }
    }

    /**
     * Método para cerrar todos los recursos y hilos correctamente
     */
    public void detenerServidor() {
        try {
            // Detener hilos de clientes
            executorService.shutdownNow();  // Detener todos los hilos activos
            serverSocket.close();  // Cerrar el ServerSocket

            // Si es necesario, interrumpir hilos adicionales aquí
            // Asegúrate de limpiar cualquier recurso pendiente
        } catch (IOException e) {
            Logger.error("Error al cerrar el servidor: {}", e.getMessage());
        }
    }

}

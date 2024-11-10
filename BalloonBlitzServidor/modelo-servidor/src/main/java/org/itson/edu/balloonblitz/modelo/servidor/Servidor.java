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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.UnirseAServidor;

/**
 *
 * @author elimo
 */
public class Servidor {

    private final List<ControladorStreams> conexiones;
    private ConexionObserver observadorConexion;
    private EventoObserver observadorEventos;
    private ControladorStreams conexion;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private ServerSocket serverSocket;
    private final int MINIMO_JUGADORES = 2;
    private Set<Partida> partidas = new HashSet<>();

    public Servidor() {

        this.conexiones = new ArrayList<>();
    }

    public void setObservadorConexion(ConexionObserver observadorConexion) {
        this.observadorConexion = observadorConexion;
    }

    public void setObservadorEventos(EventoObserver observadorEventos) {
        this.observadorEventos = observadorEventos;
    }

    // Método para aceptar conexiones de jugadores
    public void aceptarClientes() throws IOException {
        while (true) {
            Socket socketCliente = serverSocket.accept();  // Acepta la conexión del cliente
            Jugador jugador = new Jugador();  // Crear un jugador nuevo
            conexion = new ControladorStreams(new ObjectOutputStream(socketCliente.getOutputStream()), new ObjectInputStream(socketCliente.getInputStream()));

            conexiones.add(conexion);
            executorService.submit(() -> recibirDatosCiente(conexion.getEntrada()));
            executorService.submit(() -> mandarDatosCliente(conexion.getSalida(), new UnirseAServidor()));
            if (observadorConexion != null) {
                observadorConexion.clienteConectado(conexion);
            }

        }
    }

    public void recibirDatosCiente(ObjectInputStream entrada) {

        try {
            Evento evento = (Evento) entrada.readObject();
            if (observadorEventos != null) {
                observadorEventos.manejarEvento(evento);
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public void mandarDatosCliente(ObjectOutputStream salida, Evento evento) {

        try {

            salida.writeObject(evento);
            salida.flush();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

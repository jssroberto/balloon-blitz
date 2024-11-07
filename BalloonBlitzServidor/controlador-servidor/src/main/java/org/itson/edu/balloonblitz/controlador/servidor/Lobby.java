package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Lobby {

    private static final int NUMERO_JUGADORES_NECESARIOS = 2;
    private static final Set<ControladorStreams> clientesEnLobby = ConcurrentHashMap.newKeySet();
    private static final List<List<ControladorStreams>> partidas = new ArrayList<>();
    private ManejadorPartida manejador;

    private static Lobby instancia;

    public static synchronized Lobby getInstance() {
        if (instancia == null) {
            instancia = new Lobby();
        }
        return instancia;
    }

    
    public synchronized void agregarCliente(ObjectOutputStream salida, ObjectInputStream entrada) {
        clientesEnLobby.add(new ControladorStreams(salida, entrada));
        verificarYCrearPartida();
    }

    public synchronized void eliminarCliente(ObjectOutputStream salida) {
        clientesEnLobby.removeIf(cliente -> cliente.getSalida().equals(salida));
    }

    private synchronized void verificarYCrearPartida() {
        if (clientesEnLobby.size() >= NUMERO_JUGADORES_NECESARIOS) {
            List<ControladorStreams> nuevaPartida = new ArrayList<>();

            for (ControladorStreams cliente : clientesEnLobby) {
                nuevaPartida.add(cliente);
                if (nuevaPartida.size() == NUMERO_JUGADORES_NECESARIOS) {
                    break;
                }
            }

            clientesEnLobby.removeAll(nuevaPartida);
            partidas.add(nuevaPartida);
            manejador = new ManejadorPartida(nuevaPartida);
//            manejador.empezarPartida();
        }
    }

    private void notificarJugadoresPartida(List<ControladorStreams> jugadores) {
        String mensajeInicio = "¡La partida está lista! Puedes comenzar a jugar.";
        for (ControladorStreams jugador : jugadores) {
            try {
                jugador.getSalida().writeObject(mensajeInicio);
                jugador.getSalida().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

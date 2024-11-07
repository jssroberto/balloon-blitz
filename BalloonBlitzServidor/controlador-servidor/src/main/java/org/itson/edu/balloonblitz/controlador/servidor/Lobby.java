package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author elimo
 */
public class Lobby {

    private static final int NUMERO_JUGADORES_NECESARIOS = 2; // Número de jugadores por partida
    private static final Set<ObjectOutputStream> clientesEnLobby = ConcurrentHashMap.newKeySet();
    private static final List<List<ObjectOutputStream>> partidas = new ArrayList<>();
    private ManejadorPartida manejador;

    private static Lobby instancia;

    public static synchronized Lobby getInstance() {
        if (instancia == null) {
            instancia = new Lobby();

        }
        return instancia;
    }

    public synchronized void agregarCliente(ObjectOutputStream cliente) {
        clientesEnLobby.add(cliente);
        verificarYCrearPartida();
    }

    public synchronized void eliminarCliente(ObjectOutputStream cliente) {
        clientesEnLobby.remove(cliente);
    }

    private synchronized void verificarYCrearPartida() {
        if (clientesEnLobby.size() >= NUMERO_JUGADORES_NECESARIOS) {
            List<ObjectOutputStream> nuevaPartida = new ArrayList<>();

            for (ObjectOutputStream cliente : clientesEnLobby) {
                nuevaPartida.add(cliente);
                if (nuevaPartida.size() == NUMERO_JUGADORES_NECESARIOS) {
                    break;
                }
            }

            clientesEnLobby.removeAll(nuevaPartida);
            partidas.add(nuevaPartida);
            manejador = new ManejadorPartida(nuevaPartida);
            notificarJugadoresPartida(nuevaPartida);
        }
    }

    private void notificarJugadoresPartida(List<ObjectOutputStream> jugadores) {
        String mensajeInicio = "¡La partida está lista! Puedes comenzar a jugar.";
        for (ObjectOutputStream jugador : jugadores) {
            try {
                jugador.writeObject(mensajeInicio);
                jugador.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.modelo.servidor.ConexionObserver;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.itson.edu.balloonblitz.modelo.servidor.ControladorStreams;

public class Lobby implements ConexionObserver{

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

    
    public synchronized void agregarCliente(ControladorStreams streams) {
        clientesEnLobby.add(streams);
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
            
            // Metodo a implementar manejador.empezarPartida();
        }
    }

    @Override
    public void clienteConectado(ControladorStreams streams) {
        agregarCliente(streams);
    }

    
}

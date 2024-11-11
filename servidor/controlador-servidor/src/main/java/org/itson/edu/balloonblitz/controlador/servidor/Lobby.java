package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.modelo.servidor.ConexionObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.itson.edu.balloonblitz.modelo.servidor.ControladorStreams;
import org.itson.edu.balloonblitz.modelo.servidor.EventoObserver;
import org.itson.edu.balloonblitz.modelo.servidor.Servidor;

/**
 * Clase que representa un lobby para emparejamiento
 *
 * @author elimo
 */
public class Lobby implements ConexionObserver {

    private static final int NUMERO_JUGADORES_NECESARIOS = 2;
    private static final Set<ControladorStreams> clientesEnLobby = ConcurrentHashMap.newKeySet();
    private static final List<List<ControladorStreams>> partidas = new ArrayList<>();
    private ManejadorPartida manejador;
    private static Lobby instancia;

    /**
     * Singleton para el lobby
     *
     * @return Intancia de lobby
     */
    public static synchronized Lobby getInstance() {
        if (instancia == null) {
            instancia = new Lobby();
        }
        return instancia;
    }

    /**
     * Metodo para agregar un cliente al lobby
     *
     * @param streams I/O del cliente
     */
    public synchronized void agregarCliente(ControladorStreams streams) {
        clientesEnLobby.add(streams);
        verificarYCrearPartida();
    }

    /**
     * Metodo que elimina un cliente del lobby
     *
     * @param streams Streams del cliente a eliminar
     */
    public synchronized void eliminarCliente(ControladorStreams streams) {
        clientesEnLobby.remove(streams);
    }

    /**
     * Metodo que simula el emparejamiento de jugadores en una partida
     */
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
            EventoObserver evento = new ManejadorPartida(nuevaPartida);
            Servidor.getInstance().setObservadorEventos(evento);

        }
    }

    /**
     * Metodo de escuchador que agrega clientes al lobby
     *
     * @param streams I/O del cliente
     */
    @Override
    public void clienteConectado(ControladorStreams streams) {
        agregarCliente(streams);
    }

}

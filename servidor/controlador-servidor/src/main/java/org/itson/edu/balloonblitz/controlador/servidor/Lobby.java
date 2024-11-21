package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.ObjectInputStream;
import org.itson.edu.balloonblitz.modelo.servidor.ConexionObserver;
import org.itson.edu.balloonblitz.modelo.servidor.ControladorStreams;
import org.itson.edu.balloonblitz.modelo.servidor.EventoObserver;
import org.itson.edu.balloonblitz.modelo.servidor.Servidor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.modelo.servidor.JugadorObserver;
import org.tinylog.Logger;

/**
 * Clase que representa un lobby para emparejamiento
 *
 * @author elimo
 */
public class Lobby implements ConexionObserver, JugadorObserver {

    private static final int NUMERO_JUGADORES_NECESARIOS = 2;
    private static final Map<ControladorStreams, Jugador> clientesEnLobby = new HashMap<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static final List<Map<ControladorStreams, Jugador>> partidas = new ArrayList<>();
    private static Lobby instancia;

    /**
     * Singleton para el lobby
     *
     * @return Intancia de lobby
     */
    public static synchronized Lobby getInstancia() {
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
        Servidor.getInstance().setObservadorJugadores(this);
        clientesEnLobby.put(streams, null);
        executorService.submit(() -> Servidor.getInstance().recibirDatosCiente(streams.getEntrada()));
        executorService.submit(() -> Servidor.getInstance().mandarDatosCliente(streams.getSalida(), null));
    }

    @Override
    public synchronized void eliminarCliente(ObjectInputStream entrada) {
        // Buscar el ControladorStreams correspondiente a la entrada
        ControladorStreams controlador = clientesEnLobby.keySet().stream()
                .filter(c -> c.getEntrada().equals(entrada))
                .findFirst()
                .orElse(null);

        if (controlador != null) {
            // Si se encuentra el controlador, eliminamos el cliente del lobby
            Jugador jugador = clientesEnLobby.remove(controlador);
            System.out.println("Cliente desconectado y eliminado del lobby: "
                    + (jugador != null ? jugador.getNombre() : "Desconocido"));
        } else {
            // Si no se encuentra el controlador en el mapa
            System.err.println("No se encontró un cliente con la entrada proporcionada.");
        }
    }

    /**
     * Metodo que simula el emparejamiento de jugadores en una partida
     */
    private synchronized void verificarYCrearPartida() {
        // Filtra los clientes que tienen jugadores asociados
        Map<ControladorStreams, Jugador> jugadoresListos = clientesEnLobby.entrySet().stream()
                .filter(entry -> entry.getValue() != null) // Solo considera los streams con jugadores asignados
                .limit(NUMERO_JUGADORES_NECESARIOS) // Toma solo el número necesario para una partida
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); // Convierte nuevamente a un Map

        // Verifica si hay suficientes jugadores listos para una partida
        if (jugadoresListos.size() == NUMERO_JUGADORES_NECESARIOS) {
            // Elimina los jugadores seleccionados del lobby
            jugadoresListos.keySet().forEach(clientesEnLobby::remove);

            // Agrega la partida a la lista de partidas
            partidas.add(jugadoresListos);

            Logger.info("Nueva partida creada con jugadores: {}",
                    jugadoresListos.values().stream().map(Jugador::getNombre).toList());

            // Crea un manejador de partidas y lo notifica al servidor
            EventoObserver evento = new ManejadorPartida(jugadoresListos);
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

    @Override
    public void agregarJugador(Evento evento, ObjectInputStream entrada) {
        Jugador jugador = (Jugador) evento.getEmisor(); // Extrae el jugador del evento

        // Busca el ControladorStreams correspondiente basado en la entrada
        ControladorStreams controlador = clientesEnLobby.keySet().stream()
                .filter(c -> c.getEntrada().equals(entrada))
                .findFirst()
                .orElse(null);

        if (controlador != null) {
            clientesEnLobby.put(controlador, jugador); // Asocia el jugador con su ControladorStreams
            Logger.info("Jugador asociado correctamente: {}", jugador.getNombre());
            verificarYCrearPartida(); // Intenta crear partidas si es posible
        } else {
            Logger.warn("No se encontró un ControladorStreams asociado a esta entrada.");
        }
    }

}

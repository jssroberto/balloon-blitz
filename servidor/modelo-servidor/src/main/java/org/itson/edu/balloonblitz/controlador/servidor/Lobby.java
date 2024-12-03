package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.modelo.servidor.ConexionObserver;
import org.itson.edu.balloonblitz.modelo.servidor.ControladorStreams;
import org.itson.edu.balloonblitz.modelo.servidor.EventoObserver;
import org.itson.edu.balloonblitz.modelo.servidor.JugadorObserver;
import org.itson.edu.balloonblitz.modelo.servidor.Servidor;

public class Lobby implements ConexionObserver, JugadorObserver {

    private static final int NUMERO_JUGADORES_NECESARIOS = 2;
    private static final Map<ControladorStreams, Jugador> clientesEnLobby = new HashMap<>();
    private static final List<Map<ControladorStreams, Jugador>> partidas = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(Lobby.class.getName());
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static Lobby instancia;

    public static synchronized Lobby getInstancia() {
        if (instancia == null) {
            instancia = new Lobby();
        }
        return instancia;
    }

    public synchronized void agregarCliente(ControladorStreams streams) {
        Servidor.getInstance().setObservadorJugadores(this);
        clientesEnLobby.put(streams, null);
        executorService.submit(() -> Servidor.getInstance().recibirDatosCiente(streams.getEntrada()));
        executorService.submit(() -> Servidor.getInstance().mandarDatosCliente(streams.getSalida(), null));
        System.out.println("Número de hilos activos: " + ((ThreadPoolExecutor) executorService).getActiveCount());

        logger.info("Cliente agregado al lobby: " + streams);
    }

    @Override
    public synchronized void eliminarCliente(ObjectInputStream entrada) {
        ControladorStreams controlador = clientesEnLobby.keySet().stream()
                .filter(c -> c.getEntrada().equals(entrada))
                .findFirst()
                .orElse(null);

        if (controlador != null) {
            try {
                controlador.getEntrada().close();
                controlador.getSalida().close();
            } catch (IOException e) {
                logger.warning("Error al cerrar recursos del cliente: " + e.getMessage());
            }

            Jugador jugador = clientesEnLobby.remove(controlador);
            logger.info("Cliente desconectado y eliminado del lobby: " + (jugador != null ? jugador.getNombre() : "Desconocido"));

            // Notificar al otro jugador en la partida
            partidas.stream()
                    .filter(partida -> partida.containsKey(controlador))
                    .findFirst()
                    .ifPresent(partida -> {
                        ManejadorPartida manejadorPartida = new ManejadorPartida(partida);
                        manejadorPartida.manejarDesconexion(controlador);
                    });
        } else {
            logger.warning("No se encontró un cliente con la entrada proporcionada.");
        }
    }

    private synchronized void verificarYCrearPartida() {
        Map<ControladorStreams, Jugador> jugadoresListos = clientesEnLobby.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .limit(NUMERO_JUGADORES_NECESARIOS)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (jugadoresListos.size() == NUMERO_JUGADORES_NECESARIOS) {
            jugadoresListos.keySet().forEach(clientesEnLobby::remove);
            partidas.add(jugadoresListos);

            logger.info("Nueva partida creada con jugadores: " + jugadoresListos.values().stream()
                    .map(Jugador::getNombre)
                    .collect(Collectors.joining(", ")));

            EventoObserver evento = new ManejadorPartida(jugadoresListos);
            Servidor.getInstance().setObservadorEventos(evento);
        }
    }

    @Override
    public void clienteConectado(ControladorStreams streams) {
        agregarCliente(streams);
    }

    @Override
    public void agregarJugador(Evento evento, ObjectInputStream entrada) {
        Jugador jugador = (Jugador) evento.getEmisor();
        ControladorStreams controlador = clientesEnLobby.keySet().stream()
                .filter(c -> c.getEntrada().equals(entrada))
                .findFirst()
                .orElse(null);

        if (controlador != null) {
            clientesEnLobby.put(controlador, jugador);
            logger.info("Jugador asociado correctamente: " + jugador.getNombre());
            verificarYCrearPartida();
        } else {
            logger.warning("No se encontró un ControladorStreams asociado a esta entrada.");
        }
    }
}

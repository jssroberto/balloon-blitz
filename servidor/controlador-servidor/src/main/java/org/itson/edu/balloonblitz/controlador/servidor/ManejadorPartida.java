package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoPartida;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.*;
import org.itson.edu.balloonblitz.modelo.servidor.ControladorStreams;
import org.itson.edu.balloonblitz.modelo.servidor.EventoObserver;
import org.itson.edu.balloonblitz.modelo.servidor.Servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Clase que representa el manejador de la partida
 *
 * @author elimo
 */
public class ManejadorPartida implements EventoObserver {

    private final ControladorStreams streamsJugador1;
    private final ControladorStreams streamsJugador2;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final Servidor servidor;
    private final Partida partida;
    private boolean tiempoExcedido;

    public ManejadorPartida(List<ControladorStreams> jugadores) {
        servidor = Servidor.getInstance();
        streamsJugador1 = jugadores.get(0);
        streamsJugador2 = jugadores.get(1);
        iniciarStreams();
        partida = new Partida();
    }

    private void iniciarStreams() {
        executorService.submit(() -> servidor.recibirDatosCiente(streamsJugador1.getEntrada()));
        executorService.submit(() -> servidor.recibirDatosCiente(streamsJugador2.getEntrada()));
        executorService.submit(() -> servidor.mandarDatosCliente(streamsJugador1.getSalida(), new EnvioJugadorEvento()));
        executorService.submit(() -> servidor.mandarDatosCliente(streamsJugador2.getSalida(), new EnvioJugadorEvento()));
    }

    /**
     * Manda a manejar el evento enviado por el jugador 1
     */
    public void obtenerEventoJugador1() {
        servidor.recibirDatosCiente(streamsJugador1.getEntrada());
    }

    /**
     * Manda a manejar el evento enviado por el jugador2
     */
    public void obtenerEventoJugador2() {
        servidor.recibirDatosCiente(streamsJugador2.getEntrada());
    }

    /**
     * Manda a enviar el resultado de los eventos a los jugadores
     *
     * @param evento Resultado de un evento
     */
    public void enviarEventoAJugador(ObjectOutputStream salida, Evento evento) {
        servidor.mandarDatosCliente(salida, evento);
    }

    /**
     * Metodo suscriptor que maneja el evento obtenido del servidor
     *
     * @param evento Evento enviado por el cliente
     * @param entrada
     */
    @Override
    public void manejarEvento(Evento evento, ObjectInputStream entrada) {

        if (evento.getTipoEvento() == TipoEvento.ENVIO_JUGADOR) {
            if (entrada.equals(streamsJugador1.getEntrada())) {
                partida.setJugador1(evento.getEmisor());
                verificarYCrearPartida();

            } else if (entrada.equals(streamsJugador2.getEntrada())) {
                partida.setJugador2(evento.getEmisor());
                verificarYCrearPartida();
            }
        } else {
            enviarEventoAJugador(streamsJugador1.getSalida(), manejarPartida(evento, entrada));
            enviarEventoAJugador(streamsJugador2.getSalida(), manejarPartida(evento, entrada));

            if (partida.getJugador1().isTurno()) {
                enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
                iniciarTemporizador(30);
                enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(0));
                partida.getJugador1().setTurno(false);
                partida.getJugador2().setTurno(true);
            } else if (partida.getJugador2().isTurno()) {
                enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(30));
                iniciarTemporizador(30);
                enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(0));
                partida.getJugador2().setTurno(false);
                partida.getJugador1().setTurno(true);
            }
        }
    }

    private void verificarYCrearPartida() {
        synchronized (this) {
            if (partida.getJugador1() != null && partida.getJugador2() != null) {
                partida.getJugador1().setTurno(true);
                partida.getJugador2().setTurno(false);
                crearPartida();
            }
        }
    }

    public void crearPartida() {
        partida.setTableroJugador1(new Tablero());
        partida.setTableroJugador2(new Tablero());
        partida.setEstadoPartida(EstadoPartida.ACTIVA);

    }

    public Evento manejarPartida(Evento evento, ObjectInputStream entrada) {

        if (entrada.equals(streamsJugador1.getEntrada())) {
            evento.setEmisor(partida.getJugador1());
        } else if (entrada.equals(streamsJugador2.getEntrada())) {
            evento.setEmisor(partida.getJugador2());
        }

        if (evento.getTipoEvento() == TipoEvento.POSICION_NAVES) {
            ManejadorPosicionNavesDeprecado manejadorPosicion = new ManejadorPosicionNavesDeprecado((PosicionNavesEvento) evento);
            return manejadorPosicion.procesarEvento();

        } else if (evento.getTipoEvento() == TipoEvento.DISPARO) {
            //Obtiene el jugador rival y su tablero correspondientes al servidor
            Tablero tableroRival = obtenerTableroRival(evento.getEmisor());
            Jugador jugadorRival = obtenerJugadorRival(evento.getEmisor());
            ManejadorDisparo manejadorDisparo = new ManejadorDisparo((DisparoEvento) evento, tableroRival, jugadorRival);
            return manejadorDisparo.procesarEvento();
        }
        return null;
    }

    public String iniciarTemporizador(int segundos) {
        CompletableFuture<String> futuro = new CompletableFuture<>();
        tiempoExcedido = false;

        scheduler.schedule(() -> {
            tiempoExcedido = true;
            futuro.complete(mostrarMensajeTiempoExcedido());
        }, segundos, TimeUnit.SECONDS);

        try {
            // Esperamos el resultado del CompletableFuture
            return futuro.get(); // Bloquea hasta que el temporizador termine
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción
            return "Error al iniciar el temporizador";
        }
    }

    private String mostrarMensajeTiempoExcedido() {
        String mensaje = "El tiempo ha expirado.";
        return mensaje;
    }

    private Jugador obtenerJugadorRival(Jugador jugador) {
        return jugador.equals(partida.getJugador1()) ? partida.getJugador2() : partida.getJugador1();
    }

    private Tablero obtenerTableroRival(Jugador jugador) {
        return jugador.equals(partida.getJugador1()) ? partida.getTableroJugador2() : partida.getTableroJugador1();
    }
}

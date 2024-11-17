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
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final Servidor servidor;
    private Partida partida;
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
     * @param evento  Evento enviado por el cliente
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
        }
        manejarPartida(evento, entrada);
    }

    private void verificarYCrearPartida() {
        synchronized (this) {
            if (partida.getJugador1() != null && partida.getJugador2() != null) {
                crearPartida();
            }
        }
    }

    public void crearPartida() {
        partida.setTableroJugador1(new Tablero());
        partida.setTableroJugador2(new Tablero());
        partida.setEstadoPartida(EstadoPartida.ACTIVA);
        System.out.println(partida.getJugador1().getNombre());
        System.out.println(partida.getJugador2().getNombre());
        iniciarTemporizador(10);
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

    public void iniciarTemporizador(int segundos) {

        tiempoExcedido = false;

        // Enviar un mensaje de "Inicio del temporizador" al cliente
        enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(segundos));
        enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(segundos));

        // Ejecutar el temporizador
        scheduler.schedule(() -> {
            tiempoExcedido = true;
            mostrarMensajeTiempoExcedido();

            // Notificar a los jugadores que el tiempo ha expirado
            enviarEventoAJugador(streamsJugador1.getSalida(), new TimeOutEvento(0));  // 0 significa que el tiempo ha expirado
            enviarEventoAJugador(streamsJugador2.getSalida(), new TimeOutEvento(0));
        }, segundos, TimeUnit.SECONDS);

        System.out.println("Temporizador iniciado por " + segundos + " minutos.");
    }

    private void mostrarMensajeTiempoExcedido() {
        System.out.println("El tiempo ha expirado.");
    }

    private Jugador obtenerJugadorRival(Jugador jugador) {
        return jugador.equals(partida.getJugador1()) ? partida.getJugador2() : partida.getJugador1();
    }

    private Tablero obtenerTableroRival(Jugador jugador) {
        return jugador.equals(partida.getJugador1()) ? partida.getTableroJugador2() : partida.getTableroJugador1();
    }
}

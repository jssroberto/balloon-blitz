package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoPartida;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;
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
    private Jugador jugador1;
    private ManejadorTurno turno;
    boolean tiempoExcedido;
    private Jugador jugador2;
    private Partida partida;
    private Tablero tableroJugador1;
    private Tablero tableroJugador2;

    /**
     * Constructor que agrega los I/O de los jugadores
     */
    public ManejadorPartida(List<ControladorStreams> jugadores) {
        servidor = Servidor.getInstance();
        streamsJugador1 = jugadores.get(0);
        streamsJugador2 = jugadores.get(1);
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
     * @param salida1 Output del jugador 1
     * @param evento  Resultado de un evento
     */
    public void enviarEventoAJugador1(ObjectOutputStream salida1, Evento evento) {
        servidor.mandarDatosCliente(salida1, evento);
    }

    public void enviarEventoAJugador2(ObjectOutputStream salida2, Evento evento) {
        servidor.mandarDatosCliente(salida2, evento);
    }

    /**
     * Metodo suscriptor que maneja el evento obtenido del servidor
     *
     * @param evento  Evento enviado por el cliente
     * @param entrada
     */
    @Override
    public void manejarEvento(Evento evento, ObjectInputStream entrada) {
        //por que esto esta afuera del método manejar partida?
        if (evento.getTipoEvento() == TipoEvento.ENVIO_JUGADOR) {
            if (entrada.equals(streamsJugador1.getEntrada())) {

                jugador1 = evento.getEmisor();
                verificarYCrearPartida();

            } else if (entrada.equals(streamsJugador2.getEntrada())) {

                jugador2 = evento.getEmisor();
                verificarYCrearPartida();
            }
        }
        manejarPartida(evento, entrada);
    }

    private void verificarYCrearPartida() {
        if (jugador1 != null && jugador2 != null) { // Primera comprobación (no sincronizada)
            synchronized (this) {
                if (jugador1 != null && jugador2 != null && partida == null) { // Segunda comprobación
                    crearPartida();
                }
            }
        }
    }

    public void crearPartida() {
        partida = new Partida(jugador1, jugador2);
        partida.setTableroJugador1(tableroJugador1);
        partida.setTableroJugador2(tableroJugador2);
        partida.setEstadoPartida(EstadoPartida.ACTIVA);
        System.out.println(jugador1.getNombre());
        System.out.println(jugador2.getNombre());
        iniciarTemporizador(10);

    }

    public Object manejarPartida(Evento evento, ObjectInputStream entrada) {

        if (entrada.equals(streamsJugador1.getEntrada())) {
            evento.setEmisor(jugador1);
        } else if (entrada.equals(streamsJugador2.getEntrada())) {
            evento.setEmisor(jugador2);
        }

        if (evento.getTipoEvento() == TipoEvento.POSICION_NAVES) {
            ManejadorPosicionNaves manejadorPosicion = new ManejadorPosicionNaves(evento);
            return manejadorPosicion.obtenerEvento();

        } else if (evento.getTipoEvento() == TipoEvento.DISPARO) {

            //TODO devolver un evento en lugar de null
            ManejadorDisparo manejadorDisparo = new ManejadorDisparo((DisparoEvento) evento);
            return null;
        }
        return null;
    }

    public void iniciarTemporizador(int segundos) {

        tiempoExcedido = false;

        // Enviar un mensaje de "Inicio del temporizador" al cliente
        enviarEventoAJugador1(streamsJugador1.getSalida(), new TimeOutEvento(segundos));
        enviarEventoAJugador2(streamsJugador2.getSalida(), new TimeOutEvento(segundos));

        // Ejecutar el temporizador
        scheduler.schedule(() -> {
            tiempoExcedido = true;
            mostrarMensajeTiempoExcedido();

            // Notificar a los jugadores que el tiempo ha expirado
            enviarEventoAJugador1(streamsJugador1.getSalida(), new TimeOutEvento(0));  // 0 significa que el tiempo ha expirado
            enviarEventoAJugador2(streamsJugador2.getSalida(), new TimeOutEvento(0));
        }, segundos, TimeUnit.SECONDS);

        System.out.println("Temporizador iniciado por " + segundos + " minutos.");
    }

    private void mostrarMensajeTiempoExcedido() {
        System.out.println("El tiempo ha expirado.");
    }

}

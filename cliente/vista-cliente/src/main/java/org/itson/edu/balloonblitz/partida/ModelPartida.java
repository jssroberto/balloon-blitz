/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.partida;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;

/**
 *
 * @author elimo
 */
public class ModelPartida {

    private final List<ObserverPartida> observers = new ArrayList<>();
    private String texto;
    private final int contador = 0;
    Jugador jugadorRival;
    int tiempoRestante;
    private Tablero tablero;
    private Tablero tableroDeRival;
    private ScheduledExecutorService temporizadorActual;
// Señal para detener el temporizador
    private final AtomicBoolean detener = new AtomicBoolean(false);

    public ModelPartida() {
    }

    public void addObserver(ObserverPartida observer) {
        observers.add(observer);
    }

    public void removeOberver(ObserverPartida observer) {
        observers.remove(observer);
    }

    private void notifyObservers(UpdateEventPartida event) {
        for (ObserverPartida observer : observers) {
            observer.update(event);
        }
    }

    public Jugador getJugadorRival() {
        return jugadorRival;
    }

    public void setJugadorRival(Jugador jugadorRival) {
        this.jugadorRival = jugadorRival;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
        notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_TABLERO_PROPIO));
    }

    public Tablero getTableroDeRival() {
        return tableroDeRival;
    }

    public void setTableroDeRival(Tablero tableroDeRival) {
        this.tableroDeRival = tableroDeRival;
        notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_TABLERO_RIVAL));
    }

    public void notificarJugador() {
        notifyObservers(new UpdateEventPartida(this, EventTypePartida.ENVIAR_JUGADOR));
    }



public void correrTiempo(TimeOutEvento evento) {
    detenerTemporizadorActivo(); // Limpia cualquier temporizador previo.
    tiempoRestante = evento.getTiempoRestante();

    if (tiempoRestante > 0) {
        temporizadorActual = Executors.newSingleThreadScheduledExecutor();
        detener.set(false); // Reinicia la señal de detención.

        temporizadorActual.scheduleAtFixedRate(() -> {
            if (detener.get()) {
                temporizadorActual.shutdown(); // Detenemos el temporizador si se activa la señal.
                return;
            }

            if (tiempoRestante > 0) {
                setTexto(String.valueOf(tiempoRestante));
                notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_LABEL_TIEMPO));
                tiempoRestante--;
            } 
        }, 0, 1, TimeUnit.SECONDS);
    } else if (evento.getTiempoRestante() == 0) {
        setTexto("Tiempo expirado");
        notifyObservers(new UpdateEventPartida(this, EventTypePartida.TIEMPO_TERMINADO));
    }
}

/**
 * Detiene cualquier temporizador activo y reinicia la señal de detención.
 */
private void detenerTemporizadorActivo() {
    if (temporizadorActual != null && !temporizadorActual.isShutdown()) {
        detener.set(true); // Activa la señal para detener el temporizador actual.
        temporizadorActual.shutdown();
        try {
            if (!temporizadorActual.awaitTermination(1, TimeUnit.SECONDS)) {
                temporizadorActual.shutdownNow(); // Forzar detención si no termina en el tiempo esperado.
            }
        } catch (InterruptedException e) {
            temporizadorActual.shutdownNow();
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción del hilo.
        }
    }
}


    public void obtenerTurno(ResultadoEvento evento) {
        if (evento.isValid()) {
            notifyObservers(new UpdateEventPartida(this, EventTypePartida.TURNO_ACTIVO));
        } else {
            notifyObservers(new UpdateEventPartida(this, EventTypePartida.TURNO_INACTIVO));
        }
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}

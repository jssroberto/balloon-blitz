/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.partida;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.itson.edu.balloonblitz.entidades.*;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;
import org.itson.edu.balloonblitz.vista.music.MusicPlayer;

/**
 * @author elimo
 */
public class ModelPartida {

    private final List<ObserverPartida> observers = new ArrayList<>();
    private String textoTiempoRestante;
    private int tiempoRestante;
    private Jugador jugadorRival;
    private Tablero tablero;
    private Tablero tableroDeRival;
    private ScheduledExecutorService temporizadorActual;
    private final AtomicBoolean detener = new AtomicBoolean(false);
    private Casilla ultimoDisparo;
    private final MusicPlayer playerPop;
    private final MusicPlayer playerQuack;
    private final MusicPlayer playerExplosion;
    private boolean victoria;

    public ModelPartida() {
        playerPop = new MusicPlayer("/audio/sound-effects/pop.wav");
        playerQuack = new MusicPlayer("/audio/sound-effects/quack.wav");
        playerExplosion = new MusicPlayer("/audio/sound-effects/explosion.wav");
    }

    public void addObserver(ObserverPartida observer) {
        observers.add(observer);
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

    public String getTextoTiempoRestante() {
        return textoTiempoRestante;
    }

    public void setTextoTiempoRestante(String textoTiempoRestante) {
        this.textoTiempoRestante = textoTiempoRestante;
    }

    public MusicPlayer getPlayerPop() {
        return playerPop;
    }

    public MusicPlayer getPlayerQuack() {
        return playerQuack;
    }

    public MusicPlayer getPlayerExplosion() {
        return playerExplosion;
    }

    public Casilla getUltimoDisparo() {
        return ultimoDisparo;
    }

    public void setUltimoDisparo(Casilla ultimoDisparo) {
        this.ultimoDisparo = ultimoDisparo;
        notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_ULTIMO_DISPARO));
    }

    public boolean isVictoria() {
        return victoria;
    }

    public void setVictoria(boolean victoria) {
        this.victoria = victoria;
        notifyObservers(new UpdateEventPartida(this, EventTypePartida.VICTORIA));
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
                    setTextoTiempoRestante(String.valueOf(tiempoRestante));
                    notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_LABEL_TIEMPO));
                    tiempoRestante--;
                }
            }, 0, 1, TimeUnit.SECONDS);
        } else if (evento.getTiempoRestante() == 0) {
            setTextoTiempoRestante("Tiempo expirado");
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
}

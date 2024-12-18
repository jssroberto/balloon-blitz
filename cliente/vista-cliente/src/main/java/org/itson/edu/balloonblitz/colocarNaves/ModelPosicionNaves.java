/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.colocarNaves;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;

/**
 *
 * @author elimo
 */
public class ModelPosicionNaves {

    private String texto;
    Jugador jugador;
    private final List<ObserverPosicionNaves> observers = new ArrayList<>();
    private ScheduledExecutorService temporizadorActual; // Referencia al temporizador actual.
    private final AtomicBoolean detener = new AtomicBoolean(false); // Señal para detener el temporizador.
    int tiempoRestante;
    boolean entrarPartida;
    boolean esperandoJugador;

    public ModelPosicionNaves() {
    }

    public void addObserver(ObserverPosicionNaves observer) {
        observers.add(observer);
    }

    private void notifyObservers(UpdateEventPosicionNaves event) {
        for (ObserverPosicionNaves observer : observers) {
            observer.actualizarInterfaz(event);
        }
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
        notifyObservers(new UpdateEventPosicionNaves(this, EventTypePosicionNaves.OBTENER_JUGADOR));
    }

    public void correrTiempo(TimeOutEvento evento) {
        if (!isEntrarPartida() && !esperandoJugador) {
            tiempoRestante = evento.getTiempoRestante();
            detenerTemporizadorActivo(); // Limpia cualquier temporizador previo.

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
                        notifyObservers(new UpdateEventPosicionNaves(this, EventTypePosicionNaves.ACTUALIZAR_LABEL));
                        tiempoRestante--;
                    }
                }, 0, 1, TimeUnit.SECONDS);
            }
        } else if (esperandoJugador) {
            detenerTemporizadorActivo();

            if (tiempoRestante > 0) {
                temporizadorActual = Executors.newSingleThreadScheduledExecutor();
                detener.set(false); // Reinicia la señal de detención.

                temporizadorActual.scheduleAtFixedRate(() -> {
                    if (detener.get()) {
                        temporizadorActual.shutdown(); // Detenemos el temporizador si se activa la señal.
                        return;
                    }

                    if (tiempoRestante > 0) {
                        setTexto("Esperando jugador, tiempo restante: " + String.valueOf(tiempoRestante));
                        notifyObservers(new UpdateEventPosicionNaves(this, EventTypePosicionNaves.ACTUALIZAR_LABEL));
                        tiempoRestante--;
                    }
                }, 0, 1, TimeUnit.SECONDS);
            }

        }

        if (evento.getTiempoRestante() == 0) {
            setTexto("Tiempo expirado");
            if (isEntrarPartida() == false) {
                notifyObservers(new UpdateEventPosicionNaves(this, EventTypePosicionNaves.TERMINAR_TIEMPO));
            }
        }
    }

    /**
     * Detiene cualquier temporizador activo.
     */
    private void detenerTemporizadorActivo() {
        detener.set(true); // Activa la señal de detención.
        if (temporizadorActual != null && !temporizadorActual.isShutdown()) {
            temporizadorActual.shutdownNow(); // Detenemos cualquier temporizador activo.
            temporizadorActual = null;
        }
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isEntrarPartida() {
        return entrarPartida;
    }

    public void setEntrarPartida(boolean entrarPartida) {
        this.entrarPartida = entrarPartida;
        notifyObservers(new UpdateEventPosicionNaves(this, EventTypePosicionNaves.ENTRAR_PARTIDA));
    }

    public boolean isEsperandoJugador() {
        return esperandoJugador;
    }

    public void setEsperandoJugador(boolean esperandoJugador) {
        this.esperandoJugador = esperandoJugador;
    }

}

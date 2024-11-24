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
    int tiempoRestante;

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
        tiempoRestante = evento.getTiempoRestante();

        if (tiempoRestante > 0) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            scheduler.scheduleAtFixedRate(() -> {
                if (tiempoRestante > 0) {
                    setTexto(String.valueOf(tiempoRestante));
                    notifyObservers(new UpdateEventPosicionNaves(this, EventTypePosicionNaves.ACTUALIZAR_LABEL));
                    tiempoRestante--;
                } else {

                    scheduler.shutdown(); // Detenemos el scheduler cuando tiempoRestante llega a 1
                }
            }, 0, 1, TimeUnit.SECONDS);
        } else if (evento.getTiempoRestante() == 0) {
            setTexto("Tiempo expirado");
            notifyObservers(new UpdateEventPosicionNaves(this, EventTypePosicionNaves.TERMINAR_TIEMPO));
        }
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}

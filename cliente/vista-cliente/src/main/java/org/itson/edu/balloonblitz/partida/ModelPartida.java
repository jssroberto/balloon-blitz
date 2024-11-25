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
import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;

/**
 *
 * @author elimo
 */
public class ModelPartida {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final List<ObserverPartida> observers = new ArrayList<>();
    private String texto;
    private int contador = 0;
    Casilla casilla;
    boolean hundido;
    boolean atinado;
    Jugador jugadorRival;
    int tiempoRestante;
    boolean turno;

    public ModelPartida() {
    }

    public Casilla getCasilla() {
        return casilla;
    }

    public void setCasilla(Casilla casilla) {
        this.casilla = casilla;
    }

    public boolean isHundido() {
        return hundido;
    }

    public void setHundido(boolean hundido) {
        this.hundido = hundido;
    }

    public boolean isAtinado() {
        return atinado;
    }

    public void setAtinado(boolean atinado) {
        this.atinado = atinado;
    }

    public void cargarDisparoMiTurno() {
        notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_TABLERO_RIVAL));

    }

    public void cargarDisparoTurnoRival() {
        notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_TABLERO_PROPIO));
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

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    

    public void notificarJugador() {
        notifyObservers(new UpdateEventPartida(this, EventTypePartida.ENVIAR_JUGADOR));
    }

    public void correrTiempo(TimeOutEvento evento) {
        tiempoRestante = evento.getTiempoRestante();

        if (tiempoRestante > 0) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            scheduler.scheduleAtFixedRate(() -> {
                if (tiempoRestante > 0) {
                    setTexto(String.valueOf(tiempoRestante));
                    notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_LABEL_TIEMPO));
                    tiempoRestante--;
                } else {

                    scheduler.shutdown(); // Detenemos el scheduler cuando tiempoRestante llega a 1
                }
            }, 0, 1, TimeUnit.SECONDS);
        } else if (evento.getTiempoRestante() == 0) {
            setTexto("Tiempo expirado");
            notifyObservers(new UpdateEventPartida(this, EventTypePartida.TIEMPO_TERMINADO));
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
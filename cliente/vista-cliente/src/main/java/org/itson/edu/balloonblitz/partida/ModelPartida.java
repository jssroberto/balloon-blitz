/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.partida;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.tinylog.Logger;

/**
 *
 * @author elimo
 */
public class ModelPartida {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final List<ObserverPartida> observers = new ArrayList<>();
    private String texto;
    private int contador = 0;
    boolean partidaEncontrada;
    private Tablero tablero;
    private Tablero tableroOponente;
    Jugador jugadorRival;

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

    public Tablero getTableroOponente() {
        return tableroOponente;
    }

    public void setTableroOponente(Tablero tableroOponente) {
        this.tableroOponente = tableroOponente;
         notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_TABLERO_RIVAL));
    }
    
    

    public void notificarJugador() {
        System.out.println("entramos al notificador");
        notifyObservers(new UpdateEventPartida(this, EventTypePartida.ENVIAR_JUGADOR));
    }

    public void correrTiempo() {
        if (!observers.isEmpty()) {
            executorService.submit(() -> {
                try {
                    while (!partidaEncontrada) {
                        SwingUtilities.invokeLater(() -> {
                            switch (contador % 3) {
                                case 0 ->
                                    setTexto("Esperando jugador.");
                                case 1 ->
                                    setTexto("Esperando jugador..");
                                case 2 ->
                                    setTexto("Esperando jugador...");
                            }
                            notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_LABEL_TIEMPO));
                        });
                        contador++;
                        Thread.sleep(500);
                    }
                    setTexto("Partida encontrada");
                    notifyObservers(new UpdateEventPartida(this, EventTypePartida.ACTUALIZAR_LABEL_TIEMPO));
                    Thread.sleep(2000);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Logger.error("Hilo interrumpido durante la espera del jugador.");
                }
            });
        }
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.emparejamiento;


import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import org.tinylog.Logger;

/**
 * @author elimo
 */
public class ModelEmparejamiento {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final List<ObserverEmparejamiento> observers = new ArrayList<>();
    private String texto;
    private int contador = 0;
    boolean partidaEncontrada;

    public void addObserver(ObserverEmparejamiento observer) {
        observers.add(observer);
    }

    public void removeOberver(ObserverEmparejamiento observer) {
        observers.remove(observer);
    }

    private void notifyObservers(UpdateEventEmparejamiento event) {
        for (ObserverEmparejamiento observer : observers) {
            observer.update(event);
        }
    }

    public ModelEmparejamiento() {
    }

    public void setPartidaEncontrada() {
        this.partidaEncontrada = true;
    }

    public void buscarPartida() {
        if (!observers.isEmpty()) {
            executorService.submit(() -> {
                try {
                    while (!partidaEncontrada) {
                        SwingUtilities.invokeLater(() -> {
                            switch (contador % 3) {
                                case 0 -> setTexto("Esperando jugador.");
                                case 1 -> setTexto("Esperando jugador..");
                                case 2 -> setTexto("Esperando jugador...");
                            }
                            notifyObservers(new UpdateEventEmparejamiento(this, EventTypeEmparejamiento.ACTUALIZAR_LABEL));
                        });
                        contador++;
                        Thread.sleep(500);
                    }
                    notifyObservers(new UpdateEventEmparejamiento(this, EventTypeEmparejamiento.ACTUALIZAR_LABEL));
                    Thread.sleep(3000);
                    notifyObservers(new UpdateEventEmparejamiento(this, EventTypeEmparejamiento.CONFIRMAR_UNION_PARTIDA));
                    SwingUtilities.invokeLater(() -> notifyObservers(
                            new UpdateEventEmparejamiento(
                                    this, EventTypeEmparejamiento.CAMBIAR_PANEL_COLOCACION_NAVES)));
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

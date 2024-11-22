/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.emparejamiento;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.itson.edu.balloonblitz.colocarNaves.ColocacionPanel;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.FramePrincipal;

/**
 *
 * @author elimo
 */
public class EmparejamientoModel {

    private static final Logger logger = Logger.getLogger(EmparejamientoModel.class.getName());
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final List<EmparejamientoObserver> observers = new ArrayList<>();
    private String texto;
    private EmparejamientoControlador controlador; // Eliminamos la inicialización interna
    private int contador = 0;
    boolean valido;

    public void addObserver(EmparejamientoObserver observer) {
        observers.add(observer);
    }

    public void removeOberver(EmparejamientoObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(UpdateEvent event) {
        for (EmparejamientoObserver observer : observers) {
            observer.update(event);
        }
    }

    // Constructor que recibe el controlador como dependencia
    public EmparejamientoModel(EmparejamientoControlador controlador) {
        this.controlador = controlador;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public void unirsePartida(FramePrincipal framePrincipal) {
        if (!observers.isEmpty()) {
            executorService.submit(() -> {
                try {
                    while (!valido) {
                        SwingUtilities.invokeLater(() -> {
                            switch (contador % 3) {
                                case 0 ->
                                    setTexto("Esperando jugador.");
                                case 1 ->
                                    setTexto("Esperando jugador..");
                                case 2 ->
                                    setTexto("Esperando jugador...");
                            }
                            notifyObservers(new UpdateEvent(this, EventType.ACTUALIZAR_LABEL));
                        });
                        contador++;
                        Thread.sleep(500); // Simula la espera
                        if (valido) {
                            System.out.println(valido);
                        }
                    }
                    System.out.println("se deberia de actualizar");
                    setTexto("Partida encontrada");
                    notifyObservers(new UpdateEvent(this, EventType.ACTUALIZAR_LABEL));
                    Thread.sleep(3000);
                    controlador.enviarEvento(new ResultadoEvento(true));
                    SwingUtilities.invokeLater(() -> framePrincipal.cambiarPanel(new ColocacionPanel(framePrincipal)));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.log(Level.SEVERE, "Hilo interrumpido durante la espera del jugador.", e);
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

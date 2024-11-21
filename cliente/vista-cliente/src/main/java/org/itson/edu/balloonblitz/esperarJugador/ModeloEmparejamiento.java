/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.esperarJugador;

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
public class ModeloEmparejamiento implements IModeloEmparejamiento {

    private static final Logger logger = Logger.getLogger(ModeloEmparejamiento.class.getName());
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private ObservadorEncontrarPartida observador;
    private ControladorEmparejamiento controlador; // Eliminamos la inicialización interna
    private int contador = 0;
    boolean valido;

    // Constructor que recibe el controlador como dependencia
    public ModeloEmparejamiento(ControladorEmparejamiento controlador) {
        this.controlador = controlador;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }
    
    

    @Override
    public void setObservador(ObservadorEncontrarPartida observador) {
        this.observador = observador;
    }

    public void unirsePartida(FramePrincipal framePrincipal) {
        if (observador != null) {
            executorService.submit(() -> {
                try {
                    while (!valido) {
                        SwingUtilities.invokeLater(() -> {
                            switch (contador % 3) {
                                case 0 -> observador.actualizarInterfaz("Esperando jugador.");
                                case 1 -> observador.actualizarInterfaz("Esperando jugador..");
                                case 2 -> observador.actualizarInterfaz("Esperando jugador...");
                            }
                        });
                        contador++;
                        Thread.sleep(500); // Simula la espera
                        if(valido){
                        System.out.println(valido);
                        }
                    }
                    System.out.println("se deberia de actualizar");
                    observador.actualizarInterfaz("Partida encontrada");
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
}

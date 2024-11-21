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
import org.itson.edu.balloonblitz.vista.FramePrincipal;
import org.itson.edu.balloonblitz.vista.InicioPanel;

/**
 *
 * @author elimo
 */
public class ModeloEmparejamiento implements IModeloEmparejamiento{

    private static final Logger logger = Logger.getLogger(InicioPanel.class.getName());
    private static ModeloEmparejamiento instancia;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    ObservadorEncontrarPartida observador;
    int contador = 0;

    public ModeloEmparejamiento() {
        
    }

    @Override
    public void setObservador(ObservadorEncontrarPartida observador) {
        this.observador = observador;
    }

    public void unirsePartida(FramePrincipal framePrincipal, boolean valido) {
        if(observador!=null){
        executorService.submit(() -> {
            try {

                while (!valido) {
                    // Actualizar la UI de forma segura
                    SwingUtilities.invokeLater(() -> {
                        switch (contador % 3) {
                            case 0 ->
                                observador.actualizarInterfaz("Esperando jugador.");
                            case 1 ->
                               observador.actualizarInterfaz("Esperando jugador..");
                            case 2 ->
                               observador.actualizarInterfaz("Esperando jugador...");
                        }
                    });

                    contador++;
                    Thread.sleep(500); // Simula la espera (medio segundo entre cambios)
                }
                observador.actualizarInterfaz("Partida encontrada");
                Thread.sleep(3000);
                // Si es válido, proceder
                // Cambiar al panel de colocación en el hilo principal
                SwingUtilities.invokeLater(() -> framePrincipal.cambiarPanel(new ColocacionPanel(framePrincipal)));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log(Level.SEVERE, "Hilo interrumpido durante la espera del jugador.", e);
            }
        });
    }
    }
    

    public static ModeloEmparejamiento getInstancia() {
        if (instancia == null) {
            instancia = new ModeloEmparejamiento();
        }
        return instancia;
    }

}


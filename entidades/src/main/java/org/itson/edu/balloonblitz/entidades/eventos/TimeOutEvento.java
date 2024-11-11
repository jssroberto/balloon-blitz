/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades.eventos;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoEvento;

/**
 *
 * @author elimo
 */
public final class TimeOutEvento extends Evento implements Serializable {
    private static final long serialVersionUID = 1L;


    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private boolean tiempoExcedido = false;

    public TimeOutEvento(int tiempoLimite) {
        super(TipoEvento.TIMEOUT);
        iniciarTemporizador(tiempoLimite);
    }
    
    

    public void iniciarTemporizador(int minutos) {
        tiempoExcedido = false;

        scheduler.schedule(() -> {
            tiempoExcedido = true;
            mostrarMensajeTiempoExcedido();
            // Notificar al servidor aquí si es necesario
        }, minutos, TimeUnit.MINUTES);

        System.out.println("Temporizador iniciado por " + minutos + " minutos.");
    }

    private void mostrarMensajeTiempoExcedido() {
        System.out.println("El tiempo ha expirado. Has perdido tu turno.");
        // Notificar al servidor o tomar acciones para pasar el turno
        scheduler.shutdown(); // Cerrar el scheduler después de que el tiempo se cumpla
    }

    // Llamar a este método cuando el cliente complete la acción antes de que el tiempo expire
    public void cancelarTemporizador() {
        if (!tiempoExcedido) {
            scheduler.shutdownNow();
            System.out.println("Temporizador cancelado: el turno ha sido completado.");
        }
    }

}

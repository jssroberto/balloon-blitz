/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;
import org.itson.edu.balloonblitz.modelo.ClienteControlador;
import org.itson.edu.balloonblitz.modelo.ObservadorTiempo;

/**
 *
 * @author elimo
 */
public class ControladorTiempo implements ObservadorTiempo {

    ClienteControlador cliente;
    int tiempoRestante;

    public ControladorTiempo() {
    }

    public void enviarEvento(Evento evento) {
        cliente.enviarMensaje(evento);
    }

    @Override
    public void manejarEvento(TimeOutEvento evento) {
        tiempoRestante = evento.getTiempoRestante();
        if (tiempoRestante > 0) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                if (tiempoRestante > 0) {
                    System.out.println("El temporizador está corriendo. Tiempo restante: " + tiempoRestante + " minutos.");
                    tiempoRestante--;
                } else {
                    System.out.println("El tiempo ha expirado. Has perdido tu turno.");
                    scheduler.shutdown();
                }
            }, 0, 1, TimeUnit.SECONDS);
        } else {
            System.out.println("El tiempo ha expirado. Has perdido tu turno.");
        }
    }

}

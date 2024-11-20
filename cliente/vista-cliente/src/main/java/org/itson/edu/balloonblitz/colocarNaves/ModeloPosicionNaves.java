/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.colocarNaves;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JLabel;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;
import org.itson.edu.balloonblitz.modelo.ClienteControlador;
import org.itson.edu.balloonblitz.modelo.ObservadorTiempo;

/**
 *
 * @author elimo
 */
public class ModeloPosicionNaves implements ObservadorTiempo {

    private static ModeloPosicionNaves instancia;
    ClienteControlador cliente;
    JLabel label;
    int tiempoRestante;

    public ModeloPosicionNaves() {
    }

    public static ModeloPosicionNaves getInstancia() {
        if (instancia == null) {
            instancia = new ModeloPosicionNaves();
        }
        return instancia;
    }

    public void enviarEvento(Evento evento) {
        cliente.enviarMensaje(evento);
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    @Override
    public void manejarEvento(TimeOutEvento evento) {
        correrTiempo(evento, label);
    }

    public void correrTiempo(TimeOutEvento evento, JLabel label) {
    tiempoRestante = evento.getTiempoRestante();

    if (tiempoRestante > 0) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.scheduleAtFixedRate(() -> {
            if (tiempoRestante > 1) {
                label.setText(String.valueOf(tiempoRestante));
                tiempoRestante--;
            } else {
                label.setText("El tiempo ha expirado. Has perdido tu turno.");
                scheduler.shutdown(); // Detenemos el scheduler cuando tiempoRestante llega a 1
            }
        }, 0, 1, TimeUnit.SECONDS);
    } else if (evento.getTiempoRestante() == 0) {
        label.setText("El tiempo ha expirado. Has perdido tu turno.");
    }
}


}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.colocarNaves;

import javax.swing.JLabel;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;

/**
 *
 * @author elimo
 */
public class ModeloPosicionNaves {

    ConexionCliente cliente;
    ObservadorPosicionNaves observador;

    public ModeloPosicionNaves() {
        cliente = ConexionCliente.getInstancia();

        controladorEnvio = new ControladorEnvio();
    }

    public void setObservador(ObservadorPosicionNaves observador) {
        this.observador = observador;
    }

    public void cerrar() {
        cliente.eliminarObservadorTiempo();
    }

    public void correrTiempo(TimeOutEvento evento, ObservadorPosicionNaves observador) {
//        tiempoRestante = evento.getTiempoRestante();
//
//        if (tiempoRestante > 0) {
//            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//            scheduler.scheduleAtFixedRate(() -> {
//                if (tiempoRestante > 1) {
//                    label.setText(String.valueOf(tiempoRestante));
//                    tiempoRestante--;
//                } else {
//                    label.setText("El tiempo ha expirado. Has perdido tu turno.");
//                    scheduler.shutdown(); // Detenemos el scheduler cuando tiempoRestante llega a 1
//                }
//            }, 0, 1, TimeUnit.SECONDS);
//        } else if (evento.getTiempoRestante() == 0) {
//            label.setText("El tiempo ha expirado. Has perdido tu turno.");
//        }
        observador.actualizarInterfaz(String.valueOf(evento.getTiempoRestante()));
    }

}

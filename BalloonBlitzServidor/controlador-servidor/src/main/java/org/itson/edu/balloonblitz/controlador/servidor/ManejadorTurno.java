/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Partida;

/**
 *
 * @author elimo
 */
public class ManejadorTurno {

    Partida partida;
    private static final int LIMITE_TIEMPO_TURNO = 3; // minutos
    private final ScheduledExecutorService temporizadorTurno = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> temporizadorActual;
    private Jugador jugadorActual;
    private Jugador jugador1;
    private Jugador jugador2;

    public void iniciarTurno(Jugador jugador) {
        jugadorActual = jugador;
        temporizadorActual = temporizadorTurno.schedule(() -> {
            System.out.println("El tiempo de turno ha expirado para " + jugadorActual.getNombre());
            cambiarTurno();
        }, LIMITE_TIEMPO_TURNO, TimeUnit.MINUTES);

    }

    private void cambiarTurno() {
        if (temporizadorActual != null) {
            temporizadorActual.cancel(true); // Cancelar el temporizador del turno actual
        }

        jugadorActual = (jugadorActual.equals(jugador1)) ? jugador2 : jugador1;

        iniciarTurno(jugadorActual);
    }

}

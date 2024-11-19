/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

    private final ScheduledExecutorService temporizadorTurno = Executors.newSingleThreadScheduledExecutor();
    private boolean tiempoExcedido;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    public ManejadorTurno() {
    }

    public int iniciarTemporizador(int segundos) {
        CompletableFuture<String> futuro = new CompletableFuture<>();
        tiempoExcedido = false;

        scheduler.schedule(() -> {
            tiempoExcedido = true;
            futuro.complete(mostrarMensajeTiempoExcedido());
        }, segundos, TimeUnit.SECONDS);

        try {
            // Esperamos el resultado del CompletableFuture
            futuro.get();
            return 0;
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt(); // Restablece el estado de interrupción
        }
        return -1;
    }

    
    private String mostrarMensajeTiempoExcedido() {
        return "El tiempo ha expirado.";
    }
}

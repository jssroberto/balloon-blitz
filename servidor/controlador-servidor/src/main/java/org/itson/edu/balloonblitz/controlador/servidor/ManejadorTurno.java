/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    CountDownLatch latch = new CountDownLatch(1); // Sincronizador para esperar al temporizador
    try {
        scheduler.schedule(() -> {
            System.out.println("El tiempo ha expirado.");
            latch.countDown(); // Libera el hilo principal
        }, segundos, TimeUnit.SECONDS);

        // Espera a que el latch se libere o el tiempo expire
        latch.await(); // Bloquea hasta que el tiempo expira
        return 0; // Devuelve 0 cuando el tiempo expira
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt(); // Manejo adecuado de interrupciones
        return -1; // Devuelve -1 si ocurre una interrupción
    } finally {
        scheduler.shutdown(); // Detenemos el scheduler
    }
}




    private String mostrarMensajeTiempoExcedido() {
        return "El tiempo ha expirado.";
    }
}

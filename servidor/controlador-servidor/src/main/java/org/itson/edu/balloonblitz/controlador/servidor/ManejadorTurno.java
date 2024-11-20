/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.util.concurrent.CompletableFuture;
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
    try {
        scheduler.schedule(() -> {
            System.out.println("El tiempo ha expirado.");
            scheduler.shutdown(); // Detenemos el scheduler
        }, segundos, TimeUnit.SECONDS);

        // Bucle que espera hasta que el scheduler termine
        while (!scheduler.isTerminated()) {
            // Aquí puedes agregar lógica si es necesario.
        }
        return 0; // Devuelve 0 cuando el tiempo expira
    } finally {
        scheduler.shutdownNow(); // Asegúrate de detener el scheduler si ocurre un error
    }
}




    private String mostrarMensajeTiempoExcedido() {
        return "El tiempo ha expirado.";
    }
}

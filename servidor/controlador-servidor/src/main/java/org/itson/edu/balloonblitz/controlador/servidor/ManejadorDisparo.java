/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.ObjectInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.itson.edu.balloonblitz.entidades.Disparo;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.modelo.servidor.EventoObserver;

/**
 *
 * @author elimo
 */
public class ManejadorDisparo implements Runnable, EventoObserver{

    private final DisparoEvento disparoEvento;

    public ManejadorDisparo(DisparoEvento disparoEvento) {
        this.disparoEvento = disparoEvento;
    }

    public DisparoEvento getDisparoEvento() {
        return disparoEvento;
    }


    //TODO hacer que esto devuela un evento en lugar de void
    @Override
    public void manejarEvento(Evento evento, ObjectInputStream entrada) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    

    

}

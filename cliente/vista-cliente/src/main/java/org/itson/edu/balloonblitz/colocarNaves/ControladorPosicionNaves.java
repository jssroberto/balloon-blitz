/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.colocarNaves;

import javax.swing.JLabel;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;
import org.itson.edu.balloonblitz.modelo.ObservadorJugador;
import org.itson.edu.balloonblitz.modelo.ObservadorTiempo;

public class ControladorPosicionNaves implements ObservadorTiempo, ObservadorJugador {

    private static ControladorPosicionNaves instancia;
    private ConexionCliente cliente;
    private JLabel label;
    private Jugador jugador;
    private int tiempoRestante;
    private ObservadorPosicionNaves observador;
    ModeloPosicionNaves modelo;

    private ControladorPosicionNaves() {
        cliente = ConexionCliente.getInstancia();
        cliente.setObservadorTiempo(this);
        modelo = new ModeloPosicionNaves();
    }

    public void setObservador(ObservadorPosicionNaves observador) {
        this.observador = observador;
    }

    // Método estático que devuelve la instancia única del singleton
    public static ControladorPosicionNaves getInstancia() {
        if (instancia == null) {
            instancia = new ControladorPosicionNaves();
        }
        return instancia;
    }

    // Método que obtiene el jugador
    public Jugador getJugador() {
        return jugador;
    }

    // Método para enviar eventos al cliente
    public void enviarEvento(Evento evento) {
        cliente.enviarMensaje(evento);
    }

    // Getter para el label
    public JLabel getLabel() {
        return label;
    }

    // Setter para el label
    public void setLabel(JLabel label) {
        this.label = label;
    }

    @Override
    public void manejarEvento(Evento evento) {

        modelo.correrTiempo((TimeOutEvento) evento, observador);

    }

    @Override
    public void manejarEvento(EnvioJugadorEvento evento) {
        jugador = evento.getEmisor();
    }
}

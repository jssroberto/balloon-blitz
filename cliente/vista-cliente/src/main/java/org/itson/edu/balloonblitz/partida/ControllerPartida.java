/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.partida;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoDisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;
import org.itson.edu.balloonblitz.modelo.ObservadorDisparo;
import org.itson.edu.balloonblitz.modelo.ObservadorJugador;
import org.itson.edu.balloonblitz.modelo.ObservadorResultado;
import org.itson.edu.balloonblitz.modelo.ObservadorTiempo;

/**
 *
 * @author elimo
 */
public class ControllerPartida implements ActionHandlerPartida, ObservadorDisparo, ObservadorJugador, ObservadorTiempo, ObservadorResultado {

    private final PartidaPanel view;
    private final ModelPartida model;
    private Jugador jugador;
    private Tablero tablero;
    private Tablero tableroOponente;

    public ControllerPartida(PartidaPanel view, ModelPartida model) {
        this.view = view;
        this.model = model;
        this.view.setActionHandler(this);
        this.model.addObserver(view);
    }

    @Override
    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        System.out.println("todo bien hasta aqui");
        this.jugador = jugador;
        this.tablero = jugador.getTableroPropio();
        System.out.println("entrando a notificar");
        model.notificarJugador();
    }

    public void procesarDisparo(ResultadoDisparoEvento evento) {
        jugador = evento.getEmisor();
        if (jugador.equals(this.jugador)) {
            tableroOponente = evento.getTablero();
        } else {
            tablero = evento.getTablero();
        }

    }

    public void actualizarTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public void actualizarTableroOponente(Tablero tablero) {
        this.tableroOponente = tablero;
    }

    @Override
    public void manejarEvento(ResultadoDisparoEvento evento) {
        if (jugador.isTurno()) {
            model.setTableroOponente(evento.getTablero());
        } else {
            model.setTablero(evento.getTablero());
        }
    }

    @Override
    public void enviarEvento(DisparoEvento evento) {
        ConexionCliente.getInstancia().enviarMensaje(evento);
    }

    @Override
    public void manejarEvento(EnvioJugadorEvento evento) {
        setJugador(evento.getEmisor());
    }

    @Override
    public void manejarEvento(Evento evento) {
        model.correrTiempo((TimeOutEvento) evento);
    }

    @Override
    public void manejarEvento(ResultadoEvento evento) {
        model.obtenerTurno(evento);
    }

}

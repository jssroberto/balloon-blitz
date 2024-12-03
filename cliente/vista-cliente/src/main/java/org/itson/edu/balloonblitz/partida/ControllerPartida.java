/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.partida;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoNave;
import org.itson.edu.balloonblitz.entidades.eventos.*;
import org.itson.edu.balloonblitz.modelo.*;

/**
 * @author elimo
 */
public class ControllerPartida implements ActionHandlerPartida, ObservadorDisparo, ObservadorJugador, ObservadorTiempo, ObservadorResultado, ObservadorVictoria, ObservadorDesconexion {

    private final PartidaPanel view;
    private final ModelPartida model;

    public ControllerPartida(PartidaPanel view, ModelPartida model) {
        this.view = view;
        this.model = model;
        this.view.setActionHandler(this);
        this.model.addObserver(view);
    }

    public void setJugador(Jugador jugadorRival) {
        model.setJugadorRival(jugadorRival);
        model.notificarJugador();
    }

    @Override
    public void manejarEvento(ResultadoDisparoEvento evento) {
        reproducirSonidoDisparo(evento);
        if (evento.getEmisor().equals(model.getJugadorRival())) {
            model.setTablero(evento.getTablero());
            view.getFramePrincipal().getJugador().setNaves(evento.getNaves());
            Casilla casilla = model.getTablero().getCasilla(evento.getCoordenada());
            model.setUltimoDisparo(casilla);
        } else {
            model.setTableroDeRival(evento.getTablero());
            model.getJugadorRival().setNaves(evento.getNaves());
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

    //TODO aquí se tiene que recibir un evento específico, no el evento padre
    @Override
    public void manejarEvento(TimeOutEvento evento) {
        model.correrTiempo(evento);
    }

    //Resultado para los turnos
    @Override
    public void manejarEvento(ResultadoEvento evento) {
        model.obtenerTurno(evento);
    }

    private void reproducirSonidoDisparo(ResultadoDisparoEvento evento) {
        if (evento.getTablero().getCasilla(evento.getCoordenada()).getNave().isEmpty()) {
            model.getPlayerQuack().playOnce();
        } else {
            if (evento.getTablero().getCasilla(evento.getCoordenada()).getNave().get().getEstadoNave() == EstadoNave.AVERIADA) {
                model.getPlayerPop().playOnce();
            } else {
                model.getPlayerExplosion().playOnce();
            }
        }
    }

    @Override
    public void manejarEvento(VictoriaEvento evento) {
        if (evento.isVictoria()) {
            model.setVictoria(true);
        } else {
            model.setVictoria(false);
        }
    }

    @Override
    public void manejarEvento(DesconexionEvento evento) {
        model.setVictoria(true);
    }
}

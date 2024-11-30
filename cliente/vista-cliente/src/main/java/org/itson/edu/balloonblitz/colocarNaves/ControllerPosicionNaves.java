/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.colocarNaves;

import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.PosicionNavesEvento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.TimeOutEvento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;
import org.itson.edu.balloonblitz.modelo.ObservadorJugador;
import org.itson.edu.balloonblitz.modelo.ObservadorResultado;
import org.itson.edu.balloonblitz.modelo.ObservadorTiempo;

public class ControllerPosicionNaves implements ObservadorTiempo, ObservadorJugador, ObservadorResultado,ActionHandlerColocarNaves {

    private ConexionCliente cliente;
    private int tiempoRestante;
    private ColocacionPanel view;
    ModelPosicionNaves model;
    Jugador jugador;


    public ControllerPosicionNaves(ColocacionPanel view, ModelPosicionNaves model) {
        cliente = ConexionCliente.getInstancia();
        this.model = model;
        this.model.addObserver(view);
        this.view = view;
        this.view.setActionHandler(this);

    }

    public void enviarEvento(Evento evento) {
        cliente.enviarMensaje(evento);
    }

    public void cerrar() {
        cliente.eliminarTodosLosObservadores();
    }

    @Override
    public void manejarEvento(Evento evento) {
        model.correrTiempo((TimeOutEvento) evento);
    }

    @Override
    public void manejarEvento(EnvioJugadorEvento evento) {
        jugador = evento.getEmisor();
        model.setJugador(jugador);
    }

    @Override
    public void enviarPosicionNaves(Tablero tablero) {
        Evento eventoTablero = new PosicionNavesEvento(tablero);
        eventoTablero.setEmisor(jugador);
        for (int i = 0; i < tablero.getFilas(); i++) {
            StringBuilder fila = new StringBuilder();
            for (int j = 0; j < tablero.getColumnas(); j++) {
                fila.append(tablero.getCasilla(new Coordenada(i, j)).getNave().isPresent() ? "1 " : "0 ");
            }
            System.out.println(fila.toString().trim());
        }
        enviarEvento(eventoTablero);
    }

    @Override
    public void confirmarUnion() {
        enviarEvento(new ResultadoEvento(true));
    }

    @Override
    public void manejarEvento(ResultadoEvento evento) {
        if(evento.isValid()){
            model.setEntrarPartida(true);
        }else{
            model.setEsperandoJugador(true);
        }
    }
}

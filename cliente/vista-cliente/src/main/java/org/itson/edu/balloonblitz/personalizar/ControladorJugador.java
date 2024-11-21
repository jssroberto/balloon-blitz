/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.personalizar;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;
import org.itson.edu.balloonblitz.vista.FramePrincipal;

/**
 *
 * @author elimo
 */
public class ControladorJugador {
    
    private static ControladorJugador instancia;
    private final ModeloJugador modeloJugador;
    ConexionCliente conexion;
    
    public ControladorJugador() {
        modeloJugador = ModeloJugador.getInstancia();
        conexion = ConexionCliente.getInstancia();
    }
    
    public static ControladorJugador getInstancia() {
        if (instancia == null) {
            instancia = new ControladorJugador();
        }
        return instancia;
    }
    
    public void cambiarPanel(FramePrincipal framePrincipal) {
        modeloJugador.cambiarPanel(framePrincipal);
    }
    
    public void enviarJugador(Jugador jugador){
        Evento evento = new EnvioJugadorEvento();
        evento.setEmisor(jugador);
        conexion.enviarMensaje(evento);
    }
    
}

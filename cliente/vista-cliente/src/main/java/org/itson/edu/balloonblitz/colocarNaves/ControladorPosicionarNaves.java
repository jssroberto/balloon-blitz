/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.colocarNaves;

import javax.swing.JLabel;
import org.itson.edu.balloonblitz.personalizar.*;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;

/**
 *
 * @author elimo
 */
public class ControladorPosicionarNaves {

    private static ControladorPosicionarNaves instancia;
    ConexionCliente cliente;
    private final ModeloPosicionNaves posicion;
    private final ModeloJugador modeloJugador;
    private final ControladorEnvio controladorEnvio;

    public ControladorPosicionarNaves() {
        cliente = ConexionCliente.getInstancia();
        posicion = ModeloPosicionNaves.getInstancia();
        cliente.setObservadorTiempo(posicion);
        modeloJugador = ModeloJugador.getInstancia();
        controladorEnvio = new ControladorEnvio();
    }

    
    public void enviarEvento(Evento evento){
        controladorEnvio.enviarEvento(evento);
    }
    
    public static ControladorPosicionarNaves getInstancia() {
        if (instancia == null) {
            instancia = new ControladorPosicionarNaves();
        }
        return instancia;
    }
    
    public void setLabel(JLabel label){
        posicion.setLabel(label);
    }
    
    
    
    public void cerrar(){
        cliente.eliminarObservadorTiempo();
    }
    
    

}

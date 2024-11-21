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
public class ModeloPosicionNaves {

    ConexionCliente cliente;
    private final ModeloPosicionNaves posicion;
    private final ModeloJugador modeloJugador;

    public ModeloPosicionNaves() {
        cliente = ConexionCliente.getInstancia();
        posicion = new ModeloPosicionNaves();
        
        modeloJugador = ModeloJugador.getInstancia();
    }

    
    
    public void setLabel(JLabel label){
        posicion.setLabel(label);
    }
    
    
    
    public void cerrar(){
        cliente.eliminarObservadorTiempo();
    }
    
    

}

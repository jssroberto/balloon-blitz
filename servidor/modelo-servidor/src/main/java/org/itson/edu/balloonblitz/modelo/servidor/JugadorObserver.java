/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.edu.balloonblitz.modelo.servidor;

import java.io.ObjectInputStream;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;

/**
 * Interfaz para los suscriptores de la conexion
 * @author elimo
 */
public interface JugadorObserver {
    
    /**
     * Metodo que agrega un jugador
     * @param jugador 
     */
    void agregarJugador(Evento jugador, ObjectInputStream entrada);
    
    public void eliminarCliente(ObjectInputStream entrada);
}

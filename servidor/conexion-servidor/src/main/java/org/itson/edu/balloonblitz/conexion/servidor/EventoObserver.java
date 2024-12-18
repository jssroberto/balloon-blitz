/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.edu.balloonblitz.conexion.servidor;

import java.io.ObjectInputStream;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;

/**
 * Interfaz para los suscriptores de los eventos
 *
 * @author elimo
 */
public interface EventoObserver {

    /**
     * Metodo para manejar eventos
     *
     * @param evento Eventos a manejar por el observador
     * @return
     */
    void manejarEvento(Evento evento, ObjectInputStream entrada);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.edu.balloonblitz.partida;

import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;

/**
 *
 * @author elimo
 */
public interface ActionHandlerPartida {
    void enviarEvento(DisparoEvento evento);
}

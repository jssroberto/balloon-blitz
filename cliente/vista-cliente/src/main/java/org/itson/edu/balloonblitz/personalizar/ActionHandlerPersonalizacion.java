/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.edu.balloonblitz.personalizar;

import org.itson.edu.balloonblitz.entidades.Jugador;

/**
 *
 * @author elimo
 */
public interface ActionHandlerPersonalizacion {

    //aquí irían eventos de interacción de la vista que se enviarían al controlador
//    void clickRegresar();
    void enviarJugador(Jugador jugador);

    public void cambiarPanelYEnviarJugador();
}

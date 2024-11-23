/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.edu.balloonblitz.colocarNaves;

import org.itson.edu.balloonblitz.entidades.Tablero;

/**
 *
 * @author elimo
 */
public interface ObserverPosicionNaves {

    public void actualizarInterfaz(UpdateEventPosicionNaves event);
    
    public void enviarTablero(Tablero tablero);

}

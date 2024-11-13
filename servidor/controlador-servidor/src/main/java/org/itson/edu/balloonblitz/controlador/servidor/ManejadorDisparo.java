/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;

/**
 *
 * @author elimo
 */
public class ManejadorDisparo{

    private final DisparoEvento disparoEvento;

    public ManejadorDisparo(DisparoEvento disparoEvento) {
        this.disparoEvento = disparoEvento;
    }

    //TODO ocupo discernir de donde salio el jugador, ya que no tengo id
    public DisparoEvento procesarDisparo(DisparoEvento disparoEvento){
        Jugador jugador = disparoEvento.getEmisor();
        Coordenada coordenada = disparoEvento.getDisparo().getCasilla().getCoordenada();

        return null;
    }

    
    

    

}

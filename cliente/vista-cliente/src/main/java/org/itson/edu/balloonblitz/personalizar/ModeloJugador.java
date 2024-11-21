/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.personalizar;

import org.itson.edu.balloonblitz.esperarJugador.EsperandoJugadorPanel;
import org.itson.edu.balloonblitz.FramePrincipal;

/**
 *
 * @author elimo
 */
public class ModeloJugador {

    private static ModeloJugador instancia;

    public ModeloJugador() {
    }

    public static ModeloJugador getInstancia() {
        if (instancia == null) {
            instancia = new ModeloJugador();
        }
        return instancia;
    }
    
    public void cambiarPanel(FramePrincipal framePrincipal){
         framePrincipal.cambiarPanel(new EsperandoJugadorPanel(framePrincipal));
    }
}

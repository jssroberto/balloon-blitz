/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.esperarJugador;

import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.modelo.ObservadorResultado;

/**
 *
 * @author elimo
 */
public class ModeloResultadoEmparejamiento implements ObservadorResultado {

    private static ModeloResultadoEmparejamiento instancia;
    boolean valido;

    public ModeloResultadoEmparejamiento() {
        valido = false;
    }

    @Override
    public void manejarEvento(ResultadoEvento evento) {
        valido = evento.isValid();
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public static ModeloResultadoEmparejamiento getInstancia() {
        if (instancia == null) {
            instancia = new ModeloResultadoEmparejamiento();
        }
        return instancia;
    }
}

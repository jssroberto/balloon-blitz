/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador;

import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.modelo.ClienteControlador;
import org.itson.edu.balloonblitz.modelo.ObservadorResultado;

/**
 *
 * @author elimo
 */
public class ControladorResultado implements ObservadorResultado {

    ClienteControlador cliente;
    boolean valido;

    public ControladorResultado() {
        valido = false;
        ClienteControlador.getInstancia().setObservadorResultado(this);
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
}

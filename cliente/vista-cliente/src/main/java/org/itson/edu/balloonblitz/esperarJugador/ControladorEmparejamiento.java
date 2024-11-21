/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.esperarJugador;

import java.util.ArrayList;
import java.util.List;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;
import org.itson.edu.balloonblitz.modelo.ObservadorResultado;
import org.itson.edu.balloonblitz.vista.FramePrincipal;

/**
 *
 * @author elimo
 */
public class ControladorEmparejamiento implements ObservadorResultado {

    private static ControladorEmparejamiento instancia;
    private ModeloEmparejamiento modelo;
    private List<FramePrincipal> suscriptores = new ArrayList<>();
    private FramePrincipal frame;
    boolean valido;

    public ControladorEmparejamiento() {
        ConexionCliente.getInstancia().setObservadorResultado(this);
        valido = false;
        modelo = ModeloEmparejamiento.getInstancia();
    }

    public void cambiarPanel(FramePrincipal frame) {
        this.frame = frame;
    }

    public void setObservador(ObservadorEncontrarPartida observador) {
        modelo.setObservador(observador);
    }

    @Override
    public void manejarEvento(ResultadoEvento evento) {
        valido = evento.isValid();
        unirsePartida();

    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }

    public static ControladorEmparejamiento getInstancia() {
        if (instancia == null) {
            instancia = new ControladorEmparejamiento();
        }
        return instancia;
    }

    public void unirsePartida() {
        ModeloEmparejamiento.getInstancia().unirsePartida(frame, valido);
    }
}

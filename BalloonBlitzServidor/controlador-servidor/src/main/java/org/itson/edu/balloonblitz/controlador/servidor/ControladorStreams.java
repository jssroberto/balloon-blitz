/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author elimo
 */
public class ControladorStreams {

    private final ObjectOutputStream salida;
    private final ObjectInputStream entrada;

    public ControladorStreams(ObjectOutputStream salida, ObjectInputStream entrada) {
        this.salida = salida;
        this.entrada = entrada;
    }

    public ObjectOutputStream getSalida() {
        return salida;
    }

    public ObjectInputStream getEntrada() {
        return entrada;
    }
}

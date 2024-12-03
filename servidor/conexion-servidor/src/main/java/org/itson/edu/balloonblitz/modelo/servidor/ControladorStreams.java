/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.modelo.servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Clase para trabsportar los I/O del cliente
 * @author elimo
 */
public class ControladorStreams {

    private final ObjectOutputStream salida;
    private final ObjectInputStream entrada;

    /**
     * Constructor que obtiene los I/O del cliente
     *
     * @param salida ObjectOutputStream del cliente
     * @param entrada ObjectInputStream del cliente
     */
    public ControladorStreams(ObjectOutputStream salida, ObjectInputStream entrada) {
        this.salida = salida;
        this.entrada = entrada;
    }

    /**
     * Metodo para obtener el output del cliente
     *
     * @return Output del cliente
     */
    public ObjectOutputStream getSalida() {
        return salida;
    }

    /**
     * Metodo para obtener el input del cliente
     *
     * @return Input del cliente
     */
    public ObjectInputStream getEntrada() {
        return entrada;
    }

}

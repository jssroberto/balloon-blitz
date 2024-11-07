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
 * @param <T>
 */
public class ControladorStreams<T> {

    private final ObjectOutputStream salida;
    private final ObjectInputStream entrada;
    private T objetoRecibido;

    public ControladorStreams(ObjectOutputStream salida, ObjectInputStream entrada) {
        this.salida = salida;
        this.entrada = entrada;
    }

    public T getObjetoRecibido() {
        return objetoRecibido;
    }

    public void setObjetoRecibido(T objetoRecibido) {
        this.objetoRecibido = objetoRecibido;
    }

    public ObjectOutputStream getSalida() {
        return salida;
    }

    public ObjectInputStream getEntrada() {
        return entrada;
    }

    

   
}

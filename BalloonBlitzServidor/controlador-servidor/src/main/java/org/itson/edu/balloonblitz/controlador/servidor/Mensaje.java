/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L;
    private String contenido;
    private String remitente;

    public Mensaje(String contenido, String remitente) {
        this.contenido = contenido;
        this.remitente = remitente;
    }

    public String getContenido() {
        return contenido;
    }

    public String getRemitente() {
        return remitente;
    }
}

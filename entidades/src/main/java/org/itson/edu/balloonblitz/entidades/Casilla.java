/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoCasilla;

/**
 * @author elimo
 */
public class Casilla implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private EstadoCasilla estado;
    private Coordenada coordenada;
    private Nave nave;

    public Casilla(Coordenada coordenada) {
        this.coordenada = coordenada;
        this.estado = EstadoCasilla.INTACTA;
        this.nave = null;
    }

    public EstadoCasilla getEstado() {
        return estado;
    }

    public void setEstado(EstadoCasilla estado) {
        this.estado = estado;
    }

    public Coordenada getCoordenada() {
        return coordenada;
    }

    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    public Optional<Nave> getNave() {
        return Optional.ofNullable(nave);
    }

    public void setNave(Nave nave) {
        this.nave = nave;
    }
}

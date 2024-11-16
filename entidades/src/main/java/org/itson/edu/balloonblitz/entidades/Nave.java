/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoNave;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author elimo
 */
public abstract class Nave implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String id;
    private final TipoNave tipoNave;
    private final int tamano;
    private EstadoNave estadoNave;
    private int impactos;

    public Nave(TipoNave tipoNave, int tamano) {
        this.id = UUID.randomUUID().toString();
        this.tipoNave = tipoNave;
        this.tamano = tamano;
        this.estadoNave = EstadoNave.COMPLETA;
        this.impactos = 0;
    }

    public TipoNave getTipoNave() {
        return tipoNave;
    }

    public int getTamano() {
        return tamano;
    }

    public EstadoNave getEstadoNave() {
        return estadoNave;
    }

    public void setEstadoNave(EstadoNave estadoNave) {
        this.estadoNave = estadoNave;
    }

    public int getImpactos() {
        return impactos;
    }

    public String getId() {
        return id;
    }

    public int recibirImpacto() {
        return ++impactos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nave nave)) return false;
        return Objects.equals(id, nave.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
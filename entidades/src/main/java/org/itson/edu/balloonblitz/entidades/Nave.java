/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoNave;

/**
 *
 * @author elimo
 */
public abstract class Nave implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    protected final TipoNave tipoNave;
    protected final int tamano;
    protected EstadoNave estadoNave;
    protected int impactos;

    public Nave(TipoNave tipoNave, int tamano) {
        this.tipoNave = tipoNave;
        this.tamano = tamano;
        this.estadoNave = EstadoNave.COMPLETA;
        this.impactos = 0;
    }

    public void recibirImpacto() {
        impactos++;
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
}
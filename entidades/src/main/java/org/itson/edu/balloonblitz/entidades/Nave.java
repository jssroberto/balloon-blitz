/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import java.io.Serializable;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoNave;

/**
 *
 * @author elimo
 */
public abstract class Nave implements Serializable{
    
    private static final long serialVersionUID = 1L;
    protected TipoNave tipoNave;
    protected int tamaño;
    protected EstadoNave estadoNave;
    protected int impactos;

    public Nave(TipoNave tipoNave, int tamaño) {
        this.tipoNave = tipoNave;
        this.tamaño = tamaño;
        this.estadoNave = EstadoNave.COMPLETA;
        this.impactos = 0;
    }

    public void recibirImpacto() {
        impactos++;
        if (impactos < tamaño) {
            estadoNave = EstadoNave.AVERIADA;
        } else {
            estadoNave = EstadoNave.HUNDIDA;
        }
    }

    public TipoNave getTipoNave() {
        return tipoNave;
    }

    public void setTipoNave(TipoNave tipoNave) {
        this.tipoNave = tipoNave;
    }

    public int getTamaño() {
        return tamaño;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
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

    public void setImpactos(int impactos) {
        this.impactos = impactos;
    }
    
    
    
}

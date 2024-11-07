/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.entidades;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author elimo
 */
public class Jugador implements Serializable {

    private String nombre;
    private String fotoPerfil;
    private Tablero tableroPropio;
    private Tablero tableroContrincante;
    private List<Nave> naves;
    private int navesRestantes;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Tablero getTableroPropio() {
        return tableroPropio;
    }

    public void setTableroPropio(Tablero tableroPropio) {
        this.tableroPropio = tableroPropio;
    }

    public Tablero getTableroContrincante() {
        return tableroContrincante;
    }

    public void setTableroContrincante(Tablero tableroContrincante) {
        this.tableroContrincante = tableroContrincante;
    }

    public List<Nave> getNaves() {
        return naves;
    }

    public void setNaves(List<Nave> naves) {
        this.naves = naves;
    }

    public int getNavesRestantes() {
        return navesRestantes;
    }

    public void setNavesRestantes(int navesRestantes) {
        this.navesRestantes = navesRestantes;
    }
    
    

}

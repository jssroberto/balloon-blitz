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
import java.io.Serializable;
import java.util.List;

public class Jugador implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String nombre;
    private final String fotoPerfil;
    private final Tablero tableroPropio;
    private final Tablero tableroContrincante;
    private final List<Nave> naves;
    private final int navesRestantes;

    // Constructor privado que recibe el builder
    private Jugador(Builder builder) {
        this.nombre = builder.nombre;
        this.fotoPerfil = builder.fotoPerfil;
        this.tableroPropio = builder.tableroPropio;
        this.tableroContrincante = builder.tableroContrincante;
        this.naves = builder.naves;
        this.navesRestantes = builder.navesRestantes;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public Tablero getTableroPropio() {
        return tableroPropio;
    }

    public Tablero getTableroContrincante() {
        return tableroContrincante;
    }

    public List<Nave> getNaves() {
        return naves;
    }

    public int getNavesRestantes() {
        return navesRestantes;
    }

    
    public static class Builder {
        private String nombre;
        private String fotoPerfil;
        private Tablero tableroPropio;
        private Tablero tableroContrincante;
        private List<Nave> naves;
        private int navesRestantes;

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder fotoPerfil(String fotoPerfil) {
            this.fotoPerfil = fotoPerfil;
            return this;
        }

        public Builder tableroPropio(Tablero tableroPropio) {
            this.tableroPropio = tableroPropio;
            return this;
        }

        public Builder tableroContrincante(Tablero tableroContrincante) {
            this.tableroContrincante = tableroContrincante;
            return this;
        }

        public Builder naves(List<Nave> naves) {
            this.naves = naves;
            return this;
        }

        public Builder navesRestantes(int navesRestantes) {
            this.navesRestantes = navesRestantes;
            return this;
        }

        // Método build que crea una nueva instancia de Jugador
        public Jugador build() {
            return new Jugador(this);
        }
    }
}


package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.itson.edu.balloonblitz.entidades.enumeradores.ColorNaves;

/**
 * Representa a un jugador en el juego, con su nombre, foto de perfil, tableros, naves, colores y el estado de su turno.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class Jugador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String id;
    private Integer numJugador;
    private String nombre;
    private String fotoPerfil;
    private Tablero tableroPropio;
    private Tablero tableroContrincante;
    private List<Nave> naves;
    private ColorNaves colorPropio;
    private ColorNaves colorRival;
    private Integer navesRestantes;
    private boolean turno;

    public Jugador() {
        this.id = UUID.randomUUID().toString();
    }

    public Jugador(String nombre, ColorNaves colorPropio, ColorNaves colorRival, String fotoPerfil) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.colorPropio = colorPropio;
        this.colorRival = colorRival;
        this.fotoPerfil = fotoPerfil;
    }

    public String getId() {
        return id;
    }

    public Integer getNumJugador() {
        return numJugador;
    }

    public void setNumJugador(Integer numJugador) {
        this.numJugador = numJugador;
    }

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

    public ColorNaves getColorPropio() {
        return colorPropio;
    }

    public void setColorPropio(ColorNaves colorPropio) {
        this.colorPropio = colorPropio;
    }

    public ColorNaves getColorRival() {
        return colorRival;
    }

    public void setColorRival(ColorNaves colorRival) {
        this.colorRival = colorRival;
    }

    public Integer getNavesRestantes() {
        return navesRestantes;
    }

    public void setNavesRestantes(Integer navesRestantes) {
        this.navesRestantes = navesRestantes;
    }

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Jugador jugador)) return false;
        return Objects.equals(getId(), jugador.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}


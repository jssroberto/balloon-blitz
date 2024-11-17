package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;

/**
 * Representa un disparo realizado por un jugador en una casilla del tablero.
 * Contiene la información de la casilla objetivo y el jugador que realizó el disparo.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class Disparo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Casilla casilla;
    private Jugador jugador;

    /**
     * Constructor de la clase Disparo que recibe una casilla y un jugador.
     *
     * @param casilla La casilla que es objetivo del disparo.
     * @param jugador El jugador que realiza el disparo.
     */
    public Disparo(Casilla casilla, Jugador jugador) {
        this.casilla = casilla;
        this.jugador = jugador;
    }

    /**
     * Obtiene la casilla objetivo del disparo.
     *
     * @return La casilla objetivo.
     */
    public Casilla getCasilla() {
        return casilla;
    }

    /**
     * Establece la casilla objetivo del disparo.
     *
     * @param casilla La nueva casilla objetivo.
     */
    public void setCasilla(Casilla casilla) {
        this.casilla = casilla;
    }

    /**
     * Obtiene el jugador que realiza el disparo.
     *
     * @return El jugador que realiza el disparo.
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Establece el jugador que realiza el disparo.
     *
     * @param jugador El nuevo jugador que realiza el disparo.
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
}


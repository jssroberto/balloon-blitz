package org.itson.edu.balloonblitz.entidades;

import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoCasilla;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

/**
 * Representa una casilla en el tablero de juego.
 * Cada casilla tiene una coordenada, un estado que indica su condición,
 * y una nave que puede estar asociada a ella.
 * La casilla puede contener una nave o estar vacía.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class Casilla implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private EstadoCasilla estado;
    private Coordenada coordenada;
    private Nave nave;

    /**
     * Constructor que inicializa la casilla con una coordenada específica.
     * El estado de la casilla se establece como intacta y no tiene nave asociada inicialmente.
     *
     * @param coordenada La coordenada de la casilla en el tablero.
     */
    public Casilla(Coordenada coordenada) {
        this.coordenada = coordenada;
        this.estado = EstadoCasilla.INTACTA;
        this.nave = null;
    }

    /**
     * Obtiene el estado actual de la casilla.
     *
     * @return El estado de la casilla.
     */
    public EstadoCasilla getEstado() {
        return estado;
    }

    /**
     * Establece un nuevo estado para la casilla.
     *
     * @param estado El nuevo estado de la casilla.
     */
    public void setEstado(EstadoCasilla estado) {
        this.estado = estado;
    }

    /**
     * Obtiene las coordenadas de la casilla en el tablero.
     *
     * @return La coordenada de la casilla.
     */
    public Coordenada getCoordenada() {
        return coordenada;
    }

    /**
     * Establece las coordenadas de la casilla.
     *
     * @param coordenada La nueva coordenada de la casilla.
     */
    public void setCoordenada(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    /**
     * Obtiene la nave asociada a la casilla si existe.
     *
     * @return Un Optional que puede contener la nave si está presente, o vacío si no.
     */
    public Optional<Nave> getNave() {
        return Optional.ofNullable(nave);
    }

    /**
     * Establece una nave en la casilla.
     *
     * @param nave La nave a asociar a la casilla.
     */
    public void setNave(Nave nave) {
        this.nave = nave;
    }
}


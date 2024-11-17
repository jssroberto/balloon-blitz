package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;

/**
 * Representa una coordenada en un tablero, definida por una fila y una columna.
 * Esta clase es inmutable y serializable.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public record Coordenada(int fila, int columna) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}

package org.itson.edu.balloonblitz.entidades.navefactory;

import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;

/**
 * Representa un crucero en el juego.
 * El crucero es una nave de tamaño 3.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class Crucero extends Nave {

    /**
     * Constructor que inicializa un crucero con su tipo y tamaño predeterminados.
     */
    public Crucero() {
        super(TipoNave.CRUCERO, 3);
    }
}


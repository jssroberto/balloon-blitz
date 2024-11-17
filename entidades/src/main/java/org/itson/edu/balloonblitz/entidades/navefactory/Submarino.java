package org.itson.edu.balloonblitz.entidades.navefactory;

import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;

/**
 * Representa un submarino en el juego.
 * El submarino es una nave de tamaño 2.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class Submarino extends Nave {

    /**
     * Constructor que inicializa un submarino con su tipo y tamaño predeterminados.
     */
    public Submarino() {
        super(TipoNave.SUBMARINO, 2);
    }
}


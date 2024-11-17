package org.itson.edu.balloonblitz.entidades.navefactory;

import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;

/**
 * Representa un barco en el juego.
 * El barco es una nave de tamaño 1.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class Barco extends Nave {

    /**
     * Constructor que inicializa un barco con su tipo y tamaño predeterminados.
     */
    public Barco() {
        super(TipoNave.BARCO, 1);
    }
}


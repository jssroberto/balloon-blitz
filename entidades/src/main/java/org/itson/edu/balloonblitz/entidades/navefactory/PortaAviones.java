package org.itson.edu.balloonblitz.entidades.navefactory;

import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;

/**
 * Representa un portaaviones en el juego.
 * El portaaviones es una nave de tamaño 4.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class PortaAviones extends Nave {

    /**
     * Constructor que inicializa un portaaviones con su tipo y tamaño predeterminados.
     */
    public PortaAviones() {
        super(TipoNave.PORTAAVIONES, 4);
    }
}


package org.itson.edu.balloonblitz.entidades.navefactory;

import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;

/**
 * Simple Factory para crear instancias de diferentes tipos de naves.
 * Esta clase proporciona un método estático para crear naves basadas en el tipo especificado.
 * Utiliza un patrón de diseño Factory para abstraer la creación de naves.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class NaveFactory {

    /**
     * Crea una nave según el tipo especificado.
     *
     * @param tipo El tipo de nave que se desea crear.
     * @return una instancia de la nave correspondiente al tipo.
     * @throws IllegalArgumentException si el tipo de nave no es reconocido.
     */
    public static Nave crearNave(TipoNave tipo) {
        return switch (tipo) {
            case PORTAAVIONES -> new PortaAviones();
            case CRUCERO -> new Crucero();
            case SUBMARINO -> new Submarino();
            case BARCO -> new Barco();
        };
    }
}


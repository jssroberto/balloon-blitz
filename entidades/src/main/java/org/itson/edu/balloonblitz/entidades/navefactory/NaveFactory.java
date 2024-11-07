
package org.itson.edu.balloonblitz.entidades.navefactory;

import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;

/**
 *
 * @author elimo
 */
public class NaveFactory {
    
    public static Nave crearNave(TipoNave tipo) {
        switch (tipo) {
            case PORTAAVIONES -> {
                return new PortaAviones();
            }
            case CRUCERO -> {
                return new Crucero();
            }
            case SUBMARINO -> {
                return new Submarino();
            }
            case BARCO -> {
                return new Barco();
            }
            default -> throw new IllegalArgumentException("Tipo de nave no reconocido: " + tipo);
        }
    }
    
}

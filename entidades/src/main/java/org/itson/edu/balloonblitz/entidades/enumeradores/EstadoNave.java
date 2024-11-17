package org.itson.edu.balloonblitz.entidades.enumeradores;

/**
 * Enum que representa los posibles estados de una nave en el juego.
 * Cada estado refleja la condición actual de una nave durante el transcurso del juego.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public enum EstadoNave {

     /**
      * Estado inicial de la nave. La nave está intacta y funcional.
      */
     COMPLETA,

     /**
      * Estado de la nave cuando ha sufrido daños, pero aún está en funcionamiento.
      */
     AVERIADA,

     /**
      * Estado de la nave cuando ha sido completamente destruida.
      */
     HUNDIDA;
}


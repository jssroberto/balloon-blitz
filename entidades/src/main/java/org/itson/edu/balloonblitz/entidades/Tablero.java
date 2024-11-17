package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;

/**
 * Representa el tablero de juego, que consiste en una matriz de casillas. Cada
 * casilla tiene una coordenada específica y un estado, y el tablero puede
 * contener varias naves.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class Tablero implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Casilla[][] matriz;

    /**
     * Constructor que inicializa el tablero con una matriz de casillas de
     * tamaño 10x10. Cada casilla se crea con sus coordenadas correspondientes.
     */
    public Tablero() {
        matriz = new Casilla[10][10];
        inicializarTablero();
    }

    /**
     * Inicializa el tablero, creando una casilla para cada coordenada en una
     * matriz 10x10.
     */
    private void inicializarTablero() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matriz[i][j] = new Casilla(new Coordenada(i, j));
            }
        }
    }

    /**
     * Obtiene el número de filas del tablero.
     *
     * @return El número de filas del tablero.
     */
    public int getFilas() {
        return matriz.length;
    }

    /**
     * Obtiene el número de columnas del tablero.
     *
     * @return El número de columnas del tablero.
     */
    public int getColumnas() {
        return matriz[0].length;
    }

    /**
     * Obtiene la matriz de casillas que representa el tablero.
     *
     * @return La matriz de casillas del tablero.
     */
    public Casilla[][] getMatriz() {
        return matriz;
    }

    /**
     * Obtiene la casilla ubicada en una coordenada específica.
     *
     * @param coordenada Las coordenadas de la casilla a obtener.
     * @return La casilla en las coordenadas proporcionadas.
     */
    public Casilla getCasilla(Coordenada coordenada) {
        return matriz[coordenada.fila()][coordenada.columna()];
    }

    /**
     * Establece una casilla en el tablero en las coordenadas de la casilla
     * proporcionada.
     *
     * @param casilla La casilla a colocar en el tablero.
     */
    public void setCasilla(Casilla casilla) {
        matriz[casilla.getCoordenada().fila()][casilla.getCoordenada().columna()] = casilla;
    }

}

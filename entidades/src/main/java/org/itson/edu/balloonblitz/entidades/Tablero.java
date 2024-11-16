package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 * @author elimo
 */
public class Tablero implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Casilla[][] tablero;

    public Tablero() {
        tablero = new Casilla[10][10];
        inicializarTablero();
    }

    private void inicializarTablero() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tablero[i][j] = new Casilla(new Coordenada(i, j));
            }
        }
    }

    public int getFilas() {
        return tablero.length;
    }

    public int getColumnas() {
        return tablero[0].length;
    }

    public Casilla[][] getTablero() {
        return tablero;
    }

    public Casilla getCasilla(Coordenada coordenada){
        return tablero[coordenada.fila()][coordenada.columna()];
    }

    public void setCasilla(Casilla casilla){
        tablero[casilla.getCoordenada().fila()][casilla.getCoordenada().columna()] = casilla;
    }
}

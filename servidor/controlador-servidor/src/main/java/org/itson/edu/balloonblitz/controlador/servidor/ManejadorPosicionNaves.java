package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Tablero;

public class ManejadorPosicionNaves {


    public boolean validarTablero(Tablero tablero) {
        for (int fila = 0; fila < tablero.getFilas(); fila++) {
            for (int columna = 0; columna < tablero.getColumnas(); columna++) {
                Casilla casilla = tablero.getCasilla(new Coordenada(fila, columna));
                // Si la casilla tiene una nave, validamos
                if (casilla.getNave().isPresent()) {
                    Nave nave = casilla.getNave().get();
                    if (!validarTamanioNave(nave)) {
                        return false;
                    }
                    if (!validarAdyacencia(casilla, tablero, nave)) {
                        return false;
                    }
                    if (!validarPosicionNave(casilla, tablero, nave)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean validarTamanioNave(Nave nave) {
        int tamanio = nave.getTamano();
        return tamanio >= 1 && tamanio <= 4; // El tamaño de las naves debe ser entre 1 y 4
    }

    private static boolean validarAdyacencia(Casilla casilla, Tablero tablero, Nave nave) {
        int fila = casilla.getCoordenada().fila();
        int columna = casilla.getCoordenada().columna();
        // Verificamos las casillas adyacentes en las 8 direcciones posibles
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Ignorar la casilla actual
                if (i == 0 && j == 0) continue;
                int nuevaFila = fila + i;
                int nuevaColumna = columna + j;
                if (nuevaFila >= 0 && nuevaFila < 10 && nuevaColumna >= 0 && nuevaColumna < 10) {
                    Casilla casillaAdyacente = tablero.getCasilla(new Coordenada(nuevaFila, nuevaColumna));
                    if (casillaAdyacente.getNave().isPresent()) {
                        return false; // Si una casilla adyacente tiene una nave, no es válido
                    }
                }
            }
        }
        return true;
    }

    private static boolean validarPosicionNave(Casilla casilla, Tablero tablero, Nave nave) {
        int fila = casilla.getCoordenada().fila();
        int columna = casilla.getCoordenada().columna();
        int tamanio = nave.getTamano();
        // Comprobamos si la nave está alineada vertical u horizontalmente
        boolean esVertical = false;
        boolean esHorizontal = false;

        // Comprobar si se puede colocar verticalmente
        if (fila + tamanio <= 10) {
            esVertical = true;
            for (int i = 0; i < tamanio; i++) {
                Casilla casillaVertical = tablero.getCasilla(new Coordenada(fila + i, columna));
                if (casillaVertical.getNave().isPresent()) {
                    esVertical = false; // Ya hay una nave en esta casilla
                    break;
                }
            }
        }

        // Comprobar si se puede colocar horizontalmente
        if (columna + tamanio <= 10) {
            esHorizontal = true;
            for (int i = 0; i < tamanio; i++) {
                Casilla casillaHorizontal = tablero.getCasilla(new Coordenada(fila, columna + i));
                if (casillaHorizontal.getNave().isPresent()) {
                    esHorizontal = false; // Ya hay una nave en esta casilla
                    break;
                }
            }
        }

        // La nave debe ser colocada de manera vertical o horizontal
        return esVertical || esHorizontal;
    }


}

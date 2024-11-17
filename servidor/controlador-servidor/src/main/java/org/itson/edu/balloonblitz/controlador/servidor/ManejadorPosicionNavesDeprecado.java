/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.eventos.PosicionNavesEvento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoPosicionNavesEvento;

import java.util.ArrayList;
import java.util.List;

/**
 * @author elimo
 */
public class ManejadorPosicionNavesDeprecado {
    private final PosicionNavesEvento evento;
    private final Casilla[][] tablero;

    public ManejadorPosicionNavesDeprecado(PosicionNavesEvento evento) {
        this.evento = evento;
        this.tablero = evento.getTablero().getTablero();
    }

    public ResultadoPosicionNavesEvento procesarEvento() {
        return validarTablero() ? new ResultadoPosicionNavesEvento(true) : new ResultadoPosicionNavesEvento(false);
    }

    public boolean validarTablero() {
        // Recorrer el tablero para validar las naves
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                Casilla casilla = tablero[i][j];
                if (casilla.getNave().isPresent()) {
                    Nave nave = casilla.getNave().get();
                    // Verificar que la nave esté correctamente alineada (horizontal o vertical)
                    if (!esNaveContinua(casilla)) {
                        return false;
                    }
                    // Verificar que no haya naves adyacentes
                    if (hayNaveAdyacente(i, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean esNaveContinua(Casilla casilla) {
        Nave nave = casilla.getNave().orElseThrow();
        // Verificar si la nave está correctamente alineada en horizontal o vertical
        List<Casilla> navesAdyacentes = obtenerNavesAdyacentes(casilla.getCoordenada());

        // Si la cantidad de casillas con la misma nave no es igual al tamaño de la nave, es inválido
        long navesEnLinea = navesAdyacentes.stream()
                .filter(c -> c.getNave().isPresent() && c.getNave().get().equals(nave))
                .count();

        return navesEnLinea == nave.getTamano();
    }

    private boolean hayNaveAdyacente(int fila, int columna) {
        // Comprobar en las 4 direcciones si hay una nave adyacente
        int[][] direcciones = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] direccion : direcciones) {
            int nuevaFila = fila + direccion[0];
            int nuevaColumna = columna + direccion[1];
            if (esPosicionValida(nuevaFila, nuevaColumna)) {
                Casilla casillaAdyacente = tablero[nuevaFila][nuevaColumna];
                if (casillaAdyacente.getNave().isPresent()) {
                    return true;  // Se encontró una nave adyacente
                }
            }
        }
        return false;
    }

    private boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < tablero.length && columna >= 0 && columna < tablero[0].length;
    }

    private List<Casilla> obtenerNavesAdyacentes(Coordenada coordenada) {
        List<Casilla> adyacentes = new ArrayList<>();
        int[][] direcciones = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Horizontal y Vertical
        for (int[] dir : direcciones) {
            int nuevaFila = coordenada.fila() + dir[0];
            int nuevaColumna = coordenada.columna() + dir[1];
            if (esPosicionValida(nuevaFila, nuevaColumna)) {
                adyacentes.add(tablero[nuevaFila][nuevaColumna]);
            }
        }
        return adyacentes;
    }
}

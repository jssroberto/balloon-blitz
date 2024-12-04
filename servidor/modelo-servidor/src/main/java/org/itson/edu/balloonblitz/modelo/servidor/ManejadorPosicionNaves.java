package org.itson.edu.balloonblitz.modelo.servidor;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;
import org.itson.edu.balloonblitz.entidades.eventos.PosicionNavesEvento;

import java.util.*;

public class ManejadorPosicionNaves {
    private final Tablero tablero;

    public ManejadorPosicionNaves(PosicionNavesEvento evento) {
        this.tablero = evento.getTablero();
    }

    public boolean procesarEvento() {
        return validarTablero();
    }

    private boolean validarTablero() {
        boolean[][] visitadas = new boolean[tablero.getFilas()][tablero.getColumnas()];
        Map<TipoNave, Integer> contadorNaves = inicializarContadorNaves();

        Casilla[][] casillas = tablero.getMatriz();
        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (!visitadas[i][j] && casillas[i][j].getNave().isPresent()) {
                    Nave nave = casillas[i][j].getNave().get();
                    if (!validarNave(tablero, i, j, visitadas, nave)) {
                        return false;
                    }
                    contadorNaves.put(nave.getTipoNave(), contadorNaves.get(nave.getTipoNave()) + 1);
                }
            }
        }
        return true;
    }

    private boolean validarNave(Tablero tablero, int fila, int columna, boolean[][] visitadas, Nave nave) {
        Casilla[][] casillas = tablero.getMatriz();
        List<Coordenada> coordenadas = new ArrayList<>();
        int tamano = nave.getTamano();

        // Detectar la orientación de la nave (horizontal o vertical)
        boolean horizontal = (columna + 1 < tablero.getColumnas() && casillas[fila][columna + 1].getNave().orElse(null) == nave);
        boolean vertical = (fila + 1 < tablero.getFilas() && casillas[fila + 1][columna].getNave().orElse(null) == nave);

        // Recopilar las coordenadas de la nave
        for (int k = 0; k < tamano; k++) {
            int f = fila + (vertical ? k : 0);
            int c = columna + (horizontal ? k : 0);
            if (f >= tablero.getFilas() || c >= tablero.getColumnas() || visitadas[f][c] || casillas[f][c].getNave().orElse(null) != nave) {
                return false; // Tamaño incorrecto o conflicto con otra nave
            }
            coordenadas.add(new Coordenada(f, c));
            visitadas[f][c] = true;
        }

        // Verificar las casillas adyacentes
        for (Coordenada coordenada : coordenadas) {
            if (!verificarAdyacencias(tablero, coordenada, nave)) {
                return false;
            }
        }

        return true;
    }

    private boolean verificarAdyacencias(Tablero tablero, Coordenada coordenada, Nave naveActual) {
        Casilla[][] casillas = tablero.getMatriz();
        int fila = coordenada.fila();
        int columna = coordenada.columna();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int f = fila + i;
                int c = columna + j;
                if ((f != fila || c != columna) && f >= 0 && f < tablero.getFilas() && c >= 0 && c < tablero.getColumnas()) {
                    Optional<Nave> naveAdyacente = casillas[f][c].getNave();
                    if (naveAdyacente.isPresent() && !naveAdyacente.get().getId().equals(naveActual.getId())) {
                        // Detecta una nave adyacente que no es la misma nave actual
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private Map<TipoNave, Integer> inicializarContadorNaves() {
        Map<TipoNave, Integer> contador = new EnumMap<>(TipoNave.class);
        contador.put(TipoNave.PORTAAVIONES, 0);
        contador.put(TipoNave.CRUCERO, 0);
        contador.put(TipoNave.SUBMARINO, 0);
        contador.put(TipoNave.BARCO, 0);
        return contador;
    }

    private boolean validarCantidadNaves(Map<TipoNave, Integer> contadorNaves) {
        return contadorNaves.get(TipoNave.PORTAAVIONES) == 1 &&
                contadorNaves.get(TipoNave.CRUCERO) == 2 &&
                contadorNaves.get(TipoNave.SUBMARINO) == 3 &&
                contadorNaves.get(TipoNave.BARCO) == 4;
    }

    public static List<Nave> inicializarNaves(Tablero tablero) {
        List<Nave> naves = new ArrayList<>();
        Casilla[][] casillas = tablero.getMatriz();

        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                if (casillas[i][j].getNave().isPresent()) {
                    Nave nave = casillas[i][j].getNave().get();
                    if (!naves.contains(nave)) {
                        naves.add(nave);
                    }
                }
            }
        }
        return naves;
    }
}

package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.eventos.PosicionNavesEvento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoPosicionNavesEvento;
import org.itson.edu.balloonblitz.entidades.navefactory.Barco;
import org.itson.edu.balloonblitz.entidades.navefactory.Crucero;
import org.itson.edu.balloonblitz.entidades.navefactory.PortaAviones;
import org.itson.edu.balloonblitz.entidades.navefactory.Submarino;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManejadorPosicionNavesTest {

    private Tablero tablero;

    @BeforeEach
    public void setUp() {
        tablero = new Tablero();

        // Agregar 2 PortaAviones (4 casillas de longitud)
        colocarNave(new PortaAviones(), 0, 0, true);
        colocarNave(new PortaAviones(), 2, 2, false);

        // Agregar 2 Cruceros (3 casillas de longitud)
        colocarNave(new Crucero(), 5, 0, false);
        colocarNave(new Crucero(), 8, 2, true);

        // Agregar 3 Submarinos (2 casillas de longitud)
        colocarNave(new Submarino(), 4, 5, true);
        colocarNave(new Submarino(), 7, 7, false);
        colocarNave(new Submarino(), 7, 9, false);

        // Agregar 4 Barcos (1 casilla de longitud)
        colocarNave(new Barco(), 1, 8, true);
        colocarNave(new Barco(), 2, 4, true);
        colocarNave(new Barco(), 6, 5, true);
        colocarNave(new Barco(), 4, 8, true);
    }

    @Test
    public void testTableroValido() {
        // Crear el evento y el manejador
        PosicionNavesEvento evento = new PosicionNavesEvento(tablero);
        ManejadorPosicionNaves manejador = new ManejadorPosicionNaves(evento);

        // Procesar el evento y validar el resultado
        ResultadoPosicionNavesEvento resultado = manejador.procesarEvento();
        assertTrue(resultado.isValid(), "El tablero debería ser válido con las naves correctamente posicionadas.");
    }

    @Test
    public void testTableroInvalido() {
        // Colocar una nave inválida que rompe las reglas (adyacente)
        colocarNave(new Barco(), 0, 1, true);

        // Crear el evento y el manejador
        PosicionNavesEvento evento = new PosicionNavesEvento(tablero);
        ManejadorPosicionNaves manejador = new ManejadorPosicionNaves(evento);

        // Procesar el evento y validar el resultado
        ResultadoPosicionNavesEvento resultado = manejador.procesarEvento();
        assertFalse(resultado.isValid(), "El tablero debería ser inválido porque hay un barco enseguida de un portaavion.");
    }

    @Test
    public void testTableroVacio() {
        tablero = new Tablero(); // Reinicia el tablero sin naves

        PosicionNavesEvento evento = new PosicionNavesEvento(tablero);
        ManejadorPosicionNaves manejador = new ManejadorPosicionNaves(evento);

        ResultadoPosicionNavesEvento resultado = manejador.procesarEvento();
        assertTrue(resultado.isValid(), "Un tablero vacío debería ser válido si no hay restricciones de cantidad mínima.");
    }

    private void colocarNave(Nave nave, int fila, int columna, boolean horizontal) {
        for (int i = 0; i < nave.getTamano(); i++) {
            int f = fila + (horizontal ? 0 : i);
            int c = columna + (horizontal ? i : 0);
            Casilla casilla = tablero.getTablero()[f][c];
            casilla.setNave(nave);
        }
    }
}


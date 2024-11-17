/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.navefactory.Barco;
import org.itson.edu.balloonblitz.entidades.navefactory.PortaAviones;
import org.itson.edu.balloonblitz.entidades.navefactory.Submarino;

/**
 *
 * @author elimo
 */
public class pruebasservidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Servidor.getInstance();
//        ConexionObserver conexion = new Lobby();
//        Servidor.getInstance().setObservadorConexion(conexion);



        Tablero tablero = new Tablero();

        // Crear algunas naves
        Nave barco1 = new Barco();
        Nave submarino1 = new Submarino();
        Nave portaaviones1 = new PortaAviones();

        // Colocar las naves en el tablero (esto es solo un ejemplo de cómo podrías agregarlas)
        agregarNave(tablero, barco1, new Coordenada(0, 0), true);  // Barco en (0,0) horizontal
        agregarNave(tablero, submarino1, new Coordenada(2, 2), false);  // Submarino en (2,2) vertical
        agregarNave(tablero, portaaviones1, new Coordenada(5, 5), true);  // Portaaviones en (5,5) horizontal

        // Crear el validador
        ManejadorPosicionNaves validador = new ManejadorPosicionNaves();

        // Validar el tablero
        boolean esValido = validador.validarTablero(tablero);
        System.out.println("¿El tablero es válido? " + esValido);
    }

    // Método para agregar una nave en una posición específica con una orientación
    public static void agregarNave(Tablero tablero, Nave nave, Coordenada coordenada, boolean esHorizontal) {
        int tamano = nave.getTamano();
        if (esHorizontal) {
            for (int i = 0; i < tamano; i++) {
                tablero.getCasilla(new Coordenada(coordenada.fila(), coordenada.columna() + i)).setNave(nave);
            }
        } else {
            for (int i = 0; i < tamano; i++) {
                tablero.getCasilla(new Coordenada(coordenada.fila() + i, coordenada.columna())).setNave(nave);
            }
        }
    }
}

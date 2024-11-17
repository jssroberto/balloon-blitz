/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.modelo.servidor.ConexionObserver;
import org.itson.edu.balloonblitz.modelo.servidor.Servidor;

/**
 *
 * @author elimo
 */
public class pruebasservidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Servidor.getInstance();
        ConexionObserver conexion = new Lobby();
        Servidor.getInstance().setObservadorConexion(conexion);
    }
}

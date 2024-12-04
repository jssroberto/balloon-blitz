package org.itson.edu.balloonblitz.main.servidor;

import org.itson.edu.balloonblitz.modelo.servidor.Lobby;
import org.itson.edu.balloonblitz.conexion.servidor.ConexionObserver;
import org.itson.edu.balloonblitz.conexion.servidor.Servidor;

public class MainServidor {
    public static void main(String[] args) {
        Servidor.getInstance();
        ConexionObserver conexion = new Lobby();
        Servidor.getInstance().setObservadorConexion(conexion);
    }
}
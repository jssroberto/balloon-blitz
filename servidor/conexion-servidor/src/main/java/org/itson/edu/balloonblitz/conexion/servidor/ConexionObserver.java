/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.itson.edu.balloonblitz.conexion.servidor;

/**
 * Interfaz para los suscriptores de la conexion
 * @author elimo
 */
public interface ConexionObserver {
    
    /**
     * Metodo que obtiene los I/O de cada cliente para su manejo por parte de los suscriptores
     * @param streams Streams del cliente
     */
    void clienteConectado(ControladorStreams streams);
}

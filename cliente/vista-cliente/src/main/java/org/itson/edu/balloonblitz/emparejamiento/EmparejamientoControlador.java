/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.emparejamiento;

import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;
import org.itson.edu.balloonblitz.modelo.ObservadorResultado;
import org.itson.edu.balloonblitz.FramePrincipal;

/**
 *
 * @author elimo
 */
public class EmparejamientoControlador implements ObservadorResultado {

    private static EmparejamientoControlador instancia; // Instancia única de la clase
    private EmparejamientoPanel vista;
    private EmparejamientoModel modelo;
    private FramePrincipal frame;
    private boolean valido;

    // Constructor privado para evitar que se instancien múltiples objetos
    private EmparejamientoControlador() {
        
        valido = false;
        modelo = new EmparejamientoModel(this);
        this.setObservador(vista);
        unirsePartida();
    }

    // Método público estático para obtener la instancia única
    public static synchronized EmparejamientoControlador getInstancia() {
        if (instancia == null) {
            instancia = new EmparejamientoControlador(); // Se crea la instancia si no existe
        }
        return instancia;
    }

    public void cambiarPanel(FramePrincipal frame) {
        this.frame = frame;
    }

    @Override
    public void manejarEvento(ResultadoEvento evento) {
        System.out.println("Entramos al ejecutador del metodo");
        valido = evento.isValid();
        modelo.setValido(valido);
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        modelo.setValido(valido);
    }

    public void unirsePartida() {
        modelo.unirsePartida(frame);
    }

    public void enviarEvento(Evento evento) {
        ConexionCliente.getInstancia().enviarMensaje(evento);
    }

    @Override
    public void actualizarInterfaz(String mensaje) {
        vista.getLblEsperando().setText("Partida Encottrada");
    }
}

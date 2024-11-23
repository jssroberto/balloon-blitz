/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.personalizar;

import java.util.ArrayList;
import java.util.List;
import org.itson.edu.balloonblitz.FramePrincipal;
//import org.itson.edu.balloonblitz.vista.FramePrincipal;

/**
 *
 * @author elimo
 */
public class ModelPersonalizar {

    private final List<ObserverPersonalizar> observers = new ArrayList<>();

    public ModelPersonalizar() {
    }

    public void addObserver(ObserverPersonalizar observer) {
        observers.add(observer);
    }

    private void notifyObservers(UpdateEventPersonalizar event) {
        for (ObserverPersonalizar observer : observers) {
            observer.update(event);
        }
    }

    public void cambiarPanel() {
        notifyObservers(new UpdateEventPersonalizar(this, EventTypePersonalizar.CAMBIAR_PANEL_ESPERAR_JUGADOR));
        notifyObservers(new UpdateEventPersonalizar(this, EventTypePersonalizar.ENVIO_JUGADOR));
       
    }
}

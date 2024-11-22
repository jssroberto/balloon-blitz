/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.esperarJugador;

/**
 *
 * @author elimo
 */
public final class UpdateEvent {
    private final EmparejamientoModel source;
    private final EventType eventType;

    public UpdateEvent(EmparejamientoModel source, EventType eventType) {
        this.source = source;
        this.eventType = eventType;
    }

    public EmparejamientoModel getSource() {
        return source;
    }

    public EventType getEventType() {
        return eventType;
    }
    
    
}

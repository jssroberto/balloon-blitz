/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.modelo.servidor;

import org.itson.edu.balloonblitz.entidades.*;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoCasilla;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoNave;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoDisparoEvento;

/**
 * @author elimo
 */
public class ManejadorDisparo {
    private final DisparoEvento disparoEvento;
    private final Tablero tableroRival;
    private final Jugador jugadorRival;

    public ManejadorDisparo(DisparoEvento disparoEvento, Tablero tableroRival, Jugador jugadorRival) {
        this.disparoEvento = disparoEvento;
        this.tableroRival = tableroRival;
        this.jugadorRival = jugadorRival;
    }

    public ResultadoDisparoEvento procesar() {
        return procesarEvento();
    }

    //Los cambios que se le hagan al tablero y jugador en esta clase se reflejarán en la partida que tiene el servidor (i hope)
    public ResultadoDisparoEvento procesarEvento() {
        Casilla casilla = tableroRival.getCasilla(disparoEvento.getCoordenada());


        casilla.setEstado(EstadoCasilla.GOLPEADA);

        // Usar Optional para obtener la nave en la casilla
        if (casilla.getNave().isEmpty()) {
            // Si no hay nave en la casilla, retornar un evento de resultado sin impacto
            return new ResultadoDisparoEvento(tableroRival, jugadorRival.getNaves());
        }
        return procesarDisparo(casilla);
    }

    /**
     * Procesa el disparo realizado por un jugador.
     *
     * @return Resultado del evento de disparo.
     */
    private ResultadoDisparoEvento procesarDisparo(Casilla casilla) {
        Nave nave = casilla.getNave().orElseThrow();
        int impactos = nave.recibirImpacto();

        if (impactos > nave.getTamano()) {
            throw new IllegalStateException("El número de impactos excede el tamaño de la nave");
        } else if (impactos < nave.getTamano()) {
            procesarNaveAveriada(casilla, nave);
        } else {
            // Si la nave está hundida, procesar el hundimiento
            procesarNaveHundida(tableroRival, jugadorRival, casilla);
            return new ResultadoDisparoEvento(tableroRival, jugadorRival.getNaves());
        }
        return new ResultadoDisparoEvento(tableroRival, jugadorRival.getNaves());
    }

    private void procesarNaveAveriada(Casilla casilla, Nave nave) {
        nave.setEstadoNave(EstadoNave.AVERIADA);
        tableroRival.setCasilla(casilla);
    }

    private void procesarNaveHundida(Tablero tableroRival, Jugador jugadorRival, Casilla casilla) {
        Nave nave = casilla.getNave().orElseThrow();

        // Usar un stream para encontrar la nave en la lista de naves del jugador rival y marcarla como hundida
        boolean naveEncontrada = jugadorRival.getNaves().stream()
                .filter(n -> n.equals(nave))
                .peek(n -> n.setEstadoNave(EstadoNave.HUNDIDA))
                .findFirst()
                .isPresent();

        if (!naveEncontrada) {
            throw new IllegalStateException("La nave no se encuentra en la lista de naves del jugador.");
        }
        actualizarNavesTablero(tableroRival, nave);
    }

    private void actualizarNavesTablero(Tablero tableroRival, Nave nave) {
        for (int i = 0; i < tableroRival.getFilas(); i++) {
            for (int j = 0; j < tableroRival.getColumnas(); j++) {
                Casilla c = tableroRival.getCasilla(new Coordenada(i, j));
                if (c.getNave().isPresent() && c.getNave().get().equals(nave)) {
                    c.setEstado(EstadoCasilla.GOLPEADA);
                    tableroRival.setCasilla(c);
                }
            }
        }
    }
}

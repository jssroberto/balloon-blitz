/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.controlador.servidor;

import org.itson.edu.balloonblitz.entidades.*;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoCasilla;
import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoNave;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.ResultadoEvento;

import java.util.Optional;

/**
 * @author elimo
 */
public class ManejadorDisparo {

    private final DisparoEvento disparoEvento;
    private final Partida partida;

    public ManejadorDisparo(DisparoEvento disparoEvento, Partida partida) {
        this.disparoEvento = disparoEvento;
        this.partida = partida;
    }

    public ResultadoEvento procesarDisparo(DisparoEvento disparoEvento) {
        Jugador jugador = disparoEvento.getEmisor();
        Coordenada coordenada = disparoEvento.getCoordenda();
        Tablero tablero = obtenerTableroRival(jugador);
        Casilla casilla = tablero.getCasilla(coordenada);

        if (casilla.getEstado() == EstadoCasilla.GOLPEADA) {
            throw new IllegalArgumentException("La casilla ya fue golpeada");
        }
        casilla.setEstado(EstadoCasilla.GOLPEADA);

        // Usar Optional para obtener la nave
        Optional<Nave> naveOptional = casilla.getNave();
        if (naveOptional.isEmpty()) {
            return new ResultadoEvento(casilla, partida, false);
        }

        Nave nave = naveOptional.get();
        nave.recibirImpacto();
        nave.setEstadoNave(EstadoNave.AVERIADA);

        if (isNaveHundida(nave)) {
            // Pasamos la nave como parámetro
//            procesarNaveHundida(casilla, tablero, nave);
        }

        return new ResultadoEvento(casilla, partida, true);
    }

    private Tablero obtenerTableroRival(Jugador jugador) {
        return jugador.equals(partida.getJugador1()) ? partida.getTableroJugador2() : partida.getTableroJugador1();
    }

    private boolean isNaveHundida(Nave nave) {
        return nave.getImpactos() == nave.getTamano();
    }






}

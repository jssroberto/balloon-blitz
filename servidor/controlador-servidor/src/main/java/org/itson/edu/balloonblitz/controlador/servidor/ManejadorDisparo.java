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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author elimo
 */
public class ManejadorDisparo {

    private final DisparoEvento disparoEvento;
    private final Tablero tableroRival;
    private final Jugador jugadorRival;
    private List<Casilla> casillasAtacadas;

    public ManejadorDisparo(DisparoEvento disparoEvento, Tablero tableroRival, Jugador jugadorRival) {
        this.disparoEvento = disparoEvento;
        this.tableroRival = tableroRival;
        this.jugadorRival = jugadorRival;
        this.casillasAtacadas = new ArrayList<>();
    }

    /**
     * Procesa el disparo realizado por un jugador.
     *
     * @return Resultado del evento de disparo.
     */
    public ResultadoEvento procesarDisparo() {
        // Obtener el jugador que realizó el disparo y la coordenada del disparo
        Jugador jugador = disparoEvento.getEmisor();
        Coordenada coordenada = disparoEvento.getCoordenda();

        // Obtener el tablero del rival y la casilla objetivo
        Casilla casilla = tableroRival.getCasilla(coordenada);

        // Verificar si la casilla ya fue golpeada
        if (casilla.getEstado() == EstadoCasilla.GOLPEADA) {
            throw new IllegalArgumentException("La casilla ya fue golpeada");
        }
        casilla.setEstado(EstadoCasilla.GOLPEADA);

        // Usar Optional para obtener la nave en la casilla
        Optional<Nave> naveOptional = casilla.getNave();
        if (naveOptional.isEmpty()) {
            // Si no hay nave en la casilla, retornar un evento de resultado sin impacto
            return new ResultadoEvento(casillasAtacadas, false);
        }

        // Si hay una nave, procesar el impacto
        Nave nave = naveOptional.get();
        int impactos = nave.recibirImpacto();
        // Verificar si el número de impactos excede el tamaño de la nave
        if (impactos > nave.getTamano()) {
            throw new IllegalStateException("El número de impactos excede el tamaño de la nave");
        }

        if (impactos < nave.getTamano()) {
            nave.setEstadoNave(EstadoNave.AVERIADA);
            return new ResultadoEvento(casillasAtacadas, true);

        } else if (impactos == nave.getTamano()) {
            // Si la nave está hundida, procesar el hundimiento
            nave.setEstadoNave(EstadoNave.HUNDIDA);
            procesarNaveHundida(tableroRival, jugadorRival, casilla);
        }

        // Retornar un evento de resultado con impacto
        return new ResultadoEvento(casillasAtacadas, true);
    }


    private void procesarNaveHundida(Tablero tableroRival, Jugador jugadorRival, Casilla casilla) {
        Nave nave = casilla.getNave().orElseThrow();
        List<Nave> navesJugador = jugadorRival.getNaves();

        // Usar un stream para encontrar la nave en la lista de naves del jugador rival y marcarla como hundida
        boolean naveEncontrada = navesJugador.stream()
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
                    casillasAtacadas.add(c);
                }
            }
        }
    }
}

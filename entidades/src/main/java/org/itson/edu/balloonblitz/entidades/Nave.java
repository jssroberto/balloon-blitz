package org.itson.edu.balloonblitz.entidades;

import org.itson.edu.balloonblitz.entidades.enumeradores.EstadoNave;
import org.itson.edu.balloonblitz.entidades.enumeradores.TipoNave;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Clase abstracta que representa una nave en el juego.
 * Cada nave tiene un tipo, tamaño, estado y puede recibir impactos durante el juego.
 * Esta clase es serializable e inmutable en cuanto a su identificador único y tipo de nave.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public abstract class Nave implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String id;
    private final TipoNave tipoNave;
    private final int tamano;
    private EstadoNave estadoNave;
    private int impactos;

    /**
     * Constructor para inicializar una nave con un tipo específico y un tamaño definido.
     * El identificador único se genera automáticamente.
     *
     * @param tipoNave tipo de la nave.
     * @param tamano tamaño de la nave.
     */
    public Nave(TipoNave tipoNave, int tamano) {
        this.id = UUID.randomUUID().toString();
        this.tipoNave = tipoNave;
        this.tamano = tamano;
        this.estadoNave = EstadoNave.COMPLETA;
        this.impactos = 0;
    }

    /**
     * Devuelve el tipo de nave.
     *
     * @return el tipo de la nave.
     */
    public TipoNave getTipoNave() {
        return tipoNave;
    }

    /**
     * Devuelve el tamaño de la nave.
     *
     * @return el tamaño de la nave.
     */
    public int getTamano() {
        return tamano;
    }

    /**
     * Devuelve el estado actual de la nave.
     *
     * @return el estado de la nave.
     */
    public EstadoNave getEstadoNave() {
        return estadoNave;
    }

    /**
     * Establece el estado de la nave.
     *
     * @param estadoNave nuevo estado de la nave.
     */
    public void setEstadoNave(EstadoNave estadoNave) {
        this.estadoNave = estadoNave;
    }

    /**
     * Devuelve el número de impactos recibidos por la nave.
     *
     * @return el número de impactos.
     */
    public int getImpactos() {
        return impactos;
    }

    /**
     * Devuelve el identificador único de la nave.
     *
     * @return el identificador único.
     */
    public String getId() {
        return id;
    }

    /**
     * Incrementa el número de impactos recibidos y devuelve el nuevo valor.
     *
     * @return el número de impactos después de incrementar.
     */
    public int recibirImpacto() {
        return ++impactos;
    }

    /**
     * Compara esta nave con otro objeto para determinar si son iguales.
     *
     * @param o el objeto a comparar.
     * @return true si las naves tienen el mismo identificador, de lo contrario, false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nave nave)) return false;
        return Objects.equals(id, nave.id);
    }

    /**
     * Genera un código hash para la nave basado en su identificador único.
     *
     * @return el código hash de la nave.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
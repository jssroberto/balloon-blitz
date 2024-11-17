package org.itson.edu.balloonblitz.entidades;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import org.itson.edu.balloonblitz.entidades.enumeradores.ColorNaves;

/**
 * Representa a un jugador en el juego, con su nombre, foto de perfil, tableros, naves, colores y el estado de su turno.
 * Utiliza el patrón Builder para facilitar la creación de objetos de tipo Jugador.
 *
 * @author Eliana Monge
 * @author Cristina Castro
 * @author Eduardo Talavera
 * @author Roberto Garcia
 * @version 1.0
 */
public class Jugador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String nombre;
    private final String fotoPerfil;
    private final Tablero tableroPropio;
    private final Tablero tableroContrincante;
    private final List<Nave> naves;
    private final ColorNaves colorPropio;
    private final ColorNaves colorRival;
    private final int navesRestantes;
    public boolean turno;

    /**
     * Constructor privado que recibe el builder para crear un objeto Jugador.
     *
     * @param builder El builder que contiene los datos para construir el jugador.
     */
    private Jugador(Builder builder) {
        this.nombre = builder.nombre;
        this.fotoPerfil = builder.fotoPerfil;
        this.tableroPropio = builder.tableroPropio;
        this.tableroContrincante = builder.tableroContrincante;
        this.naves = builder.naves;
        this.colorPropio = builder.colorPropio;
        this.colorRival = builder.colorRival;
        this.navesRestantes = builder.navesRestantes;
    }

    /**
     * Establece el turno del jugador.
     *
     * @param turno El valor que indica si es el turno del jugador.
     */
    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    /**
     * Obtiene el estado del turno del jugador.
     *
     * @return True si es el turno del jugador, false en caso contrario.
     */
    public boolean isTurno() {
        return turno;
    }

    /**
     * Obtiene el nombre del jugador.
     *
     * @return El nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la foto de perfil del jugador.
     *
     * @return La foto de perfil del jugador.
     */
    public String getFotoPerfil() {
        return fotoPerfil;
    }

    /**
     * Obtiene el tablero propio del jugador.
     *
     * @return El tablero propio del jugador.
     */
    public Tablero getTableroPropio() {
        return tableroPropio;
    }

    /**
     * Obtiene el tablero del contrincante del jugador.
     *
     * @return El tablero del contrincante.
     */
    public Tablero getTableroContrincante() {
        return tableroContrincante;
    }

    /**
     * Obtiene la lista de naves del jugador.
     *
     * @return La lista de naves del jugador.
     */
    public List<Nave> getNaves() {
        return naves;
    }

    /**
     * Obtiene el color propio del jugador.
     *
     * @return El color propio del jugador.
     */
    public ColorNaves getColorPropio() {
        return colorPropio;
    }

    /**
     * Obtiene el color del rival del jugador.
     *
     * @return El color del rival del jugador.
     */
    public ColorNaves getColorRival() {
        return colorRival;
    }

    /**
     * Obtiene el número de naves restantes del jugador.
     *
     * @return El número de naves restantes.
     */
    public int getNavesRestantes() {
        return navesRestantes;
    }

    /**
     * Clase Builder que permite crear instancias de Jugador de forma fluida.
     */
    public static class Builder {

        private String nombre;
        private String fotoPerfil;
        private Tablero tableroPropio;
        private Tablero tableroContrincante;
        private List<Nave> naves;
        private ColorNaves colorPropio;
        private ColorNaves colorRival;
        private int navesRestantes;

        /**
         * Establece el nombre del jugador.
         *
         * @param nombre El nombre del jugador.
         * @return El Builder con el nombre actualizado.
         */
        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        /**
         * Establece la foto de perfil del jugador.
         *
         * @param fotoPerfil La foto de perfil del jugador.
         * @return El Builder con la foto de perfil actualizada.
         */
        public Builder fotoPerfil(String fotoPerfil) {
            this.fotoPerfil = fotoPerfil;
            return this;
        }

        /**
         * Establece el tablero propio del jugador.
         *
         * @param tableroPropio El tablero propio del jugador.
         * @return El Builder con el tablero propio actualizado.
         */
        public Builder tableroPropio(Tablero tableroPropio) {
            this.tableroPropio = tableroPropio;
            return this;
        }

        /**
         * Establece el tablero del contrincante del jugador.
         *
         * @param tableroContrincante El tablero del contrincante.
         * @return El Builder con el tablero del contrincante actualizado.
         */
        public Builder tableroContrincante(Tablero tableroContrincante) {
            this.tableroContrincante = tableroContrincante;
            return this;
        }

        /**
         * Establece las naves del jugador.
         *
         * @param naves La lista de naves del jugador.
         * @return El Builder con las naves actualizadas.
         */
        public Builder naves(List<Nave> naves) {
            this.naves = naves;
            return this;
        }

        /**
         * Establece el color propio del jugador.
         *
         * @param colorPropio El color propio del jugador.
         * @return El Builder con el color propio actualizado.
         */
        public Builder colorPropio(ColorNaves colorPropio) {
            this.colorPropio = colorPropio;
            return this;
        }

        /**
         * Establece el color del rival del jugador.
         *
         * @param colorRival El color del rival.
         * @return El Builder con el color del rival actualizado.
         */
        public Builder colorRival(ColorNaves colorRival) {
            this.colorRival = colorRival;
            return this;
        }

        /**
         * Establece el número de naves restantes del jugador.
         *
         * @param navesRestantes El número de naves restantes.
         * @return El Builder con el número de naves restantes actualizado.
         */
        public Builder navesRestantes(int navesRestantes) {
            this.navesRestantes = navesRestantes;
            return this;
        }

        /**
         * Crea una nueva instancia de Jugador con los valores establecidos en el Builder.
         *
         * @return El objeto Jugador creado.
         */
        public Jugador build() {
            return new Jugador(this);
        }
    }
}


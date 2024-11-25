package org.itson.edu.balloonblitz.auxiliar;

import org.itson.edu.balloonblitz.colocarNaves.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Tablero;

public class TableroRenderer {

    private static final String BALLOON_BASE_PATH = "/images/ballons/";
    private static final int CELL_SIZE = 45;
    private static final int GRID_OFFSET_X = 0;
    private static final int GRID_OFFSET_Y = 0;

    public static void renderizarTablero(JLabel tableroLabel, Tablero tablero, Jugador jugador) {
        tableroLabel.removeAll();
        tableroLabel.setLayout(null);

        Casilla[][] casillas = tablero.getMatriz();
        Set<Nave> navesYaProcesadas = new HashSet<>();

        for (int i = 0; i < tablero.getFilas(); i++) {
            for (int j = 0; j < tablero.getColumnas(); j++) {
                Optional<Nave> naveOpt = casillas[i][j].getNave();

                if (naveOpt.isPresent() && navesYaProcesadas.add(naveOpt.get())) {
                    Nave nave = naveOpt.get();
                    JLabel globoLabel = crearGloboLabel(nave, jugador);
                    posicionarGlobo(globoLabel, i, j);
                    tableroLabel.add(globoLabel);
                }
            }
        }

        tableroLabel.revalidate();
        tableroLabel.repaint();
    }

    public static void cargarTableroRival(JLabel tableroRival, Casilla casilla, boolean hundido, boolean atinado) {
        tableroRival.removeAll();
        tableroRival.setLayout(null);

        if (atinado) {
            JLabel globoLabel = crearGloboImpactado();
            posicionarGlobo(globoLabel, casilla.getCoordenada().fila(), casilla.getCoordenada().columna());
            tableroRival.add(globoLabel);
        }

        tableroRival.revalidate();
        tableroRival.repaint();
    }

    private static JLabel crearGloboLabel(Nave nave, Jugador jugador) {
        String color = jugador.getColorPropio().toString().toLowerCase();
        int tamano = nave.getTamano();

        String imagePath = BALLOON_BASE_PATH + color + "/" + color + "-" + tamano + ".png";
        ImageIcon icon = new ImageIcon(TableroRenderer.class.getResource(imagePath));

        JLabel label = new JLabel(icon);
        label.setSize(icon.getIconWidth(), icon.getIconHeight());
        return label;
    }

    private static JLabel crearGloboImpactado() {

        String imagePath = BALLOON_BASE_PATH + "destruida" + ".png";
        ImageIcon icon = new ImageIcon(TableroRenderer.class.getResource(imagePath));

        JLabel label = new JLabel(icon);
        label.setSize(icon.getIconWidth(), icon.getIconHeight());
        return label;
    }

    private static void posicionarGlobo(JLabel globoLabel, int fila, int columna) {
        int x = GRID_OFFSET_X + (columna * CELL_SIZE) + 2;  // Añadimos +8 aquí
        int y = GRID_OFFSET_Y + (fila * CELL_SIZE) + 2;
        globoLabel.setLocation(x, y);
    }
}

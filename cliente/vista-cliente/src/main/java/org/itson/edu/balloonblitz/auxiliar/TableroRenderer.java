package org.itson.edu.balloonblitz.auxiliar;

import java.util.List;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Tablero;

public class TableroRenderer {

    private static final String BALLOON_BASE_PATH = "/images/ballons/";
    private static final int CELL_SIZE = 46;
    private static final int GRID_OFFSET_X = 0;
    private static final int GRID_OFFSET_Y = 0;

    public static void renderizarTablero(JLabel tableroLabel, Tablero tablero, List<Nave> naves) {
        // Eliminar todos los componentes previos
        tableroLabel.removeAll();
        tableroLabel.setLayout(null);

        // Iterar sobre cada nave en la lista de naves
        for (Nave nave : naves) {
            // Encontrar todas las casillas que ocupa esta nave
            Casilla[][] casillas = tablero.getTablero();
            for (int i = 0; i < tablero.getFilas(); i++) {
                for (int j = 0; j < tablero.getColumnas(); j++) {
                    Optional<Nave> naveOpt = casillas[i][j].getNave();
                    if (naveOpt.isPresent() && naveOpt.get().equals(nave)) {
                        // Crear y posicionar un globo para cada casilla de la nave
                        JLabel globoLabel = crearGloboLabel(nave);
                        posicionarGlobo(globoLabel, i, j, nave);
                        tableroLabel.add(globoLabel);
                    }
                }
            }
        }

        tableroLabel.revalidate();
        tableroLabel.repaint();
    }

    private static JLabel crearGloboLabel(Nave nave) {
        String color = "rojo";
        String tipo = nave.getTipoNave().toString().toLowerCase();
        int tamano = nave.getTamano();

        String imagePath = BALLOON_BASE_PATH + color + "/" + color + "-" + tamano + ".png";
        ImageIcon icon = new ImageIcon(TableroRenderer.class.getResource(imagePath));

        JLabel label = new JLabel(icon);
        label.setSize(icon.getIconWidth(), icon.getIconHeight());
        return label;
    }

    private static void posicionarGlobo(JLabel globoLabel, int fila, int columna, Nave nave) {
        int x = GRID_OFFSET_X + (columna * CELL_SIZE);
        int y = GRID_OFFSET_Y + (fila * CELL_SIZE);
        globoLabel.setLocation(x, y);
    }
}

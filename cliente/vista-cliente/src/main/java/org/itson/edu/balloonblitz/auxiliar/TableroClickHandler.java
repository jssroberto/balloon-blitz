package org.itson.edu.balloonblitz.auxiliar;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.itson.edu.balloonblitz.entidades.Coordenada;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;
import org.itson.edu.balloonblitz.partida.PartidaPanel;

public class TableroClickHandler {

    private static final int CELL_SIZE = 45;
    private static final int GRID_SIZE = 10;
    private static final int GRID_OFFSET_X = 0;
    private static final int GRID_OFFSET_Y = 0;

    public static void configurarTableroRival(JLabel tableroRival, Jugador jugador, PartidaPanel panel) {
        // Create an overlay panel for the grid
            JPanel gridPanel = new JPanel(null);
            gridPanel.setOpaque(false);
            gridPanel.setBounds(0, 0, CELL_SIZE * GRID_SIZE, CELL_SIZE * GRID_SIZE);

            // Create clickable cells
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    JPanel cell = createCell(i, j, jugador, panel);
                    gridPanel.add(cell);
                }
            }

            tableroRival.setLayout(null);
            tableroRival.add(gridPanel);
        
    }

    private static JPanel createCell(int row, int col, Jugador jugador, PartidaPanel panel) {
        JPanel cell = new JPanel();
        cell.setOpaque(false);
        cell.setBounds(
                GRID_OFFSET_X + (col * CELL_SIZE),
                GRID_OFFSET_Y + (row * CELL_SIZE),
                CELL_SIZE,
                CELL_SIZE
        );

        // Add hover effect
        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (cell.isEnabled()) {
                    cell.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 128), 2));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cell.setBorder(null);

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (cell.isEnabled()) {
                    cell.setEnabled(false);
                    realizarDisparo(row, col, jugador, panel);
                    System.out.println("permitido");
                } else {
                    System.out.println("no permitido");
                }
            }
        });

        return cell;

    }

    private static void realizarDisparo(int row, int col, Jugador jugador, PartidaPanel panel) {
        DisparoEvento disparo = new DisparoEvento(new Coordenada(row, col));
        disparo.setEmisor(jugador);
        panel.enviarEvento(disparo);
    }

}

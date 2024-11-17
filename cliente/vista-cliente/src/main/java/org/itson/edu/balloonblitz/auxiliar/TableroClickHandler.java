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
import org.itson.edu.balloonblitz.modelo.ClienteControlador;

public class TableroClickHandler {

    private static final int CELL_SIZE = 46;
    private static final int GRID_SIZE = 10;
    private static final int GRID_OFFSET_X = 0;
    private static final int GRID_OFFSET_Y = 0;
    private static final ClienteControlador controlador = ClienteControlador.getInstancia("localhost", 1234);

    public static void configurarTableroRival(JLabel tableroRival, Jugador jugador) {
        // Create an overlay panel for the grid
        JPanel gridPanel = new JPanel(null);
        gridPanel.setOpaque(false);
        gridPanel.setBounds(0, 0, CELL_SIZE * GRID_SIZE, CELL_SIZE * GRID_SIZE);

        // Create clickable cells
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                JPanel cell = createCell(i, j, jugador);
                gridPanel.add(cell);
            }
        }

        tableroRival.setLayout(null);
        tableroRival.add(gridPanel);
    }

    private static JPanel createCell(int row, int col, Jugador jugador) {
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
                cell.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 128), 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cell.setBorder(null);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                realizarDisparo(row, col, jugador);
            }
        });

        return cell;
    }

    private static void realizarDisparo(int row, int col, Jugador jugador) {
        DisparoEvento disparo = new DisparoEvento(new Coordenada(row, col));
        disparo.setEmisor(jugador);

        controlador.enviarMensaje(disparo);

        System.out.println("Disparo realizado en posicion: " + row + ", " + col);
    }
}

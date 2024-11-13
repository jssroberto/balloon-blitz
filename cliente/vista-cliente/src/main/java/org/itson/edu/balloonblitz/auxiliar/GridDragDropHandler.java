package org.itson.edu.balloonblitz.auxiliar;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import javax.swing.*;
import java.io.IOException;
import org.itson.edu.balloonblitz.entidades.*;
import org.itson.edu.balloonblitz.entidades.navefactory.Barco;

public class GridDragDropHandler extends DropTargetAdapter {

    private final JLabel tableroPanel;
    private final Tablero tablero;
    private final Casilla[][] matriz;

    private static final int GRID_SIZE = 10;
    private static final int CELL_SIZE = 46;
    private static final int OFFSET_X = 0;
    private static final int OFFSET_Y = 0;

    public GridDragDropHandler(JLabel tableroPanel) {
        this.tableroPanel = tableroPanel;
        this.tablero = new Tablero();
        this.matriz = tablero.getMatriz();
        new DropTarget(tableroPanel, DnDConstants.ACTION_COPY, this);
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            if (!dtde.getTransferable().isDataFlavorSupported(DataFlavor.imageFlavor)) {
                dtde.rejectDrop();
                return;
            }

            Point dropPoint = dtde.getLocation();
            int gridX = (dropPoint.x - OFFSET_X) / CELL_SIZE;
            int gridY = (dropPoint.y - OFFSET_Y) / CELL_SIZE;

            String balloonType = getBalloonType(dtde.getTransferable());
            int size = getBalloonSize(balloonType);

            if (!isValidPosition(gridX, gridY, size)) {
                dtde.rejectDrop();
                return;
            }

            dtde.acceptDrop(DnDConstants.ACTION_COPY);
            ImageIcon icon = new ImageIcon((Image) dtde.getTransferable().getTransferData(DataFlavor.imageFlavor));
            placeBalloons(gridX, gridY, size, icon);
            dtde.dropComplete(true);

        } catch (UnsupportedFlavorException | IOException e) {
            System.out.println("Error al colocar el globo: " + e.getMessage());
            dtde.dropComplete(false);
        }
    }

    private String getBalloonType(Transferable transferable) throws UnsupportedFlavorException, IOException {
        return transferable.isDataFlavorSupported(DataFlavor.stringFlavor)
                ? (String) transferable.getTransferData(DataFlavor.stringFlavor)
                : "single";
    }

    private int getBalloonSize(String balloonType) {
        return switch (balloonType) {
            case "quadruple" ->
                4;
            case "triple" ->
                3;
            case "double" ->
                2;
            default ->
                1;
        };
    }

    private void placeBalloons(int gridX, int gridY, int size, ImageIcon icon) {
        Nave nave = new Barco();

        for (int i = 0; i < size; i++) {
            int snapX = ((gridX + i) * CELL_SIZE) + OFFSET_X;
            int snapY = (gridY * CELL_SIZE) + OFFSET_Y;

            matriz[gridY][gridX + i].setNave(nave);
            tableroPanel.add(createBalloonLabel(icon, snapX, snapY));
        }

        tableroPanel.repaint();
    }

    private JLabel createBalloonLabel(ImageIcon icon, int x, int y) {
        JLabel balloon = new JLabel(new ImageIcon(icon.getImage()));
        balloon.setBounds(x, y, CELL_SIZE, CELL_SIZE);
        return balloon;
    }

    private boolean isValidPosition(int x, int y, int size) {
        if (x < 0 || y < 0 || y >= GRID_SIZE || (x + size - 1) >= GRID_SIZE) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (matriz[y][x + i].getNave() != null) {
                return false;
            }
        }
        return true;
    }

    public Tablero obtenerTablero() {
        return tablero;
    }
}

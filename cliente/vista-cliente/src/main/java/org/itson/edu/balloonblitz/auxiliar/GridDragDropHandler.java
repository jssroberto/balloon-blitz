package org.itson.edu.balloonblitz.auxiliar;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.io.IOException;
import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.navefactory.Barco;

public class GridDragDropHandler extends DropTargetAdapter {

    private final JLabel tableroPanel;
    private final int GRID_SIZE = 10;
    private final int CELL_SIZE = 46;
    private final int OFFSET_X = 0;
    private final int OFFSET_Y = 0;
    private final Casilla[][] matriz;
    private final Tablero tablero;

    public GridDragDropHandler(JLabel tableroPanel) {
        this.tableroPanel = tableroPanel;
        new DropTarget(tableroPanel, DnDConstants.ACTION_COPY, this);
        tablero = new Tablero();
        this.matriz = tablero.getMatriz();
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            if (!validarTransferible(dtde)) {
                return;
            }

            Transferable transferable = dtde.getTransferable();
            Point dropPoint = dtde.getLocation();
            GridPosition position = calcularPosicionGrilla(dropPoint);
            String balloonType = obtenerTipoBalloon(transferable);

            if (!isValidPosition(position.x, position.y, balloonType)) {
                rechazarDrop(dtde, "Posicion no valida para el tipo de globo.");
                return;
            }

            colocarBalloon(dtde, transferable, position, balloonType);
            dtde.dropComplete(true);

        } catch (UnsupportedFlavorException | IOException e) {
            System.out.println("Error al colocar el globo: " + e.getMessage());
            dtde.dropComplete(false);
        }
    }

    private boolean validarTransferible(DropTargetDropEvent dtde) {
        if (!dtde.getTransferable().isDataFlavorSupported(DataFlavor.imageFlavor)) {
            rechazarDrop(dtde, "Formato de imagen no soportado");
            return false;
        }
        return true;
    }

    private void rechazarDrop(DropTargetDropEvent dtde, String mensaje) {
        dtde.rejectDrop();
        System.out.println("Drop rechazado: " + mensaje);
    }

    private GridPosition calcularPosicionGrilla(Point dropPoint) {
        int gridX = (dropPoint.x - OFFSET_X) / CELL_SIZE;
        int gridY = (dropPoint.y - OFFSET_Y) / CELL_SIZE;
        return new GridPosition(gridX, gridY);
    }

    private String obtenerTipoBalloon(Transferable transferable) throws UnsupportedFlavorException, IOException {
        if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return (String) transferable.getTransferData(DataFlavor.stringFlavor);
        }
        return "single";
    }

    private void colocarBalloon(DropTargetDropEvent dtde, Transferable transferable,
            GridPosition position, String balloonType)
            throws UnsupportedFlavorException, IOException {
        dtde.acceptDrop(DnDConstants.ACTION_COPY);
        ImageIcon icon = new ImageIcon((Image) transferable.getTransferData(DataFlavor.imageFlavor));

        switch (balloonType) {
            case "quadruple" ->
                placeQuadBalloon(position.x, position.y, icon);
            case "triple" ->
                placeTripleBalloon(position.x, position.y, icon);
            case "double" ->
                placeDualBalloon(position.x, position.y, icon);
            default ->
                placeSingleBalloon(position.x, position.y, icon);
        }
    }

// Clase auxiliar para manejar posiciones
    private static class GridPosition {

        final int x;
        final int y;

        GridPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void placeSingleBalloon(int gridX, int gridY, ImageIcon icon) {
        int snapX = (gridX * CELL_SIZE) + OFFSET_X;
        int snapY = (gridY * CELL_SIZE) + OFFSET_Y;

        JLabel newBalloon = createBalloonLabel(icon, snapX, snapY, CELL_SIZE, CELL_SIZE);

        Nave nave = new Barco();
        matriz[gridY][gridX].setNave(nave);

        tableroPanel.add(newBalloon);
        tableroPanel.repaint();
    }

    private void placeDualBalloon(int gridX, int gridY, ImageIcon icon) {
        int snapX1 = (gridX * CELL_SIZE) + OFFSET_X;
        int snapX2 = ((gridX + 1) * CELL_SIZE) + OFFSET_X;
        int snapY = (gridY * CELL_SIZE) + OFFSET_Y;

        JLabel firstBalloon = createBalloonLabel(icon, snapX1, snapY, CELL_SIZE, CELL_SIZE);
        JLabel secondBalloon = createBalloonLabel(icon, snapX2, snapY, CELL_SIZE, CELL_SIZE);

        Nave nave = new Barco();
        matriz[gridY][gridX].setNave(nave);
        matriz[gridY][gridX + 1].setNave(nave);

        tableroPanel.add(firstBalloon);
        tableroPanel.add(secondBalloon);
        tableroPanel.repaint();
    }

    private void placeTripleBalloon(int gridX, int gridY, ImageIcon icon) {
        int snapX1 = (gridX * CELL_SIZE) + OFFSET_X;
        int snapX2 = ((gridX + 1) * CELL_SIZE) + OFFSET_X;
        int snapX3 = ((gridX + 2) * CELL_SIZE) + OFFSET_X;
        int snapY = (gridY * CELL_SIZE) + OFFSET_Y;

        JLabel firstBalloon = createBalloonLabel(icon, snapX1, snapY, CELL_SIZE, CELL_SIZE);
        JLabel secondBalloon = createBalloonLabel(icon, snapX2, snapY, CELL_SIZE, CELL_SIZE);
        JLabel thirdBallon = createBalloonLabel(icon, snapX3, snapY, CELL_SIZE, CELL_SIZE);

        Nave nave = new Barco();
        matriz[gridY][gridX].setNave(nave);
        matriz[gridY][gridX + 1].setNave(nave);
        matriz[gridY][gridX + 2].setNave(nave);

        tableroPanel.add(firstBalloon);
        tableroPanel.add(secondBalloon);
        tableroPanel.add(thirdBallon);
        tableroPanel.repaint();
    }

    private void placeQuadBalloon(int gridX, int gridY, ImageIcon icon) {
        int snapX1 = (gridX * CELL_SIZE) + OFFSET_X;
        int snapX2 = ((gridX + 1) * CELL_SIZE) + OFFSET_X;
        int snapX3 = ((gridX + 2) * CELL_SIZE) + OFFSET_X;
        int snapX4 = ((gridX + 3) * CELL_SIZE) + OFFSET_X;
        int snapY = (gridY * CELL_SIZE) + OFFSET_Y;

        JLabel firstBalloon = createBalloonLabel(icon, snapX1, snapY, CELL_SIZE, CELL_SIZE);
        JLabel secondBalloon = createBalloonLabel(icon, snapX2, snapY, CELL_SIZE, CELL_SIZE);
        JLabel thirdBallon = createBalloonLabel(icon, snapX3, snapY, CELL_SIZE, CELL_SIZE);
        JLabel quadBallon = createBalloonLabel(icon, snapX4, snapY, CELL_SIZE, CELL_SIZE);

        Nave nave = new Barco();
        matriz[gridY][gridX].setNave(nave);
        matriz[gridY][gridX + 1].setNave(nave);
        matriz[gridY][gridX + 2].setNave(nave);
        matriz[gridY][gridX + 3].setNave(nave);

        tableroPanel.add(firstBalloon);
        tableroPanel.add(secondBalloon);
        tableroPanel.add(thirdBallon);
        tableroPanel.add(quadBallon);
        tableroPanel.repaint();
    }

    private JLabel createBalloonLabel(ImageIcon icon, int x, int y, int width, int height) {
        JLabel balloon = new JLabel(new ImageIcon(icon.getImage())) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        balloon.setBounds(x, y, width, height);
        return balloon;
    }

    private boolean isValidPosition(int x, int y, String balloonType) {
        // Verificar que la posición inicial está dentro del tablero
        if (x < 0 || y < 0 || y >= GRID_SIZE) {
            return false;
        }

        // Determinar el tamaño necesario según el tipo de globo
        int tamanoRequerido = switch (balloonType) {
            case "quadruple" ->
                4;
            case "triple" ->
                3;
            case "double" ->
                2;
            default ->
                1;
        };

        // Verificar que hay espacio suficiente en el tablero
        if ((x + tamanoRequerido - 1) >= GRID_SIZE) {
            return false;
        }

        // Verificar que todas las casillas necesarias están libres
        for (int i = 0; i < tamanoRequerido; i++) {
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

package org.itson.edu.balloonblitz.auxiliar;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

    private boolean isVertical = false;
    private JLabel lastPlacedBalloon;
    private int lastPlacedX = -1;
    private int lastPlacedY = -1;
    private int lastPlacedSize = 0;

    // Variables para el preview durante el arrastre
    private JLabel[] previewLabels;
    private ImageIcon currentIcon;
    private int currentSize;
    private boolean isDragging = false;

    private Point lastDragPoint;

    public GridDragDropHandler(JLabel tableroPanel) {
        this.tableroPanel = tableroPanel;
        this.tablero = new Tablero();
        this.matriz = tablero.getMatriz();
        new DropTarget(tableroPanel, DnDConstants.ACTION_COPY, this);
        setupKeyListener();
        setupPreviewLabels();

        // Agregar key listener al tableroPanel
        tableroPanel.setFocusable(true);
        tableroPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isDragging && (e.getKeyChar() == 'r' || e.getKeyChar() == 'R')) {
                    isVertical = !isVertical;
                    updatePreview(lastDragPoint);
                }
            }
        });
    }

    private void setupPreviewLabels() {
        previewLabels = new JLabel[4]; // Máximo tamaño de barco es 4
        for (int i = 0; i < previewLabels.length; i++) {
            previewLabels[i] = new JLabel();
            previewLabels[i].setVisible(false);
            tableroPanel.add(previewLabels[i]);
        }
    }

    private void setupKeyListener() {
        // Mantener el KeyEventDispatcher como respaldo
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (isDragging && e.getID() == KeyEvent.KEY_PRESSED
                    && (e.getKeyChar() == 'r' || e.getKeyChar() == 'R')) {
                isVertical = !isVertical;
                updatePreview(lastDragPoint);
                return true;
            }
            return false;
        });
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        isDragging = true;
        tableroPanel.requestFocusInWindow(); // Solicitar el foco al iniciar el arrastre
        try {
            currentIcon = new ImageIcon((Image) dtde.getTransferable().getTransferData(DataFlavor.imageFlavor));
            String balloonType = getBalloonType(dtde.getTransferable());
            currentSize = getBalloonSize(balloonType);
        } catch (UnsupportedFlavorException | IOException e) {
        }
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        lastDragPoint = dtde.getLocation();
        updatePreview(lastDragPoint);
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        isDragging = false;
        hidePreview();
    }

    private void hidePreview() {
        for (JLabel label : previewLabels) {
            label.setVisible(false);
        }
    }

    public void updatePreview(Point point) {
        if (point == null) {
            return;
        }

        int gridX = (point.x - OFFSET_X) / CELL_SIZE;
        int gridY = (point.y - OFFSET_Y) / CELL_SIZE;

        if (isValidPosition(gridX, gridY, currentSize, isVertical)) {
            for (int i = 0; i < currentSize; i++) {
                previewLabels[i].setIcon(currentIcon);
                previewLabels[i].setVisible(true);

                if (isVertical) {
                    previewLabels[i].setBounds(
                            gridX * CELL_SIZE + OFFSET_X,
                            (gridY + i) * CELL_SIZE + OFFSET_Y,
                            CELL_SIZE,
                            CELL_SIZE
                    );
                } else {
                    previewLabels[i].setBounds(
                            (gridX + i) * CELL_SIZE + OFFSET_X,
                            gridY * CELL_SIZE + OFFSET_Y,
                            CELL_SIZE,
                            CELL_SIZE
                    );
                }
            }
            // Ocultar los labels no usados
            for (int i = currentSize; i < previewLabels.length; i++) {
                previewLabels[i].setVisible(false);
            }
        } else {
            hidePreview();
        }
        tableroPanel.repaint();
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

            if (!isValidPosition(gridX, gridY, currentSize, isVertical)) {
                dtde.rejectDrop();
                return;
            }

            dtde.acceptDrop(DnDConstants.ACTION_COPY);

            // Guardar información del último globo colocado
            lastPlacedX = gridX;
            lastPlacedY = gridY;
            lastPlacedSize = currentSize;

            placeBalloons(gridX, gridY, currentSize, currentIcon);
            dtde.dropComplete(true);
            hidePreview();
            isDragging = false;
            tableroPanel.requestFocusInWindow();
        } catch (Exception e) {
            System.out.println("Error al colocar el globo: " + e.getMessage());
            dtde.dropComplete(false);
        }
    }

    private void tryRotateLastPlacedBalloon() {
        if (lastPlacedBalloon != null && lastPlacedSize > 1) {
            boolean canRotate = isVertical
                    ? isValidPosition(lastPlacedX, lastPlacedY, lastPlacedSize, false)
                    : isValidPosition(lastPlacedX, lastPlacedY, lastPlacedSize, true);

            if (canRotate) {
                clearCurrentPosition();
                isVertical = !isVertical;
                placeBalloons(lastPlacedX, lastPlacedY, lastPlacedSize, (ImageIcon) lastPlacedBalloon.getIcon());
            }
        }
    }

    // Los métodos restantes permanecen igual...
    private void clearCurrentPosition() {
        if (isVertical) {
            for (int i = 0; i < lastPlacedSize; i++) {
                matriz[lastPlacedY + i][lastPlacedX].setNave(null);
            }
        } else {
            for (int i = 0; i < lastPlacedSize; i++) {
                matriz[lastPlacedY][lastPlacedX + i].setNave(null);
            }
        }
        for (Component comp : tableroPanel.getComponents()) {
            if (comp instanceof JLabel && ((JLabel) comp).getIcon() != null) {
                tableroPanel.remove(comp);
            }
        }
        tableroPanel.repaint();
    }

    private boolean isValidPosition(int x, int y, int size, boolean vertical) {
        if (vertical) {
            if (x < 0 || y < 0 || x >= GRID_SIZE || (y + size - 1) >= GRID_SIZE) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (matriz[y + i][x].getNave() != null) {
                    return false;
                }
            }
        } else {
            if (x < 0 || y < 0 || y >= GRID_SIZE || (x + size - 1) >= GRID_SIZE) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (matriz[y][x + i].getNave() != null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void placeBalloons(int gridX, int gridY, int size, ImageIcon icon) {
        Nave nave = new Barco();

        if (isVertical) {
            for (int i = 0; i < size; i++) {
                int snapX = gridX * CELL_SIZE + OFFSET_X;
                int snapY = ((gridY + i) * CELL_SIZE) + OFFSET_Y;
                matriz[gridY + i][gridX].setNave(nave);
                JLabel balloon = createBalloonLabel(icon, snapX, snapY);
                tableroPanel.add(balloon);
                if (i == 0) {
                    lastPlacedBalloon = balloon;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                int snapX = ((gridX + i) * CELL_SIZE) + OFFSET_X;
                int snapY = gridY * CELL_SIZE + OFFSET_Y;
                matriz[gridY][gridX + i].setNave(nave);
                JLabel balloon = createBalloonLabel(icon, snapX, snapY);
                tableroPanel.add(balloon);
                if (i == 0) {
                    lastPlacedBalloon = balloon;
                }
            }
        }

        tableroPanel.repaint();
    }

    private JLabel createBalloonLabel(ImageIcon icon, int x, int y) {
        JLabel balloon = new JLabel(new ImageIcon(icon.getImage()));
        balloon.setBounds(x, y, CELL_SIZE, CELL_SIZE);
        return balloon;
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

    public Tablero obtenerTablero() {
        return tablero;
    }
}

package org.itson.edu.balloonblitz.auxiliar;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import org.itson.edu.balloonblitz.entidades.*;
import org.itson.edu.balloonblitz.entidades.navefactory.Barco;
import org.itson.edu.balloonblitz.entidades.navefactory.Crucero;
import org.itson.edu.balloonblitz.entidades.navefactory.PortaAviones;
import org.itson.edu.balloonblitz.entidades.navefactory.Submarino;

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

    private final List<Nave> naves;

    private Point lastDragPoint;

    public GridDragDropHandler(JLabel tableroPanel) {
        this.tableroPanel = tableroPanel;
        this.tablero = new Tablero();
        naves = new ArrayList<>();
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
        try {
            isDragging = true;
            tableroPanel.requestFocusInWindow();
            currentIcon = new ImageIcon((Image) dtde.getTransferable().getTransferData(DataFlavor.imageFlavor));

            if (dtde.getTransferable().isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String balloonType = (String) dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);
                currentSize = getBalloonSize(balloonType);
            }
        } catch (UnsupportedFlavorException | IOException ex) {
            Logger.getLogger(GridDragDropHandler.class.getName()).log(Level.SEVERE, null, ex);
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

            hidePreview();
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

        // Verificar si el punto está completamente fuera del tablero
        if (gridX < 0 || gridY < 0 || gridX >= GRID_SIZE || gridY >= GRID_SIZE) {
            hidePreview();
            return;
        }

        // Verificar si alguna parte del barco estaría fuera del tablero
        boolean outOfBounds = isVertical
                ? (gridY + currentSize > GRID_SIZE)
                : (gridX + currentSize > GRID_SIZE);

        if (outOfBounds) {
            hidePreview();
            return;
        }

        boolean isValid = isValidPosition(gridX, gridY, currentSize, isVertical);
        Color backgroundColor = isValid ? Color.WHITE : Color.RED;

        for (int i = 0; i < currentSize; i++) {
            previewLabels[i].setIcon(currentIcon);
            previewLabels[i].setVisible(true);
            previewLabels[i].setBackground(backgroundColor);
            previewLabels[i].setOpaque(true);

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

            // Verificar si está fuera del tablero
            if (gridX < 0 || gridY < 0 || gridX >= GRID_SIZE || gridY >= GRID_SIZE) {
                dtde.rejectDrop();
                return;
            }

            // Verificar si alguna parte del barco quedaría fuera
            boolean outOfBounds = isVertical
                    ? (gridY + currentSize > GRID_SIZE)
                    : (gridX + currentSize > GRID_SIZE);

            if (outOfBounds || !isValidPosition(gridX, gridY, currentSize, isVertical)) {
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
        Nave nave;

        switch (size) {
            case 4 ->
                nave = new PortaAviones();
            case 3 ->
                nave = new Crucero();
            case 2 ->
                nave = new Submarino();
            default ->
                nave = new Barco();
        }

        // Agregar la nave a la lista de naves
        naves.add(nave);

        System.out.println("\nNave de tipo " + nave.getTipoNave().toString().toLowerCase() + " colocada en las casillas:");
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
                System.out.printf("Casilla [%d, %d]%n", gridY + i, gridX);
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
                System.out.printf("Casilla [%d, %d]%n", gridY, gridX + i);
            }
        }

        tableroPanel.repaint();
    }

    private JLabel createBalloonLabel(ImageIcon icon, int x, int y) {
        JLabel balloon = new JLabel(new ImageIcon(icon.getImage()));
        balloon.setBounds(x, y, CELL_SIZE, CELL_SIZE);
        return balloon;
    }

    private int getBalloonSize(String balloonType) {
        return switch (balloonType) {
            case "barco" ->
                1;
            case "submarino" ->
                2;
            case "crucero" ->
                3;
            case "portaAviones" ->
                4;
            default ->
                throw new IllegalArgumentException("Tipo de globo no válido: " + balloonType);
        };
    }

    public Tablero obtenerTablero() {
        return tablero;
    }

    public List<Nave> obtenerNaves() {
        return naves;
    }
}

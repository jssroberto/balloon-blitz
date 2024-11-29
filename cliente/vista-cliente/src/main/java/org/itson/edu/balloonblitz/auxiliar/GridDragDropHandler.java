package org.itson.edu.balloonblitz.auxiliar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.navefactory.Barco;
import org.itson.edu.balloonblitz.entidades.navefactory.Crucero;
import org.itson.edu.balloonblitz.entidades.navefactory.PortaAviones;
import org.itson.edu.balloonblitz.entidades.navefactory.Submarino;

public class GridDragDropHandler extends DropTargetAdapter {

    private final JLabel tableroPanel;
    private final Tablero tablero;
    private final Casilla[][] matriz;
    private static final int GRID_SIZE = 10;
    private static final int CELL_SIZE = 45;
    private static final int OFFSET_X = 0;
    private static final int OFFSET_Y = 0;
    private Nave[][] matriz1 = new Nave[GRID_SIZE][GRID_SIZE];
    private String color;// Representación de las celdas del tablero

    private boolean isVertical = false;

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

        // Verificar si el punto está fuera del tablero
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

        // Mostrar el preview con el color correspondiente
        for (int i = 0; i < currentSize; i++) {
            previewLabels[i].setIcon(currentIcon);
            previewLabels[i].setVisible(true);
            previewLabels[i].setBackground(isValid ? Color.WHITE : Color.RED);
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

            // Verificar si alguna parte del barco estaría fuera o si la posición no es válida
            boolean outOfBounds = isVertical
                    ? (gridY + currentSize > GRID_SIZE)
                    : (gridX + currentSize > GRID_SIZE);

            if (outOfBounds || !isValidPosition(gridX, gridY, currentSize, isVertical)) {
                dtde.rejectDrop();
                return;
            }

            dtde.acceptDrop(DnDConstants.ACTION_COPY);

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
        try {
            // Verificar el área extendida alrededor del barco (incluyendo diagonales)
            int startX = Math.max(0, x - 1);
            int startY = Math.max(0, y - 1);
            int endX = vertical ? Math.min(GRID_SIZE - 1, x + 1) : Math.min(GRID_SIZE - 1, x + size);
            int endY = vertical ? Math.min(GRID_SIZE - 1, y + size) : Math.min(GRID_SIZE - 1, y + 1);

            // Verificar todas las casillas alrededor
            for (int i = startY; i <= endY; i++) {
                for (int j = startX; j <= endX; j++) {
                    if (matriz[i][j].getNave().isPresent()) {
                        return false;
                    }
                }
            }

            // Verificar límites del tablero
            if (vertical) {
                if (y + size > GRID_SIZE) {
                    return false;
                }
            } else {
                if (x + size > GRID_SIZE) {
                    return false;
                }
            }

            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }


    private void placeBalloons(int gridX, int gridY, int size, ImageIcon icon) {
        Nave nave;

        switch (size) {
            case 4 -> nave = new PortaAviones();
            case 3 -> nave = new Crucero();
            case 2 -> nave = new Submarino();
            default -> nave = new Barco();
        }

        naves.add(nave);

        System.out.println("\nNave de tipo " + nave.getTipoNave().toString().toLowerCase() + " colocada en las casillas:");
        if (isVertical) {
            for (int i = 0; i < size; i++) {
                int snapX = gridX * CELL_SIZE + OFFSET_X;
                int snapY = ((gridY + i) * CELL_SIZE) + OFFSET_Y;
                matriz[gridY + i][gridX].setNave(nave);
                JLabel balloon = createBalloonLabel(icon, snapX, snapY);
                tableroPanel.add(balloon);
                System.out.printf("Casilla [%d, %d]%n", gridY + i, gridX);
            }
        } else {
            for (int i = 0; i < size; i++) {
                int snapX = ((gridX + i) * CELL_SIZE) + OFFSET_X;
                int snapY = gridY * CELL_SIZE + OFFSET_Y;
                matriz[gridY][gridX + i].setNave(nave);
                JLabel balloon = createBalloonLabel(icon, snapX, snapY);
                tableroPanel.add(balloon);
                System.out.printf("Casilla [%d, %d]%n", gridY, gridX + i);
            }
        }

        tableroPanel.repaint();
    }

    private void placeBalloonsDefecto(int gridX, int gridY, int size, ImageIcon icon) {
        Nave nave;

        switch (size) {
            case 4 -> nave = new PortaAviones();
            case 3 -> nave = new Crucero();
            case 2 -> nave = new Submarino();
            default -> nave = new Barco();
        }

        naves.add(nave);

        System.out.println("\nNave de tipo " + nave.getTipoNave().toString().toLowerCase() + " colocada en las casillas:");
        if (isVertical) {
            for (int i = 0; i < size; i++) {
                int snapX = gridX * CELL_SIZE + OFFSET_X;
                int snapY = ((gridY + i) * CELL_SIZE) + OFFSET_Y;
                matriz[gridX + i][gridY].setNave(nave);
                JLabel balloon = createBalloonLabel(icon, snapX, snapY);
                tableroPanel.add(balloon);
                System.out.printf("Casilla [%d, %d]%n",gridX + i, gridY);
            }
        } else {
            for (int i = 0; i < size; i++) {
                int snapX = ((gridX + i) * CELL_SIZE) + OFFSET_X;
                int snapY = gridY * CELL_SIZE + OFFSET_Y;
                matriz[gridX][gridY + i].setNave(nave);
                JLabel balloon = createBalloonLabel(icon, snapX, snapY);
                tableroPanel.add(balloon);
                System.out.printf("Casilla [%d, %d]%n", gridX, gridY + i);
            }
        }

        tableroPanel.repaint();
    }

    private JLabel createBalloonLabel(ImageIcon icon, int x, int y) {
        JLabel balloon = new JLabel(new ImageIcon(icon.getImage()));
        balloon.setBounds(x, y, CELL_SIZE, CELL_SIZE);
        return balloon;
    }

    //    private void placeBalloonsRandom(int gridX, int gridY, int size, ImageIcon icon) {
    //        Nave nave;
    //
    //        switch (size) {
    //            case 4 ->
    //                nave = new PortaAviones();
    //            case 3 ->
    //                nave = new Crucero();
    //            case 2 ->
    //                nave = new Submarino();
    //            default ->
    //                nave = new Barco();
    //        }
    //
    //        naves.add(nave);
    //
    //        System.out.println("\nNave de tipo " + nave.getTipoNave().toString().toLowerCase() + " colocada en las casillas:");
    //        if (isVertical) {
    //            for (int i = 0; i < size; i++) {
    //                int snapX = gridX * CELL_SIZE + OFFSET_X;
    //                int snapY = ((gridY + i) * CELL_SIZE) + OFFSET_Y;
    //                matriz[gridY + i][gridX].setNave(nave);
    //                JLabel balloon = createBalloonLabel(icon, snapX, snapY);
    //                tableroPanel.add(balloon);
    //                System.out.printf("Casilla [%d, %d]%n", gridY + i, gridX);
    //            }
    //        } else {
    //            for (int i = 0; i < size; i++) {
    //                int snapX = ((gridX + i) * CELL_SIZE) + OFFSET_X;
    //                int snapY = gridY * CELL_SIZE + OFFSET_Y;
    //                matriz[gridY][gridX + i].setNave(nave);
    //                JLabel balloon = createBalloonLabel(icon, snapX, snapY);
    //                tableroPanel.add(balloon);
    //                System.out.printf("Casilla [%d, %d]%n", gridY, gridX + i);
    //            }
    //        }
    //        tableroPanel.repaint();
    //    }


    private int getBalloonSize(String balloonType) {
        return switch (balloonType) {
            case "barco" -> 1;
            case "submarino" -> 2;
            case "crucero" -> 3;
            case "portaAviones" -> 4;
            default -> throw new IllegalArgumentException("Tipo de globo no válido: " + balloonType);
        };
    }

    public Tablero obtenerTablero() {
        return tablero;
    }

    public List<Nave> obtenerNaves() {
        return naves;
    }

    public void colorGlobos(String color) {
        this.color = color;
    }

    public ImageIcon cargarIcono(String tipo) {
        String rutaImagen = "";

        switch (tipo) {
            case "portaAviones" ->
                    rutaImagen = "/images/ballons/" + color.toLowerCase() + "/" + color.toLowerCase() + "-4.png";
            case "crucero" ->
                    rutaImagen = "/images/ballons/" + color.toLowerCase() + "/" + color.toLowerCase() + "-3.png";
            case "submarino" ->
                    rutaImagen = "/images/ballons/" + color.toLowerCase() + "/" + color.toLowerCase() + "-2.png";
            case "barco" ->
                    rutaImagen = "/images/ballons/" + color.toLowerCase() + "/" + color.toLowerCase() + "-1.png";
            default -> throw new IllegalArgumentException("Tipo de globo no válido: " + tipo);
        }

        // Intentar cargar la imagen desde el recurso
        URL imageUrl = getClass().getResource(rutaImagen);
        if (imageUrl == null) {
            System.err.println("Error: no se pudo encontrar la imagen en la ruta: " + rutaImagen);
            return null; // Retornar null si la imagen no se encuentra
        }

        return new ImageIcon(imageUrl);
    }

    public void posicionarGlobosPorDefecto() {
            isVertical = false;
            placeBalloonsDefecto(8, 1, 4, cargarIcono("portaAviones"));
            placeBalloonsDefecto(1, 7, 3, cargarIcono("crucero"));
            placeBalloonsDefecto(2, 1, 2, cargarIcono("submarino"));
            placeBalloonsDefecto(6, 0, 2, cargarIcono("submarino"));
            placeBalloonsDefecto(9, 7, 1, cargarIcono("submarino"));

            isVertical = true;
            placeBalloonsDefecto(4, 8, 4, cargarIcono("portaAviones"));
            placeBalloonsDefecto(1, 4, 3, cargarIcono("crucero"));
            placeBalloonsDefecto(4, 6, 2, cargarIcono("submarino"));
            placeBalloonsDefecto(0, 0, 1, cargarIcono("barco"));
            placeBalloonsDefecto(4, 2, 1, cargarIcono("barco"));
            placeBalloonsDefecto(6, 4, 1, cargarIcono("barco"));

        //        // Recorrer cada tipo de globo y sus posiciones específicas
        //        for (int i = 0; i < tiposGlobos.length; i++) {
        //            String tipo = tiposGlobos[i];             // Obtener el tipo de globo
        //            int[][] posiciones = posicionesGlobos[i]; // Obtener las posiciones de este globo
        //
        //            // Cargar el icono correspondiente al tipo de globo
        //            ImageIcon icon = cargarIcono(tipo);
        //
        //            // Verificar si el icono se ha cargado correctamente
        //            if (icon == null) {
        //                System.err.println("Error al cargar el icono para el tipo: " + tipo);
        //                continue; // Si no se puede cargar el icono, se salta al siguiente tipo
        //            }
        //
        //            // Colocar el globo en las casillas correspondientes
        //            for (int[] casilla : posiciones) {
        //                int gridX = casilla[0];
        //                int gridY = casilla[1];
        //
        //                // Colocar el globo en la posición específica
        //                placeBalloons(gridX, gridY, getBalloonSize(tipo), icon); // Aquí '1' es el tamaño, ajustable según el tipo de nave
        //            }
        //        }
        //
        //        // Actualizar el tablero para reflejar las posiciones de los globos
                tableroPanel.repaint();
        //
                deshabilitarColocacionGlobos();
    }

    public void deshabilitarColocacionGlobos() {
        // Establecer el máximo de globos colocados para cada tipo
        BalloonTransferHandler.setMaxPlacedBalloons("barco", 3);
        BalloonTransferHandler.setMaxPlacedBalloons("submarino", 4);
        BalloonTransferHandler.setMaxPlacedBalloons("crucero", 2);
        BalloonTransferHandler.setMaxPlacedBalloons("portaAviones", 2);
    }

    public void limpiarTablero() {
        // Limpiar la matriz lógica de casillas
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                matriz[i][j].setNave(null);
            }
        }

        // Limpiar la lista de naves
        naves.clear();

        // Eliminar todos los globos (componentes) del tablero visual
        for (Component component : tableroPanel.getComponents()) {
            if (component instanceof JLabel) {
                tableroPanel.remove(component);
            }
        }

        // Actualizar el panel para reflejar los cambios
        tableroPanel.repaint();
        
        // Esto resetea el contador de globos
        BalloonTransferHandler.resetPlacedBalloons();
        
        // Y se tiene que habilidar la funcionalidad del preview de los labels
        setupPreviewLabels();
        new DropTarget(tableroPanel, DnDConstants.ACTION_COPY, this);
    }

}

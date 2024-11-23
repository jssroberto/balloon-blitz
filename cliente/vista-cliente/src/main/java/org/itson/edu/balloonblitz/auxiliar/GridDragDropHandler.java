package org.itson.edu.balloonblitz.auxiliar;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
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
    private static final int CELL_SIZE = 45;
    private static final int OFFSET_X = 0;
    private static final int OFFSET_Y = 0;
    private Nave[][] matriz1 = new Nave[GRID_SIZE][GRID_SIZE];  // Representación de las celdas del tablero

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
            case 4 ->
                nave = new PortaAviones();
            case 3 ->
                nave = new Crucero();
            case 2 ->
                nave = new Submarino();
            default ->
                nave = new Barco();
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

    private ImageIcon cargarIcono(String tipo) {
        String rutaImagen = "";

        switch (tipo) {
            case "portaAviones" ->
                rutaImagen = "/images/ballons/rojo/rojo-4.png"; // Ruta a la imagen del portaaviones
            case "crucero" ->
                rutaImagen = "/images/ballons/rojo/rojo-3.png"; // Ruta a la imagen del crucero
            case "submarino" ->
                rutaImagen = "/images/ballons/rojo/rojo-2.png"; // Ruta a la imagen del submarino
            case "barco" ->
                rutaImagen = "/images/ballons/rojo/rojo-1.png"; // Ruta a la imagen del barco
            default ->
                throw new IllegalArgumentException("Tipo de globo no válido: " + tipo);
        }

        // Intentar cargar la imagen desde el recurso
        URL imageUrl = getClass().getResource(rutaImagen);
        if (imageUrl == null) {
            System.err.println("Error: no se pudo encontrar la imagen en la ruta: " + rutaImagen);
            return null; // Retornar null si la imagen no se encuentra
        }

        return new ImageIcon(imageUrl);
    }

    public void posicionarGlobosExactamente() {
        // Definir las posiciones específicas de cada tipo de nave
        int[][] posicionesPortaAviones1 = {{3, 9}, {4, 9}, {5, 9}, {6, 9}};
        int[][] posicionesPortaAviones2 = {{5, 0}, {6, 0}, {7, 0}, {8, 0}};
        int[][] posicionesCrucero1 = {{4, 3}, {4, 4}, {4, 5}};
        int[][] posicionesCrucero2 = {{8, 4}, {8, 5}, {8, 6}};
        int[][] posicionesSubmarino1 = {{1, 0}, {1, 1}};
        int[][] posicionesSubmarino2 = {{1, 7}, {2, 7}};
        int[][] posicionesBarco1 = {{2, 3}};
        int[][] posicionesBarco2 = {{0, 4}};
        int[][] posicionesBarco3 = {{6, 2}};
        int[][] posicionesBarco4 = {{3, 0}};
        int[][] posicionesSubmarino3 = {{5, 7}, {6, 7}};

        // Crear un arreglo de tipos de globos en el orden específico
        String[] tiposGlobos = {
            "portaAviones", "portaAviones", "crucero", "crucero",
            "submarino", "submarino", "barco", "barco", "barco",
            "barco", "submarino"
        };

        // Arreglo de las posiciones para cada tipo de globo
        int[][][] posicionesGlobos = {
            posicionesPortaAviones1, // Portaaviones 1
            posicionesPortaAviones2, // Portaaviones 2
            posicionesCrucero1, // Crucero 1
            posicionesCrucero2, // Crucero 2
            posicionesSubmarino1, // Submarino 1
            posicionesSubmarino2, // Submarino 2
            posicionesBarco1, // Barco 1
            posicionesBarco2, // Barco 2
            posicionesBarco3, // Barco 3
            posicionesBarco4, // Barco 4
            posicionesSubmarino3 // Submarino 3
        };

        // Recorrer cada tipo de globo y sus posiciones específicas
        for (int i = 0; i < tiposGlobos.length; i++) {
            String tipo = tiposGlobos[i];             // Obtener el tipo de globo
            int[][] posiciones = posicionesGlobos[i]; // Obtener las posiciones de este globo

            // Cargar el icono correspondiente al tipo de globo
            ImageIcon icon = cargarIcono(tipo);

            // Verificar si el icono se ha cargado correctamente
            if (icon == null) {
                System.err.println("Error al cargar el icono para el tipo: " + tipo);
                continue; // Si no se puede cargar el icono, se salta al siguiente tipo
            }

            // Colocar el globo en las casillas correspondientes
            for (int[] casilla : posiciones) {
                int gridX = casilla[0];
                int gridY = casilla[1];

                // Colocar el globo en la posición específica
                placeBalloons(gridX, gridY, 1, icon); // Aquí '1' es el tamaño, ajustable según el tipo de nave
            }
        }

        // Actualizar el tablero para reflejar las posiciones de los globos
        tableroPanel.repaint();

        deshabilitarColocacionGlobos();
    }

    public void deshabilitarColocacionGlobos() {
        // Establecer el máximo de globos colocados para cada tipo
        BalloonTransferHandler.setMaxPlacedBalloons("barco", 3);
        BalloonTransferHandler.setMaxPlacedBalloons("submarino", 4);
        BalloonTransferHandler.setMaxPlacedBalloons("crucero", 2);
        BalloonTransferHandler.setMaxPlacedBalloons("portaAviones", 2);
    }

}

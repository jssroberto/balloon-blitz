package org.itson.edu.balloonblitz.auxiliar;

import java.awt.Point;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.Nave;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.navefactory.Barco;

public class GridDragDropHandler extends DropTargetAdapter {

    private final JLabel tableroPanel;
    private final int GRID_SIZE = 10;
    private final int CELL_SIZE = 46; // Ajusta este valor según el tamaño de tus celdas
    private final int OFFSET_X = 0;   // Ajusta estos offsets según la posición de tu tablero
    private final int OFFSET_Y = 0;
    private final Casilla[][] matriz;
    private final Tablero tablero;
//    private final int[][] tablero = new int[GRID_SIZE][GRID_SIZE];

    public GridDragDropHandler(JLabel tableroPanel) {
        this.tableroPanel = tableroPanel;
        DropTarget dropTarget = new DropTarget(tableroPanel, DnDConstants.ACTION_COPY, this);

        tablero = new Tablero();
        this.matriz = tablero.getMatriz();
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            Transferable transferable = dtde.getTransferable();
            if (!transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                dtde.rejectDrop();
                System.out.println("Drop rechazado: Formato de imagen no soportado");
                return;
            }

            // Obtener la posición del drop
            Point dropPoint = dtde.getLocation();
            // Convertir la posición a coordenadas de la cuadrícula
            int gridX = (dropPoint.x - OFFSET_X) / CELL_SIZE;
            int gridY = (dropPoint.y - OFFSET_Y) / CELL_SIZE;

            System.out.println("Intento de colocar globo en posicion: [" + gridX + ", " + gridY + "]");

            // Verificar si las coordenadas están dentro del tablero
            if (!isValidPosition(gridX, gridY)) {
                dtde.rejectDrop();
                System.out.println("Drop rechazado: No se puede poner un globo encima de otro.");
                return;
            }

            // Si llegamos aquí, aceptamos el drop
            dtde.acceptDrop(DnDConstants.ACTION_COPY);

            // Obtener la imagen
            Image image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
            ImageIcon icon = new ImageIcon(image);

            // Calcular la posición exacta para la celda
            int snapX = (gridX * CELL_SIZE) + OFFSET_X;
            int snapY = (gridY * CELL_SIZE) + OFFSET_Y;

            // Crear nuevo JLabel para el globo
            JLabel newBalloon = new JLabel(icon);
            newBalloon.setBounds(snapX, snapY, CELL_SIZE, CELL_SIZE);

            // Agregar funcionalidad de rotación con clic derecho
            newBalloon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        rotateBalloon(newBalloon);
                        System.out.println("Globo rotado en posicion: [" + gridX + ", " + gridY + "]");
                    }
                }
            });

            Nave nave = new Barco();
            matriz[gridY][gridX].setNave(nave);

            // Marcar la posición como ocupada en el tablero
//            tablero[gridY][gridX] = 1;
            tableroPanel.add(newBalloon);
            tableroPanel.repaint();

            System.out.println("Globo colocado exitosamente en posicion: [" + gridX + ", " + gridY + "]");
            System.out.println("Estado actual del tablero en esta posicion: " + matriz[gridY][gridX].getNave().getTipoNave().name());

            dtde.dropComplete(true);
        } catch (Exception e) {
            System.out.println("Error al colocar el globo: " + e.getMessage());
            dtde.dropComplete(false);
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
//        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE && tablero[y][x] == 0;
    }

    private void rotateBalloon(JLabel balloon) {
        // Aquí puedes implementar la lógica de rotación
        // Por ejemplo:
        ImageIcon icon = (ImageIcon) balloon.getIcon();
        Image img = icon.getImage();
        // Implementar la rotación de la imagen
    }
}

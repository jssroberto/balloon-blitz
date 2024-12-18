package org.itson.edu.balloonblitz.auxiliar;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

public class BalloonTransferHandler extends TransferHandler {

    private final JLabel sourceLabel;
    private final String balloonType;

    // Mapa estático para llevar el conteo de globos colocados
    private static final Map<String, Integer> placedBalloons = new HashMap<>();

    // Mapa con los límites para cada tipo de globo
    public static final Map<String, Integer> BALLOON_LIMITS = Map.of(
            "barco", 3, // 4 barcos de 1 casilla
            "submarino", 4, // 3 submarinos de 2 casillas
            "crucero", 2, // 2 cruceros de 3 casillas
            "portaAviones", 2 // 1 portaaviones de 4 casillas
    );

    public BalloonTransferHandler(JLabel label, String balloonType) {
        this.sourceLabel = label;
        this.balloonType = balloonType;
        // Inicializar el contador para este tipo de globo si no existe
        placedBalloons.putIfAbsent(balloonType, 0);
    }

    @Override
    public int getSourceActions(JComponent c) {
        // Solo permitir el arrastre si no se ha alcanzado el límite
        return canPlaceMoreBalloons() ? TransferHandler.COPY : TransferHandler.NONE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        // Solo crear el Transferable si no se ha alcanzado el límite
        if (!canPlaceMoreBalloons()) {
            return null;
        }

        return new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor, DataFlavor.stringFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return flavor.equals(DataFlavor.imageFlavor)
                        || flavor.equals(DataFlavor.stringFlavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                if (flavor.equals(DataFlavor.imageFlavor)) {
                    return ((ImageIcon) sourceLabel.getIcon()).getImage();
                } else if (flavor.equals(DataFlavor.stringFlavor)) {
                    return balloonType;
                }
                throw new UnsupportedFlavorException(flavor);
            }
        };
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        // Incrementar el contador solo si la transferencia fue exitosa
        if (action == TransferHandler.COPY) {
            incrementPlacedBalloons();
            updateSourceLabelAppearance();
        }
    }

    private boolean canPlaceMoreBalloons() {
        int currentCount = placedBalloons.getOrDefault(balloonType, 0);
        int limit = BALLOON_LIMITS.getOrDefault(balloonType, 0);
        return currentCount < limit;
    }

    private void incrementPlacedBalloons() {
        int currentCount = placedBalloons.getOrDefault(balloonType, 0);
        placedBalloons.put(balloonType, currentCount + 1);
    }

    private void updateSourceLabelAppearance() {
        if (!canPlaceMoreBalloons()) {
            // Deshabilitar visualmente el label cuando se alcanza el límite
            sourceLabel.setEnabled(false);
            // Agregar un texto indicando que se alcanzó el límite
            int limit = BALLOON_LIMITS.get(balloonType);
            sourceLabel.setToolTipText("Límite alcanzado (" + limit + "/" + limit + ")");
        } else {
            // Mostrar el contador actual
            int current = placedBalloons.get(balloonType);
            int limit = BALLOON_LIMITS.get(balloonType);
            sourceLabel.setToolTipText("Colocados: " + current + "/" + limit);
        }
    }

    // Método para resetear los contadores
    public static void resetPlacedBalloons() {
        placedBalloons.clear();
        for (java.awt.Window window : java.awt.Window.getWindows()) {
            updateLabelsInContainer(window, null);
        }
    }

    // Método para obtener el número de globos colocados de un tipo
    public static int getPlacedBalloonCount(String type) {
        return placedBalloons.getOrDefault(type, 0);
    }

    public static void setMaxPlacedBalloons(String type, int count) {
        placedBalloons.put(type, count);

        // Buscar todos los BalloonTransferHandlers activos para este tipo y actualizar su apariencia
        for (java.awt.Window window : java.awt.Window.getWindows()) {
            updateLabelsInContainer(window, type);
        }
    }

    // Método auxiliar para buscar y actualizar los labels
    private static void updateLabelsInContainer(java.awt.Container container, String type) {
        for (java.awt.Component comp : container.getComponents()) {
            if (comp instanceof JLabel label) {
                TransferHandler handler = label.getTransferHandler();
                if (handler instanceof BalloonTransferHandler balloonHandler) {
                    if (type == null || type.equals(balloonHandler.balloonType)) {
                        label.setEnabled(true);
                        label.setToolTipText(null);
                    }
                }
            }
            
            if (comp instanceof java.awt.Container) {
                updateLabelsInContainer((java.awt.Container) comp, type);
            }
        }
    }
}

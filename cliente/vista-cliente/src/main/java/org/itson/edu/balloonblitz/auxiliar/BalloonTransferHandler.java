/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.auxiliar;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

/**
 *
 * @author user
 */
public class BalloonTransferHandler extends TransferHandler {

    private final JLabel sourceLabel;
    private final String balloonType;

    public BalloonTransferHandler(JLabel label, String balloonType) {
        this.sourceLabel = label;
        this.balloonType = balloonType;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
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
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.auxiliar;

import java.awt.datatransfer.Transferable;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY;

/**
 *
 * @author user
 */
public class BalloonTransferHandler extends TransferHandler {

    private final JLabel sourceLabel;

    public BalloonTransferHandler(JLabel sourceLabel) {
        this.sourceLabel = sourceLabel;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        if (sourceLabel.getIcon() != null) {
            return new ImageTransferable(((ImageIcon) sourceLabel.getIcon()).getImage());
        }
        return null;
    }
}

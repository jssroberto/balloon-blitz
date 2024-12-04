/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package org.itson.edu.balloonblitz.main.cliente;

import org.itson.edu.balloonblitz.FramePrincipal;
import org.itson.edu.balloonblitz.vista.InicioPanel;

/**
 *
 * @author rover
 */
public class MainCliente {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FramePrincipal framePrincipal = new FramePrincipal();
            framePrincipal.cambiarPanel(new InicioPanel(framePrincipal));
            framePrincipal.setVisible(true);
        });
    }
}

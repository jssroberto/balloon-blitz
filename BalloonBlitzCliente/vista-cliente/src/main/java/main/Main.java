/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import org.itson.edu.balloonblitz.vista.FramePrincipal;
import org.itson.edu.balloonblitz.vista.InicioPanel;

/**
 *
 * @author user
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FramePrincipal frameContenedor = new FramePrincipal();
        frameContenedor.cambiarPanel(new InicioPanel(frameContenedor));    
        frameContenedor.setVisible(true);
    }

}

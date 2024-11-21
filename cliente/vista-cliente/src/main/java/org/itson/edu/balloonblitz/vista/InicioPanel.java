/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.itson.edu.balloonblitz.vista;


import org.itson.edu.balloonblitz.FramePrincipal;
import org.itson.edu.balloonblitz.colocarNaves.ControladorPosicionNaves;
import org.itson.edu.balloonblitz.esperarJugador.ControladorEmparejamiento;
import org.itson.edu.balloonblitz.modelo.ConexionCliente;
import org.itson.edu.balloonblitz.personalizar.PersonalizarPanel;

/**
 *
 * @author rover
 */
public class InicioPanel extends javax.swing.JPanel {

    private final FramePrincipal framePrincipal;
    

    /**
     * Creates new form InicioPanel
     *
     * @param framePrincipal
     */
    public InicioPanel(FramePrincipal framePrincipal) {
        initComponents();
        this.framePrincipal = framePrincipal;
        ConexionCliente.getInstancia().setObservadorJugador(ControladorPosicionNaves.getInstancia());
        ConexionCliente.getInstancia().setObservadorResultado(ControladorEmparejamiento.getInstancia());
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnJugar = new javax.swing.JButton();
        lblFondo = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1280, 680));
        setMinimumSize(new java.awt.Dimension(1280, 680));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnJugar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/jugar.png"))); // NOI18N
        btnJugar.setBorder(null);
        btnJugar.setBorderPainted(false);
        btnJugar.setContentAreaFilled(false);
        btnJugar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnJugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJugarActionPerformed(evt);
            }
        });
        add(btnJugar, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 540, 243, 70));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/inicio.png"))); // NOI18N
        add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 680));

        getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    private void btnJugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJugarActionPerformed

        framePrincipal.cambiarPanel(new PersonalizarPanel(framePrincipal));
    }//GEN-LAST:event_btnJugarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnJugar;
    private javax.swing.JLabel lblFondo;
    // End of variables declaration//GEN-END:variables
}

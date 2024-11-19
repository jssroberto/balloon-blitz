/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.itson.edu.balloonblitz.esperarJugador;

import org.itson.edu.balloonblitz.personalizar.PersonalizarPanel;
import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.itson.edu.balloonblitz.controlador.ControladorEnvio;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.colocarNaves.ColocacionPanel;
import org.itson.edu.balloonblitz.vista.FramePrincipal;
import org.itson.edu.balloonblitz.vista.InicioPanel;

/**
 *
 * @author user
 */
public class EsperandoJugadorPanel extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(InicioPanel.class.getName());
    private final FramePrincipal framePrincipal;
    int contador = 0;
    ControladorEmparejamiento controlador;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    ControladorEnvio envio;

    /**
     * Creates new form PersonalizarPanel
     *
     * @param framePrincipal
     */
    public EsperandoJugadorPanel(FramePrincipal framePrincipal) {
        this.framePrincipal = framePrincipal;
        initComponents();
        envio = new ControladorEnvio();
        controlador = ControladorEmparejamiento.getInstancia();
        try {
            setFuentes();
        } catch (FontFormatException | IOException e) {
            logger.log(Level.SEVERE, "Error al cargar fuentes: ", e);
        }
    }

    private void setFuentes() throws FontFormatException, IOException {
        lblMenu.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));
        lblEsperando.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));

        addTextBorder(lblMenu);
        addTextBorder(lblEsperando);
        unirsePartida();
    }

    // Método para añadir borde al texto en JLabel
    private void addTextBorder(JLabel label) {
        label.setForeground(Color.WHITE); // Color principal del texto
        label.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Definir el grosor del borde y color
                int borderThickness = 2;
                Color borderColor = Color.BLACK;

                // Posición del texto
                String text = label.getText();
                FontMetrics metrics = g2d.getFontMetrics(label.getFont());
                int x = (label.getWidth() - metrics.stringWidth(text)) / 2;
                int y = (label.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

                // Dibujar borde en varias direcciones alrededor del texto
                g2d.setColor(borderColor);
                for (int i = -borderThickness; i <= borderThickness; i++) {
                    for (int j = -borderThickness; j <= borderThickness; j++) {
                        g2d.drawString(text, x + i, y + j);
                    }
                }

                // Dibujar el texto principal encima del borde
                g2d.setColor(label.getForeground());
                g2d.drawString(text, x, y);

                g2d.dispose();
            }
        });
    }

    private void unirsePartida() {
        executorService.submit(() -> {
            try {

                while (!controlador.isValido()) {
                    // Actualizar la UI de forma segura
                    SwingUtilities.invokeLater(() -> {
                        switch (contador % 3) {
                            case 0 ->
                                lblEsperando.setText("Esperando jugador.");
                            case 1 ->
                                lblEsperando.setText("Esperando jugador..");
                            case 2 ->
                                lblEsperando.setText("Esperando jugador...");
                        }
                    });

                    contador++;
                    Thread.sleep(500); // Simula la espera (medio segundo entre cambios)
                }

                // Si es válido, proceder
                Evento jugador2 = new EnvioJugadorEvento();
                jugador2.setEmisor(controlador.obtenerJugador());
                envio.enviarEvento(jugador2);
                lblEsperando.setText("Partida encontrada");
                Thread.sleep(5000);
                // Cambiar al panel de colocación en el hilo principal
                SwingUtilities.invokeLater(() -> framePrincipal.cambiarPanel(new ColocacionPanel(framePrincipal)));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.log(Level.SEVERE, "Hilo interrumpido durante la espera del jugador.", e);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblMenu = new javax.swing.JLabel();
        lblEsperando = new javax.swing.JLabel();
        lblVolver = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblFondo = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMenu.setText("Menu");
        jPanel1.add(lblMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, 280, 40));

        lblEsperando.setText("Esperando a otro jugador...");
        jPanel1.add(lblEsperando, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 416, 700, 40));

        lblVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/volver.jpg"))); // NOI18N
        lblVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblVolverMouseClicked(evt);
            }
        });
        jPanel1.add(lblVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        jButton1.setText("ignorar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 620, -1, -1));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/espera.png"))); // NOI18N
        jPanel1.add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblVolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblVolverMouseClicked
        framePrincipal.cambiarPanel(new PersonalizarPanel(framePrincipal));
    }//GEN-LAST:event_lblVolverMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        framePrincipal.cambiarPanel(new ColocacionPanel(framePrincipal));
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblEsperando;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblMenu;
    private javax.swing.JLabel lblVolver;
    // End of variables declaration//GEN-END:variables
}
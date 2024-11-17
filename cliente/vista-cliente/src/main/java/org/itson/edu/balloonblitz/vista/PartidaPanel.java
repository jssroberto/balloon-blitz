/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.itson.edu.balloonblitz.vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.itson.edu.balloonblitz.auxiliar.BalloonTransferHandler;
import org.itson.edu.balloonblitz.auxiliar.GridDragDropHandler;
import org.itson.edu.balloonblitz.auxiliar.TableroRenderer;
import org.itson.edu.balloonblitz.entidades.Tablero;

/**
 *
 * @author user
 */
public class PartidaPanel extends javax.swing.JPanel {

    private Tablero tablero;
    private GridDragDropHandler gridDragDropHandler;
    private static final Logger logger = Logger.getLogger(InicioPanel.class.getName());
    private final FramePrincipal framePrincipal;

    private static final String FONT_PATH = "/fonts/oetztype/OETZTYPE.TTF";
    private static final float TITLE_FONT_SIZE = 28.0F;

    // Segun el tipo de 
    private static final String BALLOON_BASE_PATH = "/images/ballons/rojo/rojo-";
    private static final int BORDER_THICKNESS = 2;

    /**
     * Creates new form PersonalizarPanel
     *
     * @param framePrincipal
     * @param gridDragDropHandler
     */
    // Esto está mockeado falta saber como voy a obtener el tablero que ya se hizo antes
    public PartidaPanel(FramePrincipal framePrincipal, GridDragDropHandler gridDragDropHandler) {
        this.framePrincipal = framePrincipal;
        this.gridDragDropHandler = gridDragDropHandler;
        initComponents();
        try {
            setupUI();
        } catch (FontFormatException | IOException e) {
            logger.log(Level.SEVERE, "Error al cargar fuentes: ", e);
        }
    }

    private void setupUI() throws FontFormatException, IOException {
        setupFonts();
        setupBalloons();
        renderizarTableroJugador();
    }

    private void renderizarTableroJugador() {
//        Jugador jugador = new Jugador.Builder()
//                .nombre(txtNombre.getText())
//                .colorPropio(colorNaves)
//                .fotoPerfil(fotoPerfil)
//                .build();

        TableroRenderer.renderizarTablero(
                tableroJugador,
                gridDragDropHandler.obtenerTablero(),
                gridDragDropHandler.obtenerNaves()
        );
    }

    private void setupFonts() throws FontFormatException, IOException {
        Font titleFont = framePrincipal.cargarFuente(FONT_PATH, TITLE_FONT_SIZE);

        lblEsperandoMovimiento.setFont(titleFont);
        lblNombre.setFont(titleFont);
        lblNombreEnemigo.setFont(titleFont);

        addTextBorder(lblEsperandoMovimiento);
        addTextBorder(lblNombre);
        addTextBorder(lblNombreEnemigo);
    }

    private void addTextBorder(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                String text = label.getText();
                FontMetrics metrics = g2d.getFontMetrics(label.getFont());
                int x = (label.getWidth() - metrics.stringWidth(text)) / 2;
                int y = (label.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

                // Dibuja los bordes
                g2d.setColor(Color.BLACK);
                for (int i = -BORDER_THICKNESS; i <= BORDER_THICKNESS; i++) {
                    for (int j = -BORDER_THICKNESS; j <= BORDER_THICKNESS; j++) {
                        g2d.drawString(text, x + i, y + j);
                    }
                }

                // Dibuja el texto
                g2d.setColor(label.getForeground());
                g2d.drawString(text, x, y);
                g2d.dispose();
            }
        });
    }

    private void setupBalloons() {
        tableroJugador.setLayout(null);

        // Tipos de barco
        String[] balloonTypes = {"barco", "submarino", "crucero", "portaAviones"};
        int baseX = 150;
        int baseY = 25;
        int yOffset = 50;

        for (int i = 0; i < balloonTypes.length; i++) {
            JLabel balloon = createBalloon(i + 1, balloonTypes[i]);

            ImageIcon icon = (ImageIcon) balloon.getIcon();
            int x = baseX - tableroJugador.getX();
            int y = baseY + (i * yOffset) - tableroJugador.getY();

            balloon.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
            tableroJugador.add(balloon);
        }

        tableroJugador.repaint();
    }

    private JLabel createBalloon(int size, String type) {
        String imagePath = BALLOON_BASE_PATH + size + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        JLabel balloon = new JLabel(icon);
        balloon.setTransferHandler(new BalloonTransferHandler(balloon, type));
        return balloon;
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
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        lblNombre = new javax.swing.JLabel();
        lblNombreEnemigo = new javax.swing.JLabel();
        lblEsperandoMovimiento = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tableroJugador = new javax.swing.JLabel();
        tableroRival = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setText("ganar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 630, -1, -1));

        jButton3.setText("perder");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 630, -1, -1));

        jButton4.setText("volver");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 630, -1, -1));

        lblNombre.setText("lblNombre");
        jPanel1.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(137, 50, 420, 30));

        lblNombreEnemigo.setText("lblNombreEnemigo");
        jPanel1.add(lblNombreEnemigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(714, 50, 440, 30));

        lblEsperandoMovimiento.setText("Esperando movimiento...");
        jPanel1.add(lblEsperandoMovimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, -1, 50));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/benjamin.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 20, -1, -1));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/patFusty.png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        tableroJugador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/tablero.png"))); // NOI18N
        jPanel1.add(tableroJugador, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, -1, 460));

        tableroRival.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/tablero.png"))); // NOI18N
        jPanel1.add(tableroRival, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 160, 460, 460));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/partidaSinTablero.png"))); // NOI18N
        jPanel1.add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        framePrincipal.cambiarPanel(new GanarPanel(framePrincipal));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        framePrincipal.cambiarPanel(new PerderPanel(framePrincipal));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        framePrincipal.cambiarPanel(new ColocacionPanel(framePrincipal));
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblEsperandoMovimiento;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombreEnemigo;
    private javax.swing.JLabel tableroJugador;
    private javax.swing.JLabel tableroRival;
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.itson.edu.balloonblitz.vista;

import org.itson.edu.balloonblitz.auxiliar.BalloonTransferHandler;
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
import javax.swing.TransferHandler;
import org.itson.edu.balloonblitz.auxiliar.GridDragDropHandler;
import org.itson.edu.balloonblitz.entidades.Jugador;

/**
 *
 * @author user
 */
public class ColocacionPanel extends javax.swing.JPanel {

    private static final Logger LOGGER = Logger.getLogger(InicioPanel.class.getName());
    private static final String FONT_PATH = "/fonts/oetztype/OETZTYPE.TTF";
    private static final float TITLE_FONT_SIZE = 28.0F;

    // Segun el tipo de 
    private static final String BALLOON_BASE_PATH = "/images/ballons/rojo/rojo-";
    private static final int BORDER_THICKNESS = 2;

    private final FramePrincipal framePrincipal;
    private GridDragDropHandler gridDragDropHandler;

    /**
     * Creates new form PersonalizarPanel
     *
     * @param framePrincipal
     */
    public ColocacionPanel(FramePrincipal framePrincipal) {
        this.framePrincipal = framePrincipal;
        initComponents();
        this.gridDragDropHandler = new GridDragDropHandler(panelTablero);

        try {
            setupUI();
        } catch (FontFormatException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error initializing UI: ", e);
        }
    }

    private void setupUI() throws FontFormatException, IOException {
        setupFonts();
        setupBalloons();
    }

    private void setupFonts() throws FontFormatException, IOException {
        Font titleFont = framePrincipal.cargarFuente(FONT_PATH, TITLE_FONT_SIZE);
        lblTitulo1.setFont(titleFont);
        lblTitulo2.setFont(titleFont);

        addTextBorder(lblTitulo1);
        addTextBorder(lblTitulo2);
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
        panelContenedorGlobos.setLayout(null);

        // Tipos de barco
        String[] balloonTypes = {"barco", "submarino", "crucero", "portaAviones"};
        int baseX = 150;
        int baseY = 25;
        int yOffset = 50;

        for (int i = 0; i < balloonTypes.length; i++) {
            JLabel balloon = createBalloon(i + 1, balloonTypes[i]);

            ImageIcon icon = (ImageIcon) balloon.getIcon();
            int x = baseX - panelContenedorGlobos.getX();
            int y = baseY + (i * yOffset) - panelContenedorGlobos.getY();

            balloon.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());
            addDragListener(balloon);
            panelContenedorGlobos.add(balloon);
        }

        panelContenedorGlobos.repaint();
    }

    // Aqui el numero de la foto define el tamaño de la nave
    private JLabel createBalloon(int size, String type) {
        String imagePath = BALLOON_BASE_PATH + size + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        JLabel balloon = new JLabel(icon);
        balloon.setTransferHandler(new BalloonTransferHandler(balloon, type));
        return balloon;
    }

    private void addDragListener(JLabel balloon) {
        balloon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                // Iniciar el arrastre
                TransferHandler handler = balloon.getTransferHandler();
                handler.exportAsDrag(balloon, evt, TransferHandler.COPY);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                // Restaurar el cursor por defecto
                balloon.setCursor(java.awt.Cursor.getDefaultCursor());
            }
        });
    }

    private void construirTablero() {
//        List<Nave> naves = gridDragDropHandler.obtenerNaves();
//        Tablero tablero = gridDragDropHandler.obtenerTablero();

//        // Tamaño 1
//        naves.add(new Barco());
//        naves.add(new Barco());
//        naves.add(new Barco());
//
//        // Tamaño 2
//        naves.add(new Submarino());
//        naves.add(new Submarino());
//        naves.add(new Submarino());
//        naves.add(new Submarino());
//
//        // Tamaño 3
//        naves.add(new Crucero());
//        naves.add(new Crucero());
//
//        // Tamaño 4
//        naves.add(new PortaAviones());
//        naves.add(new PortaAviones());
        new Jugador.Builder()
                .naves(gridDragDropHandler.obtenerNaves())
                .tableroPropio(gridDragDropHandler.obtenerTablero())
                .build();
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
        lblTitulo1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblTitulo2 = new javax.swing.JLabel();
        panelTablero = new javax.swing.JLabel();
        panelContenedorGlobos = new javax.swing.JLabel();
        btnConfirmar = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo1.setText("Arrastra tus naves (globos) para colocarlas en el tablero, usa click");
        jPanel1.add(lblTitulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 6, 1020, 40));

        jLabel2.setText("cantGlobo1");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 240, -1, -1));

        jLabel1.setText("cantGlobo2");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 290, -1, -1));

        jLabel3.setText("cantGlobo3");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 340, -1, -1));

        jLabel4.setText("cantGlobo4");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 390, -1, -1));

        jButton1.setText("regresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 630, -1, -1));

        lblTitulo2.setText("derecho para rotarlas.");
        jPanel1.add(lblTitulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, -1, 40));

        panelTablero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/tablero.png"))); // NOI18N
        jPanel1.add(panelTablero, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 140, -1, 460));

        panelContenedorGlobos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/contenedorGlobos.png"))); // NOI18N
        jPanel1.add(panelContenedorGlobos, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 240, 240));

        btnConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/confirmar.png"))); // NOI18N
        btnConfirmar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirmar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConfirmarMouseClicked(evt);
            }
        });
        jPanel1.add(btnConfirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 470, 260, 80));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/colocacionSinTablero.png"))); // NOI18N
        jPanel1.add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 680));

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        framePrincipal.cambiarPanel(new EsperandoJugador(framePrincipal));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnConfirmarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmarMouseClicked
        construirTablero();
        // Así no va quedar final, es para probar que se acomoden los globos en el tablero
        framePrincipal.cambiarPanel(new PartidaPanel(framePrincipal, gridDragDropHandler));
    }//GEN-LAST:event_btnConfirmarMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnConfirmar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblTitulo2;
    private javax.swing.JLabel panelContenedorGlobos;
    private javax.swing.JLabel panelTablero;
    // End of variables declaration//GEN-END:variables
}

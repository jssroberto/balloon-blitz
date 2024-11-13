/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.itson.edu.balloonblitz.vista;

import org.itson.edu.balloonblitz.auxiliar.BalloonTransferHandler;
import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;
import org.itson.edu.balloonblitz.auxiliar.GridDragDropHandler;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Nave;

/**
 *
 * @author user
 */
public class ColocacionPanel extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(InicioPanel.class.getName());
    private final FramePrincipal framePrincipal;
    private GridDragDropHandler gridDragDropHandler;

    /**
     * Creates new form PersonalizarPanel
     *
     * @param framePrincipal
     */
    public ColocacionPanel(FramePrincipal framePrincipal) {
        this.gridDragDropHandler = new GridDragDropHandler(panelTablero);
        this.framePrincipal = framePrincipal;
        initComponents();

        try {
            setFuentes();
            setGlobos();
            enableDrop();
        } catch (FontFormatException | IOException e) {
            logger.log(Level.SEVERE, "Error al cargar fuentes: ", e);
        }
    }

    private void setFuentes() throws FontFormatException, IOException {
        lblTitulo1.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));
        lblTitulo2.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));

        addTextBorder(lblTitulo1);
        addTextBorder(lblTitulo2);
    }

    // Le agrega bordo a todo
    private void addTextBorder(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int borderThickness = 2;
                Color borderColor = Color.BLACK;

                String text = label.getText();
                FontMetrics metrics = g2d.getFontMetrics(label.getFont());
                int x = (label.getWidth() - metrics.stringWidth(text)) / 2;
                int y = (label.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

                g2d.setColor(borderColor);
                for (int i = -borderThickness; i <= borderThickness; i++) {
                    for (int j = -borderThickness; j <= borderThickness; j++) {
                        g2d.drawString(text, x + i, y + j);
                    }
                }

                g2d.setColor(label.getForeground());
                g2d.drawString(text, x, y);

                g2d.dispose();
            }
        });
    }

    private void setGlobos() throws FontFormatException, IOException {
        // Globo de una casilla
        ImageIcon iconRojo = new ImageIcon(getClass().getResource("/images/ballons/rojo/rojo-1.png"));
        JLabel globoRojo = new JLabel(iconRojo);
        globoRojo.setTransferHandler(new BalloonTransferHandler(globoRojo, "single"));

        // Globo de dos casillas
        ImageIcon iconDoble = new ImageIcon(getClass().getResource("/images/ballons/rojo/rojo-2.png"));
        JLabel globoDoble = new JLabel(iconDoble);
        globoDoble.setTransferHandler(new BalloonTransferHandler(globoDoble, "double"));

        // Globo de tres casillas
        ImageIcon iconTriple = new ImageIcon(getClass().getResource("/images/ballons/rojo/rojo-3.png"));
        JLabel globoTriple = new JLabel(iconTriple);
        globoTriple.setTransferHandler(new BalloonTransferHandler(globoTriple, "triple"));

        // Globo de cuatro casillas
        ImageIcon iconQuad = new ImageIcon(getClass().getResource("/images/ballons/rojo/rojo-4.png"));
        JLabel globoQuad = new JLabel(iconQuad);
        globoQuad.setTransferHandler(new BalloonTransferHandler(globoQuad, "quadruple"));

        panelContenedorGlobos.setLayout(null);

        int x = 150; // posición x dentro del contenedor
        int y = 25; // posición y dentro del contenedor
        int width = iconRojo.getIconWidth();
        int height = iconRojo.getIconHeight();

        globoRojo.setBounds(x - panelContenedorGlobos.getX(),
                y - panelContenedorGlobos.getY(),
                width,
                height);

        globoDoble.setBounds(x - panelContenedorGlobos.getX(),
                y + 50 - panelContenedorGlobos.getY(),
                width,
                height);

        globoTriple.setBounds(x - panelContenedorGlobos.getX(),
                y + 100 - panelContenedorGlobos.getY(),
                width,
                height);

        globoQuad.setBounds(x - panelContenedorGlobos.getX(),
                y + 150 - panelContenedorGlobos.getY(),
                width,
                height);

        globoRojo.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                JLabel source = (JLabel) evt.getSource();
                TransferHandler handler = source.getTransferHandler();
                handler.exportAsDrag(source, evt, TransferHandler.COPY);
            }
        });

        globoDoble.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                JLabel source = (JLabel) evt.getSource();
                TransferHandler handler = source.getTransferHandler();
                handler.exportAsDrag(source, evt, TransferHandler.COPY);
            }
        });

        globoTriple.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                JLabel source = (JLabel) evt.getSource();
                TransferHandler handler = source.getTransferHandler();
                handler.exportAsDrag(source, evt, TransferHandler.COPY);
            }
        });

        globoQuad.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                JLabel source = (JLabel) evt.getSource();
                TransferHandler handler = source.getTransferHandler();
                handler.exportAsDrag(source, evt, TransferHandler.COPY);
            }
        });

        panelContenedorGlobos.add(globoRojo);
        panelContenedorGlobos.add(globoDoble);
        panelContenedorGlobos.add(globoTriple);
        panelContenedorGlobos.add(globoQuad);
        panelContenedorGlobos.repaint();
    }

    private void enableDrop() {
        new GridDragDropHandler(panelTablero);
    }

    private void construirJugador() {
        List<Nave> naves = null;
        new Jugador.Builder()
                .naves(naves)
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
        construirJugador();
        framePrincipal.cambiarPanel(new PartidaPanel(framePrincipal));
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

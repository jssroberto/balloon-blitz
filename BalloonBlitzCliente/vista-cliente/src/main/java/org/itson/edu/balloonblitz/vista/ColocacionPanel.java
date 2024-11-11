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
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

/**
 *
 * @author user
 */
public class ColocacionPanel extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(InicioPanel.class.getName());
    private final FramePrincipal framePrincipal;

    /**
     * Creates new form PersonalizarPanel
     *
     * @param framePrincipal
     */
    public ColocacionPanel(FramePrincipal framePrincipal) {
        this.framePrincipal = framePrincipal;
        initComponents();
        try {
            setFuentes();
            setGlobos();
        } catch (FontFormatException | IOException e) {
            logger.log(Level.SEVERE, "Error al cargar fuentes: ", e);
        }
        enableDrop(panelTablero);
    }

    private void setFuentes() throws FontFormatException, IOException {
        lblTitulo1.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));
        lblTitulo2.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));

        addTextBorder(lblTitulo1);
        addTextBorder(lblTitulo2);
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

    private void setGlobos() throws FontFormatException, IOException {
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/ballons/rojo/rojo-1.png"));
        JLabel globoRojo = new JLabel(icon);
        globoRojo.setTransferHandler(new BalloonTransferHandler(globoRojo));

        panelContenedorGlobos.setLayout(null);

        // Especificamos las coordenadas exactas donde queremos que aparezca el globo
        // Estos valores los puedes ajustar según necesites
        int x = 150; // posición x dentro del contenedor
        int y = 25; // posición y dentro del contenedor
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        globoRojo.setBounds(x - panelContenedorGlobos.getX(),
                y - panelContenedorGlobos.getY(),
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

        panelContenedorGlobos.add(globoRojo);
        panelContenedorGlobos.repaint();
    }

    private void enableDrop(JComponent jc) {
        DropTarget dropTarget = new DropTarget(jc, DnDConstants.ACTION_COPY, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    Transferable transferable = dtde.getTransferable();

                    if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY);
                        Image image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                        ImageIcon icon = new ImageIcon(image);

                        Point dropPoint = dtde.getLocation();

                        // Imprimir las coordenadas de donde se soltó el objeto
                        System.out.println("Coordenadas de drop: " + dropPoint.x + ", " + dropPoint.y);

                        JLabel newBalloon = new JLabel(icon);

                        // Ajustar la posición para centrar el globo en el punto de drop
                        int balloonWidth = icon.getIconWidth();
                        int balloonHeight = icon.getIconHeight();
                        newBalloon.setBounds(
                                dropPoint.x - (balloonWidth / 2),
                                dropPoint.y - (balloonHeight / 2),
                                balloonWidth,
                                balloonHeight
                        );

                        // Añadir un listener de clic derecho para rotación si es necesario
                        newBalloon.addMouseListener(new java.awt.event.MouseAdapter() {
                            @Override
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                                    // Implementar lógica de rotación si se necesita
                                }
                            }
                        });

                        panelTablero.add(newBalloon);
                        panelTablero.repaint();
                        dtde.dropComplete(true);
                    } else {
                        dtde.rejectDrop();
                    }
                } catch (Exception e) {
                    dtde.rejectDrop();
                    logger.log(Level.SEVERE, "Error al procesar el drop: ", e);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
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
        jButton2 = new javax.swing.JButton();
        lblTitulo2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        panelTablero = new javax.swing.JLabel();
        panelContenedorGlobos = new javax.swing.JLabel();
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
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 630, -1, -1));

        jButton2.setText("avanzar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 630, -1, -1));

        lblTitulo2.setText("derecho para rotarlas.");
        jPanel1.add(lblTitulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, -1, 40));

        jLabel6.setText("jLabel6");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 290, -1, -1));

        jLabel7.setText("jLabel7");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 340, -1, -1));

        jLabel8.setText("jLabel8");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 390, -1, -1));

        panelTablero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/tablero.png"))); // NOI18N
        jPanel1.add(panelTablero, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 140, -1, 460));

        panelContenedorGlobos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/contenedorGlobos.png"))); // NOI18N
        jPanel1.add(panelContenedorGlobos, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 240, 240));

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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        framePrincipal.cambiarPanel(new PartidaPanel(framePrincipal));
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblTitulo2;
    private javax.swing.JLabel panelContenedorGlobos;
    private javax.swing.JLabel panelTablero;
    // End of variables declaration//GEN-END:variables
}

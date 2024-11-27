/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.itson.edu.balloonblitz.partida;

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
import org.itson.edu.balloonblitz.auxiliar.GridDragDropHandler;
import org.itson.edu.balloonblitz.auxiliar.TableroClickHandler;
import org.itson.edu.balloonblitz.auxiliar.TableroRenderer;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.FramePrincipal;
import org.itson.edu.balloonblitz.entidades.Casilla;
import org.itson.edu.balloonblitz.entidades.eventos.DisparoEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.vista.GanarPanel;
import org.itson.edu.balloonblitz.vista.PerderPanel;

/**
 *
 * @author user
 */
public class PartidaPanel extends javax.swing.JPanel implements ObserverPartida {

    private Jugador jugador;
    private Jugador jugadorRival;
    private Tablero tablero;
    private Tablero tableroDeRival;
    private GridDragDropHandler gridDragDropHandler;
    private static final Logger logger = Logger.getLogger(PartidaPanel.class.getName());
    private final FramePrincipal framePrincipal;
    private static final String FONT_PATH = "/fonts/oetztype/OETZTYPE.TTF";
    private static final float TITLE_FONT_SIZE = 28.0F;
    private static final int BORDER_THICKNESS = 2;
    private ActionHandlerPartida actionHandler;

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
        jugador = framePrincipal.getJugador();
        tablero = gridDragDropHandler.obtenerTablero();
    }

    @Override
    public void update(UpdateEventPartida event) {
        if (null != event.eventType()) {
            switch (event.eventType()) {
                case ENVIAR_JUGADOR:
                    jugadorRival = event.model().getJugadorRival();
                    lblNombreEnemigo.setText(jugadorRival.getNombre());

                    try {
                        setupUI();
                    } catch (FontFormatException | IOException ex) {
                        Logger.getLogger(PartidaPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    break;

                case ACTUALIZAR_LABEL_TIEMPO:
                    lblTiempoRestante.setText(event.model().getTexto());
                    break;
                case ACTUALIZAR_TABLERO_PROPIO:
                    tablero = event.model().getTablero();
                    cargarTableroPropio(tableroJugador, tablero);
                    break;
                case ACTUALIZAR_TABLERO_RIVAL:
                    tableroDeRival = event.model().getTableroDeRival();
                    cargarTableroRival(tableroRival, tableroDeRival);
                    break;
                case TURNO_ACTIVO:
                    TableroClickHandler.configurarTableroRival(tableroRival, jugador, this);
                    tableroRival.setEnabled(true);

                    break;
                case TURNO_INACTIVO:
                    tableroRival.setEnabled(false);
                    break;
                case TIEMPO_TERMINADO:
                    lblTiempoRestante.setText(event.model().getTexto());
                    break;
                default: {
                }
            }
        }

    }

    @Override
    public void enviarEvento(Evento event) {
        actionHandler.enviarEvento((DisparoEvento) event);
    }

    public ActionHandlerPartida getActionHandler() {
        return actionHandler;
    }

    public void setActionHandler(ActionHandlerPartida actionHandler) {
        this.actionHandler = actionHandler;
    }

    private void setupUI() throws FontFormatException, IOException {
        setupFonts();
        renderizarTableroJugador();

    }

    private void renderizarTableroJugador() {
        TableroRenderer.renderizarTablero(tableroJugador, tablero, jugador);
    }

    private void cargarTableroPropio(JLabel tableroLabel, Tablero tablero) {
        TableroRenderer.renderizarTablero(tableroLabel, tablero, jugador);
    }

    private void cargarTableroRival(JLabel tableroLabel, Tablero tablero) {
        TableroRenderer.cargarTableroRival(tableroLabel, tablero, String.valueOf(jugador.getColorRival()).toLowerCase());
    }

    private void setupFonts() throws FontFormatException, IOException {
        Font titleFont = framePrincipal.cargarFuente(FONT_PATH, TITLE_FONT_SIZE);

        lblTiempoRestante.setFont(titleFont);
        lblEsperandoMovimiento.setFont(titleFont);
        lblNombre.setFont(titleFont);
        lblNombreEnemigo.setFont(titleFont);

        addTextBorder(lblEsperandoMovimiento);
        addTextBorder(lblNombre);
        addTextBorder(lblNombreEnemigo);

        lblNombre.setText(jugador.getNombre());

        pfpJugador.setIcon(new ImageIcon(getClass().getResource(jugador.getFotoPerfil())));
        pfpRival.setIcon(new ImageIcon(getClass().getResource(jugadorRival.getFotoPerfil())));

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        lblNombre = new javax.swing.JLabel();
        lblNombreEnemigo = new javax.swing.JLabel();
        lblEsperandoMovimiento = new javax.swing.JLabel();
        pfpRival = new javax.swing.JLabel();
        pfpJugador = new javax.swing.JLabel();
        tableroJugador = new javax.swing.JLabel();
        tableroRival = new javax.swing.JLabel();
        panelBorde = new javax.swing.JLabel();
        panelBorde1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblTiempoRestante = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/clock.png"))); // NOI18N

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

        pfpRival.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/benjamin.png"))); // NOI18N
        pfpRival.setPreferredSize(new java.awt.Dimension(85, 93));
        jPanel1.add(pfpRival, new org.netbeans.lib.awtextra.AbsoluteConstraints(1180, 20, -1, -1));

        pfpJugador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/patFusty.png"))); // NOI18N
        jPanel1.add(pfpJugador, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        tableroJugador.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/cuadricula.png"))); // NOI18N
        jPanel1.add(tableroJugador, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 160, 450, 450));

        tableroRival.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/cuadricula.png"))); // NOI18N
        jPanel1.add(tableroRival, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 160, 450, 450));

        panelBorde.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/borde.png"))); // NOI18N
        jPanel1.add(panelBorde, new org.netbeans.lib.awtextra.AbsoluteConstraints(156, 156, 458, 458));

        panelBorde1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/borde.png"))); // NOI18N
        jPanel1.add(panelBorde1, new org.netbeans.lib.awtextra.AbsoluteConstraints(656, 156, 458, 458));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/clock.png"))); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 630, -1, -1));

        lblTiempoRestante.setText("Hora");
        jPanel1.add(lblTiempoRestante, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 630, 70, 40));

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
//        framePrincipal.cambiarPanel(new ColocacionPanel(framePrincipal, jugador));
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
    private javax.swing.JLabel lblTiempoRestante;
    private javax.swing.JLabel panelBorde;
    private javax.swing.JLabel panelBorde1;
    private javax.swing.JLabel pfpJugador;
    private javax.swing.JLabel pfpRival;
    private javax.swing.JLabel tableroJugador;
    private javax.swing.JLabel tableroRival;
    // End of variables declaration//GEN-END:variables

}

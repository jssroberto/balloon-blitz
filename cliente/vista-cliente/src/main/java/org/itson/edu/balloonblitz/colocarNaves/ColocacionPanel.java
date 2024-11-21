/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.itson.edu.balloonblitz.colocarNaves;

import org.itson.edu.balloonblitz.auxiliar.BalloonTransferHandler;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.TransferHandler;
import org.itson.edu.balloonblitz.auxiliar.GridDragDropHandler;
import org.itson.edu.balloonblitz.entidades.Tablero;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.entidades.eventos.PosicionNavesEvento;
import org.itson.edu.balloonblitz.vista.FramePrincipal;
import org.itson.edu.balloonblitz.vista.InicioPanel;
import org.itson.edu.balloonblitz.partida.PartidaPanel;

/**
 *
 * @author user
 */
public class ColocacionPanel extends javax.swing.JPanel {

    private static final Logger LOGGER = Logger.getLogger(InicioPanel.class.getName());
    private static final String FONT_PATH = "/fonts/oetztype/OETZTYPE.TTF";
    private static final float TITLE_FONT_SIZE = 28.0F;
    private static final float TEXT_FONT_SIZE = 20.0F;
    private String balloon_base_path;
    private static final int BORDER_THICKNESS = 2;
    private final FramePrincipal framePrincipal;
    private GridDragDropHandler gridDragDropHandler;
    ControladorPosicionarNaves controlador;

    //TODO: portaaviones son 2 no 1
    private static final Map<String, Integer> BALLOON_LIMITS = Map.of(
            "barco", 4, // 4 barcos de 1 casilla
            "submarino", 3, // 3 submarinos de 2 casillas
            "crucero", 2, // 2 cruceros de 3 casillas
            "portaAviones", 1 // 1 portaaviones de 4 casillas
    );

    /**
     * Creates new form PersonalizarPanel
     *
     * @param framePrincipal
     * @param jugador
     */
    public ColocacionPanel(FramePrincipal framePrincipal) {
        initComponents();
        controlador = ControladorPosicionarNaves.getInstancia();
        controlador.setLabel(lblTiempoRestante);
        this.framePrincipal = framePrincipal;
        this.gridDragDropHandler = new GridDragDropHandler(panelTablero);
//        this.balloon_base_path = "/images/ballons/" + controlador.obtenerJugador().getColorPropio() + "/" + controlador.obtenerJugador().getColorPropio() + "-";

        // Inicializar los labels de cantidad
        cantNave = new JLabel("x4");
        cantBarco = new JLabel("x3");
        cantCrucero = new JLabel("x2");
        cantPortaAviones = new JLabel("x1");

        try {
            setupUI();
        } catch (FontFormatException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error initializing UI: ", e);
        }
    }

    private void setupUI() throws FontFormatException, IOException {
        setupFonts();
        setupBalloons();
        // Asegurarse de que los contadores estén en cero al iniciar
        BalloonTransferHandler.resetPlacedBalloons();
    }

    private void setupFonts() throws FontFormatException, IOException {
        Font titleFont = framePrincipal.cargarFuente(FONT_PATH, TITLE_FONT_SIZE);
        Font textFont = framePrincipal.cargarFuente(FONT_PATH, TEXT_FONT_SIZE);

        lblTiempoRestante.setFont(titleFont);
        lblTitulo1.setFont(titleFont);
        lblTitulo2.setFont(titleFont);

        cantNave.setFont(textFont);
        cantBarco.setFont(textFont);
        cantCrucero.setFont(textFont);
        cantPortaAviones.setFont(textFont);

        lblNaves.setFont(textFont);
        lblBarcos.setFont(textFont);
        lblCruceros.setFont(textFont);
        lblPortaAviones.setFont(textFont);

        addTextBorder(lblTitulo1);
        addTextBorder(lblTitulo2);

        addTextBorder(cantNave);
        addTextBorder(cantBarco);
        addTextBorder(cantCrucero);
        addTextBorder(cantPortaAviones);

        addTextBorder(lblNaves);
        addTextBorder(lblBarcos);
        addTextBorder(lblCruceros);
        addTextBorder(lblPortaAviones);
    }

    public void reiniciarColocacion() {
//        panelContenedorGlobos.removeAll();
//        BalloonTransferHandler.resetPlacedBalloons();
//        this.gridDragDropHandler = new GridDragDropHandler(panelTablero);
//        setupBalloons();
//        panelContenedorGlobos.revalidate();
//        panelContenedorGlobos.repaint();
    }

    private void actualizarLabelsContadores() {
        int barcosRestantes = BALLOON_LIMITS.get("barco") - BalloonTransferHandler.getPlacedBalloonCount("barco");
        int submarinosRestantes = BALLOON_LIMITS.get("submarino") - BalloonTransferHandler.getPlacedBalloonCount("submarino");
        int crucerosRestantes = BALLOON_LIMITS.get("crucero") - BalloonTransferHandler.getPlacedBalloonCount("crucero");
        int portaAvionesRestantes = BALLOON_LIMITS.get("portaAviones") - BalloonTransferHandler.getPlacedBalloonCount("portaAviones");

        cantNave.setText("x" + barcosRestantes);
        cantBarco.setText("x" + submarinosRestantes);
        cantCrucero.setText("x" + crucerosRestantes);
        cantPortaAviones.setText("x" + portaAvionesRestantes);

        // Opcional: cambiar el color si no quedan más
        cantNave.setForeground(barcosRestantes > 0 ? Color.WHITE : Color.RED);
        cantBarco.setForeground(submarinosRestantes > 0 ? Color.WHITE : Color.RED);
        cantCrucero.setForeground(crucerosRestantes > 0 ? Color.WHITE : Color.RED);
        cantPortaAviones.setForeground(portaAvionesRestantes > 0 ? Color.WHITE : Color.RED);
    }

    // Modificar el método construirTablero para incluir validación
    private boolean construirTablero() {
        // Verificar que se hayan colocado todos los barcos necesarios
        if (!todosLosGlobosColocados()) {
            JOptionPane.showMessageDialog(this,
                    "Debes colocar todos los barcos antes de continuar",
                    "Barcos Incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Tablero tablero = gridDragDropHandler.obtenerTablero();
        Evento eventoTablero = new PosicionNavesEvento(tablero);
//        eventoTablero.setEmisor(controlador.obtenerJugador());
        controlador.enviarEvento(eventoTablero);

        return true;
    }

    // Método para verificar si se han colocado todos los barcos necesarios
    private boolean todosLosGlobosColocados() {
        return BalloonTransferHandler.getPlacedBalloonCount("barco") == 4
                && BalloonTransferHandler.getPlacedBalloonCount("submarino") == 3
                && BalloonTransferHandler.getPlacedBalloonCount("crucero") == 2
                && BalloonTransferHandler.getPlacedBalloonCount("portaAviones") == 1;
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

        // Tipos de barco y sus labels correspondientes
        String[] balloonTypes = {"barco", "submarino", "crucero", "portaAviones"};
        JLabel[] countLabels = {cantNave, cantBarco, cantCrucero, cantPortaAviones};

        int baseX = 200;
        int baseY = 25;
        int yOffset = 50;

        for (int i = 0; i < balloonTypes.length; i++) {
            JLabel balloon = createBalloon(i + 1, balloonTypes[i]);
            JLabel countLabel = countLabels[i];

            // Configurar el balloon
            ImageIcon icon = (ImageIcon) balloon.getIcon();
            int x = baseX - panelContenedorGlobos.getX();
            int y = baseY + (i * yOffset) - panelContenedorGlobos.getY();
            balloon.setBounds(x, y, icon.getIconWidth(), icon.getIconHeight());

            // Configurar el label de cantidad
            countLabel.setForeground(Color.WHITE);
            countLabel.setFont(new Font("Arial", Font.BOLD, 16));
            countLabel.setBounds(x + icon.getIconWidth() + 5, y, 30, 30);

            // Agregar el TransferHandler con listener para actualizar cantidades
            BalloonTransferHandler handler = new BalloonTransferHandler(balloon, balloonTypes[i]) {
                @Override
                protected void exportDone(JComponent source, Transferable data, int action) {
                    super.exportDone(source, data, action);
                    actualizarLabelsContadores();
                }
            };
            balloon.setTransferHandler(handler);

            addDragListener(balloon);
            panelContenedorGlobos.add(balloon);
            panelContenedorGlobos.add(countLabel);
        }

        actualizarLabelsContadores();
        panelContenedorGlobos.repaint();
    }

    // Aqui el numero de la foto define el tamaño de la nave
    private JLabel createBalloon(int size, String type) {
        String imagePath = balloon_base_path + size + ".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath.toLowerCase()));
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
        cantNave = new javax.swing.JLabel();
        cantBarco = new javax.swing.JLabel();
        cantCrucero = new javax.swing.JLabel();
        cantPortaAviones = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        lblTitulo2 = new javax.swing.JLabel();
        panelTablero = new javax.swing.JLabel();
        btnConfirmar = new javax.swing.JLabel();
        btnReiniciar = new javax.swing.JLabel();
        lblBarcos = new javax.swing.JLabel();
        lblCruceros = new javax.swing.JLabel();
        lblPortaAviones = new javax.swing.JLabel();
        lblTiempoRestante = new javax.swing.JLabel();
        lblNaves = new javax.swing.JLabel();
        panelContenedorGlobos = new javax.swing.JLabel();
        panelBorde = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo1.setText("Arrastra tus naves (globos) para colocarlas en el tablero, usa click");
        jPanel1.add(lblTitulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 6, 1020, 40));
        jPanel1.add(cantNave, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 170, -1, 40));
        jPanel1.add(cantBarco, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 220, -1, 40));
        jPanel1.add(cantCrucero, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 270, 50, 40));
        jPanel1.add(cantPortaAviones, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 320, 70, 40));

        jButton1.setText("regresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 630, -1, -1));

        lblTitulo2.setText("derecho para rotarlas.");
        jPanel1.add(lblTitulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 50, -1, 40));

        panelTablero.setFont(new java.awt.Font("Segoe UI", 0, 8)); // NOI18N
        panelTablero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/cuadricula.png"))); // NOI18N
        jPanel1.add(panelTablero, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 140, 450, 450));

        btnConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/confirmar.png"))); // NOI18N
        btnConfirmar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfirmar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConfirmarMouseClicked(evt);
            }
        });
        jPanel1.add(btnConfirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 410, 260, 80));

        btnReiniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/reiniciar.png"))); // NOI18N
        btnReiniciar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReiniciar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnReiniciarMouseClicked(evt);
            }
        });
        jPanel1.add(btnReiniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 500, -1, -1));

        lblBarcos.setText("Barcos");
        jPanel1.add(lblBarcos, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 220, 80, 40));

        lblCruceros.setText("Cruceros");
        jPanel1.add(lblCruceros, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 270, 110, 40));

        lblPortaAviones.setText("Portaaviones");
        jPanel1.add(lblPortaAviones, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 320, 160, 40));

        lblTiempoRestante.setText("Hora");
        jPanel1.add(lblTiempoRestante, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 620, 470, 40));

        lblNaves.setText("Naves");
        jPanel1.add(lblNaves, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 170, 80, 40));

        panelContenedorGlobos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/contenedorGlobos.png"))); // NOI18N
        jPanel1.add(panelContenedorGlobos, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 150, 300, 240));

        panelBorde.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/borde.png"))); // NOI18N
        jPanel1.add(panelBorde, new org.netbeans.lib.awtextra.AbsoluteConstraints(576, 136, 458, 458));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/clock.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 620, -1, -1));

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
//        framePrincipal.cambiarPanel(new EsperandoJugador(framePrincipal, jugador));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnConfirmarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmarMouseClicked
        if (construirTablero()) {
            // Así no va quedar final, es para probar que se acomoden los globos en el tablero
            framePrincipal.cambiarPanel(new PartidaPanel(framePrincipal, gridDragDropHandler));
        }
    }//GEN-LAST:event_btnConfirmarMouseClicked

    private void btnReiniciarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnReiniciarMouseClicked
        reiniciarColocacion();
    }//GEN-LAST:event_btnReiniciarMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnConfirmar;
    private javax.swing.JLabel btnReiniciar;
    private javax.swing.JLabel cantBarco;
    private javax.swing.JLabel cantCrucero;
    private javax.swing.JLabel cantNave;
    private javax.swing.JLabel cantPortaAviones;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblBarcos;
    private javax.swing.JLabel lblCruceros;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblNaves;
    private javax.swing.JLabel lblPortaAviones;
    private javax.swing.JLabel lblTiempoRestante;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblTitulo2;
    private javax.swing.JLabel panelBorde;
    private javax.swing.JLabel panelContenedorGlobos;
    private javax.swing.JLabel panelTablero;
    // End of variables declaration//GEN-END:variables
}

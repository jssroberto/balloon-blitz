/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package org.itson.edu.balloonblitz.vista;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.itson.edu.balloonblitz.entidades.Jugador;
import org.itson.edu.balloonblitz.entidades.enumeradores.ColorNaves;
import org.itson.edu.balloonblitz.entidades.eventos.EnvioJugadorEvento;
import org.itson.edu.balloonblitz.entidades.eventos.Evento;
import org.itson.edu.balloonblitz.modelo.ClienteControlador;

/**
 *
 * @author user
 */
public class PersonalizarPanel extends javax.swing.JPanel {

    private static final Logger logger = Logger.getLogger(InicioPanel.class.getName());
    private final FramePrincipal framePrincipal;

    private ColorNaves colorNaves;
    private ColorNaves colorNavesRival;
    private String fotoPerfil;

    private ClienteControlador controlador;
    private Jugador jugador;

    private record ImageProperties(int width, int height, boolean isEnlarged) {

    }
    private final Map<String, ImageProperties> imageCache = new HashMap<>();
    private String currentlySelectedPath = null;
    private String currentlySelectedRivalPath = null;  // Para globos del rival

    // Necesario para elegir la imagen de perfil
    private int anchoOriginalQuincy = 0, altoOriginalQuincy = 0;
    private int anchoOriginalGwendolin = 0, altoOriginalGwendolin = 0;
    private int anchoOriginalPatFusty = 0, altoOriginalPatFusty = 0;
    private int anchoOriginalBenjamin = 0, altoOriginalBenjamin = 0;

    private boolean imagenBenjaminAumentada = false;
    private boolean imagenQuincyAumentada = false;
    private boolean imagenGwendolinAumentada = false;
    private boolean imagenPatFustyAumentada = false;

    /**
     * Creates new form PersonalizarPanel
     *
     * @param framePrincipal
     */
    public PersonalizarPanel(FramePrincipal framePrincipal) {
        this.controlador = ClienteControlador.getInstancia("localhost", 1234);
        this.framePrincipal = framePrincipal;
        initComponents();
        try {
            setFuentes();
        } catch (FontFormatException | IOException e) {
            logger.log(Level.SEVERE, "Error al cargar fuentes: ", e);
        }
    }

    private void setFuentes() throws FontFormatException, IOException {
        lblTituloNombre.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));
        lblPersonalizar.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));

        lblFotoPerfil.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));
        lblNavesPropias.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));
        lblNavesContrincante.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 28.0F));
        txtNombre.setFont(framePrincipal.cargarFuente("/fonts/oetztype/OETZTYPE.TTF", 20.0F));

        // Llamaa a al método que añade el borde a cada JLabel
        addTextBorder(lblTituloNombre);
        addTextBorder(lblPersonalizar);
        addTextBorder(lblFotoPerfil);
        addTextBorder(lblNavesPropias);
        addTextBorder(lblNavesContrincante);
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

    private void crearJugador() {
        this.jugador = new Jugador.Builder()
                .nombre(txtNombre.getText())
                .colorPropio(colorNaves)
                .colorRival(colorNavesRival)
                .fotoPerfil(fotoPerfil)
                .build();

        Evento jugador2 = new EnvioJugadorEvento();
        jugador2.setEmisor(jugador);

        System.out.println("Emisor antes de enviar: " + jugador2.getEmisor().getNombre());

        controlador.enviarMensaje(jugador2);
        System.out.println("Evento enviado al servidor.");
    }

    private void restaurarImagenesPFP(String imagenExcluida) {
        ImageIcon iconoOriginal;
        Image imagenRedimensionada;

        if (!imagenExcluida.equalsIgnoreCase("benjamin") && imagenBenjaminAumentada) {
            iconoOriginal = new ImageIcon(getClass().getResource("/images/icons/benjamin.png"));
            imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(anchoOriginalBenjamin, altoOriginalBenjamin, Image.SCALE_SMOOTH);
            pfpBenjamin.setIcon(new ImageIcon(imagenRedimensionada));
            imagenBenjaminAumentada = false;
        }

        if (!imagenExcluida.equalsIgnoreCase("quincy") && imagenQuincyAumentada) {
            iconoOriginal = new ImageIcon(getClass().getResource("/images/icons/quincy.png"));
            imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(anchoOriginalQuincy, altoOriginalQuincy, Image.SCALE_SMOOTH);
            pfpQuincy.setIcon(new ImageIcon(imagenRedimensionada));
            imagenQuincyAumentada = false;
        }

        if (!imagenExcluida.equalsIgnoreCase("patfusty") && imagenPatFustyAumentada) {
            iconoOriginal = new ImageIcon(getClass().getResource("/images/icons/patFusty.png"));
            imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(anchoOriginalPatFusty, altoOriginalPatFusty, Image.SCALE_SMOOTH);
            pfpFat.setIcon(new ImageIcon(imagenRedimensionada));
            imagenPatFustyAumentada = false;
        }

        if (!imagenExcluida.equalsIgnoreCase("gwendolin") && imagenGwendolinAumentada) {
            iconoOriginal = new ImageIcon(getClass().getResource("/images/icons/gwendolin.png"));
            imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(anchoOriginalGwendolin, altoOriginalGwendolin, Image.SCALE_SMOOTH);
            pfpGwendolin.setIcon(new ImageIcon(imagenRedimensionada));
            imagenGwendolinAumentada = false;
        }
    }

    private void handleGloboClick(String imagePath, JLabel label, ColorNaves color, boolean isRival) {
        // Actualizar el color correspondiente según si es rival o no
        if (isRival) {
            colorNavesRival = color;
        } else {
            colorNaves = color;
        }

        String currentPath = isRival ? currentlySelectedRivalPath : currentlySelectedPath;

        // Si hay un globo seleccionado y es diferente al actual, restaurarlo
        if (currentPath != null && !currentPath.equals(imagePath)) {
            JLabel previousLabel = getLabelForPath(currentPath, isRival);
            if (previousLabel != null) {
                ImageProperties prevProps = imageCache.get(currentPath);
                updateGloboImage(currentPath, previousLabel, prevProps.width(), prevProps.height());
                imageCache.put(currentPath, new ImageProperties(prevProps.width(), prevProps.height(), false));
            }
        }

        // Procesa el nuevo globo seleccionado
        ImageProperties props = imageCache.computeIfAbsent(imagePath, path -> {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            return new ImageProperties(icon.getIconWidth(), icon.getIconHeight(), false);
        });

        // Siempre aumenta el tamaño del nuevo globo seleccionado
        int newWidth = props.width() + 8;
        int newHeight = props.height() + 8;

        updateGloboImage(imagePath, label, newWidth, newHeight);
        imageCache.put(imagePath, new ImageProperties(props.width(), props.height(), true));

        // Actualizar el path seleccionado correspondiente
        if (isRival) {
            currentlySelectedRivalPath = imagePath;
        } else {
            currentlySelectedPath = imagePath;
        }
    }

    private JLabel getLabelForPath(String path, boolean isRival) {
        // Mapeo de rutas de imagen a sus correspondientes JLabels
        if (isRival) {
            return switch (path) {
                case "/images/ballons/59x73/azul.png" ->
                    globoAzulOP;
                case "/images/ballons/59x73/verde.png" ->
                    globoVerdeOP;
                case "/images/ballons/59x73/amarillo.png" ->
                    globoAmarilloOP;
                case "/images/ballons/59x73/rojo.png" ->
                    globoRojoOP;
                case "/images/ballons/59x73/rosa.png" ->
                    globoRosaOP;
                default ->
                    null;
            };
        } else {
            return switch (path) {
                case "/images/ballons/59x73/azul.png" ->
                    globoAzul;
                case "/images/ballons/59x73/verde.png" ->
                    globoVerde;
                case "/images/ballons/59x73/amarillo.png" ->
                    globoAmarillo;
                case "/images/ballons/59x73/rojo.png" ->
                    globoRojo;
                case "/images/ballons/59x73/rosa.png" ->
                    globoRosa;
                default ->
                    null;
            };
        }
    }

    private void updateGloboImage(String imagePath, JLabel label, int width, int height) {
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(imagePath));
        Image resizedImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(resizedImage));
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
        lblNavesPropias = new javax.swing.JLabel();
        pfpQuincy = new javax.swing.JLabel();
        pfpBenjamin = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblPersonalizar = new javax.swing.JLabel();
        lblVolver = new javax.swing.JLabel();
        lblTituloNombre = new javax.swing.JLabel();
        lblFotoPerfil = new javax.swing.JLabel();
        lblNavesContrincante = new javax.swing.JLabel();
        lblContinuar = new javax.swing.JLabel();
        pfpGwendolin = new javax.swing.JLabel();
        pfpFat = new javax.swing.JLabel();
        globoRojo = new javax.swing.JLabel();
        globoAzul = new javax.swing.JLabel();
        globoVerde = new javax.swing.JLabel();
        globoAmarillo = new javax.swing.JLabel();
        globoRosa = new javax.swing.JLabel();
        globoRojoOP = new javax.swing.JLabel();
        globoAzulOP = new javax.swing.JLabel();
        globoVerdeOP = new javax.swing.JLabel();
        globoAmarilloOP = new javax.swing.JLabel();
        globoRosaOP = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();

        setMaximumSize(new java.awt.Dimension(1280, 680));
        setMinimumSize(new java.awt.Dimension(1280, 680));
        setPreferredSize(new java.awt.Dimension(1280, 680));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblNavesPropias.setText("COLOR NAVES PROPIAS");
        jPanel1.add(lblNavesPropias, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 280, 380, 40));

        pfpQuincy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/quincy.png"))); // NOI18N
        pfpQuincy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pfpQuincy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pfpQuincyMouseClicked(evt);
            }
        });
        jPanel1.add(pfpQuincy, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 190, -1, -1));

        pfpBenjamin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/benjamin.png"))); // NOI18N
        pfpBenjamin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pfpBenjamin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pfpBenjaminMouseClicked(evt);
            }
        });
        jPanel1.add(pfpBenjamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 190, -1, -1));

        txtNombre.setBackground(new java.awt.Color(62, 81, 111));
        txtNombre.setForeground(new java.awt.Color(255, 255, 255));
        txtNombre.setText("Introduzca su nombre...");
        txtNombre.setBorder(null);
        txtNombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNombreFocusLost(evt);
            }
        });
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 190, 310, 30));

        lblPersonalizar.setText("PERSONALIZAR");
        jPanel1.add(lblPersonalizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(503, 26, 310, 30));

        lblVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/volver.jpg"))); // NOI18N
        lblVolver.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblVolverMouseClicked(evt);
            }
        });
        jPanel1.add(lblVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, -1, -1));

        lblTituloNombre.setText("NOMBRE");
        jPanel1.add(lblTituloNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, 160, 40));

        lblFotoPerfil.setText("FOTO DE PERFIL");
        jPanel1.add(lblFotoPerfil, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 140, 250, -1));

        lblNavesContrincante.setText("COLOR NAVES CONTRINCANTE");
        jPanel1.add(lblNavesContrincante, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 440, 480, 40));

        lblContinuar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/continuar.png"))); // NOI18N
        lblContinuar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblContinuar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblContinuarMouseClicked(evt);
            }
        });
        jPanel1.add(lblContinuar, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 520, -1, -1));

        pfpGwendolin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/gwendolin.png"))); // NOI18N
        pfpGwendolin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pfpGwendolin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pfpGwendolinMouseClicked(evt);
            }
        });
        jPanel1.add(pfpGwendolin, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 310, -1, -1));

        pfpFat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/patFusty.png"))); // NOI18N
        pfpFat.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pfpFat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pfpFatMouseClicked(evt);
            }
        });
        jPanel1.add(pfpFat, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 310, -1, -1));

        globoRojo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ballons/59x73/rojo.png"))); // NOI18N
        globoRojo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        globoRojo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                globoRojoMouseClicked(evt);
            }
        });
        jPanel1.add(globoRojo, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, -1, -1));

        globoAzul.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ballons/59x73/azul.png"))); // NOI18N
        globoAzul.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        globoAzul.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                globoAzulMouseClicked(evt);
            }
        });
        jPanel1.add(globoAzul, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 330, -1, -1));

        globoVerde.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ballons/59x73/verde.png"))); // NOI18N
        globoVerde.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        globoVerde.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                globoVerdeMouseClicked(evt);
            }
        });
        jPanel1.add(globoVerde, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 330, -1, -1));

        globoAmarillo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ballons/59x73/amarillo.png"))); // NOI18N
        globoAmarillo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        globoAmarillo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                globoAmarilloMouseClicked(evt);
            }
        });
        jPanel1.add(globoAmarillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 330, -1, -1));

        globoRosa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ballons/59x73/rosa.png"))); // NOI18N
        globoRosa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        globoRosa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                globoRosaMouseClicked(evt);
            }
        });
        jPanel1.add(globoRosa, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 330, -1, -1));

        globoRojoOP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ballons/59x73/rojo.png"))); // NOI18N
        globoRojoOP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        globoRojoOP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                globoRojoOPMouseClicked(evt);
            }
        });
        jPanel1.add(globoRojoOP, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 490, -1, -1));

        globoAzulOP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ballons/59x73/azul.png"))); // NOI18N
        globoAzulOP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        globoAzulOP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                globoAzulOPMouseClicked(evt);
            }
        });
        jPanel1.add(globoAzulOP, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 490, -1, -1));

        globoVerdeOP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ballons/59x73/verde.png"))); // NOI18N
        globoVerdeOP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        globoVerdeOP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                globoVerdeOPMouseClicked(evt);
            }
        });
        jPanel1.add(globoVerdeOP, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 490, -1, -1));

        globoAmarilloOP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ballons/59x73/amarillo.png"))); // NOI18N
        globoAmarilloOP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        globoAmarilloOP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                globoAmarilloOPMouseClicked(evt);
            }
        });
        jPanel1.add(globoAmarilloOP, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 490, -1, -1));

        globoRosaOP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ballons/59x73/rosa.png"))); // NOI18N
        globoRosaOP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        globoRosaOP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                globoRosaOPMouseClicked(evt);
            }
        });
        jPanel1.add(globoRosaOP, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 490, -1, -1));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/panels/personalizar.png"))); // NOI18N
        jPanel1.add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 680));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 180, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1280, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblVolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblVolverMouseClicked
        framePrincipal.cambiarPanel(new InicioPanel(framePrincipal));
    }//GEN-LAST:event_lblVolverMouseClicked

    private void lblContinuarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblContinuarMouseClicked
        crearJugador();
        framePrincipal.cambiarPanel(new EsperandoJugador(framePrincipal, jugador));
    }//GEN-LAST:event_lblContinuarMouseClicked

    private void txtNombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreFocusGained
        if (txtNombre.getText().equals("Introduzca su nombre...")) {
            txtNombre.setText("");
        }
    }//GEN-LAST:event_txtNombreFocusGained

    private void txtNombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreFocusLost
        if (txtNombre.getText().isEmpty()) {
            txtNombre.setText("Introduzca su nombre...");
        }
    }//GEN-LAST:event_txtNombreFocusLost

    private void globoRojoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_globoRojoMouseClicked
        handleGloboClick("/images/ballons/59x73/rojo.png", globoRojo, ColorNaves.ROJO, false);
    }//GEN-LAST:event_globoRojoMouseClicked

    private void globoAzulMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_globoAzulMouseClicked
        handleGloboClick("/images/ballons/59x73/azul.png", globoAzul, ColorNaves.AZUL, false);
    }//GEN-LAST:event_globoAzulMouseClicked

    private void globoVerdeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_globoVerdeMouseClicked
        handleGloboClick("/images/ballons/59x73/verde.png", globoVerde, ColorNaves.VERDE, false);
    }//GEN-LAST:event_globoVerdeMouseClicked

    private void globoAmarilloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_globoAmarilloMouseClicked
        handleGloboClick("/images/ballons/59x73/amarillo.png", globoAmarillo, ColorNaves.AMARILLO, false);
    }//GEN-LAST:event_globoAmarilloMouseClicked

    private void globoRosaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_globoRosaMouseClicked
        handleGloboClick("/images/ballons/59x73/rosa.png", globoRosa, ColorNaves.ROSA, false);
    }//GEN-LAST:event_globoRosaMouseClicked

    private void pfpGwendolinMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pfpGwendolinMouseClicked
        String rutaImagen = "/images/icons/gwendolin.png";
        fotoPerfil = rutaImagen;

        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaImagen));

        if (!imagenGwendolinAumentada && anchoOriginalGwendolin == 0 && altoOriginalGwendolin == 0) {
            anchoOriginalGwendolin = iconoOriginal.getIconWidth();
            altoOriginalGwendolin = iconoOriginal.getIconHeight();
        }

        int ancho = imagenGwendolinAumentada ? anchoOriginalGwendolin : anchoOriginalGwendolin + 8;
        int alto = imagenGwendolinAumentada ? altoOriginalGwendolin : altoOriginalGwendolin + 8;

        Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        pfpGwendolin.setIcon(new ImageIcon(imagenRedimensionada));

        imagenGwendolinAumentada = !imagenGwendolinAumentada;

        if (imagenGwendolinAumentada) {
            restaurarImagenesPFP("gwendolin");
        }
    }//GEN-LAST:event_pfpGwendolinMouseClicked

    private void pfpFatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pfpFatMouseClicked
        String rutaImagen = "/images/icons/patFusty.png";
        fotoPerfil = rutaImagen;

        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaImagen));

        if (!imagenPatFustyAumentada && anchoOriginalPatFusty == 0 && altoOriginalPatFusty == 0) {
            anchoOriginalPatFusty = iconoOriginal.getIconWidth();
            altoOriginalPatFusty = iconoOriginal.getIconHeight();
        }

        int ancho = imagenPatFustyAumentada ? anchoOriginalPatFusty : anchoOriginalPatFusty + 8;
        int alto = imagenPatFustyAumentada ? altoOriginalPatFusty : altoOriginalPatFusty + 8;

        Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        pfpFat.setIcon(new ImageIcon(imagenRedimensionada));

        imagenPatFustyAumentada = !imagenPatFustyAumentada;

        if (imagenPatFustyAumentada) {
            restaurarImagenesPFP("patFusty");
        }
    }//GEN-LAST:event_pfpFatMouseClicked

    private void pfpBenjaminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pfpBenjaminMouseClicked
        String rutaImagen = "/images/icons/benjamin.png";
        fotoPerfil = rutaImagen;

        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaImagen));

        if (!imagenBenjaminAumentada && anchoOriginalBenjamin == 0 && altoOriginalBenjamin == 0) {
            anchoOriginalBenjamin = iconoOriginal.getIconWidth();
            altoOriginalBenjamin = iconoOriginal.getIconHeight();
        }

        int ancho = imagenBenjaminAumentada ? anchoOriginalBenjamin : anchoOriginalBenjamin + 8;
        int alto = imagenBenjaminAumentada ? altoOriginalBenjamin : altoOriginalBenjamin + 8;

        Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        pfpBenjamin.setIcon(new ImageIcon(imagenRedimensionada));

        imagenBenjaminAumentada = !imagenBenjaminAumentada;

        if (imagenBenjaminAumentada) {
            restaurarImagenesPFP("benjamin");
        }
    }//GEN-LAST:event_pfpBenjaminMouseClicked

    private void pfpQuincyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pfpQuincyMouseClicked
        String rutaImagen = "/images/icons/quincy.png";
        fotoPerfil = rutaImagen;

        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(rutaImagen));

        if (!imagenQuincyAumentada && anchoOriginalQuincy == 0 && altoOriginalQuincy == 0) {
            anchoOriginalQuincy = iconoOriginal.getIconWidth();
            altoOriginalQuincy = iconoOriginal.getIconHeight();
        }

        int ancho = imagenQuincyAumentada ? anchoOriginalQuincy : anchoOriginalQuincy + 8;
        int alto = imagenQuincyAumentada ? altoOriginalQuincy : altoOriginalQuincy + 8;

        Image imagenRedimensionada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        pfpQuincy.setIcon(new ImageIcon(imagenRedimensionada));

        imagenQuincyAumentada = !imagenQuincyAumentada;

        if (imagenQuincyAumentada) {
            restaurarImagenesPFP("quincy");
        }
    }//GEN-LAST:event_pfpQuincyMouseClicked

    private void globoRojoOPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_globoRojoOPMouseClicked
        handleGloboClick("/images/ballons/59x73/rojo.png", globoRojoOP, ColorNaves.ROSA, true);
    }//GEN-LAST:event_globoRojoOPMouseClicked

    private void globoAzulOPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_globoAzulOPMouseClicked
        handleGloboClick("/images/ballons/59x73/azul.png", globoAzulOP, ColorNaves.AZUL, true);
    }//GEN-LAST:event_globoAzulOPMouseClicked

    private void globoVerdeOPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_globoVerdeOPMouseClicked
        handleGloboClick("/images/ballons/59x73/verde.png", globoVerdeOP, ColorNaves.VERDE, true);
    }//GEN-LAST:event_globoVerdeOPMouseClicked

    private void globoAmarilloOPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_globoAmarilloOPMouseClicked
        handleGloboClick("/images/ballons/59x73/amarillo.png", globoAmarilloOP, ColorNaves.AMARILLO, true);
    }//GEN-LAST:event_globoAmarilloOPMouseClicked

    private void globoRosaOPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_globoRosaOPMouseClicked
        handleGloboClick("/images/ballons/59x73/rosa.png", globoRosaOP, ColorNaves.ROSA, true);
    }//GEN-LAST:event_globoRosaOPMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel globoAmarillo;
    private javax.swing.JLabel globoAmarilloOP;
    private javax.swing.JLabel globoAzul;
    private javax.swing.JLabel globoAzulOP;
    private javax.swing.JLabel globoRojo;
    private javax.swing.JLabel globoRojoOP;
    private javax.swing.JLabel globoRosa;
    private javax.swing.JLabel globoRosaOP;
    private javax.swing.JLabel globoVerde;
    private javax.swing.JLabel globoVerdeOP;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblContinuar;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblFotoPerfil;
    private javax.swing.JLabel lblNavesContrincante;
    private javax.swing.JLabel lblNavesPropias;
    private javax.swing.JLabel lblPersonalizar;
    private javax.swing.JLabel lblTituloNombre;
    private javax.swing.JLabel lblVolver;
    private javax.swing.JLabel pfpBenjamin;
    private javax.swing.JLabel pfpFat;
    private javax.swing.JLabel pfpGwendolin;
    private javax.swing.JLabel pfpQuincy;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}

package org.itson.edu.balloonblitz.vista;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.itson.edu.balloonblitz.vista.music.MusicPlayer;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author rover
 */
public class FramePrincipal extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(FramePrincipal.class.getName());
    private JPanel panelActual;

    /**
     * Creates new form FramePrincipal
     */
    public FramePrincipal() {
        this.setLayout(new AbsoluteLayout());
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            logger.log(Level.WARNING, "Failed to initialize LaF");
        }
        initComponents();
        setIcons();
        iniciarMusica(70.0F);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ballon Blitz");
        setMaximumSize(new java.awt.Dimension(1280, 680));
        setMinimumSize(new java.awt.Dimension(1280, 680));
        setName("framePrincipal"); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(1280, 680));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getAccessibleContext().setAccessibleDescription("");
        getAccessibleContext().setAccessibleParent(this);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Método para limpiar el contenido de la ventana. Si hay un panel
     * actualmente mostrado, lo elimina de la ventana.
     */
    private void limpiarFrame() {
        if (panelActual != null) {
            this.remove(panelActual);
        }
    }

    /**
     * Método para agregar un panel a la ventana. Este método agrega el panel
     * especificado a la ventana, lo posiciona y ajusta su tamaño
     * automáticamente.
     *
     * @param panel El panel que se va a agregar a la ventana.
     */
    private void ponerEnFrame(JPanel panel) {
        this.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 680));
        this.setLocationRelativeTo(null);
        this.pack();
    }

    private void setIcons() {
        List<Image> iconImages = new ArrayList<>();
        iconImages.add(new ImageIcon(FramePrincipal.class.getResource("/icons/icon16.png")).getImage());
        iconImages.add(new ImageIcon(FramePrincipal.class.getResource("/icons/icon32.png")).getImage());
        iconImages.add(new ImageIcon(FramePrincipal.class.getResource("/icons/icon64.png")).getImage());
        iconImages.add(new ImageIcon(FramePrincipal.class.getResource("/icons/icon128.png")).getImage());
        initComponents();
        this.setIconImages(iconImages);
    }

    private void iniciarMusica(float volume) {
        MusicPlayer musicPlayer = new MusicPlayer("/assets/main-theme.wav");
        musicPlayer.play();
        musicPlayer.setVolume(volume);
    }

    // Métodos de aviso, confirmación, etc., sin cambios
    public void mostrarAviso(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }

    public boolean mostrarConfirmacion(String mensaje, String titulo) {
        int respuesta = JOptionPane.showConfirmDialog(null, mensaje, titulo, JOptionPane.OK_CANCEL_OPTION);
        return respuesta == JOptionPane.OK_OPTION;
    }

    public void mostrarInformacion(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public Font cargarFuente(String fontPath, float fontSize) throws FontFormatException, IOException {
        InputStream fontStream = this.getClass().getResourceAsStream(fontPath);
        Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
        return font.deriveFont(fontSize);
    }

    public void cambiarPanel(JPanel panel) {
        limpiarFrame();
        ponerEnFrame(panel);
        panelActual = panel;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

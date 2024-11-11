/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.itson.edu.balloonblitz.vista.music;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MusicPlayer {

    private static final Logger logger = Logger.getLogger(MusicPlayer.class.getName());
    private Clip clip;
    private FloatControl volumeControl;

    // Constructor que inicializa el clip
    public MusicPlayer(String filePath) {
        try {
            // Obtiene el archivo de música como un flujo de audio
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(filePath));
            
            // Crea un clip y lo abre con el flujo de audio
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            
            // Obtiene el control de volumen
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    // Método para reproducir el audio en loop
    public void play() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    // Método para pausar el audio
    public void pause() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    // Método para reanudar el audio
    public void resume() {
        if (!clip.isRunning()) {
            clip.start();
        }
    }

    // Método para detener el audio
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0); // Resetea la posición a inicio
        }
    }

    // Método para establecer el volumen
    
    public void setVolume(float volume) {
        if (volumeControl != null) {
            // Ajusta el volumen. El rango va de -80.0 (silencio) a 6.0 (máximo)
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float value = min + (max - min) * (volume / 100.0f); // Escala de 0 a 100
            volumeControl.setValue(value);
        }
    }
}


package quiz;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import javafx.beans.value.ChangeListener;

public final class AudioManager {

    private AudioManager() {}

    private static Clip musicClip;
    private static double musicVolume = 0.4;
    private static ChangeListener<Boolean> musicListener;

    public static void startMusic(String name) {
        stopMusic();

        URL url = AudioManager.class.getResource("/music/" + name + ".wav");
        if (url == null) {
            System.err.println("[Audio] Missing music: " + name);
            return;
        }

        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(url);
            musicClip = AudioSystem.getClip();
            musicClip.open(stream);
            applyVolume(musicClip, musicVolume);

            if (Settings.musicOn.get()) {
                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            if (musicListener != null) {
                Settings.musicOn.removeListener(musicListener);
            }
            musicListener = (obs, oldV, newV) -> {
                if (musicClip == null) return;
                if (newV) {
                    musicClip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    musicClip.stop();
                }
            };
            Settings.musicOn.addListener(musicListener);

        } catch (Exception e) {
            System.err.println("[Audio] Music init failed: " + e.getMessage());
            musicClip = null;
        }
    }

    public static void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
        }
    }

    public static void setMusicVolume(double v) {
        musicVolume = Math.max(0, Math.min(1, v));
        if (musicClip != null) applyVolume(musicClip, musicVolume);
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    private static void applyVolume(Clip clip, double volume) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;
        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        float dB;
        if (volume <= 0.0001) {
            dB = gain.getMinimum();
        } else {
            dB = (float) (20 * Math.log10(volume));
            dB = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), dB));
        }
        gain.setValue(dB);
    }

    public static void stopAll() {
        stopMusic();
    }
}
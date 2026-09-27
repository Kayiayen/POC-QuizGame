package quiz;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.media.AudioClip;

public final class Sfx {

    private Sfx() {}

    private static final Map<String, AudioClip> CACHE = new HashMap<>();
    private static double volume = 0.5;

    public static void setVolume(double v) {
        volume = Math.max(0, Math.min(1, v));
        for (AudioClip c : CACHE.values()) c.setVolume(volume);
    }

    public static double getVolume() {
        return volume;
    }

    public static void play(String name) {
        AudioClip clip = CACHE.get(name);
        if (clip == null) {
            URL url = Sfx.class.getResource("/sfx/" + name + ".wav");
            if (url == null) {
                System.err.println("[Sfx] missing: /sfx/" + name + ".wav");
                return;
            }
            clip = new AudioClip(url.toExternalForm());
            clip.setVolume(volume);
            CACHE.put(name, clip);   // cached only on success
        }
        clip.play();
    }
}
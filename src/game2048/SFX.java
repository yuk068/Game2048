package game2048;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SFX {

    protected static int volumeLevel = 2;
    private static Map<Integer, Float> decibelMap = new HashMap<>();

    static {
        decibelMap.put(0, -40.0f);
        decibelMap.put(1, -30.0f);
        decibelMap.put(2, -20.0f);
        decibelMap.put(3, -10.0f);
    }

    public static void cycleVolumeLevel() {
        volumeLevel = (volumeLevel + 1) % 4;
    }

    public static void playSound(String fileName) {
        try {
            ClassLoader classLoader = SFX.class.getClassLoader();

            String filePath = SFX.class.getPackageName().replace('.', '/') + "/" + fileName;
            URL soundURL = classLoader.getResource(filePath);

            if (soundURL != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(decibelMap.get(volumeLevel));
                clip.start();
            } else {
                System.err.println("Sound file not found: " + fileName);
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}


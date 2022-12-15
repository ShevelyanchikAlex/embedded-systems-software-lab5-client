package by.bsuir.povs.lab5.service.impl;

import by.bsuir.povs.lab5.service.SoundService;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundServiceImpl implements SoundService {
    private final static float MUTE_VOLUME = -80.0f;
    private final static float MAX_VOLUME = 6.0f;
    private final static float SCALE_VOLUME = 1.0f;

    private Clip clip;
    private FloatControl floatControl;
    private int currentFramePosition = 0;
    private float prevVolume = 0;
    private float currentVolume = 0;
    private boolean isMute = false;

    @Override
    public void setFile(File file) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void play() {
        clip.setFramePosition(currentFramePosition);
        clip.start();
    }

    @Override
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void stop() {
        currentFramePosition = clip.getFramePosition();
        clip.stop();
    }

    @Override
    public void volumeUp() {
        currentVolume += SCALE_VOLUME;
        if (currentVolume > MAX_VOLUME) {
            currentVolume = MAX_VOLUME;
        }
        floatControl.setValue(currentVolume);
    }

    @Override
    public void volumeDown() {
        currentVolume -= SCALE_VOLUME;
        if (currentVolume < MUTE_VOLUME) {
            currentVolume = MUTE_VOLUME;
        }
        floatControl.setValue(currentVolume);
    }

    @Override
    public void changeVolume(float volume) {
        volume += MUTE_VOLUME;
        if (!(volume < MUTE_VOLUME) && !(volume > MAX_VOLUME)) {
            floatControl.setValue(volume);
        }
    }

    @Override
    public void volumeMute() {
        if (!isMute) {
            prevVolume = currentVolume;
            currentVolume = MUTE_VOLUME;
        } else {
            currentVolume = prevVolume;
        }
        floatControl.setValue(currentVolume);
        isMute = !isMute;
    }
}

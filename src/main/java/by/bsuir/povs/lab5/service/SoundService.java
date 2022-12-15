package by.bsuir.povs.lab5.service;

import java.io.File;

public interface SoundService {
    void setFile(File file);
    void play();
    void loop();
    void stop();
    void volumeUp();
    void volumeDown();
    void changeVolume(float volume);
    void volumeMute();
}

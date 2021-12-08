package com.company;

import javax.sound.sampled.*;
import java.io.*;

public class WavePlayer extends AbstractPlayer<BufferedInputStream> {
    private BufferedInputStream inputStream;
    private Clip audioClip;

    public WavePlayer(Slider slider) {
        if (slider != null) {
            slider.setManualChangeListener(() -> peek(slider.getValue()));
        }
        currentState = State.Stopped;
    }

    @Override
    public void open(BufferedInputStream mediaStream) {
        inputStream = mediaStream;
        reset();
    }

    @Override
    public void close() {
        if (audioClip != null && audioClip.isOpen()) {
            audioClip.stop();
            audioClip.close();
        }
    }

    @Override
    public void play() {
        currentState = State.Playing;
        audioClip.start();
    }

    @Override
    public void pause() {
        currentState = State.Paused;
        audioClip.stop();
    }

    @Override
    public void stop() {
        currentState = State.Stopped;
        audioClip.stop();
        audioClip.close();
    }

    @Override
    void reset() {
        try {
            close();
            audioClip = AudioSystem.getClip();
            audioClip.open(AudioSystem.getAudioInputStream(inputStream));
        }
        catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void peek(long milliseconds) {
        if (audioClip != null && audioClip.isOpen()) {
            audioClip.setMicrosecondPosition(milliseconds * 1000);
        }
    }
}
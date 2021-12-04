package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

class Slider extends JSlider implements ChangeListener {

    private JLabel canvas;
    private final JLabel status;
    private final String format;
    private ArrayList<File> data;

    Slider(JLabel status, String format, ArrayList<File> video) {
        super();
        this.status = status;
        this.format = format;
        reset(video);
        addChangeListener(this);
    }

    public void forward() {
        if (isEnabled() && getMaximum() > getValue()) {
            setValue(getValue() + 1);
        }
    }

    public void back() {
        if (isEnabled() && getMinimum() < getValue()) {
            setValue(getValue() - 1);
        }
    }

    public void reset(ArrayList<File> video) {
        this.data = video;
        if (data == null || data.isEmpty()) {
            setMaximum(0);
            setEnabled(false);
        }
        else {
            setMaximum(data.size() - 1);
            setEnabled(true);
        }
        setValue(0);
        setMinimum(0);
        setPaintTicks(true);
        setPaintLabels(true);
    }

    public void setCanvas(JLabel canvas) {
        this.canvas = canvas;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        status.setText(String.format(format, getValue() + 1));
        if (canvas != null) {
            BufferedImage newImage = ImageReader.getInstance().BImgFromFile(data.get(getValue()));
            canvas.setIcon(new ImageIcon(newImage));
            canvas.repaint();
        }
    }
}
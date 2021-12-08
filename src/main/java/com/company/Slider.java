package com.company;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

class Slider extends JSlider {

    private final JLabel status;
    private final String format;

    private ManualChangeListener listener;

    Slider(JLabel status, String format, ArrayList<File> video) {
        super();
        this.format = format;
        this.status = status;
        reset(video);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (listener != null) {
                    listener.OnManualStateChange();
                }
            }
        });
    }

    @Override
    public void setValue(int n) {
        super.setValue(n);
        status.setText(String.format(format, getValue()));
    }

    public void forward() {
        if (isEnabled() && getMaximum() > getValue()) {
            setValue(getValue() + 1);
        }
    }

    public void jumpTo(int loc) {
        if (isEnabled() && getMaximum() > getValue()) {
            setValue(loc);
        }
    }

    public void back() {
        if (isEnabled() && getMinimum() < getValue()) {
            setValue(getValue() - 1);
        }
    }

    public void reset(ArrayList<File> video) {
        if (video == null || video.isEmpty()) {
            setMaximum(0);
            setEnabled(false);
        }
        else {
            setMaximum(video.size() - 1);
            setEnabled(true);
            setValue(0);
        }
        setMinimum(0);
        setPaintTicks(true);
        setPaintLabels(true);
    }

    public int getCurrentFrame() {
        return getValue();
    }

    public void setManualChangeListener(ManualChangeListener listener) {
        this.listener = listener;
    }

    public interface ManualChangeListener {
        void OnManualStateChange();
    }
}
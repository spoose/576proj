package com.company;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

class Slider extends JSlider {

    private final JLabel status;
    private ArrayList<File> data;

    private int currentFrame;
    private Map<Integer,drawDemo> video_ori_map;

    private ManualChangeListener listener;

    Slider(JLabel status, ArrayList<File> video, Map<Integer,drawDemo> video_ori_map) {
        super();
        this.status = status;
        this.video_ori_map =  video_ori_map;
        reset(video);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (listener != null) {
                    listener.OnManualStateChange();
                }
            }
            public void mouseClicked(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
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
        this.data = video;
        if (data == null || data.isEmpty()) {
            setMaximum(0);
            setEnabled(false);
        }
        else {
            setMaximum(data.size() - 1);
            setEnabled(true);
            setValue(0);
        }
        setMinimum(0);
        setPaintTicks(true);
        setPaintLabels(true);
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setManualChangeListener(ManualChangeListener listener) {
        this.listener = listener;
    }

    public interface ManualChangeListener {
        void OnManualStateChange();
    }
}
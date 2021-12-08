package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Slider extends JSlider implements ChangeListener {

    private drawDemo canvas;
    private final JLabel status;
    private final String format;
    private ArrayList<File> data;

    private int currentFrame;
    private Map<Integer,drawDemo> video_ori_map;

    private ManualChangeListener listener;

    Slider(JLabel status, String format, ArrayList<File> video, Map<Integer,drawDemo>$video_ori_map) {
        super();
        this.status = status;
        this.format = format;
        this.video_ori_map = $video_ori_map;
        reset(video);
        addChangeListener(this);
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
        }
        setValue(0);
        setMinimum(0);
        setPaintTicks(true);
        setPaintLabels(true);
    }

    public void setCanvas(drawDemo canvas) {
        this.canvas = (drawDemo) canvas;
    }


    public int getCurrentFrame() {
        return currentFrame;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println(" ");
        System.out.println("--------- slider change---------");
        currentFrame = ((JSlider)e.getSource()).getValue();
        status.setText("Value of the slider is(current frame): " + currentFrame);
        // status.setText(String.format(format, getValue() + 1));
        if (canvas != null) {
            BufferedImage newImage = ImageReader.getInstance().BImgFromFile(data.get(getValue()));
            drawDemo temp_video_ori = new drawDemo();
            int size = video_ori_map.size();
            if (video_ori_map.containsKey(currentFrame)){
                System.out.println("video_ori_map contains:"+currentFrame+"'s frame"+", the map size: "+size);
                // if(!video_ori_map.get(currentFrame).shapes.isEmpty()){
                    temp_video_ori = video_ori_map.get(currentFrame);
                // }
             }
            canvas.shapes = temp_video_ori.shapes;
            canvas.setIcon(new ImageIcon(newImage));
            System.out.println("before put, video_ori_map.get(currentFrame):"+video_ori_map.get(currentFrame)+", the map size: "+video_ori_map.size());
//            System.out.println("before put, video_ori_map shapae isEmpty:"+video_ori_map.get(currentFrame).shapes.isEmpty());
//            System.out.println("before put, video_ori_map shapae:"+video_ori_map.get(currentFrame).shapes);

            video_ori_map.put(currentFrame,temp_video_ori);

            //video_ori_map.get(currentFrame).shapes is[], when first
            System.out.println("after put, video_ori_map.get(currentFrame):"+video_ori_map.get(currentFrame)+", the map size: "+video_ori_map.size());
            System.out.println("after put, video_ori_map shapae:"+video_ori_map.get(currentFrame).shapes);

            canvas.repaint();
            System.out.println("--------- slider change-end--------");
        }
    }

    public void setManualChangeListener(ManualChangeListener listener) {
        this.listener = listener;
    }

    public interface ManualChangeListener {
        void OnManualStateChange();
    }
}
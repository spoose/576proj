package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Slider_click extends JSlider implements ChangeListener {

    private ClickablePanel canvas;
    private final JLabel status;
    private final String format;
    private ArrayList<File> data;

    // private int currentFrame;
    private int currentFrame;
    private Map<Integer,drawDemo> video_sec_map;

//    HashMap<Shape, String> currShapesColor = new HashMap<>();

    Slider_click(JLabel status, String format, ArrayList<File> video, Map<Integer,drawDemo>video_sec_map) {
        super();
        this.status = status;
        this.format = format;
        this.video_sec_map = video_sec_map;
        reset(video);
        addChangeListener(this);
    }

    public void forward() {
        if (isEnabled() && getMaximum() > getValue()) {
            setValue(getValue() + 1);
            currentFrame = getValue();
        }
    }

    public void back() {
        if (isEnabled() && getMinimum() < getValue()) {
            setValue(getValue() - 1);
            currentFrame = getValue();
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

    public void setCanvas(ClickablePanel canvas) {
        this.canvas = canvas;
    }


    public int getCurrentFrame() {
        return currentFrame;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
//         System.out.println("--------- slider2 change---------");
        // status.setText("Value of the slider is: " + ((JSlider)e.getSource()).getValue());
        currentFrame = ((JSlider)e.getSource()).getValue();
        status.setText(String.format(format, getValue()));
//        System.out.println("slider2 change current:"+currentFrame);
        if (canvas != null) {
            BufferedImage newImage = ImageReader.getInstance().BImgFromFile(data.get(getValue()));
            canvas.setIcon(new ImageIcon(newImage));
//            if (canvas.links!=null){
//                for(int i=0; i< canvas.links.size(); i++){
//                    int start_frame = canvas.links.get(i).oriFrame;
//                    int end_frame = canvas.links.get(i).linkShape.size() + start_frame;
//                    canvas.color = Color.ORANGE;
//                }
//            }
            canvas.repaint();
        }
//         System.out.println("--------- slider2 change-end--------");
    }
}
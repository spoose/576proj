package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ClickablePanel extends JLabel {

    int x, y, x2, y2;
    int currentFrame;
    Player parent;
    HyperLink link;//
    ArrayList<HyperLink> links;
    ArrayList<Color> colors = new ArrayList<>(){{
        add(Color.BLUE);
        add(Color.green);
        add(Color.CYAN);
        add(Color.ORANGE);
        add(Color.YELLOW);
    }};
    Map<Integer, ArrayList<Rectangle>> shapeListMap;
    ArrayList<String> targetJsonPathList;
    ArrayList<String> targetPathList;
    ArrayList<Integer> targetFrameList;

    Map<Integer, ArrayList<Integer>> frameColorsMap = new HashMap<>();

//    JButton btn_clear = new JButton("clear");
//    ArrayList<Shape> shapes = new ArrayList<Shape>();

    public static void main(String[] args) {
    }

    ClickablePanel(Player parent) {
        loadLink(0, 1);
//        System.out.println("ClickablePanel() link:"+link.shapeMap.get(100));
        this.shapeListMap = new HashMap<>();
        this.parent = parent;
        x = y = x2 = y2 = 0; //

        setBorder(BorderFactory.createLineBorder(Color.black));
        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public void clear() {
        this.link = null;
    }

    public void loadLink(int startFrame, int endFrame) {
        Map<Integer, ArrayList<Shape>> shapeMap = new HashMap<>();
        Map<Integer, Shape> linkShape = new HashMap<>();
        for (int i = 0; i < 8999; i++) {
            ArrayList<Shape> shapes = new ArrayList<>();
            if (i >= startFrame && i <= startFrame + endFrame) {
                int testOffset = (i - startFrame);
                shapes.add(makeRectangle(50 + testOffset, 50 + testOffset, 180 + testOffset, 190 + testOffset));
                shapeMap.put(i, shapes);
            }
        }
        System.out.println("loadLink():" + shapeMap.size());
        this.link = new HyperLink("testlink", "/Users/yze/USCOne", "/Users/yze/USCTwo", 200, 100, linkShape, Color.green.getRGB());
//        this.link = links.get(0);
//        return shapeMap;
//        shapeListMap
    }

    public Dimension getPreferredSize() {
        return new Dimension(352, 288);
    }

    public void setStartPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

//    public void setEndPoint(int x, int y) {
//        x2 = (x);
//        y2 = (y);
//    }
//
//    public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
//        int px = Math.min(x,x2);
//        int py = Math.min(y,y2);
//        int pw=Math.abs(x-x2);
//        int ph=Math.abs(y-y2);
//        g.drawRect(px, py, pw, ph);
//    }
//    class button1ActionListener implements ActionListener
//    {
//        @Override
//        public void actionPerformed(ActionEvent e)
//        {
//            shapes.clear();
//        }
//    }

    class MyMouseListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            setStartPoint(e.getX(), e.getY());
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
//            Component source = (Component) e.getSource();
//            // recursively find the root Component in my main app class
//            while (source.getParent() != null) {
//                source = source.getParent();
//            }
//            currentFrame = parent.currentFrame;

//            if (shapeListMap.get(currentFrame)!=null){
            for (int i = 0; i < links.size(); i++) {
                if (links.get(i).linkShape.get(currentFrame) != null) {
//                    System.out.println("shapeListMap.get(currentFrame): "+shapeListMap.get(currentFrame));
//                    Shape temShape = shapeListMap.get(currentFrame).get(i);//j is index of link
//                    System.out.println("in link j= "+i);
                    System.out.println("i links.name: " + links.get(i).linkName);
                    System.out.println("i links.linkShape: " + links.get(i).linkShape);
                    System.out.println("i currentFrame: " + currentFrame);
                    Shape temShape = links.get(i).linkShape.get(currentFrame);//link i: current frame
                    if (temShape.contains(e.getPoint())) {
                        System.out.println("------------------you clicked----------------");
//                            for (int i=0; i < shapeListMap.get(currentFrame).size() ;i++){//each frame's shapes
                        //                        int targetFrame = link.targetFrame;
                        //                        String targetPath = link.targetPath;

//                        JOptionPane.showMessageDialog(null, "clicked"+i+"'s frame,"+"target frame: "+links.get(i).targetFrame+", target path: "+links.get(i).targetPath, "Click", JOptionPane.INFORMATION_MESSAGE);

//                            System.out.println("targetJsonPathList.get("+i+"): "+targetJsonPathList.get(i));
//                            System.out.println("targetPathList.get("+i+"): "+targetPathList.get(i));

//                            System.out.println("targetFrameList.get("+i+") "+targetFrameList.get(i));
                        //                        parent.loadPrimaryVideo(parent.getImgPath(targetJsonPathList.get(i),i),targetJsonPathList.get(i));

                        int tempTargetFrame = links.get(i).targetFrame;

                        System.out.println("links.get(i).linkName:" + links.get(i).linkName);
                        System.out.println("links.get(i).targetFrame:" + tempTargetFrame);
                        System.out.println("links.get(i).targetPath:" + links.get(i).targetPath);
                        System.out.println("links.get(i).targetJsonPath:" + links.get(i).targetJsonPath);
                        System.out.println("temShape:" + temShape);
                        link = links.get(i);
                        parent.sourceFile.setText(links.get(i).linkName);


                        //loadPrimaryVideo leads to links change
                        parent.linkClicked= true;
                        parent.imported= false;
                        parent.loadPrimaryVideo(links.get(i).targetPath, links.get(i).targetJsonPath,tempTargetFrame);//links.get(i).targetPath
                        System.out.println("links.size after loadPrimaryVideo: " + links.size());
                        System.out.println("tempTargetFrame after loadPrimaryVideo: " + tempTargetFrame);
                        System.out.println("parent.slider_p1.getValue() before set:"+parent.slider_p1.getValue());
//                        parent.slider_p1.setValue(tempTargetFrame);
                        System.out.println("parent.slider_p1.getValue() after set:"+parent.slider_p1.getValue());
//                        parent.getJb_play().doClick();
                        System.out.println("------------------you clicked--end--------------");
                    }
                }
            }
        }
    }

//            }
//            if (link.shapeMap.get(currentFrame)!=null){
//                for (Shape s : link.shapeMap.get(currentFrame)) {
//                    if (s.contains(e.getPoint())) {//check if mouse is clicked within shape
//                        //we can either just print out the object class name
//                        int targetFrame = link.targetFrame;
//                        String targetPath = link.targetPath;
//
////                    System.out.println("Clicked a " + s.getClass().getName());
////                    System.out.println("parent " + parent.getName()+","+parent);
//
//                        JOptionPane.showMessageDialog(null, "clicked","Click",JOptionPane.INFORMATION_MESSAGE);
////                        parent.loadPrimaryVideo(targetPath);new
//                        clear();
//                        loadLink(500,600);
//                        parent.slider_p1.setValue(targetFrame);
//                        parent.getJb_play().doClick();
//                    }
//                }
//            }
//        }

//    }

    public void paintComponent(Graphics g) {
//        System.out.println("---------clickable paintComponent--------");
        super.paintComponent(g);
//        g.setColor(Color.RED);
//        drawPerfectRect(g, x, y, x2, y2);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));
        currentFrame = parent.slider_p1.getCurrentFrame();
        if (shapeListMap.get(currentFrame) != null) {
            for (int i = 0; i < shapeListMap.get(currentFrame).size(); i++) {

//            if(i == link.shapeMap.get(currentFrame).size()-1){
//                g2.setPaint(this.colors.get(i));
//                System.out.println("color in cp:" + color);
                if (frameColorsMap.get(currentFrame).get(i)!=null){
                    g2.setPaint(new Color(frameColorsMap.get(currentFrame).get(i)));
                }
                else {
                    g2.setPaint(this.colors.get(i));
                }
//            }else{
//                g2.setPaint(Color.BLACK.getRGB());
//            }
                g2.draw(shapeListMap.get(currentFrame).get(i));
            }
        }
//        if (link.shapeMap.get(currentFrame)!=null){
////            System.out.println("---------clickable: link.shapeMap.get(currentFrame)!=null--------current f:"+currentFrame);
//            for(int i=0; i< link.shapeMap.get(currentFrame).size();i++ ){
////            if(i == link.shapeMap.get(currentFrame).size()-1){
//                g2.setPaint(new Color(link.linkColor));
////            }else{
////                g2.setPaint(Color.BLACK.getRGB());
////            }
//                g2.draw(link.shapeMap.get(currentFrame).get(i));
//            }
//        }


//        if (x != 0 && x2 != 0) {
//            g2.setPaint(Color.LIGHT_GRAY);
//            Shape r = makeRectangle(x, y, x2, y2);
//            g2.draw(r);
//            System.out.println("paintComponent():g2");
//        }
    }

    //new
    public Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }


}

package com.company;

import java.awt.*;
        import java.awt.Point;
        import javax.swing.*;
import java.awt.dnd.DragGestureEvent;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


public class drawDemo extends JLabel {

    int x, y, x2, y2;
    JButton btn_clear = new JButton("clear");

    ArrayList<Shape> shapes = new ArrayList<Shape>();
    boxDemo parent;


    public static void main(String[] args) {
        ImageReader reader = ImageReader.getInstance();
        ArrayList<File> list_img = new ArrayList<File>();
        list_img = reader.FolderConfig("/Users/yze/Downloads/USC/USCOne");
        BufferedImage testImage = new BufferedImage(352,288,BufferedImage.TYPE_INT_RGB);
        testImage = reader.BImgFromFile(list_img.get(0));
        ImageIcon imageIcon = new ImageIcon(testImage);
        JLabel videoLabel = new JLabel();
        videoLabel.setPreferredSize(new Dimension(352,288));
        videoLabel.setIcon(imageIcon);
//        videoLabel.setSize(352,288);

        JFrame f = new JFrame("Draw Box Mouse 2");

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel p = new Panel();
        BoxLayout layout = new BoxLayout(p, BoxLayout.X_AXIS);
        p.setLayout(layout);


//        f.setContentPane(new drawDemo());
        drawDemo drawDemo = new drawDemo();
        p.add(drawDemo);
        p.add(drawDemo.btn_clear);

        p.add(videoLabel);

        f.add(p);
        f.setSize(800, 800);
        f.setVisible(true);
    }

    drawDemo() {
        x = y = x2 = y2 = 0; //
        setBorder(BorderFactory.createLineBorder(Color.black));

        btn_clear.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Shape s : shapes) {
                    System.out.println("clear():"+s.getBounds());
                }
                shapes.clear();
                repaint();
            }
        });

        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public Dimension getPreferredSize() {
        return new Dimension(352,288);
    }

    public void setStartPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setEndPoint(int x, int y) {
        x2 = (x);
        y2 = (y);
    }

    public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
        int px = Math.min(x,x2);
        int py = Math.min(y,y2);
        int pw=Math.abs(x-x2);
        int ph=Math.abs(y-y2);
        g.drawRect(px, py, pw, ph);
    }
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
        public void mouseDragged(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            repaint();
        }
        public void mouseReleased(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            Shape r = makeRectangle(x,y, e.getX(), e.getY());//new
            shapes.add(r);//new
            System.out.println("------drawDemo: parent.video_ori_map.get(parent.currentFrame).shapes: "+parent.video_ori_map.get(parent.currentFrame).shapes);
            repaint();
            x = y = x2 = y2 = 0;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            for (Shape s : shapes) {
                if (s.contains(e.getPoint())) {//check if mouse is clicked within shape
                    //we can either just print out the object class name
                    System.out.println("Clicked a " + s.getClass().getName());
                }
            }
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.setColor(Color.RED);
//        drawPerfectRect(g, x, y, x2, y2);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));
        for(int i=0; i< shapes.size();i++ ){//shape 0, 1, 2
            System.out.println("------drawDemo: shapes.size(): "+shapes.size()+", i:"+i);
            if (parent.frameColorsMap.get(parent.currentFrame)!=null){//if current frame doesn't contain color info or no box
//                System.out.println("------drawDemo: parent.frameColorsMap.get(parent.currentFrame).get(i): "+parent.frameColorsMap.get(parent.currentFrame).get(i)+",i is "+i);
                if (i<parent.frameColorsMap.get(parent.currentFrame).size()){
                    System.out.println("set color:"+new Color(parent.frameColorsMap.get(parent.currentFrame).get(i)));
                    g2.setPaint(new Color(parent.frameColorsMap.get(parent.currentFrame).get(i)));
                }else {
                    System.out.println("set color: red");
                    g2.setPaint(Color.red);
                }
            }else {
                g2.setPaint(Color.RED);
//                if(i == shapes.size()-1){
//                    g2.setPaint(Color.RED);
//                }else{
//                    g2.setPaint(Color.BLACK);
//                }
            }
//            g2.setPaint(parent.colors.get(i));
//            g2.setPaint(color);

            g2.draw(shapes.get(i));
        }


//        if (x != 0 && x2 != 0) {
            g2.setPaint(Color.LIGHT_GRAY);
            Shape r = makeRectangle(x, y, x2, y2);
            g2.draw(r);
//            System.out.println("paintComponent():g2");
//        }
    }

    //new
    private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }


}
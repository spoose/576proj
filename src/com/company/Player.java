package com.company;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.geom.Rectangle2D;

public class Player extends JFrame {

    //    drawDemo video_ori_label;
    boolean imported = false;
    boolean first_flag = true;
    boolean videoPlayed = false;
    Thread video_thread;

    int currentFrame = 0;
    int currentFrame_sec = 0;
    private long timeStamp = 0;
    int detectAnchorIndex = 0;
    int detectAnchorIndex2 = 0;
    ArrayList<File> primary_video;
    ArrayList<File> secondary_video;

    /**
     * panel-Top:TBD  link list, modify link, and save file
     */


    /**
     * panel-bottom left: play, stop video
     */
    JPanel panel1 = new JPanel(); //bottom-left
    JPanel panel1_control_box = new JPanel(); // slider
    JButton jb_prev = new JButton("<");
    Slider_sec slider_p1;
    JButton jb_next = new JButton(">");
    JPanel panel1_control_box2 = new JPanel();// import & play & stop
    JButton jb_import_ori = new JButton("\uDBC0\uDE05 Import");
    JButton jb_play = new JButton("\uDBC2\uDCE8 Play");
    JButton jb_stop = new JButton("\uDBC2\uDE86 Stop");
    JLabel video_area;//image area to display
    Map<Integer,drawDemo> video_ori_map;//a map of video_ori, each represents each frame, each has own shapes( box bounding info )
    Map<Integer,drawDemo> video_ori_map_sec;

    /**
     * panel2: link operation section
     */
    JPanel panel2 = new JPanel(); //bottom center panel
//    JButton jbDetect = new JButton("\uDBC0\uDE0C Frame Detect");
//    JButton jb_redraw = new JButton("\uDBC0\uDC61 Redraw");
//    JButton jb_link = new JButton("\uDBC1\uDCA1 Link!");

    /**
     * panel-bottom right: link info
     */



    public Player()
    {
        // Create and set up a frame window
//        setDefaultLookAndFeelDecorated(true);
        setSize(900,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * panel-bottom left: play, stop video
         */
        video_area = new JLabel();
        video_area.setPreferredSize(new Dimension(352, 288));
        video_ori_map = new HashMap<>();
        video_ori_map_sec = new HashMap<>();

        JLabel status = new JLabel("Import a Video to Start!", JLabel.CENTER);
        slider_p1 = new Slider_sec(status, "Value of the slider is: %d", primary_video,video_ori_map);
        slider_p1.setPreferredSize(new Dimension(380,20));
        slider_p1.setCanvas(video_area);
        currentFrame = slider_p1.getCurrentFrame();

        jb_import_ori.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_play.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_play.addActionListener( new btnPlayListener());
        jb_stop.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_stop.addActionListener( new btnStopListener());
//        jb_prev.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_prev.addActionListener( new btnPrevListener());
//        jb_next.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_next.addActionListener( new btnNextListener());
        jb_play.setEnabled (false);
        jb_stop.setEnabled (false);


        panel1_control_box.add(jb_prev);
        panel1_control_box.add(slider_p1);
        panel1_control_box.add(jb_next);

        panel1_control_box2.add(jb_import_ori);
        panel1_control_box2.add(jb_play);
        panel1_control_box2.add(jb_stop);
        panel1_control_box2.setBackground(Color.PINK);
        jb_import_ori.addActionListener(new Player.btnImportListener("Select primary video", Player.this, true));

        BoxLayout layout1 = new BoxLayout(panel1, BoxLayout.Y_AXIS);
        panel1.setLayout(layout1);
        panel1.setBorder(BorderFactory.createTitledBorder("LEFT"));

//        video_area.setBorder(new LineBorder(Color.black));

        video_area.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1_control_box.setAlignmentX(Component.CENTER_ALIGNMENT);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1_control_box2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1.add(video_area);
        panel1.add(panel1_control_box);
        panel1.add(status);
        panel1.add(panel1_control_box2);

//        video_ori.setAlignmentX(Component.CENTER_ALIGNMENT);
        /**
         * panel2
         */
        BoxLayout layout2 = new BoxLayout(panel2, BoxLayout.Y_AXIS);
        panel2.setLayout(layout2);
        panel2.setBorder(BorderFactory.createTitledBorder("INFO"));
        JLabel Link_info = new JLabel("links information", JLabel.CENTER);
        panel2.add(Link_info);


        /**
         * layout arrange
         */
        // Add the three panels into the frame
        JPanel group = new JPanel();
//        group.setLayout(new FlowLayout());
        group.add(panel1);
        group.add(panel2);
//        group.add(panel3);

        Container container=getContentPane();    //获取当前窗口的内容窗格
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.add(Box.createRigidArea(new Dimension(100,0)));
//        container.add(panel0);
        container.add(Box.createRigidArea(new Dimension(100,0)));
        container.add(group);
        setLayout(new FlowLayout());

        // Set the window to be visible as the default to be false
//        pack();
        setVisible(true);
    }

    /**
     * ActionListener
     */
    private class btnPlayListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            videoPlayed = true;

            currentFrame = slider_p1.getCurrentFrame();
            timeStamp = System.currentTimeMillis();

             video_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO : Reload MetaData Files
                    System.out.println("new thread before while:"+Thread.currentThread().getName());

                    timeStamp =  System.currentTimeMillis();
                    while(videoPlayed){
                        currentFrame = slider_p1.getCurrentFrame();
//                        System.out.println("-----------current frame is:"+currentFrame);
                        if (currentFrame == 8999){
                            videoPlayed = false;
                            break;
                        }
                        try {
                            long currentTime = System.currentTimeMillis();
                            long passTime = System.currentTimeMillis() - timeStamp;
                            if (passTime < (0 == currentFrame % 3 ? 34 : 33)) {
                                Thread.sleep(0);
                                continue;
                            }
                            timeStamp = System.currentTimeMillis();
                        }
                        catch (InterruptedException ex) {
                                ex.printStackTrace();
                        }
                        slider_p1.forward();

                        }//end while
//                    for (int i =0 ; i < 300 ; i++){//300 frame should be 10s
//                        try{
//                            Thread.sleep(31);
//                        }
//                        catch (InterruptedException e1){}
//                        slider_p1.forward();
//                    }
                }
            });
            System.out.println("Thread before start "+Thread.currentThread().getName());
            video_thread.start();
            System.out.println("Thread after start "+Thread.currentThread().getName());

            jb_stop.setEnabled (true);
            jb_play.setEnabled (false);
        }
    }

    private long frameToMilsec() {
        if (!imported || currentFrame < 1) {
            return 0;
        }
        return (currentFrame * 33) + ((long)(currentFrame / 3) + 1);
    }

    private class btnStopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentFrame = slider_p1.getCurrentFrame();
            videoPlayed = false;

            try {
                System.out.println("btnStopListener before sleep: "+Thread.currentThread().getName());
                Thread.sleep(0);
                System.out.println("btnStopListener after sleep: "+Thread.currentThread().getName());
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            jb_play.setEnabled (true);
        }
    }



    private class btnPrevListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            slider_p1.back();
        }
    }




    private class btnNextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

//            if (currentFrame == 0 && first_flag ){
//                drawDemo first_frame_primary_video = new drawDemo();
//                for (int i = 0; i < video_ori.shapes.size(); i++) {
//                    first_frame_primary_video.shapes.add(video_ori.shapes.get(i));
//                }
//                System.out.println("-----size of first_video_ori---------:"+first_frame_primary_video.shapes);
//                video_ori_map.put(0,first_frame_primary_video);
//                first_flag = false;
//            }

            slider_p1.forward();
            currentFrame = slider_p1.getCurrentFrame();
            System.out.println("btnNext() currentFrame is:"+currentFrame);
        }
    }

    private class btnImportListener implements ActionListener {

        private final String title;
        private final Component parent;
        private final boolean isPrimary;

        btnImportListener(String title, Component parent, boolean isPrimary) {
            this.title = title;
            this.parent = parent;
            this.isPrimary = isPrimary;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setDialogTitle(title);
            if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(parent)) {
//                jbDectAnchor.setEnabled (true);
//                jb_redraw.setEnabled (true);
                if (isPrimary) loadPrimaryVideo(fileChooser.getSelectedFile().getPath());
            }
        }
    }

    private void loadPrimaryVideo(String path) {
        ImageReader reader = ImageReader.getInstance();
        primary_video = reader.FolderConfig(path);
        if (!primary_video.isEmpty()) {
            imported = true;
            slider_p1.reset(primary_video);
            video_area.setIcon(new ImageIcon(reader.BImgFromFile(primary_video.get(0))));
//            video_ori.shapes.clear();
            jb_play.setEnabled (true);
//            jb_stop.setEnabled (true);
        }

        // TODO : Reload MetaData Files
        // TODO : Reset Secondary Video Panel
    }


    public static void main(String[] agrs)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Player();    //创建一个实例化对象
            }
        });

    }


}

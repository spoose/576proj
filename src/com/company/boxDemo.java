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

public class boxDemo extends JFrame {

//    drawDemo video_ori_label;
    boolean imported = false;
    boolean first_flag = true;

    int currentFrame = 0;
    int currentFrame_sec = 0;
    int detectAnchorIndex = 0;
    int detectAnchorIndex2 = 0;
    ArrayList<File> primary_video;
    ArrayList<File> secondary_video;
    /**
     * panel0: link list, modify link, and save file
     */
    JList linkList;
    JLabel linkLabel;
    JButton jb_save_file = new JButton("\uDBC0\uDE05 Save File");

    /**
     * panel1: editing video section
     */
    JPanel panel1 = new JPanel(); //bottom-left
    JPanel panel1_control_box = new JPanel(); // slider
    JButton jb_prev = new JButton("<");
    Slider slider_p1;
    JButton jb_next = new JButton(">");
    JPanel panel1_control_box2 = new JPanel();// import & stop
    JButton jb_import_ori = new JButton("\uDBC0\uDE05 Import");
    JButton jbDectAnchor = new JButton("􀍷 First Anchor ");//\uDBC1\uDF2A
    JButton jbDectAnchor2 = new JButton("􀺪 Last Anchor ");
    drawDemo video_ori;
    Map<Integer,drawDemo> video_ori_map;//a map of video_ori, each represents each frame, each has own shapes( box bounding info )
    Map<Integer,drawDemo> video_ori_map_sec;

    /**
     * panel2: link operation section
     */
    JPanel panel2 = new JPanel(); //bottom center panel
    JButton jbDetect = new JButton("\uDBC0\uDE0C Frame Detect");
    JButton jb_redraw = new JButton("\uDBC0\uDC61 Redraw");
    JButton jb_link = new JButton("\uDBC1\uDCA1 Link!");

    /**
     * panel3: secondary video section
     */
    JPanel panel3 = new JPanel(); //bottom right
    JPanel panel3_control_box = new JPanel(); // slider
    JButton jb_prev_p3 = new JButton("<");
    Slider_sec slider_p3;
    JButton jb_next_p3 = new JButton(">");
    JPanel panel3_control_box2 = new JPanel();// import & stop
    JButton jb_import_sec = new JButton("\uDBC0\uDE05");
    JButton jb_stop_sec = new JButton("\uDBC1\uDF2A");
    JLabel video_sec;


    public boxDemo()
    {
        // Create and set up a frame window
//        setDefaultLookAndFeelDecorated(true);
        setSize(1200,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /**
         * panel0: link creation, link list,  save file operation
         */
        // Define new buttons with different width on help of the ---
//        JButton jb0 = new JButton("Button 0 -");
//        JButton jb00 = new JButton("Button 00 --------");
        // Define the panel to hold the buttons

        JPanel panel0 = new JPanel();
        JPanel panel_box_info = new JPanel();
        panel_box_info.setLayout( new BoxLayout(panel_box_info, BoxLayout.Y_AXIS));
        JPanel panel_links= new JPanel();
        panel_links.setLayout( new BoxLayout(panel_links, BoxLayout.Y_AXIS));

        linkLabel=new JLabel(" ");
        JLabel link_head = new JLabel("Links");
        link_head.setFont(new Font("Dialog", Font.BOLD, 16));
        link_head.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane scrollPane=new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(300,150));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_links.add(link_head);
        panel_links.add(scrollPane);
        panel_links.add(linkLabel);

        JLabel box_head = new JLabel("Bounding Box");
        box_head.setFont(new Font("Dialog", Font.BOLD, 16));
        panel_box_info.add(box_head);
        panel_box_info.setBackground(Color.pink);
        panel_box_info.setPreferredSize(new Dimension(300,150));

        linkList=new JList();
        scrollPane.setViewportView(linkList);
        String[] listData=new String[7];
        listData[0]="《一点就通学Java》";
        listData[1]="《一点就通学PHP》";
        listData[2]="《一点就通学Visual Basic）》";
        listData[3]="《一点就通学Visual C++）》";
        listData[4]="《Java编程词典》";
        listData[5]="《PHP编程词典》";
        listData[6]="《C++编程词典》";
        linkList.setListData(listData);
        linkList.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                linkLabel.setText("感谢您购买："+linkList.getSelectedValue());
            }
        });

        // Set up the BoxLayout
        BoxLayout layout0 = new BoxLayout(panel0, BoxLayout.X_AXIS);
        panel0.setLayout(layout0);
        // Set up the title for different panels
        panel0.setBorder(BorderFactory.createTitledBorder("TOP"));
//        panel0.setPreferredSize(new Dimension(600,100));

        panel0.add(Box.createRigidArea(new Dimension(20,0)));
        panel0.add(panel_box_info);
        panel0.add(Box.createRigidArea(new Dimension(40,0)));
        panel0.add(panel_links);
        panel0.add(Box.createRigidArea(new Dimension(40,0)));
        panel0.add(jb_save_file);
        panel0.add(Box.createRigidArea(new Dimension(20,0)));



        // Add the buttons into the panel with three different alignment options
//        panel0.add(jb0);
//        panel0.add(jb00);

        /**
         * video(to be added hyperlink) section
         */



        video_ori = new drawDemo();
        video_ori_map = new HashMap<>();
        video_ori_map_sec = new HashMap<>();
        
        JLabel status = new JLabel("Import a Video to Start!", JLabel.CENTER);
        slider_p1 = new Slider(status, "Value of the slider is: %d", primary_video,video_ori_map);
        slider_p1.setCanvas(video_ori);
        currentFrame = slider_p1.getCurrentFrame();

        jb_prev.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_prev.addActionListener( new btnPrevListener());
        jb_next.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_next.addActionListener( new btnNextListener());

        panel1_control_box.add(jb_prev);
        panel1_control_box.add(slider_p1);
        panel1_control_box.add(jb_next);
//        panel1_control_box.setBackground(Color.pink);
//        panel1_control_box2.setLayout(new BoxLayout(panel1_control_box2, BoxLayout.X_AXIS));
//        panel1_control_box2.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        panel1_control_box2.add(jb_import_ori);
        panel1_control_box2.add(jbDectAnchor);
        panel1_control_box2.add(jbDectAnchor2);
        panel1_control_box2.add(jbDetect);
        panel1_control_box2.add(jb_redraw);
        panel1_control_box2.setBackground(Color.PINK);
        
        jbDectAnchor2.setEnabled (false);
        jbDectAnchor.setEnabled (false);
        jbDetect.setEnabled (false);
        jb_redraw.setEnabled (false);

        jbDectAnchor.addActionListener(new jbDectAnchorListener());//detect function: set first anchor
        jbDectAnchor2.addActionListener(new jbDectAnchorListener2());//detect function: set last anchor
        jbDetect.addActionListener(new jbDetectListener());//detect function: detect between two anchors

        jb_import_ori.addActionListener(new btnImportListener("Select primary video", boxDemo.this, true));
        jb_import_sec.addActionListener(new btnImportListener("Select secondary video", boxDemo.this, false));
        jb_redraw.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Shape s : video_ori.shapes) {
                    System.out.println("clear():"+s.getBounds());
                }
                video_ori.shapes.clear();
                repaint();
            }
        });

        BoxLayout layout1 = new BoxLayout(panel1, BoxLayout.Y_AXIS);
        panel1.setLayout(layout1);
        panel1.setBorder(BorderFactory.createTitledBorder("LEFT"));

        video_ori.setBorder(new LineBorder(Color.black));

        video_ori.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1_control_box.setAlignmentX(Component.CENTER_ALIGNMENT);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1_control_box2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1.add(video_ori);
        panel1.add(panel1_control_box);
        panel1.add(status);
        panel1.add(panel1_control_box2);

//        video_ori.setAlignmentX(Component.CENTER_ALIGNMENT);


        /**
         * panel2: for link draw and redraw operation
         */
        panel2 = new JPanel();
//        panel2.setBorder(BorderFactory.createTitledBorder("CENTER"));

//        jb_link.setMargin(new Insets(5,0,5,5));
//        jb_link.setFont(new Font("Dialog", Font.PLAIN, 20));
//        panel2.add(jb_draw);
//        panel2.add(jb_redraw);
        panel2.add(jb_link);
//        jb_draw.setAlignmentX(Component.CENTER_ALIGNMENT);
//        jb_redraw.setAlignmentX(Component.CENTER_ALIGNMENT);
        BoxLayout layout2 = new BoxLayout(panel2, BoxLayout.Y_AXIS);
        panel2.setLayout(layout2);


        /**
         * panel3: secondary video section
         */
        panel3.setBorder(BorderFactory.createTitledBorder("RIGHT"));
        BoxLayout layout3 = new BoxLayout(panel3, BoxLayout.Y_AXIS);
        panel3.setLayout(layout3);

        video_sec = new JLabel();
        video_sec.setPreferredSize(new Dimension(352, 288));
        video_sec.setBorder(new LineBorder(Color.black));
        video_sec.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel status2 = new JLabel("Import a Video to Start!", JLabel.CENTER);
        status2.setAlignmentX(Component.CENTER_ALIGNMENT);
        slider_p3 = new Slider_sec(status2, "Value of the slider is: %d", secondary_video, video_ori_map_sec);
        slider_p3.setCanvas(video_sec);

        jb_prev_p3.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_prev_p3.addActionListener( new btnPrevListener3());
        jb_next_p3.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_next_p3.addActionListener( new btnNextListener3());

        panel3_control_box.add(jb_prev_p3);
        panel3_control_box.add(slider_p3);
        panel3_control_box.add(jb_next_p3);
        panel3_control_box2.add(jb_import_sec);
        panel3_control_box2.add(jb_stop_sec);
        panel3_control_box2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel3.add(video_sec);
        panel3.add(panel3_control_box);
        panel3.add(status2);
        panel3.add(panel3_control_box2);



        // Add the three panels into the frame
        JPanel group = new JPanel();
//        group.setLayout(new FlowLayout());
        group.add(panel1);
        group.add(panel2);
        group.add(panel3);

        Container container=getContentPane();    //获取当前窗口的内容窗格
        container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
        container.add(Box.createRigidArea(new Dimension(100,0)));
        container.add(panel0);
        container.add(Box.createRigidArea(new Dimension(100,0)));
        container.add(group);
        setLayout(new FlowLayout());

        // Set the window to be visible as the default to be false
//        pack();
        setVisible(true);
    }

    private class jbDectAnchorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                currentFrame = slider_p1.getCurrentFrame();
                detectAnchorIndex = currentFrame;
                System.out.println("jbDectAnchor(): detectAnchorIndex is: "+detectAnchorIndex);
                System.out.println("jbDectAnchor(): video_ori_map["+detectAnchorIndex+"]:"+ video_ori_map.get(detectAnchorIndex).shapes.size());

                jbDectAnchor2.setEnabled (true);
            }
    }

    private class jbDectAnchorListener2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                currentFrame = slider_p1.getCurrentFrame();
                detectAnchorIndex2 = currentFrame;
                System.out.println("jbDectAnchor2(): detectAnchorIndex2 is: "+detectAnchorIndex2);
                jbDetect.setEnabled (true);
            }
    }

    private class jbDetectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int firstFrame = detectAnchorIndex;
            int lastFrame = detectAnchorIndex2;
            // int index = 0;
            int index = video_ori_map.get(firstFrame).shapes.size() -1  ; // last shape in shape-list
            System.out.println("frame detect(): first frame:"+ firstFrame +", index:"+index);

            /**
             * xFirst: top left x of first frame
             * xLast: top left x of last frame
             */
            double xFirst = video_ori_map.get(firstFrame).shapes.get(index).getBounds2D().getX();
            double xLast = video_ori_map.get(lastFrame).shapes.get(index).getBounds2D().getX();
            double yFirst = video_ori_map.get(firstFrame).shapes.get(index).getBounds2D().getY();
            double yLast = video_ori_map.get(lastFrame).shapes.get(index).getBounds2D().getY();
            double firstWidth = video_ori_map.get(firstFrame).shapes.get(index).getBounds2D().getWidth();
            double firstHeight = video_ori_map.get(firstFrame).shapes.get(index).getBounds2D().getHeight();
            double lastWidth = video_ori_map.get(lastFrame).shapes.get(index).getBounds2D().getWidth();
            double lastHeight = video_ori_map.get(lastFrame).shapes.get(index).getBounds2D().getHeight();

            // System.out.println("frame detect(): xFirst:"+xFirst+",xLast:"+xLast);
            // System.out.println("frame detect(): yFirst:"+yFirst+",yLast:"+yLast);
            // System.out.println("frame detect(): firstWidth:"+firstWidth+",firstHeight:"+firstHeight);
            // System.out.println("frame detect(): lastWidth:"+lastWidth+",lastHeight:"+lastHeight);

            double xChange = (xLast - xFirst)/(lastFrame-firstFrame);
            double yChange = (yLast - yFirst)/(lastFrame-firstFrame);
            double widthChange = (lastWidth - firstWidth)/(lastFrame-firstFrame);
            double heightChange = (lastHeight - firstHeight)/(lastFrame-firstFrame);
            System.out.println("frame detect(): xChange:"+xChange+",yChange:"+yChange);

            for(int i = firstFrame+1; i < lastFrame; i++){
                drawDemo tempItem = new drawDemo();
                int x1 = (int)(xFirst + xChange*(i-firstFrame));
                int y1 = (int)(yFirst + yChange*(i-firstFrame));
                int x2 = (int)(x1 + firstWidth + widthChange*(i-firstFrame));
                int y2 = (int)(y1 + firstHeight + heightChange*(i-firstFrame));
                
                Shape temShape = new Rectangle2D.Float(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
                // Shape temShape = new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
                System.out.println("i:"+i+",frame detect(): temShape:"+temShape);

                tempItem.shapes.add(temShape);
                video_ori_map.put(i, tempItem);
            }
            for (Shape s : video_ori_map.get(firstFrame).shapes) {
                System.out.println("frame detect():"+s.getBounds2D().getX()+","+s.getBounds2D().getY());
            }
            jbDectAnchor2.setEnabled (false);
            jbDetect.setEnabled (false);
            }
    }

    private class btnPrevListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            slider_p1.back();
        }
    }

    private class btnPrevListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            slider_p3.back();
        }
    }

    private class btnNextListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            slider_p3.forward();
        }
    }



    private class btnNextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        
            if (currentFrame == 0 && first_flag ){
                drawDemo first_frame_primary_video = new drawDemo();
//            first_video_ori.setIcon(imageIcon);
                for (int i = 0; i < video_ori.shapes.size(); i++) {
                    first_frame_primary_video.shapes.add(video_ori.shapes.get(i));
                }
                System.out.println("-----size of first_video_ori---------:"+first_frame_primary_video.shapes);
                video_ori_map.put(0,first_frame_primary_video);
                first_flag = false;
            }

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
                jbDectAnchor.setEnabled (true);
                jb_redraw.setEnabled (true);
                if (isPrimary) loadPrimaryVideo(fileChooser.getSelectedFile().getPath());
                else loadSecondaryVideo(fileChooser.getSelectedFile().getPath());
            }
        }
    }

    private void loadPrimaryVideo(String path) {
        ImageReader reader = ImageReader.getInstance();
        primary_video = reader.FolderConfig(path);
        if (!primary_video.isEmpty()) {
            imported = true;
            slider_p1.reset(primary_video);
            video_ori.setIcon(new ImageIcon(reader.BImgFromFile(primary_video.get(0))));
            video_ori.shapes.clear();
        }

        // TODO : Reload MetaData Files
        // TODO : Reset Secondary Video Panel
    }

    private void loadSecondaryVideo(String path) {
        ImageReader reader = ImageReader.getInstance();
        secondary_video = reader.FolderConfig(path);
        if (!secondary_video.isEmpty()) {
            slider_p3.reset(secondary_video);
            video_sec.setIcon(new ImageIcon(reader.BImgFromFile(secondary_video.get(0))));
        }
    }

    public static void main(String[] agrs)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new boxDemo();    //创建一个实例化对象
            }
        });

    }


}

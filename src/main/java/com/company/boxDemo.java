package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    String primary_video_path;
    String secondary_video_path;
    String current_video_name, target_video_name;
    int linkSelected;//used to index select link

    HyperLink pendingLink;
    ArrayList<HyperLink> links = new ArrayList<>();//all links
    Map<Integer, Shape> linkShape;//when creating a link, storing shapes from start anchor to end( each frame contain one shape)
    /**
     * panel0: link list, modify link, and save file
     */
    JList linkList;
    JLabel linkLabel;
    JButton jb_save_file = new JButton("\uDBC0\uDE05 Save File");
    DefaultListModel listModel;
    JLabel status;

    JTextField boxX = new JTextField();
    JTextField boxY = new JTextField();
    JTextField boxW = new JTextField();
    JTextField boxH = new JTextField();
    JButton jb_changeX = new JButton("change X");
    JButton jb_changeY = new JButton("change Y");
    JButton jb_changeWidth  = new JButton("change Width");
    JButton jb_changeHeight  = new JButton("change Height");


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
    JButton jb_editLink = new JButton("\uDBC0\uDE0A Edit link");
    drawDemo video_ori;
    Map<Integer, drawDemo> video_ori_map;//a map of video_ori, each represents each frame, each has own shapes( box bounding info )
    Map<Integer, drawDemo> video_ori_map_sec;

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
    Slider slider_p3;
    JButton jb_next_p3 = new JButton(">");
    JPanel panel3_control_box2 = new JPanel();// import & stop
    JButton jb_import_sec = new JButton("\uDBC0\uDE05");
    JButton jb_stop_sec = new JButton("\uDBC1\uDF2A");
    JLabel video_sec;


    public boxDemo() {
        // Create and set up a frame window
//        setDefaultLookAndFeelDecorated(true);
        setSize(1200, 800);
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
        panel_box_info.setLayout(new BoxLayout(panel_box_info, BoxLayout.Y_AXIS));

        JPanel panel_links = new JPanel();
        panel_links.setLayout(new BoxLayout(panel_links, BoxLayout.Y_AXIS));

        linkLabel = new JLabel(" ");
        JLabel link_head = new JLabel("Links");
        link_head.setFont(new Font("Dialog", Font.BOLD, 16));
        link_head.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(300, 150));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel_links.add(link_head);
        panel_links.add(scrollPane);
        panel_links.add(linkLabel);

        JLabel box_head = new JLabel("Bounding Box");
        box_head.setFont(new Font("Dialog", Font.BOLD, 16));

        JPanel xPanel = new JPanel();
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.X_AXIS));
        xPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        xPanel.add(boxX);
        xPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        xPanel.add(jb_changeX);

        JPanel yPanel = new JPanel();
        yPanel.setLayout(new BoxLayout(yPanel, BoxLayout.X_AXIS));
        yPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        yPanel.add(boxY);
        yPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        yPanel.add(jb_changeY);

        JPanel wPanel = new JPanel();
        wPanel.setLayout(new BoxLayout(wPanel, BoxLayout.X_AXIS));
        wPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        wPanel.add(boxW);
        wPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        wPanel.add(jb_changeWidth);

        JPanel hPanel = new JPanel();
        hPanel.setLayout(new BoxLayout(hPanel, BoxLayout.X_AXIS));
        hPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        hPanel.add(boxH);
        hPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        hPanel.add(jb_changeHeight);

        panel_box_info.add(box_head);
        panel_box_info.add(xPanel);
        panel_box_info.add(yPanel);
        panel_box_info.add(wPanel);
        panel_box_info.add(hPanel);

//        panel_box_info.setBackground(Color.pink);
        panel_box_info.setPreferredSize(new Dimension(300, 150));

        listModel = new DefaultListModel();
//        listModel.addElement("222");
//        listModel.addElement("333");
        linkList = new JList(listModel);
        scrollPane.setViewportView(linkList);


//        ArrayList<String> listData = new ArrayList<>();
//        String[] listData=new String[20];
//        listData[0]="《一点就通学Java》";
//        listData[1]="《一点就通学PHP》";
//        listData[2]="《一点就通学Visual Basic）》";
//        listData[3]="《一点就通学Visual C++）》";
//        listData[4]="《Java编程词典》";
//        listData[5]="《PHP编程词典》";
//        listData[6]="《C++编程词典》";
//        linkList.setListData(listModel);
        linkList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                System.out.println("-------linkList valueChanged----------");
                if(linkList.getSelectedIndex() ==-1){
                    System.out.println("linkList.getSelectedIndex() ==-1");
                    JOptionPane.showMessageDialog(null,"value change(because import new video), -1");
                    linkLabel.setText("nothing");
                    return;
                }
                linkSelected = linkList.getSelectedIndex();
                linkLabel.setText("you select" + linkSelected + "," + linkList.getSelectedValue());
                if (links.size() >= linkSelected + 1) {
                    System.out.println("ori frame is:" + links.get(linkSelected).oriFrame);
                    linkLabel.setText("you select index of " + linkSelected + ", name:" + linkList.getSelectedValue() + "|| ori frame is:" + links.get(linkSelected).oriFrame+" target path: "+ links.get(linkSelected).targetPath);
                    slider_p1.jumpTo(links.get(linkSelected).oriFrame);
                    currentFrame = slider_p1.getCurrentFrame();
                } else {
                    System.out.println("valueChanged: links is empty or small");
                }
                repaint();
            }
        });
        jb_editLink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = JOptionPane.showInputDialog("Enter new name:");
                System.out.println("jb_editLink newName: " + newName);
                if(linkList.getSelectedIndex() ==-1){
                    System.out.println("linkList.getSelectedIndex() ==-1");
                    JOptionPane.showInputDialog("linkList.getSelectedIndex() ==-1");
                    linkLabel.setText("nothing.");
                    return;
                }
                if (newName != null && !newName.isEmpty() ) {
                    int select = linkSelected;
                    System.out.println("jb_editLink linkSelected: " + select);
                    listModel.set(select, newName);
//                    listModel.remove(select);
//                    listModel.add(select,newName);
                    System.out.println("jb_editLink listModel: " + listModel);
//                    linkList.setSelectedIndex(select);
                    linkSelected = select;
                    if (links.size() >= select + 1) {
                        links.get(select).linkName = newName;
                    } else {
                        System.out.println("jb_editLink links: " + links);
                    }
                    System.out.println("jb_editLink links null or small");
                } else {
                    System.out.println("jb_editLink newName null or empty");
                }
            }
        });
        jb_save_file.addActionListener(new SaveFileAnchorListener());

        // Set up the BoxLayout
        BoxLayout layout0 = new BoxLayout(panel0, BoxLayout.X_AXIS);
        panel0.setLayout(layout0);
        // Set up the title for different panels
        panel0.setBorder(BorderFactory.createTitledBorder("TOP"));
//        panel0.setPreferredSize(new Dimension(600,100));

        panel0.add(Box.createRigidArea(new Dimension(20, 0)));
        panel0.add(panel_box_info);
        panel0.add(Box.createRigidArea(new Dimension(40, 0)));
        panel0.add(panel_links);
        panel0.add(Box.createRigidArea(new Dimension(40, 0)));
        panel0.add(jb_editLink);
        panel0.add(Box.createRigidArea(new Dimension(40, 0)));
        panel0.add(jb_save_file);
        panel0.add(Box.createRigidArea(new Dimension(20, 0)));


        // Add the buttons into the panel with three different alignment options
//        panel0.add(jb0);
//        panel0.add(jb00);

        /**
         * video(to be added hyperlink) section
         */

        video_ori = new drawDemo();
        video_ori_map = new HashMap<>();
        video_ori_map_sec = new HashMap<>();

        status = new JLabel("Import a Video to Start!", JLabel.CENTER);
        slider_p1 = new Slider(status, "Value of the slider is: %d", primary_video);
        slider_p1.addChangeListener(e -> {
            System.out.println(" ");
            System.out.println("--------- slider change---------");
            currentFrame = ((JSlider)e.getSource()).getValue();
            // status.setText(String.format(format, getValue() + 1));
            if (video_ori != null) {
                BufferedImage newImage = ImageReader.getInstance().BImgFromFile(primary_video.get(currentFrame));
                drawDemo temp_video_ori = new drawDemo();
                int size = video_ori_map.size();
                if (video_ori_map.containsKey(currentFrame)){
                    System.out.println("video_ori_map contains:"+currentFrame+"'s frame"+", the map size: "+size);
                    // if(!video_ori_map.get(currentFrame).shapes.isEmpty()){
                    temp_video_ori = video_ori_map.get(currentFrame);
                    // }
                }
                video_ori.shapes = temp_video_ori.shapes;
                video_ori.setIcon(new ImageIcon(newImage));
                System.out.println("before put, video_ori_map.get(currentFrame):"+video_ori_map.get(currentFrame)+", the map size: "+video_ori_map.size());
//            System.out.println("before put, video_ori_map shapae isEmpty:"+video_ori_map.get(currentFrame).shapes.isEmpty());
//            System.out.println("before put, video_ori_map shapae:"+video_ori_map.get(currentFrame).shapes);

                video_ori_map.put(currentFrame,temp_video_ori);

                //video_ori_map.get(currentFrame).shapes is[], when first
                System.out.println("after put, video_ori_map.get(currentFrame):"+video_ori_map.get(currentFrame)+", the map size: "+video_ori_map.size());
                System.out.println("after put, video_ori_map shapae:"+video_ori_map.get(currentFrame).shapes);

                System.out.println("--------- slider change-end--------");
            }
        });

        currentFrame = slider_p1.getCurrentFrame();

        jb_prev.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_prev.addActionListener(new btnPrevListener());
        jb_next.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_next.addActionListener(new btnNextListener());

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

        jb_link.setEnabled(false);
        jbDectAnchor2.setEnabled(false);
        jbDectAnchor.setEnabled(false);
        jbDetect.setEnabled(false);
        jb_redraw.setEnabled(false);

        jbDectAnchor.addActionListener(new jbDectAnchorListener());//detect function: set first anchor
        jbDectAnchor2.addActionListener(new jbDectAnchorListener2());//detect function: set last anchor
        jbDetect.addActionListener(new jbDetectListener());//detect function: detect between two anchors

        jb_import_ori.addActionListener(new FileSelector("Select primary video", boxDemo.this,
                JFileChooser.DIRECTORIES_ONLY) {
            @Override
            void onFileSelected(File selectedFile) {
                jbDectAnchor.setEnabled(true);
                jb_redraw.setEnabled(true);
                System.out.println("-------------loadPrimaryVideo----------");
                primary_video_path = selectedFile.getPath();
                current_video_name = selectedFile.getName();
                loadPrimaryVideo(selectedFile.getPath());
            }
        });

        jb_import_sec.addActionListener(new FileSelector("Select secondary video", boxDemo.this,
                JFileChooser.DIRECTORIES_ONLY) {
            @Override
            void onFileSelected(File selectedFile) {
                System.out.println("-------------loadSecondVideo------------");
                secondary_video_path = selectedFile.getPath();
                target_video_name = selectedFile.getName();
                loadSecondaryVideo(selectedFile.getPath());
            }
        });

        jb_redraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Shape s : video_ori.shapes) {
                    System.out.println("clear():" + s.getBounds());
                }
                if (!video_ori.shapes.isEmpty()){
                    video_ori.shapes.remove(video_ori.shapes.size()-1);
                }
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
        jb_link.addActionListener(new linkAnchorListener());
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
        slider_p3 = new Slider(status2, "Value of the slider is: %d", secondary_video);
        slider_p3.addChangeListener(e -> {
            currentFrame = ((JSlider)e.getSource()).getValue();
            if (video_sec != null) {
                BufferedImage newImage = ImageReader.getInstance().BImgFromFile(secondary_video.get(currentFrame));
                video_sec.setIcon(new ImageIcon(newImage));
            }
        });
        currentFrame_sec = slider_p3.getCurrentFrame();

        jb_prev_p3.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_prev_p3.addActionListener(new btnPrevListener3());
        jb_next_p3.setFont(new Font("Dialog", Font.PLAIN, 20));
        jb_next_p3.addActionListener(new btnNextListener3());

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

        Container container = getContentPane();    //获取当前窗口的内容窗格
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(Box.createRigidArea(new Dimension(100, 0)));
        container.add(panel0);
        container.add(Box.createRigidArea(new Dimension(100, 0)));
        container.add(group);
        setLayout(new FlowLayout());

        // Set the window to be visible as the default to be false
//        pack();
        setVisible(true);
    }

    private class linkAnchorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("------------linkAnchorListen()----++---------");

            currentFrame = slider_p1.getCurrentFrame();
            currentFrame_sec = slider_p3.getCurrentFrame();
            pendingLink.linkName = "link" + current_video_name + links.size();
            pendingLink.targetFrame = currentFrame_sec;
            pendingLink.targetPath = secondary_video_path;
            pendingLink.targetJsonPath = "/Users/yze/" + target_video_name + ".json";//?

            System.out.println("linkAnchorListen() link name:"+pendingLink.linkName);
//            pendingLink.oriFrame = currentFrame;
            System.out.println("linkAnchorListen() pendingLink.targetPath:"+pendingLink.targetPath);
            System.out.println("linkAnchorListen() targetFrame:" + pendingLink.targetFrame);
            System.out.println("linkAnchorListen(): currentFrame_sec:" + currentFrame_sec);

            links.add(pendingLink);

            System.out.println("linkAnchorListen() pendingLink:" + pendingLink.toString());
            System.out.println("linkAnchorListen() links:" + links.toString());
            listModel.addElement(pendingLink.linkName);
            pendingLink = new HyperLink();//clear pending link
            System.out.println("------------linkAnchorListen()-------------");
//            link.links = links; // assign this.links to HyperLink's links
//            link.addLinkToLinks();
        }
    }

    private class SaveFileAnchorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            // Java objects to String
            String json = gson.toJson(links);

//            System.out.println(json);
//            for(HyperLink link : links){
//                System.out.println("links key:"+link.linkShape.keySet());
//            }
            // Java objects to File
            try (
                    FileWriter writer = new FileWriter("/Users/yze/" + current_video_name + ".json")) {
                gson.toJson(links, writer);
                JOptionPane.showMessageDialog(null,"meta data saved: /Users/yze/" + current_video_name + ".json");
            } catch (
                    IOException ee) {
                ee.printStackTrace();
            }
        }
    }

    private class jbDectAnchorListener implements ActionListener {//first anchor set
        @Override
        public void actionPerformed(ActionEvent e) {
            currentFrame = slider_p1.getCurrentFrame();
            detectAnchorIndex = currentFrame;
            System.out.println("------jbDectAnchor()--------");
            System.out.println("jbDectAnchor(): detectAnchorIndex is: " + detectAnchorIndex);
            System.out.println("jbDectAnchor(): video_ori_map:" + video_ori_map.size());
            System.out.println("jbDectAnchor(): video_ori_map[" + detectAnchorIndex + "]:" + video_ori_map.get(detectAnchorIndex));
            System.out.println("jbDectAnchor(): video_ori_map[" + detectAnchorIndex + "].shapes:" + video_ori_map.get(detectAnchorIndex).shapes);

            jbDectAnchor2.setEnabled(true);
            System.out.println("------jbDectAnchor() end--------");
        }
    }

    private class jbDectAnchorListener2 implements ActionListener {//last anchor set
        @Override
        public void actionPerformed(ActionEvent e) {
            currentFrame = slider_p1.getCurrentFrame();
            detectAnchorIndex2 = currentFrame;
            System.out.println("jbDectAnchor2(): detectAnchorIndex2 is: " + detectAnchorIndex2);
            System.out.println("jbDectAnchor2(): video_ori_map size: " + video_ori_map.size());
            System.out.println("jbDectAnchor2(): video_ori_map size: " + video_ori_map.get(detectAnchorIndex2));
            jbDetect.setEnabled(true);
        }
    }

    private class jbDetectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int firstFrame = detectAnchorIndex;
            int lastFrame = detectAnchorIndex2;
            // int index = 0;
            System.out.println("frame detect(): video_ori_map currentFrame:" + video_ori_map.get(currentFrame));
            System.out.println("frame detect(): video_ori_map: lastFrame" + video_ori_map.get(lastFrame));
            System.out.println("frame detect(): video_ori_map:" + video_ori_map.size());
            int index = video_ori_map.get(firstFrame).shapes.size() - 1; // last shape in shape-list
            int index2 = video_ori_map.get(lastFrame).shapes.size() - 1; // last shape in shape-list
            System.out.println("frame detect(): first frame:" + firstFrame +", last frame: "+ lastFrame + ", index:" + index);

            /**
             * xFirst: top left x of first frame
             * xLast: top left x of last frame
             */
            double xFirst = video_ori_map.get(firstFrame).shapes.get(index).getBounds2D().getX();
            double xLast = video_ori_map.get(lastFrame).shapes.get(index2).getBounds2D().getX();
            double yFirst = video_ori_map.get(firstFrame).shapes.get(index).getBounds2D().getY();
            double yLast = video_ori_map.get(lastFrame).shapes.get(index2).getBounds2D().getY();
            double firstWidth = video_ori_map.get(firstFrame).shapes.get(index).getBounds2D().getWidth();
            double firstHeight = video_ori_map.get(firstFrame).shapes.get(index).getBounds2D().getHeight();
            double lastWidth = video_ori_map.get(lastFrame).shapes.get(index2).getBounds2D().getWidth();
            double lastHeight = video_ori_map.get(lastFrame).shapes.get(index2).getBounds2D().getHeight();

            System.out.println("frame detect(): xFirst:" + xFirst + ",xLast:" + xLast);
            System.out.println("frame detect(): yFirst:" + yFirst + ",yLast:" + yLast);
            // System.out.println("frame detect(): firstWidth:"+firstWidth+",firstHeight:"+firstHeight);
            // System.out.println("frame detect(): lastWidth:"+lastWidth+",lastHeight:"+lastHeight);

            double xChange = (xLast - xFirst) / (lastFrame - firstFrame);
            double yChange = (yLast - yFirst) / (lastFrame - firstFrame);
            double widthChange = (lastWidth - firstWidth) / (lastFrame - firstFrame);
            double heightChange = (lastHeight - firstHeight) / (lastFrame - firstFrame);
            System.out.println("frame detect(): xChange:" + xChange + ",yChange:" + yChange);

            /***
             * following para could be null, recheck in linkActionListen()
             */
            String linkName = "testlink";
            String oriPath = primary_video_path;
            String targetPath = secondary_video_path;
            int targetFrame = currentFrame_sec;// change
            int oriFrame = detectAnchorIndex;
            System.out.println("frame detect(): currentFrame_sec:" + currentFrame_sec);

            Map<Integer, Shape> temLinkShape = new HashMap<>();
            pendingLink = new HyperLink(linkName, oriPath, targetPath, targetFrame, oriFrame, temLinkShape, Color.PINK.getRGB());
            pendingLink.linkShape = new HashMap<>();
            for (int i = firstFrame + 1; i < lastFrame; i++) {
                drawDemo tempItem = new drawDemo();
                int x1 = (int) (xFirst + xChange * (i - firstFrame));
                int y1 = (int) (yFirst + yChange * (i - firstFrame));
                int x2 = (int) (x1 + firstWidth + widthChange * (i - firstFrame));
                int y2 = (int) (y1 + firstHeight + heightChange * (i - firstFrame));

                Shape temShape = new Rectangle2D.Float(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
                Shape temShape2 = new Rectangle2D.Float(x1, y1, Math.abs(x1 - x2), Math.abs(y1 - y2));
                // Shape temShape = new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
                System.out.println("i:" + i + ",frame detect(): temShape:" + temShape);

                if (video_ori_map.get(i) == null) {
                    System.out.println("i:" + i + ",frame detect(): video_ori_map null");
                    video_ori_map.put(i, new drawDemo());
                }
                System.out.println("i:" + i + ",frame detect(): video_ori_map.get(i).shapes:" + video_ori_map.get(i).shapes);
                if (!video_ori_map.get(i).shapes.isEmpty()) {
                    tempItem.shapes = video_ori_map.get(i).shapes;
                }
                tempItem.shapes.add(temShape);
                video_ori_map.put(i, tempItem);

                System.out.println("---------frame detect(): put frame i:" + i + ",into pendingLink,temShape.toString():" + temShape.toString());
                pendingLink.linkShape.put(i, temShape);

//                linkShape.put(i,temShape);
            }
            for (Shape s : video_ori_map.get(firstFrame).shapes) {
                System.out.println("frame detect():" + s.getBounds2D().getX() + "," + s.getBounds2D().getY());
            }
            jbDectAnchor2.setEnabled(false);
            jbDetect.setEnabled(false);
        }
    }


    private class btnPrevListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            slider_p3.back();
            currentFrame_sec = slider_p3.getCurrentFrame();
        }
    }

    private class btnNextListener3 implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            slider_p3.forward();
            currentFrame_sec = slider_p3.getCurrentFrame();
        }
    }

    private class btnPrevListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            slider_p1.back();
            currentFrame = slider_p1.getCurrentFrame();
        }
    }

    private class btnNextListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (currentFrame == 0 && first_flag) {
                drawDemo first_frame_primary_video = new drawDemo();
//            first_video_ori.setIcon(imageIcon);
                for (int i = 0; i < video_ori.shapes.size(); i++) {
                    first_frame_primary_video.shapes.add(video_ori.shapes.get(i));
                }
                System.out.println("-----size of first_video_ori---------:" + first_frame_primary_video.shapes);
                video_ori_map.put(0, first_frame_primary_video);
                first_flag = false;
            }

            slider_p1.forward();
            currentFrame = slider_p1.getCurrentFrame();
            System.out.println("btnNext() currentFrame is:" + currentFrame);
        }
    }

    private void loadPrimaryVideo(String path) {
        ImageReader reader = ImageReader.getInstance();
        primary_video = reader.FolderConfig(path);
        if (!primary_video.isEmpty()) {
            imported = true;
            slider_p1.reset(primary_video);
            video_ori.shapes.clear();

            pendingLink = new HyperLink();
            links = new ArrayList<>();
            listModel.clear();
            first_flag = true;

            System.out.println("loadPrimaryVideo b: " + video_ori_map.toString()+", size"+video_ori_map.size());
            video_ori_map.clear();
            video_ori_map.put(0,new drawDemo());

//            slider_p1 = new Slider(status, "Value of the slider is: %d", primary_video, video_ori_map);
//            slider_p1.setCanvas(video_ori);
            currentFrame = slider_p1.getCurrentFrame();

            System.out.println("loadPrimaryVideo a: " + video_ori_map.toString()+", size"+video_ori_map.size());
        }

        // TODO : Reload MetaData Files
        // TODO : Reset Secondary Video Panel
    }

    private void loadSecondaryVideo(String path) {
        ImageReader reader = ImageReader.getInstance();
        secondary_video = reader.FolderConfig(path);
        if (!secondary_video.isEmpty()) {
            slider_p3.reset(secondary_video);
            jb_link.setEnabled(true);
        }
    }

    public void exportFile(int startFrame, int endFrame) {
        Map<Integer, ArrayList<Shape>> shapeMap = new HashMap<>();

        for (int i = 0; i < video_ori_map.size(); i++) {
            shapeMap.put(i, video_ori_map.get(i).shapes);

//            for(int j=0; j < video_ori_map.get(i).shapes.size(); j++){
//                double x1 = video_ori_map.get(i).shapes.get(j).getBounds2D().getX();
//                double y1 = video_ori_map.get(i).shapes.get(j).getBounds2D().getY();
//                double x2 = x1+video_ori_map.get(i).shapes.get(j).getBounds2D().getWidth();
//                double y2 = y1+video_ori_map.get(i).shapes.get(j).getBounds2D().getHeight();
//            }
        }
    }

    public static void main(String[] agrs) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new boxDemo();    //创建一个实例化对象
            }
        });

    }


}

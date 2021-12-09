package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player extends JFrame {

    AbstractPlayer<BufferedInputStream> audioPlayer;

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
    Slider slider_p1;
    JButton jb_next = new JButton(">");
    JPanel panel1_control_box2 = new JPanel();// import & play & stop
    JButton jb_import_ori = new JButton("\uDBC0\uDE05 Import");
    JButton jb_play = new JButton("\uDBC0\uDE95 Play");
    JButton jb_stop = new JButton("\uDBC2\uDE86 Stop");
    ClickablePanel video_area;//image area to display
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
        video_area = new ClickablePanel(this);
        video_area.setPreferredSize(new Dimension(352, 288));
        video_ori_map = new HashMap<>();
        video_ori_map_sec = new HashMap<>();

        JLabel status = new JLabel("Import a Video to Start!", JLabel.CENTER);
        slider_p1 = new Slider(status, "Value of the slider is: %d", primary_video);
        slider_p1.setPreferredSize(new Dimension(380,20));
        slider_p1.addChangeListener(e -> {
            currentFrame = ((JSlider)e.getSource()).getValue();
            if (video_area != null) {
                BufferedImage newImage = ImageReader.getInstance().BImgFromFile(primary_video.get(currentFrame));
                video_area.setIcon(new ImageIcon(newImage));
            }
        });
        slider_p1.setManualChangeListener(() -> {
            audioPlayer.peek(frameToMilsec());
            timeStamp = System.currentTimeMillis();
        });

        currentFrame = slider_p1.getCurrentFrame();

        audioPlayer = new WavePlayer(null);    // create a new WavePlayer without self-control

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
        jb_import_ori.addActionListener(new FileSelector("Select primary video", Player.this,
                JFileChooser.FILES_ONLY) {
            @Override
            void onFileSelected(File selectedFile) {
                String jsonPath = selectedFile.getPath();
                String imgPath = getImgPath(jsonPath,0);//get oriPath, index doesn't matter
                loadPrimaryVideo(imgPath,jsonPath);
            }
        });

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

    public JButton getJb_play(){

        return this.jb_play;
    }


    /**
     * ActionListener
     */
    private class btnPlayListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            videoPlayed = true;

            currentFrame = slider_p1.getCurrentFrame();
            System.out.println("btnPlayListener currentFrame:"+currentFrame+"slider_p1.getValue():"+slider_p1.getValue());

            timeStamp = System.currentTimeMillis();

            video_thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO : Reload MetaData Files
                    System.out.println("new thread before while:"+Thread.currentThread().getName());
                    audioPlayer.peek(frameToMilsec());
                    audioPlayer.play();
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
        return (currentFrame * 33L) + ((long)(currentFrame / 3) + 1);
    }

    private class btnStopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            currentFrame = slider_p1.getCurrentFrame();
            videoPlayed = false;
            audioPlayer.pause();

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
            if (slider_p1.getValue() > slider_p1.getMinimum()) {
                slider_p1.back();
                audioPlayer.peek(frameToMilsec());
                timeStamp = System.currentTimeMillis();
            }
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
            if (slider_p1.getValue() < slider_p1.getMaximum()) {
                slider_p1.forward();
                audioPlayer.peek(frameToMilsec());
                timeStamp = System.currentTimeMillis();
            }
//            currentFrame = slider_p1.getCurrentFrame();
//            System.out.println("btnNext() currentFrame is:"+currentFrame);
        }
    }

    public String getImgPath(String jsonPath, int linkIndex) {
        GasonRead read = new GasonRead(jsonPath);
        String imgPath = read.getLinkList()[linkIndex].oriPath;
        return imgPath;
    }

    public void loadPrimaryVideo(String imgPath,String jsonPath) {
        ImageReader reader = ImageReader.getInstance();
        primary_video = reader.FolderConfig(imgPath);
        if (!primary_video.isEmpty()) {
            slider_p1.reset(primary_video);

            GasonRead read = new GasonRead(jsonPath);
            System.out.println("reaad:" + read.toString());
            if (read.getLinkList() == null){
                System.out.println("read.getLinkList() == null");
                System.out.println("last video, doesn't contain hyperlink ");
                video_area.shapeListMap = new HashMap<>();
                for (int j = 0; j < 8999; j++){//initialization
                    video_area.shapeListMap.put(j,new ArrayList<>());
                }
                return;
            }
//            ArrayList<Map<Integer,Shape>> linkShapeList = new ArrayList<>();
            Map<Integer,ArrayList<Rectangle>> shapeListMap = new HashMap<>();// each frame has list of shapes, used to draw more than one box in a frame
            for (int j = 0; j < 8999; j++){//initialization
                shapeListMap.put(j,new ArrayList<>());
            }
//            ArrayList<Rectangle> shapeList = new ArrayList<>();
//            Map<Integer,Rectangle> recMap = new HashMap<>();

            /**
             * links: ClickablePanel's links, used to create hyperlink
             * targetPathList: ClickablePanel's links, used to create hyperlink ...
             */
            ArrayList<HyperLink> links = new ArrayList<>();
            ArrayList<String> targetPathList = new ArrayList<>();
            ArrayList<String> targetJsonPathList = new ArrayList<>();
            ArrayList<Integer> targetFrameList = new ArrayList<>();
            Map<Integer, ArrayList<Integer>> frameColorsMap = new HashMap<>();

            for (int i = 0; i < read.getLinkList().length; i++){ //loop all link in links, i: link's index
                System.out.println("begin to read link"+i+"'s data");

                String linkName =  read.getLinkList()[i].linkName;
                String oriPath =  read.getLinkList()[i].oriPath;
                int oriFrame = read.getLinkList()[i].oriFrame;
                int targetFrame = read.getLinkList()[i].targetFrame;
                String targetPath = read.getLinkList()[i].targetPath;
                String targetJsonPath = read.getLinkList()[i].targetJsonPath;
                int linkColor = read.getLinkList()[i].linkColor;
//                HyperLink link = read.getLinkList()[];
//                HyperLink(String linkName, String oriPath, String targetPath ,int targetFrame,int oriFrame,Map<Integer,Shape> linkShape, int linkColor ){

                targetJsonPathList.add(targetJsonPath);
                targetPathList.add(targetPath);
                targetFrameList.add(targetFrame);

//                String linkName = read.getLinkList()[i].linkName;
//                ArrayList<HyperLink> frameLinkList = new ArrayList<>();


                Map<Integer, Rectangle> linkShape = read.getLinkList()[i].linkShape;//link i's shape in each frame
                if (linkShape == null){
                    JOptionPane.showMessageDialog(null,"no linkShape data");
                }
                else {
                    Map<Integer,Shape> linkShape_s = new HashMap<>();//used to create hyperlink in ClickablePanel

                    for( int j : linkShape.keySet()){//loop all shape in a link, j: shape's index and frame
                        ArrayList<Integer> colors = new ArrayList<>();
                        if (frameColorsMap.get(j)!=null){
                            colors = frameColorsMap.get(j);
                        }
                        colors.add(linkColor);
                        frameColorsMap.put(j,colors);

                        Rectangle rec = linkShape.get(j);
                        linkShape_s.put(j,rec);
//                    System.out.println("put rec into linkShape_s, frame index = "+j+",linkShape_s.get("+j+"):"+linkShape_s.get(j));
                        shapeListMap.get(j).add(rec);
                    }
                    System.out.println("after put rec into each linkShape_s and shapeListMap loop, linkShape_s.size()"+ linkShape_s.size());
                    HyperLink link = new HyperLink(linkName,oriPath,targetPath,targetFrame,oriFrame,linkShape_s,linkColor);
                    link.targetJsonPath = targetJsonPath;
                    links.add(link);
                }
            }

            video_area.frameColorsMap = frameColorsMap;
            video_area.links = links;
            video_area.shapeListMap = shapeListMap;// each frame has list of shapes, used to draw more than one box in a frame
            video_area.targetFrameList = targetFrameList;
            video_area.targetJsonPathList = targetJsonPathList;
            video_area.targetPathList = targetPathList;
//            int fRangeStart = 100;
//            int fRangeEnd = 200;
//            video_area.link = loadLinkData(fRangeStart,fRangeEnd);
//            video_area.loadLink(fRangeStart,fRangeEnd);//fake link
//            video_ori.shapes.clear();
            jb_play.setEnabled (true);
//            jb_stop.setEnabled (true);

            audioPlayer.open(reader.BWavFromFile(imgPath));
            if (imported) {
                audioPlayer.play();
            }
            imported = true;
        }

        // TODO : Reload MetaData Files
    }

//    public HyperLink loadLinkData(int startFrame, int endFrame){
//        HyperLink link;
//
//
//        // a link has one box in a frame
//        Map<Integer,Shape> linkShape = new HashMap<>();// this should read from file saved by authoring, manual create here
//        for(int i=50; i < 150; i++){
//            int testOffset = (i-startFrame);
//            linkShape.put(100,video_area.makeRectangle(50+testOffset,50+testOffset,180+testOffset,190+testOffset));
//        }
//
//        Map<Integer,Shape> linkShape2 = new HashMap<>();// this should read from file saved by authoring, manual create here
//        for(int i=100; i < 250; i++){
//            int testOffset = (i-startFrame);
//            linkShape.put(100,video_area.makeRectangle(50+testOffset,50+testOffset,180+testOffset,190+testOffset));
//        }
//        ArrayList<Map<Integer,Shape>> linkShapeList = new ArrayList<>();
//        linkShapeList.add(linkShape);
//        linkShapeList.add(linkShape2);
//
//        Map<Integer,ArrayList<Shape>> shapeMap = new HashMap<>();
//        for(int i=0; i < 8999; i++){
//            for(int j=0; j < linkShapeList.size(); j++){
//                if (linkShapeList.get(j).get(i)!=null){
//
//                }
//            }
//
//            ArrayList<Shape> shapes = new ArrayList<>();
//            if (i>=startFrame && i<=startFrame+endFrame){
//                int testOffset = (i-startFrame);
//                shapes =
////                        .add(makeRectangle(50+testOffset,50+testOffset,180+testOffset,190+testOffset));
//                shapeMap.put(i,shapes);
//            }
//        }
//        System.out.println("loadLink():"+shapeMap.size());
//        link = new HyperLink("testlink","/Users/yze/USCOne","/Users/yze/USCTwo",200,shapeMap,Color.green);
//        return link;
//    }

    public static void main(String[] agrs)
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Player();    //创建一个实例化对象
            }
        });

    }


}

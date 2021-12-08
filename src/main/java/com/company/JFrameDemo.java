package com.company;

import javax.swing.*;
import java.awt.*;

public class JFrameDemo extends JFrame {
    public JFrameDemo()
    {
        String[] items=new String[]{"link1","link3","link4","link1","link3","link4"};

        setTitle("TITLE");    //设置显示窗口标题
        setSize(1000,600);    //设置窗口显示尺寸
//        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //置窗口是否可以关闭

        JPanel panel0 = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();

        panel0.setBorder(BorderFactory.createTitledBorder("TOP"));
        panel1.setBorder(BorderFactory.createTitledBorder("LEFT"));
        panel2.setBorder(BorderFactory.createTitledBorder("CENTER"));
        panel3.setBorder(BorderFactory.createTitledBorder("RIGHT"));

        BoxLayout layout1 = new BoxLayout(panel1, BoxLayout.Y_AXIS);
        BoxLayout layout2 = new BoxLayout(panel2, BoxLayout.Y_AXIS);
        BoxLayout layout3 = new BoxLayout(panel3, BoxLayout.Y_AXIS);
        panel1.setLayout(layout1);
        panel2.setLayout(layout2);
        panel3.setLayout(layout3);


        JLabel jl=new JLabel("这是使用JFrame类创建的窗口");    //创建一个标签
        JLabel jl2=new JLabel("");
        jl2.setText("settext");
        JLabel jl3=new JLabel("Link List");
        JLabel jl4=new JLabel("这是使用JFrame类创建的2窗口");

        ImageIcon img=new ImageIcon("twitter.png");    //创建一个图标
        //创建既含有文本又含有图标的JLabel对象
        JLabel jl5=new JLabel("开始理财",img,JLabel.CENTER);

        JButton btn1=new JButton("我是普通按钮");    //创建JButton对象

        JPanel jp=new JPanel();//创建一个JPanel对象
        jp.setLayout(new FlowLayout(FlowLayout.LEADING,20,20));
        jp.setBackground(Color.pink);    //设置背景色


        JList list=new JList(items);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane=new JScrollPane(list);
        scrollPane.setViewportView(list);    //在滚动面板中显示列表
//        list.setListData(items);    //为列表填充数据
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        Container container=getContentPane();    //获取当前窗口的内容窗格
        jp.add(btn1);
        jp.add(jl);
        jp.add(jl2);
        jp.add(list);
        jp.add(jl3);
        jp.add(jl4);
        jp.add(jl5);
        jp.add(scrollPane);
        container.add(jp);
//        container.add(jp2);

        setVisible(true);    //设置窗口是否可见
    }
    public static void main(String[] agrs)
    {
        new JFrameDemo();    //创建一个实例化对象
    }
}

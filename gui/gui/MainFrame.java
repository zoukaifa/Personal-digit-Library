/*
 * Lyz is a big Dolby.
 */
package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * JFrame,各个JPanel的容器
 *
 * @author 开发
 */
public class MainFrame extends JFrame {

    private JPanel settingPanel;
    private JPanel searchPanel;
    private JPanel bookshelfPanel;
    private JPanel indexPanel;
    private JPanel calenderHint;
    private String[] fontStyle;
    JPanel mainJPanel = new JPanel();  //主JPanel
    CardLayout cl = new CardLayout();

    public MainFrame() {
        calenderHint = new JPanel();
        calenderHint.setBackground(Color.GREEN);
        calenderHint.setBounds(300, 750, 450, 200);
        cl.last(mainJPanel);
        //cl.show(calenderHint.getParent(), "calender");
        this.setIconImage(this.getToolkit().getImage("gui/source/digital_library.png"));  //logo
        this.setContentPane(mainJPanel);  //放置
        this.setFocusTraversalPolicyProvider(false);  //暂时没用,似乎可以取消tab键控制焦点
        this.setBounds(200, 50, 950, 650);
        this.setResizable(false);  //大小不可变
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
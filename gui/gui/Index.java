/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author 开发
 */
public class Index extends JPanel {

    private JButton search, shelf, about, set;
    private Calendar calendar;

    public Index() {
        super();
        this.setLayout(null);
        search = new JButton(new ImageIcon(SetUp.imageForSearchButton));
        shelf = new JButton(new ImageIcon(SetUp.imageForShelfButton));
        about = new JButton(new ImageIcon(SetUp.imageForAboutButton));
        set = new JButton(new ImageIcon(SetUp.imageForSetButton));
        calendar = new Calendar();

        search.setBounds(263, 348, 54, 28);
        search.setBorder(null);
        shelf.setBounds(140, 215, 56, 28);
        shelf.setBorder(null);
        set.setBounds(390, 215, 54, 27);
        set.setBorder(null);
        about.setBounds(518, 348, 49, 26);
        about.setBorder(null);
        calendar.setBounds(642, 178, 200, 330);

        add(search);
        add(shelf);
        add(set);
        add(about);
        add(calendar);

        search.addMouseListener(new CursorListener());
        shelf.addMouseListener(new CursorListener());
        set.addMouseListener(new CursorListener());
        about.addMouseListener(new CursorListener());
        search.addActionListener((ActionEvent e) -> {
            MainFrame.cl.show(MainFrame.changeJPanel, "search");  //这个search单词不要改。
        });
        shelf.addActionListener((ActionEvent e) -> {
            MainFrame.cl.show(MainFrame.changeJPanel, "shelf");
        });
        set.addActionListener((ActionEvent e) -> {
            MainFrame.cl.show(MainFrame.changeJPanel, "set");
        });
        about.addActionListener((ActionEvent e) -> {
            MainFrame.cl.show(MainFrame.changeJPanel, "about");
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon img = new ImageIcon(SetUp.imageForIndexBackground);
        img.paintIcon(this, g, 0, 0);
    }

    public void imageRepaint() {
        this.repaint();
        search.setIcon(new ImageIcon(SetUp.imageForSearchButton));
        set.setIcon(new ImageIcon(SetUp.imageForSetButton));
        shelf.setIcon(new ImageIcon(SetUp.imageForShelfButton));
        about.setIcon(new ImageIcon(SetUp.imageForAboutButton));
    }

    class CursorListener extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
/*=============================================================================================
Game rules:
1. Show game rules of this game.
2. Show each role's skills and rules for counting the final scores.
=============================================================================================*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class GameRule extends JFrame{
    public GameRule()
    {
        Data.rulestate=1;

        Container container = this.getContentPane();
        this.setSize(700,700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle("GoatWolfChess");
        container.setBackground(Color.WHITE);

        // back button
        JButton button1 = new JButton("Back");
        button1.setBounds(270,580,160,70);
        button1.setBackground(new Color(200,50,200));
        button1.setForeground(Color.WHITE);
        button1.setOpaque(true);
        button1.setBorderPainted(false);
        button1.setFont(new Font("宋体",Font.BOLD,20));
        button1.setFocusPainted(false);
        this.add(button1);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomePage();
                dispose();
            }
        });
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                button1.setBackground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                button1.setBackground(new Color(200,50,200));
            }
        });

        // get 15 pictures from the data center
        JLabel label = new JLabel();
        label.setBounds(5,100,690,410);
        for(int i=1;i<=15;i++)
        {
            URL url = GameRule.class.getResource("statics/picture"+i+".jpg");
            Data.Picture[i] = new ImageIcon(url);
            Data.Picture[i].setImage(Data.Picture[i].getImage().getScaledInstance(690,410, Image.SCALE_DEFAULT));
        }
        label.setIcon(Data.Picture[1]);
        this.add(label);

        // last page button
        JButton button2 = new JButton("Last Page");
        button2.setBounds(20,580,160,70);
        button2.setBackground(new Color(200,50,200));
        button2.setForeground(Color.WHITE);
        button2.setOpaque(true);
        button2.setBorderPainted(false);
        button2.setFont(new Font("宋体",Font.BOLD,20));
        button2.setFocusPainted(false);
        this.add(button2);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               Data.rulestate--;
               if(Data.rulestate==0) Data.rulestate=15;
               label.setIcon(Data.Picture[Data.rulestate]);
            }
        });
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                button2.setBackground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                button2.setBackground(new Color(200,50,200));
            }
        });

        // next page button
        JButton button3 = new JButton("Next Page");
        button3.setBounds(520,580,160,70);
        button3.setBackground(new Color(200,50,200));
        button3.setForeground(Color.WHITE);
        button3.setOpaque(true);
        button3.setBorderPainted(false);
        button3.setFont(new Font("宋体",Font.BOLD,20));
        button3.setFocusPainted(false);
        this.add(button3);
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.rulestate++;
                if(Data.rulestate==16) Data.rulestate=1;
                label.setIcon(Data.Picture[Data.rulestate]);
            }
        });
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                button3.setBackground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                button3.setBackground(new Color(200,50,200));
            }
        });

        this.setVisible(true);
    }
}

/*=============================================================================================
Role Choose:
Two players can choose roles simultaneously.
Player1 can press [A] and [D] on the keyboard to choose roles.
Player2 can press [->] and [<-] on the keyboard to choose roles.
=============================================================================================*/

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class RoleChoose extends JFrame{
    public RoleChoose()
    {
        Container container = this.getContentPane();
        this.setSize(700,700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle("GoatWolfChess");
        container.setBackground(Color.WHITE);

        // player1 label
        JLabel player1_label = new JLabel("Player 1");
        player1_label.setBounds(115,110,100,100);
        player1_label.setForeground(Color.RED);
        player1_label.setFont(new Font("Arial",Font.BOLD,25));
        this.add(player1_label);

        // player2 label
        JLabel player2_label = new JLabel("Player 2");
        player2_label.setBounds(475,110,100,100);
        player2_label.setForeground(Color.RED);
        player2_label.setFont(new Font("Arial",Font.BOLD,25));
        this.add(player2_label);

        // show the icon of player1's role
        JLabel player1_icon_label = new JLabel();
        player1_icon_label.setBounds(30,200,280,280);
        Data.player1_icon=Data.getIcon(Data.player1);
        Data.player1_icon.setImage(Data.player1_icon.getImage().getScaledInstance(280,280,Image.SCALE_DEFAULT));
        player1_icon_label.setIcon(Data.player1_icon);
        this.add(player1_icon_label);

        // show the icon of player2's role
        JLabel player2_icon_label = new JLabel();
        player2_icon_label.setBounds(390,200,280,280);
        Data.player2_icon=Data.getIcon(Data.player2);
        Data.player2_icon.setImage(Data.player2_icon.getImage().getScaledInstance(280,280,Image.SCALE_DEFAULT));
        player2_icon_label.setIcon(Data.player2_icon);
        this.add(player2_icon_label);

        // leftarray1 button
        JButton leftarray1_button = new JButton();
        leftarray1_button.setFocusable(false);
        leftarray1_button.setBounds(50,480,80,50);
        Data.leftarray.setImage(Data.leftarray.getImage().getScaledInstance(80,50,Image.SCALE_DEFAULT));
        leftarray1_button.setIcon(Data.leftarray);
        this.add(leftarray1_button);
        leftarray1_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.player1=(Data.player1-1)%10;
                if(Data.player1==0) Data.player1=10;
                new RoleChoose();
                dispose();
            }
        });

        // rightarray1 button
        JButton rightarray1_button = new JButton();
        rightarray1_button.setFocusable(false);
        rightarray1_button.setBounds(180,480,80,50);
        Data.rightarray.setImage(Data.rightarray.getImage().getScaledInstance(80,50,Image.SCALE_DEFAULT));
        rightarray1_button.setIcon(Data.rightarray);
        this.add(rightarray1_button);
        rightarray1_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.player1=Data.player1%10+1;
                new RoleChoose();
                dispose();
            }
        });

        // leftarray2 button
        JButton leftarray2_button = new JButton();
        leftarray2_button.setFocusable(false);
        leftarray2_button.setBounds(420,480,80,50);
        Data.leftarray.setImage(Data.leftarray.getImage().getScaledInstance(80,50,Image.SCALE_DEFAULT));
        leftarray2_button.setIcon(Data.leftarray);
        this.add(leftarray2_button);
        leftarray2_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.player2=(Data.player2-1)%10;
                if(Data.player2==0) Data.player2=10;
                new RoleChoose();
                dispose();
            }
        });

        // rightarray2 button
        JButton rightarray2_button = new JButton();
        rightarray2_button.setFocusable(false);
        rightarray2_button.setBounds(550,480,80,50);
        Data.rightarray.setImage(Data.rightarray.getImage().getScaledInstance(80,50,Image.SCALE_DEFAULT));
        rightarray2_button.setIcon(Data.rightarray);
        this.add(rightarray2_button);
        rightarray2_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.player2=Data.player2%10+1;
                new RoleChoose();
                dispose();
            }
        });

        // back button
        JButton back_button = new JButton("Back");
        back_button.setFocusable(false);
        back_button.setBounds(50,580,200,70);
        back_button.setBackground(new Color(200,50,200));
        back_button.setForeground(Color.WHITE);
        back_button.setOpaque(true);
        back_button.setBorderPainted(false);
        back_button.setFont(new Font("宋体",Font.BOLD,20));
        back_button.setFocusPainted(false);
        this.add(back_button);
        back_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HomePage();
                dispose();
            }
        });
        back_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                back_button.setBackground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                back_button.setBackground(new Color(200,50,200));
            }
        });

        // start game button
        JButton start_button = new JButton("Start Game");
        start_button.setFocusable(false);
        start_button.setBounds(430,580,200,70);
        start_button.setBackground(new Color(200,50,200));
        start_button.setForeground(Color.WHITE);
        start_button.setOpaque(true);
        start_button.setBorderPainted(false);
        start_button.setFont(new Font("宋体",Font.BOLD,20));
        start_button.setFocusPainted(false);
        this.add(start_button);
        start_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.gamestate=1;
                Data.clip.close();
                try {
                    Data.stream = AudioSystem.getAudioInputStream(new File("src/Music/music2.wav").getAbsoluteFile());
                } catch (UnsupportedAudioFileException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    Data.clip.open(Data.stream);
                } catch (LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Data.clip.loop(Data.clip.LOOP_CONTINUOUSLY);
                new GamePlay();
                dispose();
            }
        });
        start_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                start_button.setBackground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                start_button.setBackground(new Color(200,50,200));
            }
        });


        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode()==KeyEvent.VK_A)
                {
                    Data.player1=(Data.player1-1)%10;
                    if(Data.player1==0) Data.player1=10;
                    Data.player1_icon=Data.getIcon(Data.player1);
                    Data.player1_icon.setImage(Data.player1_icon.getImage().getScaledInstance(280,280,Image.SCALE_DEFAULT));
                    player1_icon_label.setIcon(Data.player1_icon);
                }
                if(e.getKeyCode()==KeyEvent.VK_D)
                {
                    Data.player1=Data.player1%10+1;
                    if(Data.player1==0) Data.player1=10;
                    Data.player1_icon=Data.getIcon(Data.player1);
                    Data.player1_icon.setImage(Data.player1_icon.getImage().getScaledInstance(280,280,Image.SCALE_DEFAULT));
                    player1_icon_label.setIcon(Data.player1_icon);
                }
                if(e.getKeyCode()==KeyEvent.VK_LEFT)
                {
                    Data.player2=(Data.player2-1)%10;
                    if(Data.player2==0) Data.player2=10;
                    Data.player2_icon=Data.getIcon(Data.player2);
                    Data.player2_icon.setImage(Data.player2_icon.getImage().getScaledInstance(280,280,Image.SCALE_DEFAULT));
                    player2_icon_label.setIcon(Data.player2_icon);
                }
                if(e.getKeyCode()==KeyEvent.VK_RIGHT)
                {
                    Data.player2=Data.player2%10+1;
                    Data.player2_icon=Data.getIcon(Data.player2);
                    Data.player2_icon.setImage(Data.player2_icon.getImage().getScaledInstance(280,280,Image.SCALE_DEFAULT));
                    player2_icon_label.setIcon(Data.player2_icon);
                }
            }
        });

        this.setVisible(true);
    }
}

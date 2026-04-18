import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JFrame {
    public HomePage() {
        Container container = this.getContentPane();
        this.setSize(700, 700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle("GoatWolfChess");
        container.setBackground(Color.YELLOW);

        JLabel picture_label = new JLabel();
        picture_label.setBounds(0, 0, 500, 700);
        Data.picture.setImage(Data.picture.getImage().getScaledInstance(500, 700, Image.SCALE_DEFAULT));
        picture_label.setIcon(Data.picture);
        this.add(picture_label);

        JButton startButton = new JButton("START GAME");
        startButton.setBounds(500, 250, 200, 100);
        startButton.setBackground(new Color(200, 50, 200));
        startButton.setForeground(Color.WHITE);
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);
        startButton.setFont(new Font("宋体", Font.BOLD, 18));
        startButton.setFocusPainted(false);
        this.add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.player1 = 1;
                Data.player2 = 1;
                new RoleChoose();
                dispose();
            }
        });
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(Color.BLUE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(200, 50, 200));
            }
        });

        JButton howToPlayButton = new JButton("HOW TO PLAY");
        howToPlayButton.setBounds(500, 375, 200, 100);
        howToPlayButton.setBackground(new Color(200, 50, 200));
        howToPlayButton.setForeground(Color.WHITE);
        howToPlayButton.setOpaque(true);
        howToPlayButton.setBorderPainted(false);
        howToPlayButton.setFont(new Font("宋体", Font.BOLD, 18));
        howToPlayButton.setFocusPainted(false);
        this.add(howToPlayButton);
        howToPlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameRule();
                dispose();
            }
        });
        howToPlayButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                howToPlayButton.setBackground(Color.BLUE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                howToPlayButton.setBackground(new Color(200, 50, 200));
            }
        });

        this.setVisible(true);
    }
}

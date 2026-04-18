/*=============================================================================================
The playing process:
1. Manipulate the movements and operations for each player.
2. Simulate the whole playing process.
=============================================================================================*/

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class GamePlay extends JFrame{
    static int flag;
    // flag=0: It's player1's turn
    // flag=1: It's player2's turn
    static int[][] d = {{1,0},{-1,0},{0,1},{0,-1}};
    // four directions
    static JButton button_up = new JButton();
    // up button
    static JButton button_left = new JButton();
    // left button
    static JButton button_right = new JButton();
    // right button
    static JButton button_down = new JButton();
    // down button
    static ImageIcon icon1;
    // player1's icon
    static ImageIcon icon2;
    // player2's icon
    static int focus_button;
    // the direction chosen by the current player
    static JLabel turn_label = new JLabel();
    // show it's which player's turn
    static JButton skill_button = new JButton("Skill");
    // skill button
    static JLabel round_label = new JLabel();
    // show the rounds of a game
    static int remaining_steps;
    // the remaining steps that the current player can move
    static int tmp_x;
    // the x-coordinate of the current player
    static int tmp_y;
    // the y-coordinate of the current player
    static boolean check1(int x,int y,int xx,int yy) // check whether (x,y) and (xx,yy) are connected
    {
        if(xx<1 || yy<1 || xx>8 || yy>8) return false;
        if(yy==y && xx==x-1 && Data.flag1[x-1][y]) return false;
        if(yy==y && xx==x+1 && Data.flag1[x][y]) return false;
        if(xx==x && yy==y-1 && Data.flag2[x][y-1]) return false;
        if(xx==x && yy==y+1 && Data.flag2[x][y]) return false;
        if(Data.type[xx][yy]==-1) return false;
        return true;
    }
    static boolean check2(int x,int y,int xx,int yy) // check whether the current player can move from (x,y) to (xx,yy)
    {
        if(xx<1 || yy<1 || xx>8 || yy>8) return false;
        if(Math.abs(x-xx)+Math.abs(y-yy)!=1) return false;
        if(yy==y && xx==x-1 && Data.flag1[x-1][y]) return false;
        if(yy==y && xx==x+1 && Data.flag1[x][y]) return false;
        if(xx==x && yy==y-1 && Data.flag2[x][y-1]) return false;
        if(xx==x && yy==y+1 && Data.flag2[x][y]) return false;
        if(remaining_steps==0) return false;
        if(Data.type[xx][yy]==-1) return false;
        if(flag==0)
        {
            if(Data.forbid2 && Math.abs(Data.player2_x-xx) + Math.abs(Data.player2_y-yy)<=1) return false;
            if(xx==Data.player2_x && yy==Data.player2_y)
            {
                if(Data.push1)
                {
                    int dx=xx-x, dy=yy-y;
                    return check2(xx,yy,xx+dx,yy+dy);
                }
                return false;
            }
            return true;
        }
        else
        {
            if(Data.forbid1 && Math.abs(Data.player1_x-xx) + Math.abs(Data.player1_y-yy)<=1) return false;
            if(xx==Data.player1_x && yy==Data.player1_y)
            {
                if(Data.push2)
                {
                    int dx=xx-x, dy=yy-y;
                    return check2(xx,yy,xx+dx,yy+dy);
                }
                return false;
            }
            return true;
        }
    }
    static boolean check3(int x,int y,int xx,int yy) // check whether the bananawolf at (x,y) can throw a banana to (xx,yy)
    {
        if(xx<1 || yy<1 || xx>8 || yy>8) return false;
        if(Math.abs(x-xx)+Math.abs(y-yy)!=1) return false;
        if(yy==y && xx==x-1 && Data.flag1[x-1][y]) return false;
        if(yy==y && xx==x+1 && Data.flag1[x][y]) return false;
        if(xx==x && yy==y-1 && Data.flag2[x][y-1]) return false;
        if(xx==x && yy==y+1 && Data.flag2[x][y]) return false;
        if(Data.type[xx][yy]==-1) return false;
        if(Data.hasBanana[xx][yy]) return false;
        if(xx==Data.player2_x && yy==Data.player2_y) return false;
        if(xx==Data.player1_x && yy==Data.player1_y) return false;
        return true;
    }
    static boolean check4(int x,int y,int xx,int yy) // check whether the grey wolf at (x,y) can throw a bomb to (xx,yy)
    {
        if(xx<1 || yy<1 || xx>8 || yy>8) return false;
        if(Math.abs(x-xx)+Math.abs(y-yy)!=1) return false;
        if(Data.type[xx][yy]==-1) return false;
        if(xx==Data.player2_x && yy==Data.player2_y) return false;
        if(xx==Data.player1_x && yy==Data.player1_y) return false;
        return true;
    }
    static void dfs1(int x,int y) // calculate player1's score
    {
        if(Data.hasBanana[x][y])
        {
            if(Data.player1==1)
            {
                Data.s1+=2;
                Data.w1[x][y]=2;
            }
            else
            {
                Data.s1+=1;
                Data.w1[x][y]=1;
            }
        }
        else
        {
            if(Data.type[x][y]==1)
            {
                if(Data.player1>=4)
                {
                    Data.s1+=2;
                    Data.w1[x][y]=2;
                }
                else
                {
                    Data.s1++;
                    Data.w1[x][y]=1;
                }
            }
            else if(Data.type[x][y]==2)
            {
                if(Data.player1<=4)
                {
                    Data.s1+=2;
                    Data.w1[x][y]=2;
                }
                else
                {
                    Data.s1++;
                    Data.w1[x][y]=1;
                }
            }
            else if(Data.type[x][y]==3)
            {
                if(Data.player1!=6)
                {
                    Data.s1-=2;
                    Data.w1[x][y]=-2;
                }
                else Data.w1[x][y]=0;
            }
            else
            {
                Data.s1++;
                Data.w1[x][y]=1;
            }
        }
        Data.vis1[x][y]=true;
        for(int i=0;i<4;i++)
        {
            int xx=x+d[i][0];
            int yy=y+d[i][1];
            if(check1(x,y,xx,yy) && !Data.vis1[xx][yy]) dfs1(xx,yy);
        }
    }
    static void dfs2(int x,int y) // calculate player2's score
    {
        if(Data.hasBanana[x][y])
        {
            if(Data.player2==1)
            {
                Data.s2+=2;
                Data.w2[x][y]=2;
            }
            else
            {
                Data.s2+=1;
                Data.w2[x][y]=1;
            }
        }
        else
        {
            if(Data.type[x][y]==1)
            {
                if(Data.player2>=4)
                {
                    Data.s2+=2;
                    Data.w2[x][y]=2;
                }
                else
                {
                    Data.s2++;
                    Data.w2[x][y]=1;
                }
            }
            else if(Data.type[x][y]==2)
            {
                if(Data.player2<=4)
                {
                    Data.s2+=2;
                    Data.w2[x][y]=2;
                }
                else
                {
                    Data.s2++;
                    Data.w2[x][y]=1;
                }
            }
            else if(Data.type[x][y]==3)
            {
                if(Data.player2!=6)
                {
                    Data.s2-=2;
                    Data.w2[x][y]=-2;
                }
                else Data.w2[x][y]=0;
            }
            else
            {
                Data.s2++;
                Data.w2[x][y]=1;
            }
        }
        Data.vis2[x][y]=true;
        for(int i=0;i<4;i++)
        {
            int xx=x+d[i][0];
            int yy=y+d[i][1];
            if(check1(x,y,xx,yy) && !Data.vis2[xx][yy]) dfs2(xx,yy);
        }
    }

    static void prework() // begin a new round and reset the parameters of the current player
    {
        turn_label.setVisible(true);
        if(flag==0)
        {
            remaining_steps=Data.player1_speed;
            Data.player1_speed=3;
            turn_label.setText("It's Player 1's turn");
            Data.forbid1=false;
            Data.push1=false;
        }
        else
        {
            remaining_steps=Data.player2_speed;
            Data.player2_speed=3;
            turn_label.setText("It's Player 2's turn");
            Data.forbid2=false;
            Data.push2=false;
        }
        round_label.setText("Round "+Data.round);
        if(flag==0) skill_button.setBackground(Color.RED);
        else skill_button.setBackground(new Color(90,0,255));
        if(check_skill()) skill_button.setVisible(true);
        else skill_button.setVisible(false);
    }

    static boolean check_ending() // check whether a game is at an ending
    {
        for(int i=1;i<=8;i++)
            for(int j=1;j<=8;j++)
                Data.vis1[i][j]=false;
        Data.s1=0;
        dfs1(Data.player1_x,Data.player1_y);
        if(!Data.vis1[Data.player2_x][Data.player2_y])
        {
            for(int i=1;i<=8;i++)
                for(int j=1;j<=8;j++)
                    Data.vis2[i][j]=false;
            Data.s2=0;
            dfs2(Data.player2_x,Data.player2_y);
            Data.gamestate=2;
            return true;
        }
        return false;
    }

    static void Count_Score() // show the score of each cell
    {
        for(int i=1;i<=8;i++)
            for(int j=1;j<=8;j++)
            {
                if(Data.vis1[i][j])
                {
                    if(Data.w1[i][j]==0)
                    {
                        Data.bluezero.setImage(Data.bluezero.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.bluezero);
                    }
                    else if(Data.w1[i][j]==1)
                    {
                        Data.blueone.setImage(Data.blueone.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.blueone);
                    }
                    else if(Data.w1[i][j]==2)
                    {
                        Data.bluetwo.setImage(Data.bluetwo.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.bluetwo);
                    }
                    else
                    {
                        Data.blueminustwo.setImage(Data.blueminustwo.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.blueminustwo);
                    }
                }
                else if(Data.vis2[i][j])
                {
                    if(Data.w2[i][j]==0)
                    {
                        Data.greenzero.setImage(Data.greenzero.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.greenzero);
                    }
                    else if(Data.w2[i][j]==1)
                    {
                        Data.greenone.setImage(Data.greenone.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.greenone);
                    }
                    else if(Data.w2[i][j]==2)
                    {
                        Data.greentwo.setImage(Data.greentwo.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.greentwo);
                    }
                    else
                    {
                        Data.greenminustwo.setImage(Data.greenminustwo.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.greenminustwo);
                    }
                }
            }
    }

    // player1 uses skill
    static void player1_1()
    {
        Data.last_use1=Data.round;
        Data.gamestate=3;
        tmp_x=Data.player1_x;
        tmp_y=Data.player1_y;
    }
    static void player1_2()
    {
        Data.last_use1=Data.round;
        Data.gamestate=4;
        tmp_x=Data.player1_x;
        tmp_y=Data.player1_y;
    }
    static void player1_3()
    {
        Data.last_use1=Data.round;
        Data.next_turn=true;
    }
    static void player1_5()
    {
        Data.last_use1=Data.round;
        remaining_steps++;
    }
    static void player1_7()
    {
        Data.last_use1=Data.round;
        Data.forbid1=true;
    }
    static void player1_8()
    {
        Data.last_use1=Data.round;
        Data.player2_speed--;
    }
    static void player1_9()
    {
        Data.last_use1=Data.round;
        Data.gamestate=5;
        tmp_x=Data.player1_x;
        tmp_y=Data.player1_y;
    }
    static void player1_10()
    {
        Data.last_use1=Data.round;
        Data.push1=true;
    }

    // player2 uses skill
    static void player2_1()
    {
        Data.last_use2=Data.round;
        Data.gamestate=3;
        tmp_x=Data.player2_x;
        tmp_y=Data.player2_y;
    }
    static void player2_2()
    {
        Data.last_use2=Data.round;
        Data.gamestate=4;
        tmp_x=Data.player2_x;
        tmp_y=Data.player2_y;
    }
    static void player2_3()
    {
        Data.last_use2=Data.round;
        Data.next_turn=true;
    }
    static void player2_5()
    {
        Data.last_use2=Data.round;
        remaining_steps++;
    }
    static void player2_7()
    {
        Data.last_use2=Data.round;
        Data.forbid2=true;
    }
    static void player2_8()
    {
        Data.last_use2=Data.round;
        Data.player1_speed--;
    }
    static void player2_9()
    {
        Data.last_use2=Data.round;
        Data.gamestate=5;
        tmp_x=Data.player2_x;
        tmp_y=Data.player2_y;
    }
    static void player2_10()
    {
        Data.last_use2=Data.round;
        Data.push2=true;
    }

    static void move1(int x,int y) //player1 moves from current position to (x,y)
    {
        if(Data.hasBanana[Data.player1_x][Data.player1_y])
        {
            Data.banana.setImage(Data.banana.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
            Data.button[Data.player1_x][Data.player1_y].setIcon(Data.banana);
        }
        else
        {
            Data.button[Data.player1_x][Data.player1_y].setIcon(null);
            if(Data.type[Data.player1_x][Data.player1_y]==1)
            {
                Data.grass.setImage(Data.grass.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                Data.button[Data.player1_x][Data.player1_y].setIcon(Data.grass);
            }
            else if(Data.type[Data.player1_x][Data.player1_y]==2)
            {
                Data.house.setImage(Data.house.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                Data.button[Data.player1_x][Data.player1_y].setIcon(Data.house);
            }
            else if(Data.type[Data.player1_x][Data.player1_y]==3)
            {
                Data.shit.setImage(Data.shit.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                Data.button[Data.player1_x][Data.player1_y].setIcon(Data.shit);
            }
        }
        Data.player1_x=x;
        Data.player1_y=y;
        Data.button[Data.player1_x][Data.player1_y].setText(null);
        Data.button[Data.player1_x][Data.player1_y].setIcon(icon1);
    }

    static void move2(int x,int y) //player2 moves from current position to (x,y)
    {
        if(Data.hasBanana[Data.player2_x][Data.player2_y])
        {
            Data.banana.setImage(Data.banana.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
            Data.button[Data.player2_x][Data.player2_y].setIcon(Data.banana);
        }
        else
        {
            Data.button[Data.player2_x][Data.player2_y].setIcon(null);
            if(Data.type[Data.player2_x][Data.player2_y]==1)
            {
                Data.grass.setImage(Data.grass.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                Data.button[Data.player2_x][Data.player2_y].setIcon(Data.grass);
            }
            else if(Data.type[Data.player2_x][Data.player2_y]==2)
            {
                Data.house.setImage(Data.house.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                Data.button[Data.player2_x][Data.player2_y].setIcon(Data.house);
            }
            else if(Data.type[Data.player2_x][Data.player2_y]==3)
            {
                Data.shit.setImage(Data.shit.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                Data.button[Data.player2_x][Data.player2_y].setIcon(Data.shit);
            }
        }
        Data.player2_x=x;
        Data.player2_y=y;
        Data.button[Data.player2_x][Data.player2_y].setText(null);
        Data.button[Data.player2_x][Data.player2_y].setIcon(icon2);
    }

    // player1 walks from current position to (x,y)
    // change in x-coordinate is dx, change in y-coordinate is dy
    static void walk1(int x,int y,int dx,int dy)
    {
        move1(x,y);
        int xx=x, yy=y;
        if(Data.hasBanana[x][y])
        {
            while(check2(xx,yy,xx+dx,yy+dy))
            {
                xx+=dx;
                yy+=dy;
                move1(xx,yy);
            }
        }
    }

    // player2 walks from current position to (x,y)
    // change in x-coordinate is dx, change in y-coordinate is dy
    static void walk2(int x,int y,int dx,int dy)
    {
        move2(x,y);
        int xx=x, yy=y;
        if(Data.hasBanana[x][y])
        {
            while(check2(xx,yy,xx+dx,yy+dy))
            {
                xx+=dx;
                yy+=dy;
                move2(xx,yy);
            }
        }
    }

    static void init() //initialize all the parameters for a new game
    {
        Data.last_use1=-5;
        Data.last_use2=-5;
        Data.player1_x=1;
        Data.player1_y=1;
        Data.player2_x=8;
        Data.player2_y=8;
        Data.player1_speed=3;
        Data.player2_speed=3;
        Data.round=1;
        Data.push1=false;
        Data.push2=false;
        Data.forbid1=false;
        Data.forbid2=false;
        for(int i=1;i<=8;i++)
            for(int j=1;j<=8;j++)
            {
                Data.flag1[i][j]=false;
                Data.flag2[i][j]=false;
                Data.hasBanana[i][j]=false;
            }
        for(int i=1;i<=8;i++)
            for(int j=1;j<=8;j++)
            {
                Data.type[i][j]=0;
                double x = Math.random();
                if(x<0.2) Data.type[i][j]=1;
                else if(x<0.4) Data.type[i][j]=2;
                else if(x<0.5) Data.type[i][j]=3;
            }
        for(int i=1;i<=8;i++)
            for(int j=1;j<=8;j++)
            {
                Data.lablex[i][j]=new JLabel();
                Data.labley[i][j]=new JLabel();
                Data.button[i][j]=new JButton();
            }
        flag=0;
        prework();
    }

    static boolean check_skill() // check whether the current player is able to use skills
    {
        if(Data.gamestate==2) return false;
        if(flag==0)
        {
            if(Data.player1==1)
            {
                if(Data.round-Data.last_use1<5) return false;
                for(int i=0;i<4;i++)
                {
                    int x=Data.player1_x+d[i][0];
                    int y=Data.player1_y+d[i][1];
                    if(check3(Data.player1_x,Data.player1_y,x,y))
                        return true;
                }
                return false;
            }
            else if(Data.player1==2)
            {
                if(Data.round-Data.last_use1<5) return false;
                for(int i=0;i<4;i++)
                {
                    int x=Data.player1_x+d[i][0];
                    int y=Data.player1_y+d[i][1];
                    if(check4(Data.player1_x,Data.player1_y,x,y))
                        return true;
                }
                return false;
            }
            else if(Data.player1==3)
            {
                if(Data.round-Data.last_use1<5) return false;
                for(int i=0;i<4;i++)
                {
                    int x=Data.player1_x+d[i][0];
                    int y=Data.player1_y+d[i][1];
                    if(check1(Data.player1_x,Data.player1_y,x,y) && x==Data.player2_x && y==Data.player2_y)
                        return true;
                }
                return false;
            }
            else if(Data.player1==4) return false;
            else if(Data.player1==5)
            {
                if(Data.round-Data.last_use1<5) return false;
                return true;
            }
            else if(Data.player1==6) return false;
            else if(Data.player1==7)
            {
                if(Data.round-Data.last_use1<5) return false;
                return true;
            }
            else if(Data.player1==8)
            {
                if(Data.round-Data.last_use1<5) return false;
                return true;
            }
            else if(Data.player1==9)
            {
                if(Data.round-Data.last_use1<5) return false;
                int x=Data.player1_x;
                int y=Data.player1_y;
                if(Data.flag1[x-1][y] && Data.type[x-1][y]!=-1) return true;
                if(Data.flag1[x][y] && Data.type[x+1][y]!=-1) return true;
                if(Data.flag2[x][y-1] && Data.type[x][y-1]!=-1) return true;
                if(Data.flag2[x][y] && Data.type[x][y+1]!=-1) return true;
                return false;
            }
            else
            {
                if(Data.round-Data.last_use1<5) return false;
                return true;
            }
        }
        else
        {
            if(Data.player2==1)
            {
                if(Data.round-Data.last_use2<5) return false;
                for(int i=0;i<4;i++)
                {
                    int x=Data.player2_x+d[i][0];
                    int y=Data.player2_y+d[i][1];
                    if(check3(Data.player2_x,Data.player2_y,x,y))
                        return true;
                }
                return false;
            }
            else if(Data.player2==2)
            {
                if(Data.round-Data.last_use2<5) return false;
                for(int i=0;i<4;i++)
                {
                    int x=Data.player2_x+d[i][0];
                    int y=Data.player2_y+d[i][1];
                    if(check4(Data.player2_x,Data.player2_y,x,y))
                        return true;
                }
                return false;
            }
            else if(Data.player2==3)
            {
                if(Data.round-Data.last_use2<5) return false;
                for(int i=0;i<4;i++)
                {
                    int x=Data.player2_x+d[i][0];
                    int y=Data.player2_y+d[i][1];
                    if(check1(Data.player2_x,Data.player2_y,x,y) && x==Data.player1_x && y==Data.player1_y)
                        return true;
                }
                return false;
            }
            else if(Data.player2==4) return false;
            else if(Data.player2==5)
            {
                if(Data.round-Data.last_use2<5) return false;
                return true;
            }
            else if(Data.player2==6) return false;
            else if(Data.player2==7)
            {
                if(Data.round-Data.last_use2<5) return false;
                return true;
            }
            else if(Data.player2==8)
            {
                if(Data.round-Data.last_use2<5) return false;
                return true;
            }
            else if(Data.player2==9)
            {
                if(Data.round-Data.last_use2<5) return false;
                int x=Data.player2_x;
                int y=Data.player2_y;
                if(Data.flag1[x-1][y] && Data.type[x-1][y]!=-1) return true;
                if(Data.flag1[x][y] && Data.type[x+1][y]!=-1) return true;
                if(Data.flag2[x][y-1] && Data.type[x][y-1]!=-1) return true;
                if(Data.flag2[x][y] && Data.type[x][y+1]!=-1) return true;
                return false;
            }
            else
            {
                if(Data.round-Data.last_use2<5) return false;
                return true;
            }
        }
    }
    public GamePlay() // Add the game components and manipulate the movements and operations for each player.
    {
        Container container = this.getContentPane();
        this.setSize(700,700);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle("GoatWolfChess");
        container.setBackground(Color.YELLOW);

        // add some basic game components if the game is proceeding
        if(Data.gamestate==1)
        {
            init(); // initialize and get prepared for a new game

            // show it's which player's turn
            turn_label.setBounds(200, 120, 160, 30);
            turn_label.setForeground(Color.RED);
            turn_label.setFont(new Font("Arial", Font.BOLD, 18));
            this.add(turn_label);

            // show the icon of player1
            JLabel player1_label = new JLabel();
            player1_label.setBounds(40,10,140,140);
            Data.player1_icon=Data.getIcon(Data.player1);
            Data.player1_icon.setImage(Data.player1_icon.getImage().getScaledInstance(140,140,Image.SCALE_DEFAULT));
            player1_label.setIcon(Data.player1_icon);
            this.add(player1_label);

            // show the icon of player2
            JLabel player2_label = new JLabel();
            player2_label.setBounds(375,10,140,140);
            Data.player2_icon=Data.getIcon(Data.player2);
            Data.player2_icon.setImage(Data.player2_icon.getImage().getScaledInstance(140,140,Image.SCALE_DEFAULT));
            player2_label.setIcon(Data.player2_icon);
            this.add(player2_label);

            // "VS" label
            JLabel VS_label = new JLabel("VS");
            VS_label.setBounds(250,10,140,140);
            VS_label.setForeground(Color.RED);
            VS_label.setFont(new Font("Arial",Font.BOLD,40));
            this.add(VS_label);

            // show the round of a game
            round_label.setBounds(235,10,140,40);
            round_label.setForeground(Color.BLUE);
            round_label.setFont(new Font("Arial",Font.BOLD,20));
            this.add(round_label);

            // add player1's icon to its position
            icon1 = new ImageIcon(Data.getURL(Data.player1));
            icon1.setImage(icon1.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
            Data.button[Data.player1_x][Data.player1_y].setIcon(icon1);

            // add player2's icon to its position
            icon2 = new ImageIcon(Data.getURL(Data.player2));
            icon2.setImage(icon2.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
            Data.button[Data.player2_x][Data.player2_y].setIcon(icon2);

            // control label
            JLabel control_label = new JLabel("Click the cells or press [W]  [A]  [S]  [D] to move.");
            control_label.setBounds(40,630,500,50);
            control_label.setForeground(Color.RED);
            control_label.setFont(new Font("Arial",Font.BOLD,15));
            this.add(control_label);

            // board label1
            JLabel board_label1 = new JLabel("Choose direction to");
            board_label1.setBounds(525,190,250,40);
            board_label1.setForeground(Color.RED);
            board_label1.setFont(new Font("Arial",Font.BOLD,18));
            this.add(board_label1);

            // board label2
            JLabel board_label2 = new JLabel("place or break");
            board_label2.setBounds(550,220,250,40);
            board_label2.setForeground(Color.RED);
            board_label2.setFont(new Font("Arial",Font.BOLD,18));
            this.add(board_label2);

            // board label3
            JLabel board_label3 = new JLabel("the board:");
            board_label3.setBounds(570,250,250,40);
            board_label3.setForeground(Color.RED);
            board_label3.setFont(new Font("Arial",Font.BOLD,18));
            this.add(board_label3);
        }

        // If the game is at an ending, then demonstrate the winner.
        if(Data.gamestate==2)
        {
            if(Data.s1>Data.s2) // player1 wins
            {
                JLabel label1 = new JLabel("Player 1 Wins!");
                label1.setBounds(215, 0, 140, 30);
                label1.setForeground(Color.RED);
                label1.setFont(new Font("Arial", Font.BOLD, 16));
                this.add(label1);

                // show the icon of the winner
                JLabel label2 = new JLabel();
                label2.setBounds(210, 35, 120, 120);
                ImageIcon icon = new ImageIcon(Data.getURL(Data.player1));
                icon.setImage(icon.getImage().getScaledInstance(120,120,Image.SCALE_DEFAULT));
                label2.setIcon(icon);
                this.add(label2);

                // show player1's scpre
                JLabel label3 = new JLabel("Score of Player 1: " + Data.s1);
                label3.setBounds(40, 55, 160, 50);
                label3.setForeground(Color.BLUE);
                label3.setFont(new Font("Arial", Font.BOLD, 16));
                this.add(label3);

                // show player2's score
                JLabel label4 = new JLabel("Score of Player 2: "+Data.s2);
                label4.setBounds(350,55,160,50);
                label4.setForeground(Color.BLUE);
                label4.setFont(new Font("Arial",Font.BOLD,16));
                this.add(label4);
            }
            else if(Data.s1<Data.s2) // player2 wins
            {
                JLabel label1 = new JLabel("Player 2 Wins!");
                label1.setBounds(215, 0, 140, 30);
                label1.setForeground(Color.RED);
                label1.setFont(new Font("Arial", Font.BOLD, 16));
                this.add(label1);

                // show the icon of the winner
                JLabel label2 = new JLabel();
                label2.setBounds(210, 35, 120, 120);
                ImageIcon icon = new ImageIcon(Data.getURL(Data.player2));
                icon.setImage(icon.getImage().getScaledInstance(120,120,Image.SCALE_DEFAULT));
                label2.setIcon(icon);
                this.add(label2);

                // show the score of player1
                JLabel label3 = new JLabel("Score of Player 1: " + Data.s1);
                label3.setBounds(40, 55, 160, 50);
                label3.setForeground(Color.BLUE);
                label3.setFont(new Font("Arial", Font.BOLD, 16));
                this.add(label3);

                // show the score of player2
                JLabel label4 = new JLabel("Score of Player 2: "+Data.s2);
                label4.setBounds(350,55,160,50);
                label4.setForeground(Color.BLUE);
                label4.setFont(new Font("Arial",Font.BOLD,16));
                this.add(label4);
            }
            else // it is a tie
            {
                // show the icon of player1
                JLabel label1 = new JLabel();
                label1.setBounds(40,10,140,140);
                Data.player1_icon=Data.getIcon(Data.player1);
                Data.player1_icon.setImage(Data.player1_icon.getImage().getScaledInstance(140,140,Image.SCALE_DEFAULT));
                label1.setIcon(Data.player1_icon);
                this.add(label1);

                // show the icon of player2
                JLabel label2 = new JLabel();
                label2.setBounds(380,10,140,140);
                Data.player2_icon=Data.getIcon(Data.player2);
                Data.player2_icon.setImage(Data.player2_icon.getImage().getScaledInstance(140,140,Image.SCALE_DEFAULT));
                label2.setIcon(Data.player2_icon);
                this.add(label2);

                // Tie label
                JLabel lable3 = new JLabel("Tie");
                lable3.setBounds(250,10,140,140);
                lable3.setForeground(Color.RED);
                lable3.setFont(new Font("Arial",Font.BOLD,40));
                this.add(lable3);

                // show the score of player1
                JLabel label4 = new JLabel("Score of Player 1: " + Data.s1);
                label4.setBounds(205, 10, 160, 50);
                label4.setForeground(Color.BLUE);
                label4.setFont(new Font("Arial", Font.BOLD, 16));
                this.add(label4);

                // show the score of player2
                JLabel label5 = new JLabel("Score of Player 2: "+Data.s2);
                label5.setBounds(205,100,160,50);
                label5.setForeground(Color.BLUE);
                label5.setFont(new Font("Arial",Font.BOLD,16));
                this.add(label5);
            }

            // the button to restart a new game
            JButton rebutton = new JButton("Restart");
            rebutton.setBounds(540,180,140,70);
            rebutton.setBackground(new Color(200,50,200));
            rebutton.setForeground(Color.WHITE);
            rebutton.setOpaque(true);
            rebutton.setBorderPainted(false);
            rebutton.setFont(new Font("宋体",Font.BOLD,20));
            rebutton.setFocusPainted(false);
            this.add(rebutton);
            rebutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
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
                    Data.gamestate=1;
                    new GamePlay();
                    dispose();
                }
            });
            rebutton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    rebutton.setBackground(Color.BLUE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    rebutton.setBackground(new Color(200,50,200));
                }
            });
        }

        // add vertical boards
        for(int i=1;i<=7;i++)
            for(int j=1;j<=8;j++)
            {
                Data.lablex[i][j].setBounds(30+i*60+5,100+j*60,5,55);
                if(Data.type[i][j]==-1 && Data.type[i+1][j]==-1) Data.lablex[i][j].setBackground(Color.YELLOW);
                else if(Data.flag1[i][j]) Data.lablex[i][j].setBackground(Color.RED);
                else Data.lablex[i][j].setBackground(Color.LIGHT_GRAY);
                Data.lablex[i][j].setOpaque(true);
                this.add(Data.lablex[i][j]);
            }

        // add horizontal boards
        for(int i=1;i<=8;i++)
            for(int j=1;j<=7;j++)
            {
                Data.labley[i][j].setBounds(-20+i*60,150+j*60+5,55,5);
                if(Data.type[i][j]==-1 && Data.type[i][j+1]==-1) Data.labley[i][j].setBackground(Color.YELLOW);
                else if(Data.flag2[i][j]) Data.labley[i][j].setBackground(Color.RED);
                else Data.labley[i][j].setBackground(Color.LIGHT_GRAY);
                Data.labley[i][j].setOpaque(true);
                this.add(Data.labley[i][j]);
            }

        // confirm button
        JButton confirm_button = new JButton("Confirm");
        confirm_button.setBounds(540,470,140,70);
        confirm_button.setBackground(new Color(200,50,200));
        confirm_button.setForeground(Color.WHITE);
        confirm_button.setOpaque(true);
        confirm_button.setBorderPainted(false);
        confirm_button.setFont(new Font("宋体",Font.BOLD,20));
        confirm_button.setFocusPainted(false);
        confirm_button.setFocusable(false);
        confirm_button.setVisible(false);
        this.add(confirm_button);

        confirm_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Data.gamestate==-1)
                {
                    int x=0, y=0;
                    if(flag==0)
                    {
                        x=Data.player1_x;
                        y=Data.player1_y;
                    }
                    else
                    {
                        x=Data.player2_x;
                        y=Data.player2_y;
                    }
                    if(focus_button==1)
                    {
                        Data.flag2[x][y-1]=true;
                        Data.labley[x][y-1].setBackground(Color.RED);
                    }
                    else if(focus_button==4)
                    {
                        Data.flag2[x][y]=true;
                        Data.labley[x][y].setBackground(Color.RED);
                    }
                    else if(focus_button==2)
                    {
                        Data.flag1[x-1][y]=true;
                        Data.lablex[x-1][y].setBackground(Color.RED);
                    }
                    else if(focus_button==3)
                    {
                        Data.flag1[x][y]=true;
                        Data.lablex[x][y].setBackground(Color.RED);
                    }
                    flag^=1;
                    if(flag==0) Data.round++;
                    if(Data.next_turn)
                    {
                        Data.next_turn=false;
                        flag^=1;
                        if(flag==0) Data.round++;
                    }
                    Data.gamestate=1;
                    confirm_button.setVisible(false);
                    if(!check_ending()) prework();
                    else
                    {
                        Data.clip.close();
                        try {
                            Data.stream = AudioSystem.getAudioInputStream(new File("src/Music/music3.wav").getAbsoluteFile());
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
                }
                else if(Data.gamestate==-5)
                {
                    if(focus_button==1)
                    {
                        Data.flag2[tmp_x][tmp_y-1]=false;
                        Data.labley[tmp_x][tmp_y-1].setBackground(Color.LIGHT_GRAY);
                    }
                    else if(focus_button==4)
                    {
                        Data.flag2[tmp_x][tmp_y]=false;
                        Data.labley[tmp_x][tmp_y].setBackground(Color.LIGHT_GRAY);
                    }
                    else if(focus_button==2)
                    {
                        Data.flag1[tmp_x-1][tmp_y]=false;
                        Data.lablex[tmp_x-1][tmp_y].setBackground(Color.LIGHT_GRAY);
                    }
                    else if(focus_button==3)
                    {
                        Data.flag1[tmp_x][tmp_y]=false;
                        Data.lablex[tmp_x][tmp_y].setBackground(Color.LIGHT_GRAY);
                    }
                    Data.gamestate=1;
                    confirm_button.setVisible(false);
                    //DrawBottomMap();
                }
            }
        });
        confirm_button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                confirm_button.setBackground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                confirm_button.setBackground(new Color(200,50,200));
            }
        });

        // add buttons to each cell
        for(int i=1;i<=8;i++)
            for(int j=1;j<=8;j++)
            {
                Data.button[i][j].setFocusable(false);
                Data.button[i][j].setBounds(-20 + i * 60, 100 + j * 60, 55, 55);
                Data.button[i][j].setBackground(Color.LIGHT_GRAY);
                Data.button[i][j].setOpaque(true);
                Data.button[i][j].setBorderPainted(false);
                Data.button[i][j].setFocusPainted(false);

                // show icons of special lands
                if (!(i == Data.player1_x && j == Data.player1_y) && !(i == Data.player2_x && j == Data.player2_y) && Data.gamestate==1) {
                    if (Data.type[i][j] == 1) // grass land
                    {
                        Data.grass.setImage(Data.grass.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.grass);
                    }
                    else if (Data.type[i][j] == 2) // wolf house
                    {
                        Data.house.setImage(Data.house.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.house);
                    }
                    else if (Data.type[i][j] == 3) // shit land
                    {
                        Data.shit.setImage(Data.shit.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.shit);
                    }
                    else if(Data.hasBanana[i][j]) // banana land
                    {
                        Data.banana.setImage(Data.banana.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                        Data.button[i][j].setIcon(Data.banana);
                    }
                }
                if(Data.type[i][j]==-1) // the land has been destroyed
                {
                    Data.button[i][j].setBackground(Color.YELLOW);
                    if(i>1) Data.lablex[i-1][j].setBackground(Color.LIGHT_GRAY);
                    if(i<8) Data.lablex[i][j].setBackground(Color.LIGHT_GRAY);
                    if(j>1) Data.labley[i][j-1].setBackground(Color.LIGHT_GRAY);
                    if(j<8) Data.labley[i][j].setBackground(Color.LIGHT_GRAY);
                }
                this.add(Data.button[i][j]);
                int x=i, y=j;
                Data.button[x][y].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if(Data.gamestate==1 || Data.gamestate==-1)
                        {
                            if(flag==0 && check2(Data.player1_x,Data.player1_y,x,y))
                            {
                                int dx=x-Data.player1_x;
                                int dy=y-Data.player1_y;
                                if(x==Data.player2_x && y==Data.player2_y && Data.push1)
                                {
                                    walk2(x+dx,y+dy,dx,dy);
                                    walk1(x,y,dx,dy);
                                }
                                else walk1(x,y,dx,dy);
                                remaining_steps--;
                                Data.gamestate=1;
                            }
                            else if(flag==1 && check2(Data.player2_x,Data.player2_y,x,y))
                            {
                                int dx=x-Data.player2_x;
                                int dy=y-Data.player2_y;
                                if(x==Data.player1_x && y==Data.player1_y && Data.push2)
                                {
                                    walk1(x+dx,y+dy,dx,dy);
                                    walk2(x,y,dx,dy);
                                }
                                else walk2(x,y,dx,dy);
                                remaining_steps--;
                                Data.gamestate=1;
                            }
                        }
                        if(Data.gamestate==3)
                        {
                            if(check3(tmp_x,tmp_y,x,y))
                            {
                                Data.hasBanana[x][y]=true;
                                Data.button[x][y].setText(null);
                                Data.banana.setImage(Data.banana.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
                                Data.button[x][y].setIcon(Data.banana);
                                Data.gamestate=1;
                            }
                        }
                        if(Data.gamestate==4)
                        {
                            if(check4(tmp_x,tmp_y,x,y))
                            {
                                Data.type[x][y]=-1;
                                Data.button[x][y].setBackground(Color.YELLOW);
                                Data.button[x][y].setIcon(null);
                                Data.button[x][y].setText(null);
                                if(x>1)
                                {
                                    Data.flag1[x-1][y]=true;
                                    Data.lablex[x-1][y].setBackground(Color.LIGHT_GRAY);
                                }
                                if(x<8)
                                {
                                    Data.flag1[x][y]=true;
                                    Data.lablex[x][y].setBackground(Color.LIGHT_GRAY);
                                }
                                if(y>1)
                                {
                                    Data.flag2[x][y-1]=true;
                                    Data.labley[x][y-1].setBackground(Color.LIGHT_GRAY);
                                }
                                if(y<8)
                                {
                                    Data.flag2[x][y]=true;
                                    Data.labley[x][y].setBackground(Color.LIGHT_GRAY);
                                }
                                Data.gamestate=1;
                                if(check_ending())
                                {
                                    Data.clip.close();
                                    try {
                                        Data.stream = AudioSystem.getAudioInputStream(new File("src/Music/music3.wav").getAbsoluteFile());
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
                            }
                        }

                        if(flag==0) skill_button.setBackground(Color.RED);
                        else skill_button.setBackground(new Color(90,0,255));
                        if(check_skill() && Data.gamestate!=2) skill_button.setVisible(true);
                        else skill_button.setVisible(false);
                        if(Data.gamestate==1) confirm_button.setVisible(false);
                    }
                });
                Data.button[x][y].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        if(Data.type[x][y]!=-1)
                        {
                            if(Data.gamestate==1 || Data.gamestate==-1)
                            {
                                if((flag==0 && check2(Data.player1_x,Data.player1_y,x,y)) || (flag==1 && check2(Data.player2_x,Data.player2_y,x,y)))
                                    Data.button[x][y].setBackground(Color.green);
                                else Data.button[x][y].setBackground(Color.RED);
                            }
                            else if(Data.gamestate==3)
                            {
                                if(check3(tmp_x,tmp_y,x,y)) Data.button[x][y].setBackground(Color.green);
                                else Data.button[x][y].setBackground(Color.RED);
                            }
                            else if(Data.gamestate==4)
                            {
                                if(check4(tmp_x,tmp_y,x,y)) Data.button[x][y].setBackground(Color.green);
                                else Data.button[x][y].setBackground(Color.RED);
                            }
                            else Data.button[x][y].setBackground(Color.RED);
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseEntered(e);
                        if(Data.type[x][y]!=-1) Data.button[x][y].setBackground(Color.LIGHT_GRAY);
                        else Data.button[x][y].setBackground(Color.YELLOW);
                    }
                });
            }

        // if the game is at an ending, show the scores of each cell
        if(Data.gamestate==2) Count_Score();

        // home button
        JButton button1 = new JButton("Home");
        button1.setBounds(540,10,140,70);
        button1.setBackground(new Color(200,50,200));
        button1.setForeground(Color.WHITE);
        button1.setOpaque(true);
        button1.setBorderPainted(false);
        button1.setFont(new Font("宋体",Font.BOLD,20));
        button1.setFocusPainted(false);
        button1.setFocusable(false);
        this.add(button1);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.clip.close();
                try {
                    Data.stream = AudioSystem.getAudioInputStream(new File("src/Music/music1.wav").getAbsoluteFile());
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

        // back button
        JButton button2 = new JButton("Back");
        button2.setBounds(540,90,140,70);
        button2.setBackground(new Color(200,50,200));
        button2.setForeground(Color.WHITE);
        button2.setOpaque(true);
        button2.setBorderPainted(false);
        button2.setFont(new Font("宋体",Font.BOLD,20));
        button2.setFocusPainted(false);
        button2.setFocusable(false);
        this.add(button2);

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.player1=1;
                Data.player2=1;
                Data.clip.close();
                try {
                    Data.stream = AudioSystem.getAudioInputStream(new File("src/Music/music1.wav").getAbsoluteFile());
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
                new RoleChoose();
                dispose();
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

        // up button
        button_up.setBounds(585,300,50,50);
        button_up.setBackground(new Color(200,50,200));
        button_up.setForeground(Color.WHITE);
        button_up.setOpaque(true);
        button_up.setBorderPainted(false);
        button_up.setFocusPainted(false);
        button_up.setFocusable(false);
        Data.arrayU.setImage(Data.arrayU.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
        button_up.setIcon(Data.arrayU);
        if(Data.gamestate!=2) this.add(button_up);
        button_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Data.gamestate==1 || Data.gamestate==-1)
                {
                    if(flag==0)
                    {
                        if(!Data.flag2[Data.player1_x][Data.player1_y-1] && Data.player1_y!=1)
                        {
                            Data.gamestate=-1;
                            focus_button=1;
                            confirm_button.setVisible(true);
                        }
                        else
                        {
                            Data.gamestate=1;
                            confirm_button.setVisible(false);
                        }
                    }
                    else
                    {
                        if(!Data.flag2[Data.player2_x][Data.player2_y-1] && Data.player2_y!=1)
                        {
                            Data.gamestate=-1;
                            focus_button=1;
                            confirm_button.setVisible(true);
                        }
                        else
                        {
                            Data.gamestate=1;
                            confirm_button.setVisible(false);
                        }
                    }
                }
                if(Data.gamestate==5 || Data.gamestate==-5)
                {
                    if(Data.flag2[tmp_x][tmp_y-1] && Data.type[tmp_x][tmp_y-1]!=-1)
                    {
                        Data.gamestate=-5;
                        focus_button=1;
                        confirm_button.setVisible(true);
                    }
                    else
                    {
                        Data.gamestate=5;
                        confirm_button.setVisible(false);
                    }
                }
            }
        });

        // left button
        button_left.setBounds(535,350,50,50);
        button_left.setBackground(new Color(200,50,200));
        button_left.setForeground(Color.WHITE);
        button_left.setOpaque(true);
        button_left.setBorderPainted(false);
        button_left.setFocusPainted(false);
        button_left.setFocusable(false);
        Data.arrayL.setImage(Data.arrayL.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
        button_left.setIcon(Data.arrayL);
        if(Data.gamestate!=2) this.add(button_left);
        button_left.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Data.gamestate==1 || Data.gamestate==-1)
                {
                    if(flag==0)
                    {
                        if(!Data.flag1[Data.player1_x-1][Data.player1_y] && Data.player1_x!=1)
                        {
                            Data.gamestate=-1;
                            focus_button=2;
                            confirm_button.setVisible(true);
                        }
                        else
                        {
                            Data.gamestate=1;
                            confirm_button.setVisible(false);
                        }
                    }
                    else
                    {
                        if(!Data.flag1[Data.player2_x-1][Data.player2_y] && Data.player2_x!=1)
                        {
                            Data.gamestate=-1;
                            focus_button=2;
                            confirm_button.setVisible(true);
                        }
                        else
                        {
                            Data.gamestate=1;
                            confirm_button.setVisible(false);
                        }
                    }
                }
                if(Data.gamestate==5 || Data.gamestate==-5)
                {
                    if(Data.flag1[tmp_x-1][tmp_y] && Data.type[tmp_x-1][tmp_y]!=-1)
                    {
                        Data.gamestate=-5;
                        focus_button=2;
                        confirm_button.setVisible(true);
                    }
                    else
                    {
                        Data.gamestate=5;
                        confirm_button.setVisible(false);
                    }
                }
            }
        });

        // right button
        button_right.setBounds(635,350,50,50);
        button_right.setBackground(new Color(200,50,200));
        button_right.setForeground(Color.WHITE);
        button_right.setOpaque(true);
        button_right.setBorderPainted(false);
        button_right.setFocusPainted(false);
        button_right.setFocusable(false);
        Data.arrayR.setImage(Data.arrayR.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
        button_right.setIcon(Data.arrayR);
        if(Data.gamestate!=2) this.add(button_right);
        button_right.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Data.gamestate==1 || Data.gamestate==-1)
                {
                    if(flag==0)
                    {
                        if(!Data.flag1[Data.player1_x][Data.player1_y] && Data.player1_x!=8)
                        {
                            Data.gamestate=-1;
                            focus_button=3;
                            confirm_button.setVisible(true);
                        }
                        else
                        {
                            Data.gamestate=1;
                            confirm_button.setVisible(false);
                        }
                    }
                    else
                    {
                        if(!Data.flag1[Data.player2_x][Data.player2_y] && Data.player2_x!=8)
                        {
                            Data.gamestate=-1;
                            focus_button=3;
                            confirm_button.setVisible(true);
                        }
                        else
                        {
                            Data.gamestate=1;
                            confirm_button.setVisible(false);
                        }
                    }
                }
                if(Data.gamestate==5 || Data.gamestate==-5)
                {
                    if(Data.flag1[tmp_x][tmp_y] && Data.type[tmp_x+1][tmp_y]!=-1)
                    {
                        Data.gamestate=-5;
                        focus_button=3;
                        confirm_button.setVisible(true);
                    }
                    else
                    {
                        Data.gamestate=5;
                        confirm_button.setVisible(false);
                    }
                }
            }
        });

        // down button
        button_down.setBounds(585,400,50,50);
        button_down.setBackground(new Color(200,50,200));
        button_down.setForeground(Color.WHITE);
        button_down.setOpaque(true);
        button_down.setBorderPainted(false);
        button_down.setFocusPainted(false);
        button_down.setFocusable(false);
        Data.arrayD.setImage(Data.arrayD.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT));
        button_down.setIcon(Data.arrayD);
        if(Data.gamestate!=2) this.add(button_down);
        button_down.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Data.gamestate==1 || Data.gamestate==-1)
                {
                    if(flag==0)
                    {
                        if(!Data.flag2[Data.player1_x][Data.player1_y] && Data.player1_y!=8)
                        {
                            Data.gamestate=-1;
                            focus_button=4;
                            confirm_button.setVisible(true);
                        }
                        else
                        {
                            Data.gamestate=1;
                            confirm_button.setVisible(false);
                        }

                    }
                    else
                    {
                        if(!Data.flag2[Data.player2_x][Data.player2_y] && Data.player2_y!=8)
                        {
                            Data.gamestate=-1;
                            focus_button=4;
                            confirm_button.setVisible(true);
                        }
                        else
                        {
                            Data.gamestate=1;
                            confirm_button.setVisible(false);
                        }
                    }
                }
                if(Data.gamestate==5 || Data.gamestate==-5)
                {
                    if(Data.flag2[tmp_x][tmp_y] && Data.type[tmp_x][tmp_y+1]!=-1)
                    {
                        Data.gamestate=-5;
                        focus_button=4;
                        confirm_button.setVisible(true);
                    }
                    else
                    {
                        Data.gamestate=5;
                        confirm_button.setVisible(false);
                    }
                }
            }
        });

        // skill button
        skill_button.setBounds(540,550,140,70);
        skill_button.setForeground(Color.WHITE);
        skill_button.setOpaque(true);
        skill_button.setBorderPainted(false);
        skill_button.setFont(new Font("宋体",Font.BOLD,20));
        skill_button.setFocusPainted(false);
        skill_button.setFocusable(false);
        if(Data.gamestate!=2) this.add(skill_button);

        if(!Data.Skillbutton_Listener)
        {
            Data.Skillbutton_Listener=true;
            skill_button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(flag==0)
                    {
                        if(Data.player1==1) player1_1();
                        if(Data.player1==2) player1_2();
                        if(Data.player1==3) player1_3();
                        if(Data.player1==5) player1_5();
                        if(Data.player1==7) player1_7();
                        if(Data.player1==8) player1_8();
                        if(Data.player1==9) player1_9();
                        if(Data.player1==10) player1_10();
                    }
                    else
                    {
                        if(Data.player2==1) player2_1();
                        if(Data.player2==2) player2_2();
                        if(Data.player2==3) player2_3();
                        if(Data.player2==5) player2_5();
                        if(Data.player2==7) player2_7();
                        if(Data.player2==8) player2_8();
                        if(Data.player2==9) player2_9();
                        if(Data.player2==10) player2_10();
                    }
                    if(Data.gamestate!=-1 && Data.gamestate!=-5) confirm_button.setVisible(false);
                    skill_button.setVisible(false);
                }
            });
            skill_button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    skill_button.setBackground(new Color(0,150,0));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    if(flag==0) skill_button.setBackground(Color.RED);
                    else skill_button.setBackground(new Color(90,0,255));
                }
            });
        }

        // players can press [W] [A] [S] [D] to move
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
                if(Data.gamestate==1 || Data.gamestate==-1)
                {
                    int x=0,y=0;
                    if(e.getKeyCode()==KeyEvent.VK_W)
                    {
                        if(flag==0)
                        {
                            x=Data.player1_x;
                            y=Data.player1_y-1;
                        }
                        else
                        {
                            x=Data.player2_x;
                            y=Data.player2_y-1;
                        }
                    }
                    if(e.getKeyCode()==KeyEvent.VK_S)
                    {
                        if(flag==0)
                        {
                            x=Data.player1_x;
                            y=Data.player1_y+1;
                        }
                        else
                        {
                            x=Data.player2_x;
                            y=Data.player2_y+1;
                        }
                    }
                    if(e.getKeyCode()==KeyEvent.VK_A)
                    {
                        if(flag==0)
                        {
                            x=Data.player1_x-1;
                            y=Data.player1_y;
                        }
                        else
                        {
                            x=Data.player2_x-1;
                            y=Data.player2_y;
                        }
                    }
                    if(e.getKeyCode()==KeyEvent.VK_D)
                    {
                        if(flag==0)
                        {
                            x=Data.player1_x+1;
                            y=Data.player1_y;
                        }
                        else
                        {
                            x=Data.player2_x+1;
                            y=Data.player2_y;
                        }
                    }
                    if(flag==0 && check2(Data.player1_x,Data.player1_y,x,y))
                    {
                        int dx=x-Data.player1_x;
                        int dy=y-Data.player1_y;
                        if(x==Data.player2_x && y==Data.player2_y && Data.push1)
                        {
                            walk2(x+dx,y+dy,dx,dy);
                            walk1(x,y,dx,dy);
                        }
                        else walk1(x,y,dx,dy);
                        remaining_steps--;
                        Data.gamestate=1;
                    }
                    else if(flag==1 && check2(Data.player2_x,Data.player2_y,x,y))
                    {
                        int dx=x-Data.player2_x;
                        int dy=y-Data.player2_y;
                        if(x==Data.player1_x && y==Data.player1_y && Data.push2)
                        {
                            walk1(x+dx,y+dy,dx,dy);
                            walk2(x,y,dx,dy);
                        }
                        else walk2(x,y,dx,dy);
                        remaining_steps--;
                        Data.gamestate=1;
                    }
                    if(flag==0) skill_button.setBackground(Color.RED);
                    else skill_button.setBackground(new Color(90,0,255));
                    if(check_skill() && Data.gamestate!=2) skill_button.setVisible(true);
                    else skill_button.setVisible(false);
                    if(Data.gamestate==1) confirm_button.setVisible(false);
                }
            }
        });

        this.setVisible(true);
    }
}

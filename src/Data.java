/*=============================================================================================
Data center:
Store some picture resources and some important variables.
=============================================================================================*/

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.net.URL;

public class Data {
    public static int videostate=0;
    // which video is playing
    public static int rulestate=1;
    // which rule picture is being demonstrated
    public static int player1=1;
    // which role player1 has chosen
    public static int player2=1;
    // which role player2 has chosen
    public static boolean Skillbutton_Listener;
    public static int player1_x, player1_y;
    // the coordinates of player1
    public static int player2_x, player2_y;
    // the coordinates of player2
    public static ImageIcon player1_icon;
    // the icon of player1
    public static ImageIcon player2_icon;
    // the icon of player2
    public static int round;
    // count the rounds of a game
    public static JLabel[][] lablex = new JLabel[15][15];
    // vertical boards
    public static JLabel[][] labley = new JLabel[15][15];
    // horizontal boards
    public static JButton[][] button = new JButton[15][15];
    // button on each cell
    public static int gamestate;
    // gamestate=1:  the current player hasn't chosen a direction to place the board
    // gamestate=-1: the current player has chosen a direction to place the board
    // gamestate=2:  the game is at an ending
    // gamestate=3:  the bananawolf is using his skill
    // gamestate=4:  the greywolf is using his skill
    // gamestate=5:  the stronggoat is using his skill but hasn't chosen a direction to break the board
    // gamestate=-5: the stronggoat is using his skill and has chosen a direction to break the board
    public static boolean[][] flag1 = new boolean[15][15];
    // whether a vertical board has been placed or destroyed
    public static boolean[][] flag2 = new boolean[15][15];
    // whether a horizontal board has been placed or destroyed
    public static boolean[][] vis1 = new boolean[15][15];
    // whether player1 can visit each cell
    public static boolean[][] vis2 = new boolean[15][15];
    // whether player2 can visit each cell
    public static boolean[][] hasBanana = new boolean[15][15];
    // whether a cell has been placed a banana
    public static int s1;
    // score of player1
    public static int s2;
    // score of player2
    public static int last_use1;
    // when player1 uses skill for the last time
    public static int last_use2;
    // when player2 uses skill for the last time
    public static int[][] type = new int[15][15];
    // type=-1: destroyed land
    // type=0:  vacant land
    // type=1:  grass land
    // type=2:  wolf house
    // type=3:  shit land
    public static int[][] w1 = new int[15][15];
    // the score of each cell for player1
    public static int[][] w2 = new int[15][15];
    // the score of each cell for player2
    public static int player1_speed;
    // how many steps player1 can move in one turn
    public static int player2_speed;
    // how many steps player2 can move in one turn
    public static boolean next_turn;
    // whether the redwolf has used her skill
    public static boolean forbid1;
    // For player1: whether the prettygoat has used her skill
    public static boolean forbid2;
    // For player2: whether the prettygoat has used her skill
    public static boolean push1;
    // For player1: whether the warmgoat has used her skill
    public static boolean push2;
    // For player2: whether the warmgoat has used her skill
    public static Clip clip = null;
    public static AudioInputStream stream;
    public static ImageIcon getIcon(int id) // get the icon for each role
    {
        if(id==1) return bananawolf;
        if(id==2) return greywolf;
        if(id==3) return redwolf;
        if(id==4) return smallgrey;
        if(id==5) return clevergoat;
        if(id==6) return lazygoat;
        if(id==7) return prettygoat;
        if(id==8) return slowgoat;
        if(id==9) return stronggoat;
        return warmgoat;
    }
    public static URL getURL(int id) // get URL for each role
    {
        if(id==1) return bananawolfURL;
        if(id==2) return greywolfURL;
        if(id==3) return redwolfURL;
        if(id==4) return smallgreyURL;
        if(id==5) return clevergoatURL;
        if(id==6) return lazygoatURL;
        if(id==7) return prettygoatURL;
        if(id==8) return slowgoatURL;
        if(id==9) return stronggoatURL;
        return warmgoatURL;
    }
    // Below are the picture resources
    public static URL bananawolfURL = Data.class.getResource("statics/bananawolf.jpg");
    public static ImageIcon bananawolf = new ImageIcon(bananawolfURL);
    public static URL greywolfURL = Data.class.getResource("statics/greywolf.jpg");
    public static ImageIcon greywolf = new ImageIcon(greywolfURL);
    public static URL redwolfURL = Data.class.getResource("statics/redwolf.jpg");
    public static ImageIcon redwolf = new ImageIcon(redwolfURL);
    public static URL smallgreyURL = Data.class.getResource("statics/smallgrey.jpg");
    public static ImageIcon smallgrey = new ImageIcon(smallgreyURL);
    public static URL clevergoatURL = Data.class.getResource("statics/clevergoat.jpg");
    public static ImageIcon clevergoat = new ImageIcon(clevergoatURL);
    public static URL lazygoatURL = Data.class.getResource("statics/lazygoat.jpg");
    public static ImageIcon lazygoat = new ImageIcon(lazygoatURL);
    public static URL warmgoatURL = Data.class.getResource("statics/warmgoat.jpg");
    public static ImageIcon warmgoat = new ImageIcon(warmgoatURL);
    public static URL prettygoatURL = Data.class.getResource("statics/prettygoat.jpg");
    public static ImageIcon prettygoat = new ImageIcon(prettygoatURL);
    public static URL slowgoatURL = Data.class.getResource("statics/slowgoat.jpg");
    public static ImageIcon slowgoat = new ImageIcon(slowgoatURL);
    public static URL stronggoatURL = Data.class.getResource("statics/stronggoat.jpg");
    public static ImageIcon stronggoat = new ImageIcon(stronggoatURL);
    public static URL pictureURL = Data.class.getResource("statics/picture.jpg");
    public static ImageIcon picture = new ImageIcon(pictureURL);
    public static URL leftarrayURL = Data.class.getResource("statics/leftarray.jpg");
    public static ImageIcon leftarray = new ImageIcon(leftarrayURL);
    public static URL rightarrayURL = Data.class.getResource("statics/rightarray.jpg");
    public static ImageIcon rightarray = new ImageIcon(rightarrayURL);
    public static URL arrayDURl = Data.class.getResource("statics/arrayD.jpg");
    public static ImageIcon arrayD = new ImageIcon(arrayDURl);
    public static URL arrayUURl = Data.class.getResource("statics/arrayU.jpg");
    public static ImageIcon arrayU = new ImageIcon(arrayUURl);
    public static URL arrayLURl = Data.class.getResource("statics/arrayL.jpg");
    public static ImageIcon arrayL = new ImageIcon(arrayLURl);
    public static URL arrayRURl = Data.class.getResource("statics/arrayR.jpg");
    public static ImageIcon arrayR = new ImageIcon(arrayRURl);
    public static URL greenoneURl = Data.class.getResource("statics/greenone.jpg");
    public static ImageIcon greenone = new ImageIcon(greenoneURl);
    public static URL blueoneURl = Data.class.getResource("statics/blueone.jpg");
    public static ImageIcon blueone = new ImageIcon(blueoneURl);
    public static URL greentwoURl = Data.class.getResource("statics/greentwo.jpg");
    public static ImageIcon greentwo = new ImageIcon(greentwoURl);
    public static URL bluetwoURl = Data.class.getResource("statics/bluetwo.jpg");
    public static ImageIcon bluetwo = new ImageIcon(bluetwoURl);
    public static URL greenminustwoURl = Data.class.getResource("statics/greenminustwo.jpg");
    public static ImageIcon greenminustwo = new ImageIcon(greenminustwoURl);
    public static URL blueminustwoURl = Data.class.getResource("statics/blueminustwo.jpg");
    public static ImageIcon blueminustwo = new ImageIcon(blueminustwoURl);
    public static URL bluezeroURl = Data.class.getResource("statics/bluezero.jpg");
    public static ImageIcon bluezero = new ImageIcon(bluezeroURl);
    public static URL greenzeroURl = Data.class.getResource("statics/greenzero.jpg");
    public static ImageIcon greenzero = new ImageIcon(greenzeroURl);
    public static URL bananaURl = Data.class.getResource("statics/banana.jpg");
    public static ImageIcon banana = new ImageIcon(bananaURl);
    public static ImageIcon[] Picture = new ImageIcon[25];
    public static URL grassURL = Data.class.getResource("statics/grass.jpg");
    public static ImageIcon grass = new ImageIcon(grassURL);
    public static URL houseURL = Data.class.getResource("statics/house.jpg");
    public static ImageIcon house = new ImageIcon(houseURL);
    public static URL shitURL = Data.class.getResource("statics/shit.jpg");
    public static ImageIcon shit = new ImageIcon(shitURL);
}
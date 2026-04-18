/*=============================================================================================
Start the game ^_^
Have fun ^_^
  _______  _______   _______
 /       //       / |   _   |
 `---   / `---   /  |  |_\  |
    /  /     /  /   |      /
   /  `---. /  `---.|  |\  \__
  /_______|/_______||__| \____|
=============================================================================================*/

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class StartGame{

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {

        // play the background music
        Data.stream = AudioSystem.getAudioInputStream(new File("src/Music/music1.wav").getAbsoluteFile());
        Data.clip = AudioSystem.getClip();
        Data.clip.open(Data.stream);
        Data.clip.loop(Data.clip.LOOP_CONTINUOUSLY);

        new HomePage();
    }
}

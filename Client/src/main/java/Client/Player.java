package Client;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Player {
    Long current;
    Clip clip;
    String state;
    AudioInputStream audioIn;
    static String filePath;

    //Initialization of streams and clip
    public Player(InputStream is)
            throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        // create AudioInputStream object
        audioIn = AudioSystem.getAudioInputStream(is);

        // create clip reference
        clip = AudioSystem.getClip();
        // open audioInputStream to the clip
        clip.open(audioIn);

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void play()
    {
        //start the clip
        clip.start();
        state = "play";
    }

    // Method to pause the audio
    public void pause()
    {
        if (state.equals("paused"))
        {
            System.out.println("audio is already paused");
            return;
        }
        this.current = this.clip.getMicrosecondPosition();
        clip.stop();
        state = "paused";
    }

    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        audioIn = AudioSystem.getAudioInputStream(
                new File(filePath).getAbsoluteFile());
        clip.open(audioIn);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
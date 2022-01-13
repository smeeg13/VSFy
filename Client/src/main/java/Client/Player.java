package Client;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Player {
    private Long current;
    private Clip clip;
    private String state;
    private AudioInputStream audioIn;
    static String filePath;
    private String songname;

    //Initialization of streams and clip
    public Player(String songname,InputStream is)
            throws UnsupportedAudioFileException,
            IOException, LineUnavailableException
    {
        this.songname = songname;
        // create AudioInputStream object
        audioIn = AudioSystem.getAudioInputStream(is);

        // create clip reference
        clip = AudioSystem.getClip();
        // open audioInputStream to the clip
        clip.open(audioIn);
        setState("play");
        setVolume((float) (40/100.0));


        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void play()
    {
        if (state.equals("play"))
        {
            System.out.println("audio is already playing");
            return;
        }
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

    public void changeStatus(){
        if(state.equals("paused")) {
            clip.start();
            state = "play";

        } else if(state.equals("play")){
            clip.stop();
            state = "paused";
        }
    }

    // Method to reset audio stream
    public void resetAudioStream(InputStream is) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException
    {
        audioIn = AudioSystem.getAudioInputStream(is);
        clip.open(audioIn);
        clip.loop(Clip.LOOP_CONTINUOUSLY);

    }

    public float getVolume() {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }
}
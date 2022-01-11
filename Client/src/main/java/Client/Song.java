package Client;

public class Song {

    private String artist;
    private String title;
    private double songLength;

    public Song(String artist, String title, int songLength) {
        this.artist = artist;
        this.title = title;
        this.songLength = songLength;
    }

//------------------------------------------------------------------------------------------------
//All Getters & Setters

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getSongLength() {
        return songLength;
    }

    public void setSongLength(int songLength) {
        this.songLength = songLength;
    }

    @Override
    public String toString() {
        return this.artist + ": " + this.title;
    }
}


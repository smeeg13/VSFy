package Client;

import java.io.File;
import java.util.ArrayList;

public class Playlist {
    public ArrayList<File> playlist;
    private String name;


    //creation of a playlist object containing Data.Client.SongDetails in a array
    public Playlist(String name)
    {
        System.out.println("## Playlist : "+name+" created");
        this.name=name;
        playlist=new ArrayList<File>();
    }

    public ArrayList<File> getPlaylist() {
        return playlist;
    }

//    public void addSong(SongDetails song) {
//        String artist = song.getArtist();
//        String title = song.getTitle();
//
//        playlist.add(new SongDetails("fjdksfjds", "djsakdjas", 3));
//        System.out.println("new song " + artist + " - " + title + " added to playlist ");
//    }
//
//    private SongDetails findSongWithArtist(String artist) {
//
//        for (SongDetails checkedSong : this.playlist) {
//            if (checkedSong.getArtist().equals(artist)) {
//                return checkedSong;
//            }
//        }
//        return null;
//    }
//
//    private SongDetails findSongWithTitle(String title) {
//
//        for (SongDetails checkedSong : this.playlist) {
//            if (checkedSong.getTitle().equals(title)) {
//                return checkedSong;
//            }
//        }
//        return null;
//    }
//
//    //return the artist at an index given of the tab
//    public String getArtist(int index)
//    {
//        return playlist.get(index).getArtist();
//    }
//
//    public String getTitle(int index)
//    {
//        return playlist.get(index).getTitle();
//    }
//
//    public double getSongLength(int index)
//    {
//        return playlist.get(index).getSongLength();
//    }
//
//    //remove the song at the given index
//    public void removeSong(int index)
//    {
//        playlist.remove(index);
//        System.out.println(playlist.get(index).getArtist()+" - " + playlist.get(index).getTitle()+ " has been deleted");
//
//    }
//
//    //delete playlist ?? if yes create a variable int playlistSize
//
//
//

}

package com.example.simplemusicplayer;
public class localMusicBean {
    public localMusicBean(String id, String song, String singer, String album, String duration, String path) {
        this.id = id;
        this.path = path;
        this.duration = duration;
        this.album = album;
        this.singer = singer;
        this.song = song;
    }
    public localMusicBean() {
    }
    private String id;
    private String path;
    private String duration;
    private String album;
    private String singer;
    private String song;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public String getSinger() {
        return singer;
    }
    public void setSinger(String singer) {
        this.singer = singer;
    }
    public String getSong() {
        return song;
    }
    public void setSong(String song) {
        this.song = song;
    }
}

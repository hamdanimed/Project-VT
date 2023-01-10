package ensa.project_vt;

public class DataObject {
    public YoutubeDl youtubeDl;
    public Speechmatics speechmatics;
    public DataFile dataFile;
    public String videoType;
    public FFmpeg ffmpeg;
    public DataObject(YoutubeDl y,Speechmatics s,DataFile d,FFmpeg f){
        this.youtubeDl=y;
        this.speechmatics=s;
        this.dataFile=d;
        this.ffmpeg=f;
    }
}

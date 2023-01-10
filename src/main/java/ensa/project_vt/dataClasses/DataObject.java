package ensa.project_vt.dataClasses;

import ensa.project_vt.jobClasses.DataFile;
import ensa.project_vt.jobClasses.FFmpeg;
import ensa.project_vt.jobClasses.Speechmatics;
import ensa.project_vt.jobClasses.YoutubeDl;

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

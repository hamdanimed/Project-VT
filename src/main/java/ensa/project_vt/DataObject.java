package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.DataFile;
import ensa.project_vt.GenerateSubtitles.Speechmatics;
import ensa.project_vt.GenerateSubtitles.YoutubeDl;

public class DataObject {
    public YoutubeDl youtubeDl;
    public Speechmatics speechmatics;
    public DataFile dataFile;
    public String videoType;
    public DataObject(YoutubeDl y,Speechmatics s,DataFile d){
        this.youtubeDl=y;
        this.speechmatics=s;
        this.dataFile=d;
    }
}

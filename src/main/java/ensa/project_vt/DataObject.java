package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.Speechmatics;
import ensa.project_vt.GenerateSubtitles.YoutubeDl;

public class DataObject {
    public YoutubeDl youtubeDl;
    public Speechmatics speechmatics;
    public DataObject(YoutubeDl y,Speechmatics s){
        this.youtubeDl=y;
        this.speechmatics=s;
    }
}

package ensa.project_vt.localVideo;

import ensa.project_vt.Video;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class localVideo extends Video {
    private String path;
    public localVideo(String id,String videoTitle,String duration,String path){
        super(id,videoTitle,duration);
        this.path=path;
    }



}

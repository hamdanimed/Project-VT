package ensa.project_vt.GenerateSubtitles;

import ensa.project_vt.DataObject;
import ensa.project_vt.SearchViewController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class FFmpegTask extends Task<Integer> {

    private FFmpeg ffmpeg;
    private DataFile dataFile;
    private SearchViewController searchViewController;

    public FFmpegTask(FFmpeg ffmpeg, DataFile dataFile, SearchViewController searchViewController){
        this.ffmpeg=ffmpeg;
        this.dataFile=dataFile;
        this.searchViewController=searchViewController;
    }
    @Override
    protected Integer call() throws Exception {
        int exitCode=ffmpeg.convertToAudio();
        if(exitCode==1){
            System.out.println("[FFmpegTask] 'convertToAudio' Something Went Wrong , Try again");
        }else{
            dataFile.addVideo(ffmpeg.videoPath, ffmpeg.videoTitle);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    searchViewController.launchProgressUpload(ffmpeg.audioPath,ffmpeg.videoPath);
                }
            });
        }
        return 0;
    }
}

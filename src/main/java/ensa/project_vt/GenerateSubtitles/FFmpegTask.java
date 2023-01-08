package ensa.project_vt.GenerateSubtitles;

import ensa.project_vt.DataObject;
import ensa.project_vt.SearchViewController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class FFmpegTask extends Task<Integer> {

    private FFmpeg ffmpeg;
    private DataFile dataFile;
    private SearchViewController searchViewController;
    private String action;

    public FFmpegTask(FFmpeg ffmpeg, DataFile dataFile, SearchViewController searchViewController,String action){
        this.ffmpeg=ffmpeg;
        this.dataFile=dataFile;
        this.action=action;
        this.searchViewController=searchViewController;
    }
    public FFmpegTask(FFmpeg ffmpeg, DataFile dataFile,String action){
        this.ffmpeg=ffmpeg;
        this.dataFile=dataFile;
        this.action=action;
    }
    @Override
    protected Integer call() throws Exception {
        switch (this.action){
            case "convertToAudio" ->{
                int exitCode=ffmpeg.convertToAudio();
                if(exitCode==1){
                    System.out.println("[FFmpegTask] 'convertToAudio' Something Went Wrong , Try again");
                }else{
                    dataFile.addVideo(ffmpeg.videoTitle, ffmpeg.videoTitle, ffmpeg.destinationFolder);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            searchViewController.launchProgressUpload(ffmpeg.audioPath,ffmpeg.videoPath);
                        }
                    });
                }
            }
            case "changeCodec" ->{
                int exitCode= ffmpeg.changeCodec();
                if(exitCode==1){
                    System.out.println("[FFmpegTask] 'changeCodec' Something Went Wrong , Try again");
                }else{

                }
            }
        }
        return 0;
    }
}

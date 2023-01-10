package ensa.project_vt;

import ensa.project_vt.DataFile;
import ensa.project_vt.FFmpeg;
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
                searchViewController.loadingGif.setVisible(true);
                searchViewController.msgLabel.setText("Converting to Audio");
                searchViewController.msgLabel.setVisible(true);
                int exitCode=ffmpeg.convertToAudio();
                if(exitCode==1){
                    searchViewController.loadingGif.setVisible(false);
                    searchViewController.msgLabel.setVisible(false);
                    System.out.println("[FFmpegTask] 'convertToAudio' Something Went Wrong , Try again");
                }else{
                    searchViewController.loadingGif.setVisible(false);
                    searchViewController.msgLabel.setVisible(false);
                    dataFile.addVideo(ffmpeg.videoTitle, ffmpeg.videoTitle, ffmpeg.videoPath);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            searchViewController.launchProgressUpload(ffmpeg.audioPath,ffmpeg.videoTitle, ffmpeg.videoTitle, ffmpeg.videoPath,"local");
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

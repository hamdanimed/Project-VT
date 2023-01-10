package ensa.project_vt;

import ensa.project_vt.DataFile;
import ensa.project_vt.DataObject;
import ensa.project_vt.FFmpeg;
import ensa.project_vt.YoutubeDl;
import ensa.project_vt.ProgressDownloadController;
import ensa.project_vt.ProgressQualitiesController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class YoutubeDlTask extends Task<Integer> {
    private String action;
    private YoutubeDl youtubeDl;
    private Object controller;
    private DataFile dataFile;
    private FFmpeg ffmpeg;

    private DataObject dataObject;

    public YoutubeDlTask(DataObject dataObject, Object controller, String action) {
        super();
        this.dataObject=dataObject;
        this.action=action;
        this.youtubeDl =dataObject.youtubeDl;
        this.controller=controller;
        this.dataFile=dataObject.dataFile;
        this.ffmpeg=dataObject.ffmpeg;
    }

    public DataObject getDataObject() {
        return dataObject;
    }

    @Override
    protected Integer call(){
            switch (this.action) {
                case "checkQuality" -> {
                    System.out.println("Thread checkQuality");
                    ProgressQualitiesController chooseQualitesDialogController=(ProgressQualitiesController) this.controller;
                    chooseQualitesDialogController.msgLabel.setVisible(false);
                    int exitCode=this.youtubeDl.checkAvailableQualities(chooseQualitesDialogController);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(exitCode==0){
                                dataObject.youtubeDl=youtubeDl;
                                chooseQualitesDialogController.getloadingImage().setVisible(false);
                                chooseQualitesDialogController.getStartBtn().setDisable(false);
                            }else{
                                chooseQualitesDialogController.msgLabel.setText("Something Went Wrong , Try again");
                                chooseQualitesDialogController.msgLabel.setVisible(true);
                                System.out.println("[YoutubeDlTask] 'checkQuality' Something Went Wrong , Try again");
                            }
                        }
                    });

                }
                case "downloadVideoAndAudio" -> {
                    System.out.println("Thread downloadVideoAndAudio");
                    ProgressDownloadController downloadVideoAndAudioController=(ProgressDownloadController) this.controller;
                    downloadVideoAndAudioController.getErrorMsgLabel().setVisible(false);
                    int exitCode=this.youtubeDl.downloadVideoAndAudio(downloadVideoAndAudioController);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(exitCode==0){
                                ffmpeg.videoPath= youtubeDl.videoPath;
                                ffmpeg.videoType="ytb";
                                ffmpeg.videoTitle= youtubeDl.videoId;
                                int ffmpegExitCode=ffmpeg.changeCodec();
                                if(ffmpegExitCode==1){
                                    System.out.println("[YoutubeDlTask] 'ffmpeg.changeCodec()' Something went wrong , Try Again");
                                    downloadVideoAndAudioController.getErrorMsgLabel().setText("Something went wrong , try again");
                                    downloadVideoAndAudioController.getErrorMsgLabel().setVisible(true);
                                    downloadVideoAndAudioController.getStartBtn().setDisable(false);
                                    return ;
                                }
                                youtubeDl.videoPath= ffmpeg.videoPath;
                                dataFile.addVideo(youtubeDl.videoId, youtubeDl.videoTitle, ffmpeg.destinationFolder);
                                dataObject.youtubeDl=youtubeDl;
                                dataObject.ffmpeg=ffmpeg;
                                downloadVideoAndAudioController.getNextBtn().setDisable(false);
                            }else{
                                System.out.println("[YoutubeDlTask] 'downloadVideoAndAudio' Something went wrong , Try Again");
                                downloadVideoAndAudioController.getErrorMsgLabel().setText("Something went wrong , try again");
                                downloadVideoAndAudioController.getErrorMsgLabel().setVisible(true);
                                downloadVideoAndAudioController.getStartBtn().setDisable(false);
                            }
                        }
                    });
                }
                default -> {
                    System.out.println("Task was invoked for nothing");
                    return 1;
                }
            }

        return 0;
    }


    public void sendCancelSignal(){
        this.youtubeDl.setSignal(true);
    }

}

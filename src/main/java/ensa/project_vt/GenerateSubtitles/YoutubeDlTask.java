package ensa.project_vt.GenerateSubtitles;

import ensa.project_vt.DataObject;
import ensa.project_vt.ProgressQualitiesController;
import ensa.project_vt.ProgressDownloadController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class YoutubeDlTask extends Task<Integer> {
    private String action;
    private YoutubeDl youtubeDlObject;
    private Object controller;
    private DataFile dataFile;

    public YoutubeDlTask(DataObject dataObject, Object controller, String action) {
        super();
        this.action=action;
        this.youtubeDlObject=dataObject.youtubeDl;
        this.controller=controller;
        this.dataFile=dataObject.dataFile;
    }

    @Override
    protected Integer call(){
            switch (this.action) {
                case "checkQuality" -> {
                    System.out.println("Thread checkQuality");
                    ProgressQualitiesController chooseQualitesDialogController=(ProgressQualitiesController) this.controller;
                    int exitCode=this.youtubeDlObject.checkAvailableQualities(chooseQualitesDialogController);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(exitCode==0){
                                chooseQualitesDialogController.getProgressMsg().setVisible(false);
                            }else{
//                                chooseQualitesDialogController.getProgressMsg().setLayoutX(115);
//                                chooseQualitesDialogController.getProgressMsg().setText("[YoutubeDlTask] 'checkQuality' Something Went Wrong , Try again");
                                System.out.println("[YoutubeDlTask] 'checkQuality' Something Went Wrong , Try again");
                            }
                        }
                    });

                }
                case "downloadVideoAndAudio" -> {
                    System.out.println("Thread downloadVideoAndAudio");
                    ProgressDownloadController downloadVideoAndAudioController=(ProgressDownloadController) this.controller;
                    int exitCode=this.youtubeDlObject.downloadVideoAndAudio(downloadVideoAndAudioController);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(exitCode==0){
                                dataFile.addVideo(youtubeDlObject.videoId,youtubeDlObject.videoPath,"ytb");
                                downloadVideoAndAudioController.getNextBtn().setDisable(false);
                            }else{
                                System.out.println("[YoutubeDlTask] 'downloadVideoAndAudio' Something went wrong , Try Again");
                            }
                        }
                    });
                }
                default -> {
                    System.out.println("Task was invoked for nothing");
                    return 1;
                }
            }

//        try {
//            while(!isCancelled()){
//                Thread.sleep(1000);
//            }
//        }catch( InterruptedException e){
//            System.out.println("[YoutubeDlTask] task was canceled");
//        }

        return 0;
    }

    public void sendCancelSignal(){
        this.youtubeDlObject.setSignal(true);
    }

}

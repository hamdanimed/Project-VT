package ensa.project_vt.GenerateSubtitles;

import ensa.project_vt.ProgressDownloadController;
import ensa.project_vt.ProgressQualitiesController;
import ensa.project_vt.ProgressUploadAudioController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class SpeechmaticsTask extends Task<Integer> {
    private String action;
    private YoutubeDl youtubeDlObject;
    private Speechmatics speechmaticsObject;
    private Object controller;

    public SpeechmaticsTask(YoutubeDl youtubeDlObject,Speechmatics speechmaticsObject,Object controller,String action) {
        super();
        this.action=action;
        this.youtubeDlObject=youtubeDlObject;
        this.speechmaticsObject=speechmaticsObject;
        this.controller=controller;
    }

    @Override
    protected Integer call(){

        switch (this.action) {
            case "sendAudio" -> {
                System.out.println("Thread sendAudio");
                ProgressUploadAudioController uploadAudioController= (ProgressUploadAudioController) this.controller;
                this.speechmaticsObject.setAudioPath(this.youtubeDlObject.audioPath);

                int exitCode=this.speechmaticsObject.sendAudio((ProgressUploadAudioController) this.controller);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(exitCode==0){
                            System.out.println("nice");
//                            chooseQualitesDialogController.getProgressMsg().setVisible(false);
                        }else{
                            System.out.println("Something Went Wrong, Try again");
//                            chooseQualitesDialogController.getProgressMsg().setLayoutX(115);
//                            chooseQualitesDialogController.getProgressMsg().setText("Something Went Wrong , Try again");
                        }
                    }
                });

            }
            default -> {
                System.out.println("Task was Invoked");
                return 1;
            }
        }
        return 0;
    }
}

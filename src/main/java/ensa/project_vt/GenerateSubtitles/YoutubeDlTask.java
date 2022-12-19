package ensa.project_vt.GenerateSubtitles;

import ensa.project_vt.MenuDialogController;
import ensa.project_vt.ProgressController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class YoutubeDlTask extends Task<Integer> {
    private String action;
    private YoutubeDl youtubeDlObject;
    private Object controller;

    public YoutubeDlTask(YoutubeDl youtubeDlObject,Object controller,String action) {
        super();
        this.action=action;
        this.youtubeDlObject=youtubeDlObject;
        this.controller=controller;
    }

    @Override
    protected Integer call(){

        switch (this.action) {
            case "checkQuality" -> {
                System.out.println("Thread checkQuality");
                MenuDialogController chooseQualitesDialogController=(MenuDialogController) this.controller;

                int exitCode=this.youtubeDlObject.checkAvailableQualities(chooseQualitesDialogController);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(exitCode==0){
                            chooseQualitesDialogController.getProgressMsg().setVisible(false);
                        }else{
                            chooseQualitesDialogController.getProgressMsg().setLayoutX(115);
                            chooseQualitesDialogController.getProgressMsg().setText("Something Went Wrong , Try again");
                        }
                    }
                });

            }
            case "downloadVideoAndAudio" -> {
                System.out.println("Thread downloadVideoAndAudio");
                ProgressController downloadVideoAndAudio=(ProgressController) this.controller;
                int exitCode=this.youtubeDlObject.downloadVideoAndAudio(downloadVideoAndAudio);

            }
            default -> {
                System.out.println("Task was Invoked");
                return 1;
            }
        }
        return 0;
    }

}

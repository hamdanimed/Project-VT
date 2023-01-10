package ensa.project_vt;

import ensa.project_vt.DataFile;
import ensa.project_vt.DataObject;
import ensa.project_vt.Speechmatics;
import ensa.project_vt.YoutubeDl;
import ensa.project_vt.ProgressUploadAudioController;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class SpeechmaticsTask extends Task<Integer> {
    private String action;
    private String videoType="ytb";
    private YoutubeDl youtubeDl;
    private Speechmatics speechmatics;
    private DataFile dataFile;
    private Object controller;

    public SpeechmaticsTask(DataObject dataObject, Object controller, String action) {
        super();
        this.action=action;
        this.youtubeDl =dataObject.youtubeDl;
        this.speechmatics =dataObject.speechmatics;
        this.dataFile=dataObject.dataFile;
        this.controller=controller;
        if(dataObject.videoType!=null){
            this.videoType=dataObject.videoType;
        }
    }

    @Override
    protected Integer call(){

        switch (this.action) {
            case "sendAudio" -> {
                System.out.println("Thread sendAudio");
                System.out.println(this.youtubeDl.audioPath);
                ProgressUploadAudioController uploadAudioController= (ProgressUploadAudioController) this.controller;
                uploadAudioController.getErrorMsgLabel().setVisible(false);
                this.speechmatics.setAudioPath(this.youtubeDl.audioPath);

                int exitCode=this.speechmatics.sendAudio(uploadAudioController);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(exitCode==0){
                            if (videoType.equals("local")) {
                                dataFile.setJobId(youtubeDl.videoTitle,speechmatics.getJobId());
                            }else{
                                dataFile.setJobId(youtubeDl.videoId,speechmatics.getJobId());
                            }
                            uploadAudioController.getFinishBtn().setDisable(false);
                        }else{
                            System.out.println("[SpeechmaticsTask] Something Went Wrong, Try again");
                            uploadAudioController.getErrorMsgLabel().setText("Something went wrong , try again");
                            uploadAudioController.getErrorMsgLabel().setVisible(true);
                            uploadAudioController.getStartBtn().setDisable(false);
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

    public void sendCancelSignal(){
        speechmatics.setSignal(true);
    }
}

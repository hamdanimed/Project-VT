package ensa.project_vt.GenerateSubtitles;

import ensa.project_vt.DataObject;
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
                ProgressUploadAudioController uploadAudioController= (ProgressUploadAudioController) this.controller;
                this.speechmatics.setAudioPath(this.youtubeDl.audioPath);

                int exitCode=this.speechmatics.sendAudio(uploadAudioController);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(exitCode==0){
                            if (videoType.equals("local")) {
                                dataFile.setJobId(youtubeDl.videoPath,speechmatics.getJobId(),"ytb");
                            }else{

                                dataFile.setJobId(youtubeDl.videoId,speechmatics.getJobId(),"ytb");
                            }
//                            System.out.println("nice");
                        }else{
                            System.out.println("[SpeechmaticsTask] Something Went Wrong, Try again");
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

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
                                dataFile.setJobId(youtubeDl.videoPath,speechmatics.getJobId());
                            }else{

                                dataFile.setJobId(youtubeDl.videoId,speechmatics.getJobId());
                            }
//                            System.out.println("nice");
                        }else{
                            System.out.println("[SpeechmaticsTask] Something Went Wrong, Try again");
                        }
                    }
                });

            }
            case "getSubtitles" ->{
                System.out.println("Thread getSubtitles");
                int exitCode=1;

                if(this.videoType.equals("local")){
                    exitCode=this.speechmatics.getSubstitles(dataFile.getJobId(this.youtubeDl.videoPath),"local",this.youtubeDl.videoTitle);
                }else{
                    exitCode=this.speechmatics.getSubstitles(dataFile.getJobId(this.youtubeDl.videoId),this.youtubeDl.videoId,this.youtubeDl.videoId);
                }

                int finalExitCode = exitCode;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(finalExitCode ==0){
                            if (videoType.equals("local")) {
                                dataFile.setSubtitled(youtubeDl.videoPath, true,"ytb");
                            }else{
                                dataFile.setSubtitled(youtubeDl.videoId, true,"ytb");
                            }
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

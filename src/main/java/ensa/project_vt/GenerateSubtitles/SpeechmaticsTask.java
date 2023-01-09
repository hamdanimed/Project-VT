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

    private String dataFileId;
    public SpeechmaticsTask(Speechmatics speechmatics,String dataFileId,String videoType,String action){
        this.dataFileId=dataFileId;
        this.speechmatics=speechmatics;
        this.videoType=videoType;
        this.action=action;
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
//            case "getSubtitles" ->{
//                System.out.println("Thread getSubtitles");
//                int exitCode;
//
//                if(this.videoType.equals("local")){
//                    System.out.println("something local");
//                    exitCode=speechmatics.getSubstitles(dataFile.getJobId(dataFileId),"local",dataFile.getTitle(dataFileId));
//                }else{
//                    System.out.println("somehitn ytb");
//                    System.out.println(speechmatics.checkOnJob(dataFile.getJobId(dataFileId)));
//                    exitCode=speechmatics.getSubstitles(dataFile.getJobId(dataFileId),dataFileId,dataFileId);
//                }
//                System.out.println("somehitn"+exitCode);
//
//                int finalExitCode = exitCode;
//                Platform.runLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(finalExitCode ==0){
//                            dataFile.setSubtitled(dataFileId, true);
////                            if (videoType.equals("local")) {
////                                dataFile.setSubtitled(dataFileId, true,"ytb");
////                            }else{
////                                dataFile.setSubtitled(dataFileId, true,"ytb");
////                            }
//                        }else{
//                            System.out.println("[SpeechmaticsTask] Something Went Wrong, Try again");
//
//
//                        }
//                    }
//                });
//
//            }
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

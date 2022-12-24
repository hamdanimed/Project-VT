package ensa.project_vt.GenerateSubtitles;

import ensa.project_vt.ProgressQualitiesController;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FFmpeg {

    public String videoTitle;
    public String videoPath;
    public String audioPath="";
    private String appFolder;
//    private String destinationFolderPath;
    private String executableLocation="C:\\ffmpeg\\bin\\ffmpeg.exe";

    public FFmpeg(String appFolder){
//        this.videoTitle=videoTitle;
//        this.videoPath=videoPath;
        this.appFolder=appFolder;
//        this.destinationFolderPath=destinationFolderPath;
    }

    public int convertToAudio() {
        int exitCode=1;
        System.out.println("FFmpeg convertToAudio()-------------------------------------------------------------------");
        //checking the existance of an audio file for the video
        File audioFile = new File(appFolder+"\\local\\"+videoTitle+".wav");
        System.out.println(appFolder+"\\local\\"+videoTitle+audioFile.exists());
        if(audioFile.exists()){
            System.out.println("[FFmpeg] The audio of the file "+videoTitle+" already exists in appFolder/local");
            return 1;
        }
        try {
            // C:\ffmpeg\bin\ffmpeg.exe -i videoPath C:\Users\hp\PC\project-vt-files\local\videoTitle.wav
            this.audioPath=this.appFolder+"\\local\\"+this.videoTitle+".wav";
            ProcessBuilder pb = new ProcessBuilder(executableLocation,"-i",videoPath,this.appFolder+"\\local\\"+this.videoTitle+".wav");

            pb.redirectErrorStream(true); // errorstream of the process will be redirected to standard output
            Process proc = pb.start(); // start the process
            InputStream ins = proc.getInputStream();

            // creating a buffered reader
            BufferedReader read = new BufferedReader(new InputStreamReader(ins));
            read.lines().forEach(line -> {
                System.out.println(line);
            });

            read.close();
            proc.waitFor();
            proc.destroy();

            exitCode= proc.exitValue();
            if(exitCode==1){
                System.out.println("[FFmpeg] An error accured with 'convertToAudio'");
                //System.exit(1);
                return 1;
            }

        }catch(InterruptedException e){
            System.out.println("[FFmpeg] 'convertToAudio' was interrupted .");
            return 1;
        }
        catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        return exitCode;
    }

    public void setVideo(String videoTitle,String videoPath){
        this.videoTitle=videoTitle;
        this.videoPath=videoPath;
    }
}

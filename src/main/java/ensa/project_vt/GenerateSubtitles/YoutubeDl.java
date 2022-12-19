package ensa.project_vt.GenerateSubtitles;

import ensa.project_vt.MenuDialogController;
import ensa.project_vt.ProgressController;
import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class YoutubeDl {
    private String youtubelink="";
    private static String executableLocation = "C:\\Users\\hp\\Desktop\\java\\GenerateSubs\\src\\main\\resources\\youtube-dl.exe";
    private String configurationFilePath = "C:\\Users\\hp\\Desktop\\Project_VT\\src\\main\\resources\\ensa\\project_vt\\generate_subtitles\\youtube-dl-config.conf";
    private String videoAndAudioRepository = "C:\\Users\\hp\\PC\\project-vt-files\\videos-srts\\";
    public String videoId="";
    public String audioPath="";
    public String videoPath="";
    public List<String> progressValues=new ArrayList<>();

    public YoutubeDl(String videoAndAudioRepository,String configurationFilePath,String executableLocation){

        this.youtubelink="";
        this.configurationFilePath=configurationFilePath;
        this.executableLocation=executableLocation;
        this.videoAndAudioRepository=videoAndAudioRepository;
    }

    public int downloadVideoAndAudio(ProgressController progressController){
        int exitCode=1;
        System.out.println("YoutubeDL downloadVideoAndAudio()-------------------------------------------------------------------");
        if(youtubelink.length() == 0){
            System.out.println("setYoutubeLink");
            return 1;
        }
        this.videoId=youtubelink.substring(youtubelink.length()-11);

        try {
            ProcessBuilder pb = new ProcessBuilder(YoutubeDl.executableLocation,youtubelink,"-o",videoAndAudioRepository+this.videoId+"\\"+"%(id)s.%(ext)s","-f","(webm)[height<360]+bestaudio","--config-location",configurationFilePath);


            pb.redirectErrorStream(true); // errorstream of the process will be redirected to standard output
            Process proc = pb.start(); // start the process
            InputStream ins = proc.getInputStream();

            // creating a buffered reader
            BufferedReader read = new BufferedReader(new InputStreamReader(ins));
            read.lines().forEach(line -> {
                System.out.println(line);
                this.parseDownloadVideoAndAudio(line,progressController);
            });
            read.close();

            proc.waitFor();
            proc.destroy();

            exitCode= proc.exitValue();
            if(exitCode==1){
                System.out.println("[YoutubeDl] An error accured with 'downloadVideoAndAudio'");
//                System.exit(1);
                return 1;
            }
        }
        catch(InterruptedException e){
            System.out.println("[YoutubeDl] 'downloadVideoAndAudio' was interrupted .");
            return 1;
        }
        catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        return exitCode;
    }

    private void parseDownloadVideoAndAudio(String line, ProgressController progressController){
        String[] outputLineAsArray=line.split("\s+");

        if(outputLineAsArray[0].equals("[download]")){

            //getting video path
            this.videoPath=videoAndAudioRepository+this.videoId+"\\"+this.videoId+".mp4";
//            if(outputLineAsArray[1].equals("Destination:")){
//                //getting video path
//                System.out.println(outputLineAsArray[2]);
//                this.videoPath=outputLineAsArray[2];
//            }

            if (line.contains("ETA")) {
                //Tracking progress (2 ways)
                this.progressValues=new ArrayList<>(Arrays.asList(outputLineAsArray[1],outputLineAsArray[3],outputLineAsArray[5],outputLineAsArray[7]));
//                this.progressValues.addAll(Arrays.asList(outputLineAsArray[1],outputLineAsArray[3],outputLineAsArray[5],outputLineAsArray[7]));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        progressController.setProgress(outputLineAsArray[5],outputLineAsArray[7],outputLineAsArray[1],outputLineAsArray[3]);

                    }
                });
                System.out.println(this.progressValues.toString());
//                this.progressValues.clear();
            }
        }

        if(outputLineAsArray[0].equals("[ffmpeg]")){

            if(outputLineAsArray[1].equals("Destination:")){
                //getting audio path
                this.audioPath=outputLineAsArray[2];
            }
        }
//        System.out.println(Arrays.toString(outputLineAsArray));
    }

    public int checkAvailableQualities(MenuDialogController controller) {
        int exitCode=1;
        System.out.println("YoutubeDL checkAvailableQualities()-------------------------------------------------------------------");
        if(youtubelink.length() == 0){
            System.out.println("setYoutubeLink");
            return 1;
        }
        try {
            ProcessBuilder pb = new ProcessBuilder(YoutubeDl.executableLocation,youtubelink,"-F");
            this.videoId=youtubelink.substring(youtubelink.length()-11);

            pb.redirectErrorStream(true); // errorstream of the process will be redirected to standard output
            Process proc = pb.start(); // start the process
            InputStream ins = proc.getInputStream();

            // creating a buffered reader
            BufferedReader read = new BufferedReader(new InputStreamReader(ins));
            read.lines().forEach(line -> {
//                System.out.println(line);
                parseCheckAvailableQualities(line);
            });
            read.close();
            proc.waitFor();
            proc.destroy();

            exitCode= proc.exitValue();
            if(exitCode==1){
                System.out.println("[YoutubeDl] An error accured with 'checkAvailableQualities'");
//                System.exit(1);
                return 1;
            }

            System.out.println("AUDIO OPTIONS :");
            for (int i=0;i<audioOptions.size();i++){
                System.out.println(audioOptions.get(i));
            }
            System.out.println("VIDEO OPTIONS :");
            for (int i=0;i<videoOptions.size();i++){
                System.out.println(videoOptions.get(i));
            }
            controller.setVideoQualityOptions(videoOptions,audioOptions);

        }catch(InterruptedException e){
            System.out.println("[YoutubeDl] 'checkAvailableQualities' was interrupted .");
            return 1;
        }
        catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        audioOptions=new ArrayList<>();
        videoOptions=new ArrayList<>();
        return exitCode;
    }

    List<String> audioOptions=new ArrayList<>();
    List<String> videoOptions=new ArrayList<>();
    private void parseCheckAvailableQualities(String line){
//        System.out.println(line);
        String[] lineAsArray=line.replace(",","").split("\s+");

        ArrayList<String> filteredDownLine=new ArrayList<>();

        int[] audioIndexs={0,1,5,lineAsArray.length-1};
        int[] videoIndexs={0,1,3,4,lineAsArray.length-1};

        String regex="[0-9]+";
        Pattern p= Pattern.compile(regex);
        Matcher m=p.matcher(lineAsArray[0]);
        //checking if the line is a quality line
        if(m.matches()){
            if(line.contains("audio")){
                for(int i=0;i<audioIndexs.length;i++){
                    filteredDownLine.add(lineAsArray[audioIndexs[i]]);
                }
                this.audioOptions.add(filteredDownLine.toString());
//                this.audioOptions.add(Arrays.asList(lineAsArray).toString());
            }else {
                for (int i=0;i<videoIndexs.length;i++){
                    filteredDownLine.add(lineAsArray[videoIndexs[i]]);
                }
                this.videoOptions.add(filteredDownLine.toString());
//                this.videoOptions.add(Arrays.asList(lineAsArray).toString());
            }
        }

    }

    public String getYoutubelink() {
        return youtubelink;
    }

    public void setYoutubelink(String youtubelink) {
        this.youtubelink = youtubelink;
    }
}

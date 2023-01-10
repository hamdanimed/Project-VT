package ensa.project_vt;

import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class YoutubeDl {
    private String youtubelink="";
    private static String executableLocation = "C:\\Users\\hp\\Desktop\\java\\GenerateSubs\\src\\main\\resources\\youtube-dl.exe";
    private String configurationFilePath = "C:\\Users\\hp\\Desktop\\Project_VT\\src\\main\\resources\\ensa\\project_vt\\generate_subtitles\\youtube-dl-config.conf";
    private String videoAndAudioRepository = "C:\\Users\\hp\\PC\\project-vt-files\\videos-srts\\";
    public String videoId="";
    public String videoTitle="";
    public String audioPath="";
    public String videoPath="";
    public String audioQuality="";
    public String videoQuality="";
    private boolean signal=false;
    public List<String> progressValues=new ArrayList<>();

    public YoutubeDl(String videoAndAudioRepository,String configurationFilePath,String executableLocation){

        this.youtubelink="";
        this.configurationFilePath=configurationFilePath;
        this.executableLocation=executableLocation;
        this.videoAndAudioRepository=videoAndAudioRepository;
    }

    public int downloadVideoAndAudio(ProgressDownloadController progressController){
        int exitCode=1;
        System.out.println("YoutubeDL downloadVideoAndAudio()-------------------------------------------------------------------");
        if(youtubelink.length() == 0){
            System.out.println("setYoutubeLink");
            return 1;
        }
        this.videoId=youtubelink.substring(youtubelink.length()-11);

        try {
//            ProcessBuilder pb = new ProcessBuilder(YoutubeDl.executableLocation,youtubelink,"-o",videoAndAudioRepository+this.videoId+"\\"+"%(id)s.%(ext)s","-f","(webm)[height<360]+bestaudio","--config-location",configurationFilePath);
            System.out.println(audioQuality+" "+videoQuality);
            ProcessBuilder pb = new ProcessBuilder(YoutubeDl.executableLocation,youtubelink,"-o",videoAndAudioRepository+"tmp"+"\\"+"/%(id)s.%(ext)s","-f",videoQuality+"+"+audioQuality,"--ffmpeg-location","src/main/resources/ensa/project_vt/project-vt-files/ffmpeg.exe","--config-location",configurationFilePath);


            pb.redirectErrorStream(true); // errorstream of the process will be redirected to standard output
            Process proc = pb.start(); // start the process
            InputStream ins = proc.getInputStream();

            // creating a buffered reader
            BufferedReader read = new BufferedReader(new InputStreamReader(ins));
            read.lines().forEach(line -> {
                if(this.signal){
                    proc.destroy();
                }
                //when the video is already downloaded
                if(line.contains("has already been downloaded")){
                    System.out.println("[YoutubeDl] the video has already been downloaded");
                    proc.destroy();
                }
                System.out.println(line);
                this.parseDownloadVideoAndAudio(line,progressController);
            });
            read.close();

            proc.waitFor();
            proc.destroy();

            exitCode= proc.exitValue();
            if(exitCode==1){
                if(this.signal){
                    System.out.println("[YoutubeDl] 'downloadVideoAndAudio' was canceled");
                    this.deleteDownload(new File(videoAndAudioRepository+"tmp"));
                    this.signal=false;
                }else{
                    System.out.println("[YoutubeDl] An error accured with 'downloadVideoAndAudio'");
    //                System.exit(1);
                }
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

    private void parseDownloadVideoAndAudio(String line, ProgressDownloadController progressController){
        String[] outputLineAsArray=line.split("\s+");

        if(outputLineAsArray[0].equals("[download]")){

            //getting video path
            this.videoPath=videoAndAudioRepository+"tmp"+"\\"+this.videoId+".mp4";
            this.audioPath=videoAndAudioRepository+this.videoId+"\\"+this.videoId+".wav";
//            this.videoPath=videoAndAudioRepository+this.videoId+"\\"+this.videoId+".mp4";
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

    }

    public int checkAvailableQualities(ProgressQualitiesController controller) {
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
                if(this.signal){
                    proc.destroy();
                }
//                System.out.println(line);
                parseCheckAvailableQualities(line);
            });
            read.close();
            proc.waitFor();
            proc.destroy();

            exitCode= proc.exitValue();
            if(exitCode==1){
                if(this.signal){
                    System.out.println("[YoutubeDl] 'checkAvailableQualities' was canceled");
                    this.signal=false;
                }else{
                    System.out.println("[YoutubeDl] An error accured with 'checkAvailableQualities'");
    //                System.exit(1);
                }
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
            }else {
                for (int i=0;i<videoIndexs.length;i++){
                    filteredDownLine.add(lineAsArray[videoIndexs[i]]);
                }
                this.videoOptions.add(filteredDownLine.toString());
            }
        }

    }

    private void deleteDownload(File folder){
        for(File subfile:folder.listFiles()){
            if(subfile.isDirectory()){
                deleteDownload(subfile);
            }
            subfile.delete();
        }
        folder.delete();
    }

    public String getYoutubelink() {
        return youtubelink;
    }

    public void setYoutubelink(String youtubelink) {
        this.youtubelink = youtubelink;
        this.videoId=youtubelink.substring(youtubelink.length()-11);
    }

    public void setAudioQuality(String audioQuality) {
        this.audioQuality = audioQuality;
    }

    public void setVideoQuality(String videoQuality) {
        this.videoQuality = videoQuality;
    }
    public void setSignal(boolean signal) {
        this.signal = signal;
    }
}

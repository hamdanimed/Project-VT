package ensa.project_vt;

import java.io.*;

public class FFmpeg {

    public String videoTitle;
    public String videoPath;
    public String destinationFolder;
    public String tmpFolder;
    public String audioPath="";
    private String appFolder;
    public String videoType="local";
    private String executableLocation="src\\main\\resources\\ensa\\project_vt\\project-vt-files\\ffmpeg.exe";

    public FFmpeg(String appFolder){
        this.appFolder=appFolder;
        this.tmpFolder=appFolder+"tmp";
    }

    public int convertToAudio() {
        int exitCode=1;
        System.out.println("FFmpeg convertToAudio()-------------------------------------------------------------------");
        String destination=appFolder+"local\\"+videoTitle+".wav";
        destinationFolder=appFolder+"local";
        if(!videoType.equals("local")){
            System.out.println("[FFmpeg] the video type isn't 'local' , this function isn't available for downloaded videos");
            return 1;
        }
        //checking the existance of an audio file for the video
        File audioFile = new File(destination);
//        System.out.println(appFolder+"\\local\\"+videoTitle+audioFile.exists());
        if(audioFile.exists()){
            System.out.println("[FFmpeg] The audio of the file "+videoTitle+" already exists in appFolder/local");
            return 1;
        }
        try {
            // C:\ffmpeg\bin\ffmpeg.exe -i videoPath C:\Users\hp\PC\project-vt-files\local\videoTitle.wav
            this.audioPath=this.appFolder+"\\local\\"+this.videoTitle+".wav";
            ProcessBuilder pb = new ProcessBuilder(executableLocation,"-i",videoPath,destination);

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
    public int changeCodec() {
        int exitCode=1;
        String destination;
        if(videoType.equals("local")){
            System.out.println("[FFmpeg] the video type is 'local' , this function isn't available for local videos");
            return 0;
//            destination=this.appFolder+"\\"+"local"+"\\"+this.videoPath+".mp4";
        }else{
            destination=this.appFolder+videoTitle+"\\"+this.videoTitle+".mp4";
            destinationFolder=this.appFolder+videoTitle;
        }

        File folder = new File(this.appFolder+videoTitle);
        if (!folder.exists()){
            folder.mkdirs();
        }else{
            deleteDownload(new File(this.tmpFolder));
            videoPath=destination;
            return 0;
        }

        System.out.println("FFmpeg changeCodec()-------------------------------------------------------------------");
        try {
            ProcessBuilder pb = new ProcessBuilder(executableLocation,"-i",videoPath,"-vcodec","libx264","-acodec","aac",destination);

            pb.redirectErrorStream(true); // errorstream of the process will be redirected to standard output
            Process proc = pb.start(); // start the process
            InputStream ins = proc.getInputStream();

            // creating a buffered reader
            BufferedReader read = new BufferedReader(new InputStreamReader(ins));
            read.lines().forEach(line -> {
//                System.out.println(line);
            });

            read.close();
            proc.waitFor();
            proc.destroy();

            exitCode= proc.exitValue();
            if(exitCode==1){
                System.out.println("[FFmpeg] An error accured with 'changeCodec'");
                //System.exit(1);
                return 1;
            }

        }catch(InterruptedException e){
            System.out.println("[FFmpeg] 'changeCodec' was interrupted .");
            return 1;
        }
        catch (UnsupportedOperationException | IOException e) {
            e.printStackTrace();
        }
        deleteDownload(new File(this.tmpFolder));
        videoPath=destination;
        return exitCode;
    }

    private void deleteDownload(File folder){
//        System.out.println(folder.getPath());
        for(File subfile:folder.listFiles()){
            if(subfile.isDirectory()){
                deleteDownload(subfile);
            }
            if(subfile.getName().contains(".wav")){
                File targetDirectory=new File(this.appFolder+videoTitle);
                subfile.renameTo(new File(targetDirectory.getAbsolutePath()+"\\"+subfile.getName()));
            }
            subfile.delete();
        }
        folder.delete();
    }

    public void setVideo(String videoTitle,String videoPath){
        this.videoTitle=videoTitle;
        this.videoPath=videoPath;
    }
}

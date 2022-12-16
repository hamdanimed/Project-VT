package ensa.project_vt;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Speechmatics {
    private String key="xXGWZL5fFRY8gMXLyYDNEcjIdrn5Twmt";
    private String configFilePath="config=<C:\\Users\\hp\\Desktop\\java\\GenerateSubs\\src\\main\\resources\\speechmatics-config-standard.json";
    private String subtitlesRepository = "C:\\Users\\hp\\PC\\project-vt-files\\videos-srts\\";
    private String audioPath="data_file=@";
    private String jobId="";
    private String youtubeId="";
    private int httpCode=000;
    private String jobStatus="done"; //Running-Done-Rejected
    private List<String> sendAudioProgress=new ArrayList<>(Arrays.asList("0", "0", "0", "0", "0", "0", "0", "0", "--:--:--", "--:--:--", "--:--:--", "0"));

    public Speechmatics(String subtitlesRepository,String configFilePath){
        this.subtitlesRepository=subtitlesRepository;
        this.configFilePath="config=<"+configFilePath;
    }

    public void sendAudio(){
        System.out.println("Speechmatics sendAudio()-------------------------------------------------------------------");
        try {
            //POST AN AUDIO ,RECIEVE AND ID FOR THE JOB
            ProcessBuilder pb = new ProcessBuilder("curl","-L","-X","POST","https://asr.api.speechmatics.com/v2/jobs/","-H","Authorization: Bearer "+this.key,"-F",audioPath,"-F",configFilePath,"-w","%{http_code}");

            pb.redirectErrorStream(true); // errorstream of the process will be redirected to standard output
            Process proc = pb.start(); // start the process

            InputStream ins = proc.getInputStream();

            BufferedReader read = new BufferedReader(new InputStreamReader(ins));
            String[] lastLine = new String[1];
            read.lines().forEach(line -> {
                System.out.println(line);
                parseSendAudio(line);
                lastLine[0] =line;
            });

            read.close(); // close the buffered reader
            proc.waitFor();
            proc.destroy();// finally destroy the process

            //to get http code of the post request
            System.out.println(lastLine[0]);
            this.httpCode=Integer.parseInt(lastLine[0].substring(lastLine[0].length()-3));
            if(this.httpCode > 299 || this.httpCode <200){
                System.out.println("[Speechmatics : sendAudio] An error accured with 'sendAudio' , http code : "+this.httpCode);
                System.exit(1);
                //return ;
            }

        } catch (UnsupportedOperationException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void parseSendAudio(String line){
        String[] outputLineAsArray=line.trim().split("\s+");
//        System.out.println(Arrays.toString(outputLineAsArray));

        if(outputLineAsArray.length == 1){
            //to get id
            if(outputLineAsArray[0].contains("id")){
                String idString=outputLineAsArray[0];
                Object idObject= JSONValue.parse(line);
                JSONObject idJsonObject=(JSONObject) idObject;
                this.jobId=(String)idJsonObject.get("id");
            }
        }

        if(outputLineAsArray.length >1){
            String regex="[0-9]+";
            Pattern p= Pattern.compile(regex);
            Matcher firstField=p.matcher(outputLineAsArray[0]);
            Matcher lastField=p.matcher(outputLineAsArray[outputLineAsArray.length-1]);
            //checking if the line is a progress line
            if(firstField.matches() && lastField.matches()){
//                this.sendAudioProgress=new ArrayList<>(Arrays.asList(outputLineAsArray));
                this.sendAudioProgress=new ArrayList<>();
                // % sent,size of file getting sent,speed of upload,time left for the upload to be done
                this.sendAudioProgress.add(Arrays.asList(outputLineAsArray[0],outputLineAsArray[1],outputLineAsArray[7],outputLineAsArray[10]).toString());
                System.out.println(this.sendAudioProgress.toString());
            }
        }

    }

    public void getSubstitles(String id,String youtubeId){
        if(id.length() == 0){
            System.out.println("[Speechmatics : getSubtitles] need a job id");
        }

        this.checkOnJob(id);

        if(!this.jobStatus.equals("done")){
            System.out.println("[Speechmatics : getSubtitles] The job is "+this.jobStatus+".");
            System.exit(1);
        }
        System.out.println("Speechmatics getSubtitiles()-------------------------------------------------------------------");

        //checking the existance of the srt file
        File subsFile = new File(subtitlesRepository+youtubeId+"\\"+youtubeId+".srt");
        subsFile.exists();
        if(subsFile.exists()){
            System.out.println("File "+youtubeId+".srt already exists");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Do you want to override the file ? (0:no,1:yes) :");
            int choice = scanner.nextInt();
            if(choice == 0) {
                return ;
            }
        }

        //GET SUBTITLES
        try {
            ProcessBuilder pb = new ProcessBuilder("curl","--silent","-L","-X","GET","https://asr.api.speechmatics.com/v2/jobs/"+id+"/transcript?format=srt","-H","Authorization: Bearer "+this.key,"-w","%{http_code}");

            pb.redirectErrorStream(true); // errorstream of the process will be redirected to standard output
            Process proc = pb.start(); // start the process

            InputStream ins = proc.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(ins));

            StringBuilder sb = new StringBuilder();
            read.lines().forEach(line -> {
//                System.out.println(line);
                sb.append(line+"\n");
            });
            read.close(); // close the buffered reader

            proc.waitFor();
            proc.destroy();// finally destroy the process

            //getting http code fo the request and writing the subtitles to a srt file
            parseGetSubtitles(sb,youtubeId);
        } catch (UnsupportedOperationException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        System.out.println( "File in "+subtitlesRepository+youtubeId+"\\"+youtubeId+".srt");
    }

    private void parseGetSubtitles(StringBuilder sb,String youtubeId){
        this.httpCode= Integer.parseInt(sb.substring(sb.length()-4,sb.length()-1));
        sb.delete(sb.length()-4,sb.length());

        if(this.httpCode > 299 || this.httpCode <200){
            System.out.println("[Speechmatics : getSubtitles] An error accured with 'getSubtitles' , http code : "+this.httpCode);
            System.exit(1);
//            return ;
        }

        //CREATE FILE
        try {
            File myObj = new File(subtitlesRepository+youtubeId+"\\"+youtubeId+".srt");
            if (myObj.createNewFile()) {
                System.out.println("[Speechmatics : getSubtitles] File created: " + myObj.getName());
            }
//            else {
//                System.out.println("File "+myObj.getName()+" already exists.");
//            }
        } catch (IOException e) {
            System.out.println("[Speechmatics : getSubtitles] An error occurred while creating "+youtubeId+".srt .");
            e.printStackTrace();
        }

        //WRITE TO THE FILE
        try {
            FileWriter myWriter = new FileWriter(subtitlesRepository+youtubeId+"\\"+youtubeId+".srt");
            myWriter.write(sb.toString());
            myWriter.close();
            System.out.println("[Speechmatics : getSubtitles] Successfully wrote to "+youtubeId+".srt .");
        } catch (IOException e) {
            System.out.println("[Speechmatics : getSubtitles] An error occurred while writing to "+youtubeId+".srt .");
            e.printStackTrace();
        }
    }

    public void checkOnJob(String id){
        System.out.println("Speechmatics checkOnJob()-------------------------------------------------------------------");
        try {
            //CHECK ON A JOB
            ProcessBuilder pb = new ProcessBuilder("curl","--silent","-L", "-X", "GET", "https://asr.api.speechmatics.com/v2/jobs/"+id+"/", "-H", "Authorization: Bearer xXGWZL5fFRY8gMXLyYDNEcjIdrn5Twmt", "-w", "http code : %{http_code}");

            pb.redirectErrorStream(true); // errorstream of the process will be redirected to standard output
            Process proc = pb.start(); // start the process

            InputStream ins = proc.getInputStream();

            BufferedReader read = new BufferedReader(new InputStreamReader(ins));

            read.lines().forEach(line -> {
//                System.out.println(line);
                parseCheckOnJob(line);
            });
            read.close(); // close the buffered reader
            proc.waitFor();
            proc.destroy();// finally destroy the process

        } catch (UnsupportedOperationException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void parseCheckOnJob(String line){
        String[] outputLineAsArray=line.trim().split("\s+");
//        System.out.println(Arrays.toString(outputLineAsArray));
        if(line.contains("http code")){
            this.httpCode=Integer.parseInt(line.substring(line.length()-3));

            if(this.httpCode > 299 || this.httpCode <200){
                System.out.println("[Speechmatics : checkOnJob] An error accured with 'checkOnJob' , http code : "+this.httpCode);
                System.exit(1);
    //            return ;
            }
        }
        if(outputLineAsArray.length == 1){
//            System.out.println(Arrays.toString(outputLineAsArray));
            if(outputLineAsArray[0].contains("job")){
                String initialObjectString=outputLineAsArray[0];
                Object initialObject= JSONValue.parse(initialObjectString);
                JSONObject initialJsonObject=(JSONObject) initialObject;

                JSONObject jobJsonObject=(JSONObject) initialJsonObject.get("job");
                JSONObject configJsonObject=(JSONObject) jobJsonObject.get("config");
//                System.out.println(jobJsonObject);
//                System.out.println(configJsonObject);

                this.jobStatus=(String) jobJsonObject.get("status");
                System.out.println((String) jobJsonObject.get("status"));
            }
        }
    }

    public void setAudioPath(String s){
        audioPath="data_file=@"+s;
//        audioPath.concat(s);
    }
    public String getAudioPath(){
        return audioPath;
    }
    public void setJobId(String jobId){
        this.jobId=jobId;
    }
    public String getJobId(){
        return this.jobId;
    }
//    public int getHttpCode(){
//        return this.httpCode;
//    }
    public void setYoutubeId(String youtubeId) {
        this.youtubeId=youtubeId;
    }
}

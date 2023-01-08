package ensa.project_vt.GenerateSubtitles;


import ensa.project_vt.GenerateSubtitles.DataFile;

import java.io.*;
import java.util.Scanner;

public class GenerateSubsTesting {
    public static void main(String[] args) throws InterruptedException {
//        String speechmaticsConfigFilePath="";
//        String youtubeDlConfigFilePath="";
//        String youtubeDlExePath="";
//
//        File myObjForInfo = new File("src/main/resources/ensa/project_vt/generate_subtitles/speechmatics-config-standard.json");
//        if (myObjForInfo.exists()) {
//            speechmaticsConfigFilePath= myObjForInfo.getAbsolutePath();
//        } else {
//            System.out.println("The file does not exist.");
//        }
//        myObjForInfo = new File("src/main/resources/ensa/project_vt/generate_subtitles/youtube-dl-config.conf");
//        if (myObjForInfo.exists()) {
//            youtubeDlConfigFilePath= myObjForInfo.getAbsolutePath();
//        } else {
//            System.out.println("The file does not exist.");
//        }
//        myObjForInfo = new File("src/main/resources/ensa/project_vt/generate_subtitles/youtube-dl.exe");
//        if (myObjForInfo.exists()) {
//            youtubeDlExePath= myObjForInfo.getAbsolutePath();
//        } else {
//            System.out.println("The file does not exist.");
//        }
//
//
//        YoutubeDl vid=new YoutubeDl("C:\\Users\\hp\\PC\\project-vt-files\\",youtubeDlConfigFilePath,youtubeDlExePath);
//        vid.setYoutubeLink("https://www.youtube.com/watch?v=-EbzDqtZEh4");
//        vid.checkAvailableQualities();
//        vid.downloadVideoAndAudio();
//
//        System.out.println("Video id : "+vid.videoId);
//        System.out.println("Video Path : "+vid.videoPath);
//        System.out.println("Audio Path : "+vid.audioPath);
//
//        Speechmatics speech=new Speechmatics("C:\\Users\\hp\\PC\\project-vt-files\\",speechmaticsConfigFilePath);
//        speech.setAudioPath(vid.audioPath);
//        speech.setYoutubeId(vid.videoId);
//        speech.sendAudio();
//        for(int i=60;i>=0;i--){
//            System.out.println("wait "+i+" second");
//            Thread.sleep(1000);
//        }
//        speech.getSubstitles(speech.getJobId(), vid.videoId);


//        Object dataObject=JSONValue.parse("{}");
//        JSONObject dataJsonObject=(JSONObject) dataObject;

        DataFile data=new DataFile("C:\\Users\\hp\\PC\\project-vt-files\\");
        System.out.println(data.isVideoDownloaded("l086Ps61o1U"));
        data.addVideo("l086Ps61o1U","sdlkfjlksjdf","path");
        data.addVideo("l086sds61o1U","NSIT","path");
        data.setJobId("l086Ps61o1U","dddddddttt");
        data.setSubtitled("l086Ps61o1U",true);
        System.out.println(data.isSubtitled("l086Ps61o1U"));
        System.out.println(data.getJobId("l086Ps61o1U"));
        data.deleteVideo("l086sds61o1U");

//        JSONObject dataObject=new JSONObject();
//        JSONObject content=new JSONObject();
//        content.put("jobId","sldkfjskl");
//        content.put("title","something somewhere somehow");
//
//        dataObject.put("-EbzDqtZEh4",content);
//        System.out.println(dataObject);
//
//
//        //WRITE TO A FILE
//        try {
//            FileWriter myWriter = new FileWriter("C:\\Users\\hp\\PC\\project-vt-files\\data.json");
//            myWriter.write(dataObject.toJSONString());
//            myWriter.close();
//            System.out.println("Successfully wrote to the file.");
//        } catch (IOException e) {
//            System.out.println("An error occurred while writing to the file.");
//            e.printStackTrace();
//        }
//        String data="{}";
//        //READ A FILE
//        try {
//            File myObj = new File("C:\\Users\\hp\\PC\\project-vt-files\\data.json");
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//                data = myReader.nextLine();
//                System.out.println(data);
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//
//        dataObject=(JSONObject) JSONValue.parse(data);
//        JSONObject contentObject=(JSONObject) JSONValue.parse(dataObject.get("-EbzDqtZEh4").toString());
//        System.out.println(dataObject);
//        System.out.println(contentObject);
//        System.out.println(contentObject.get("t"));


//        String idString=outputLineAsArray[0];
//        Object idObject= JSONValue.parse(line);
//        JSONObject idJsonObject=(JSONObject) idObject;
//        this.jobId=(String)idJsonObject.get("id");

//        //check if video is subtitled ?
//        File folder=new File("C:\\Users\\hp\\PC\\project-vt-files\\videos-srts\\-EbzDqtZEh4");
//        File[] list=folder.listFiles();
//
//        for (int i=0;i<list.length;i++){
//            if((list[i].getName().substring(list[i].getName().length()-4)).equals(".srt")){
//                System.out.println(list[i].getName());
//            }
//        }


//        StringBuilder sb = speechmaticsApi("z22xcbwbfy");
//        fileManipulation(sb);

//        JSONObject file=new JSONObject();
//        file.put("Full Name", "Ritu Sharma");
//        file.put("Roll No.", 1704310046);
//        file.put("Tuition Fees", 65400l);
//
//        System.out.print(file);
//        System.out.println(file.toString());
//
//        String config="{\"type\": \"transcription\",\"transcription_config\": {\"operating_point\": \"standard\",\"language\": \"en\"}}";

    }

    public static void fileManipulation(StringBuilder sb) {
        //CREATE FILE
        try {
            File myObj = new File("C:\\Users\\hp\\PC\\ytb-dl\\filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }

        //WRITE TO THE FILE
        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\hp\\PC\\ytb-dl\\filename.txt");
            myWriter.write(sb.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }

        //READ A FILE
        try {
            File myObj = new File("C:\\Users\\hp\\PC\\ytb-dl\\filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //GET FILE INFO
        File myObjForInfo = new File("C:\\Users\\hp\\PC\\ytb-dl\\filename.txt");
        if (myObjForInfo.exists()) {
            System.out.println("File name: " + myObjForInfo.getName());
            System.out.println("Absolute path: " + myObjForInfo.getAbsolutePath());
            System.out.println("Writeable: " + myObjForInfo.canWrite());
            System.out.println("Readable " + myObjForInfo.canRead());
            System.out.println("File size in bytes " + myObjForInfo.length());
        } else {
            System.out.println("The file does not exist.");
        }

        //DELETE A FILE
        File myObj = new File("C:\\Users\\hp\\PC\\ytb-dl\\filename.txt");
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    public static StringBuilder speechmaticsApi(String id) {
        try {
            //POST AN AUDIO ,RECIEVE AND ID FOR THE JOB
//            ProcessBuilder pb = new ProcessBuilder("curl","-L","-X","POST","https://asr.api.speechmatics.com/v2/jobs/","-H","Authorization: Bearer xXGWZL5fFRY8gMXLyYDNEcjIdrn5Twmt","-F","data_file=@C:\\Users\\hp\\PC\\ytb-dl\\-EbzDqtZEh4.wav","-F","config=<C:\\Users\\hp\\PC\\ytb-dl\\config2.json","-w","http code : %{http_code}");
            //GET SUBTITLES
            ProcessBuilder pb = new ProcessBuilder("curl","-L","-X","GET","https://asr.api.speechmatics.com/v2/jobs/"+id+"/transcript?format=srt","-H","Authorization: Bearer xXGWZL5fFRY8gMXLyYDNEcjIdrn5Twmt","-w","http code : %{http_code}");
            //CHECK ON A JOB
//            ProcessBuilder pb = new ProcessBuilder("curl", "--silent", "-L", "-X", "GET", "https://asr.api.speechmatics.com/v2/jobs/amszmv6lml/", "-H", "Authorization: Bearer xXGWZL5fFRY8gMXLyYDNEcjIdrn5Twmt", "-w", "http code : %{http_code}");

            pb.redirectErrorStream(true); // errorstream of the process will be redirected to standard output
            Process proc = pb.start(); // start the process

            // get the inputstream from the process which would get printed on the console / terminal
            InputStream ins = proc.getInputStream();

            // creating a buffered reader
            BufferedReader read = new BufferedReader(new InputStreamReader(ins));
            StringBuilder sb = new StringBuilder();

            read.lines().forEach(line -> {
                System.out.println(line);
                sb.append(line + "\n");
            });
            read.close(); // close the buffered reader

            // wait until process completes, this should be always after the input_stream of processbuilder is read to avoid deadlock situations
            proc.waitFor();

            // exit code can be obtained only after process completes, 0 indicates a successful completion
            // int exitCode = proc.exitValue();
            proc.destroy();// finally destroy the process

            System.out.println("exit code :" + proc.exitValue());
//            System.out.println(sb);
            return sb;

        } catch (UnsupportedOperationException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static StringBuilder useProcessBuilder(ProcessBuilder pb){
        try {
//            ProcessBuilder pb = cmd;

            pb.redirectErrorStream(true); // errorstream of the process will be redirected to standard output
            Process proc = pb.start(); // start the process

            // get the inputstream from the process which would get printed on the console / terminal
            InputStream ins = proc.getInputStream();

            // creating a buffered reader
            BufferedReader read = new BufferedReader(new InputStreamReader(ins));
            StringBuilder sb = new StringBuilder();
            read.lines().forEach(line -> {
                System.out.println(line);
                sb.append(line + "\n");
            });
            read.close(); // close the buffered reader

            // wait until process completes, this should be always after the input_stream of processbuilder is read to avoid deadlock situations
            proc.waitFor();

            // exit code can be obtained only after process completes, 0 indicates a successful completion
            // int exitCode = proc.exitValue();
            proc.destroy();// finally destroy the process

            System.out.println("exit code :" + proc.exitValue());
//            System.out.println(sb);
            return sb;
        }
        catch (UnsupportedOperationException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return new StringBuilder("null");
    }


}
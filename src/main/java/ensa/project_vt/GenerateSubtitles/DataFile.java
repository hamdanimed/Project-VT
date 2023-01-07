package ensa.project_vt.GenerateSubtitles;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DataFile {
    String appFolderPath;

    public DataFile(String appFolderPath){
        this.appFolderPath=appFolderPath;
    }

    public String isVideoDownloaded(String id) {
        String data = "{}";
        //READ A FILE
        try {
            File myObj = new File(appFolderPath + "data.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject dataObject = (JSONObject) JSONValue.parse(data);
        if (dataObject.containsKey(id)) {
            return appFolderPath + "" + id;
        } else {
            return "null";
        }
    }
    public void addVideo(String id,String title){
        String data="{}";
        //READ A FILE
        try {
            File myObj = new File(appFolderPath+"data.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject dataObject=(JSONObject) JSONValue.parse(data);
//        if(dataObject.containsKey(id)){
//            return ;
//        }
        JSONObject content=new JSONObject();
        content.put("title",title);

        dataObject.put(id,content);
        System.out.println(dataObject);

        //WRITE TO A FILE
        try {
            FileWriter myWriter = new FileWriter(appFolderPath+"data.json");
            myWriter.write(dataObject.toJSONString());
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    public void deleteVideo(String id){
        String data="{}";
        //READ A FILE
        try {
            File myObj = new File(appFolderPath+"data.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("[DataFile] An error occurred.");
            e.printStackTrace();
        }

        JSONObject dataObject=(JSONObject) JSONValue.parse(data);
        if(!dataObject.containsKey(id)){
            return ;
        }

        dataObject.remove(id);
        System.out.println(dataObject);

        //WRITE TO A FILE
        try {
            FileWriter myWriter = new FileWriter(appFolderPath+"data.json");
            myWriter.write(dataObject.toJSONString());
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    public String getJobId(String id){
        String data="{}";
        //READ A FILE
        try {
            File myObj = new File(appFolderPath+"data.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject dataObject=(JSONObject) JSONValue.parse(data);
        if(dataObject.containsKey(id)){
            JSONObject contentObject=(JSONObject) JSONValue.parse(dataObject.get(id).toString());
            if(contentObject.containsKey("jobId")){
                //check if the jobId is not equal to ""
                if(((String)contentObject.get("jobId")).length() !=0 ){
                    return (String)contentObject.get("jobId");
                }else{
                    return "null";
                }
            }else{
                return "null";
            }
        }

        return "null";
    }
    public void setJobId(String id,String jobId){
        String data="{}";
        //READ A FILE
        try {
            File myObj = new File(appFolderPath+"data.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject dataObject=(JSONObject) JSONValue.parse(data);
        if(dataObject.containsKey(id)){
            JSONObject contentObject=(JSONObject) JSONValue.parse(dataObject.get(id).toString());
            contentObject.put("jobId",jobId);
            dataObject.put(id,contentObject);
            System.out.println(dataObject);
        }

        //WRITE TO A FILE
        try {
            FileWriter myWriter = new FileWriter(appFolderPath+"data.json");
            myWriter.write(dataObject.toJSONString());
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public String getTitle(String id){
        String data="{}";
        //READ A FILE
        try {
            File myObj = new File(appFolderPath+"data.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject dataObject=(JSONObject) JSONValue.parse(data);
        if(dataObject.containsKey(id)){
            JSONObject contentObject=(JSONObject) JSONValue.parse(dataObject.get(id).toString());
            if(contentObject.containsKey("title")){
                //check if the jobId is not equal to ""
                if(((String)contentObject.get("title")).length() !=0 ){
                    return (String)contentObject.get("title");
                }
            }
        }

        return id;
    }

    public String isSubtitled(String id){

        String data="{}";
        //READ A FILE
        try {
            File myObj = new File(appFolderPath+"data.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject dataObject=(JSONObject) JSONValue.parse(data);
        if(dataObject.containsKey(id)){
            JSONObject contentObject=(JSONObject) JSONValue.parse(dataObject.get(id).toString());
            System.out.println(contentObject.get("status"));
            if(contentObject.get("status")!=null && (boolean)contentObject.get("status")){
                return appFolderPath+""+id+"\\"+id+".srt";
            }else {
                return "null";
            }
        }

        return "null";
    }
    public void setSubtitled(String id,boolean status,String type){
        if(type.equals("ytb")){
            String data="{}";
            //READ A FILE
            try {
                File myObj = new File(appFolderPath+"data.json");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    data = myReader.nextLine();
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            JSONObject dataObject=(JSONObject) JSONValue.parse(data);
            if(dataObject.containsKey(id)){
                JSONObject contentObject=(JSONObject) JSONValue.parse(dataObject.get(id).toString());
                contentObject.put("status",status);
                dataObject.put(id,contentObject);
                System.out.println(dataObject);
            }

            //WRITE TO A FILE
            try {
                FileWriter myWriter = new FileWriter(appFolderPath+"data.json");
                myWriter.write(dataObject.toJSONString());
                myWriter.close();
                //System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }

        }else if(type.equals("local")){

        }
    }

    private void setAppFolderPath(String appFolderPath){
        String data="{}";
        //READ A FILE
        try {
            File myObj = new File(appFolderPath+"data.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject dataObject=(JSONObject) JSONValue.parse(data);
        dataObject.put("appFolderPath",appFolderPath);

        //WRITE TO A FILE
        try {
            FileWriter myWriter = new FileWriter(appFolderPath+"data.json");
            myWriter.write(dataObject.toJSONString());
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    private String getAppFolderPath(){
        String data="{}";
        //READ A FILE
        try {
            File myObj = new File(appFolderPath+"\\data.json");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JSONObject dataObject=(JSONObject) JSONValue.parse(data);
        if(dataObject.containsKey("appFolderPath")){
            return (String)dataObject.get("appFolderPath");
        }else{
            return "null";
        }
    }

}

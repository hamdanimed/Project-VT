package ensa.project_vt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SousFrame1Controller {

    @FXML
    private Label Size;

    @FXML
    private Label date;

    @FXML
    private Button deleteV;

    @FXML
    private Button playV;
    @FXML
    private Button subtitlesV;

    @FXML
    private Label subNotsub;

    @FXML
    private Label videoTitle;

    private String appFolderPath="src\\main\\resources\\ensa\\project_vt\\project-vt-files\\";
    private DataFile dataFile=new DataFile(appFolderPath);
    public HomeController homeController;

    @FXML
    void DeleteVideo(ActionEvent event) throws IOException {
        String button_ID = deleteV.getId();
//        System.out.println("btn id : "+button_ID);
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete the video ?");
        if(alert.showAndWait().get()== ButtonType.OK){
            if(button_ID.contains("local")){
                delfile(button_ID+".wav");
                delfile(button_ID+".srt");
            }else{
                delfile(button_ID);
            }
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void PlayVideo(ActionEvent event) {
        String button_ID = playV.getId();
//        System.out.println(button_ID);
//        System.out.println(videoTitle+"   "+date);
        String srtPath="null";
        String videoPath="null";
        if(button_ID.contains("local")){
            srtPath=dataFile.isSubtitled(button_ID.substring(6));
            videoPath=dataFile.getPath(button_ID.substring(6));
        }else{
            srtPath=dataFile.isSubtitled(button_ID);
            videoPath=dataFile.getPath(button_ID)+"\\"+button_ID+".mp4";
        }

        Stage stage=null;
        Scene scene = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mediaplayer.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load(), 1200, 700);
            stage.setTitle("Media Player");
            stage.setScene(scene);
            VideoPlayerController videoPlayerController = fxmlLoader.getController();
            stage.show();
            videoPlayerController.intermediateFunction(videoPath,srtPath,"home.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void getSubtitles(ActionEvent event) {
        homeController.msgLabel.setVisible(false);
        homeController.loadingGif.setVisible(false);
        String button_ID = subtitlesV.getId();
        String videoType="ytb";
        if(button_ID.contains("local")){
            button_ID=button_ID.substring(6,button_ID.length());
            videoType="local";
        }

//        System.out.println(button_ID);
        if(dataFile.getJobId(button_ID).equals("null")){
            String audioPath= dataFile.getPath(button_ID)+"\\"+button_ID+".wav";
            String videoPath= dataFile.getPath(button_ID)+"\\"+button_ID+".mp4";
            String videoTitle=dataFile.getTitle(button_ID);
            if(videoType.equals("local")){
                audioPath= appFolderPath+"local"+"\\"+button_ID+".wav";
                videoPath=dataFile.getPath(button_ID);
            }

            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("You need to upload the audio to speechmatics");
            if(alert.showAndWait().get()== ButtonType.OK){
                Stage stage=null;
                Scene scene = null;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("search-view.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(fxmlLoader.load(), 1200, 700);
                    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                    stage.setTitle("Search");
                    stage.setScene(scene);
                    SearchViewController searchViewController = fxmlLoader.getController();
                    searchViewController.setStage(stage);
                    stage.show();
                    searchViewController.launchProgressUpload(audioPath,button_ID,videoTitle,videoPath,videoType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }else {
            String configFilePath=new File("src/main/resources/ensa/project_vt/generate_subtitles/speechmatics-config-standard.json").getAbsolutePath();
            Speechmatics speechmatics=new Speechmatics(appFolderPath,configFilePath);

            homeController.loadingGif.setVisible(true);
            homeController.msgLabel.setText("Getting subtitles");

            int exitCode;
            if(videoType.equals("local")){
                exitCode=speechmatics.getSubstitles(dataFile.getJobId(button_ID),"local",button_ID,homeController);
            }else{
                exitCode=speechmatics.getSubstitles(dataFile.getJobId(button_ID),button_ID,button_ID,homeController);
            }
            if(exitCode==1){
                homeController.loadingGif.setVisible(false);
                homeController.msgLabel.setText("An error accured with getting subtitles , try again");
                System.out.println("Error accured with getting subtitles");
            }else{
                homeController.loadingGif.setVisible(false);
                homeController.msgLabel.setVisible(false);
                if(videoType.equals("local")){
                    dataFile.setSubtitled(button_ID,appFolderPath+"local"+"\\"+button_ID+".srt",true);
                }else{
                    dataFile.setSubtitled(button_ID,appFolderPath+button_ID+"\\"+button_ID+".srt",true);
                }
            }
            Scene scene = null;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
    //            throw new RuntimeException(e);
            }
        }



    }

    public void setData(VideoInf videoInf){
        videoTitle.setText(videoInf.getTitle());
        date.setText(videoInf.getDate());
        subNotsub.setText(videoInf.getSubnotsub());
        playV.setId(videoInf.getId());
        deleteV.setId(videoInf.getId());
        subtitlesV.setId(videoInf.getId());
    }

    public void delfile(String id){
        try {
            // create a new file object
            System.out.println(appFolderPath+id);
            if(id.contains("local")){
                File file=new File(appFolderPath+id);
                if(file.exists() && file.delete()){
                    dataFile.deleteVideo(id.substring(6,id.length()-4));
                    System.out.println("File was deleted");
                }else{
                    System.out.println("File not Found");
                }
            }else{
                File directory = new File(appFolderPath+id);
                File[] files = directory.listFiles();
                // delete each file from the directory
                for(File file : files) {
                    System.out.println(file + " deleted.");
                    file.delete();
                }
                // delete the directory
                if(directory.delete()) {
                    System.out.println("Directory Deleted");
                    dataFile.deleteVideo(id);
                }
                else {
                    System.out.println("Directory not Found");
                }

            }

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

}

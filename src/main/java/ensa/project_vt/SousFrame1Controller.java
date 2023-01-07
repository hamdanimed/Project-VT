package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.DataFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
    private Label subNotsub;

    @FXML
    private Label videoTitle;

    private String appFolderPath="src\\main\\resources\\ensa\\project_vt\\project-vt-files\\";
    private DataFile dataFile=new DataFile(appFolderPath);

    @FXML
    void DeleteVideo(ActionEvent event) throws IOException {
        String button_ID = deleteV.getId();
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You're about to delete the video ");
        alert.setContentText("Do really want to delete this video ?");
        if(alert.showAndWait().get()== ButtonType.OK){
            delfile(button_ID);
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
        System.out.println(button_ID);
        System.out.println(videoTitle+"   "+date);

        Stage stage=null;
        Scene scene = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SearchView.class.getResource("mediaplayer.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load(), 1200, 700);
            stage.setTitle("Media Player");
            stage.setScene(scene);
            VideoPlayerController videoPlayerController = fxmlLoader.getController();
            stage.show();
            videoPlayerController.intermediateFunction(appFolderPath+button_ID+"\\"+button_ID+".mp4","home.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setData(VideoInf videoInf){
        //Image img = new Image(getClass().getResourceAsStream(videoInf.))
        videoTitle.setText(videoInf.getTitle());
        date.setText(videoInf.getDate());
        subNotsub.setText(videoInf.getSubnotsub());
        playV.setId(videoInf.getId());
        deleteV.setId(videoInf.getId());
        //Size.setText(videoInf.getSize());
    }

    public void delfile(String id){
        try {
            // create a new file object
            File directory = new File(appFolderPath+id);

            // list all the files in an array
            File[] files = directory.listFiles();

            // delete each file from the directory
            for(File file : files) {
                System.out.println(file + " deleted.");
                file.delete();
            }

            // delete the directory
            if(directory.delete()) {
                System.out.println("Directory Deleted");
            }
            else {
                System.out.println("Directory not Found");
            }

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

}

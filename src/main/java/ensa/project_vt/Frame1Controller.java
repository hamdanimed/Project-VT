package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.DataFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Frame1Controller implements Initializable {

    @FXML
    private ChoiceBox<?> myChoiceBox;

    @FXML
    private Button nextIf;

    @FXML
    private VBox videoContainer;

    private Stage stage;
    private Scene scene;

    DataFile dataFile=new DataFile("C:\\Users\\hp\\PC\\project-vt-files\\");

    @FXML
    void pageNext(ActionEvent event) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("search-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        scene = new Scene(fxmlLoader.load());
//        stage.setScene(scene);
//        stage.show();

        FXMLLoader fxmlLoader = new FXMLLoader(SearchView.class.getResource("search-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setTitle("Search");
        stage.setScene(scene);
        SearchViewController searchViewController = fxmlLoader.getController();
        searchViewController.setStage(stage);
        stage.show();

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<VideoInf> videoInfs = new ArrayList<>(videoInfs());
        for (int i=0; i<videoInfs.size(); i++){
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SousFrame1-view.fxml"));
            try {
                AnchorPane anchorPane = fxmlLoader.load();
                SousFrame1Controller sf1C = fxmlLoader.getController();
                sf1C.setData(videoInfs.get(i));
                videoContainer.getChildren().add(anchorPane);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    private List<VideoInf> videoInfs(){
        List<VideoInf> ls = new ArrayList<>();
        try{
//            File folder = new File("C:\\Users\\Mohamed Ben Arrouch\\OneDrive\\Desktop\\project-vt-files");
            File folder = new File("C:\\Users\\hp\\PC\\project-vt-files");
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                VideoInf videoInf = new VideoInf();
                Path file = Paths.get(listOfFiles[i].getAbsolutePath());
                BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
                //System.out.println("File name: " + listOfFiles[i].getName());
                //System.out.println("Date de dernier modification: "+ attr.lastModifiedTime());
                //System.out.println("File size: "+attr.size());
                videoInf.setTitle(listOfFiles[i].getName());
                videoInf.setDate(String.valueOf(attr.lastModifiedTime()).substring(0,10));
                //videoInf.setSize(String.valueOf(attr.size()/1024)+"MB");
                ls.add(videoInf);
            }
        }catch (IOException e){
            e.printStackTrace();
        }


//        for (int i=0;i<10;i++){
//            videoInf.setTitle("mimid video not for kids");
//            videoInf.setDate("11/04/2022");
//            videoInf.setSubnotsub("subbed");
//            videoInf.setSize("7 MB");
//            ls.add(videoInf);
//        }

        return ls;
    }

}

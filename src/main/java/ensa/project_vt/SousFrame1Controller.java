package ensa.project_vt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SousFrame1Controller implements Initializable {

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

    @FXML
    void DeleteVideo(ActionEvent event) throws IOException {
        String button_ID = deleteV.getId();
        //System.out.println(button_ID);
        delfile(button_ID);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Frame1-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    void PlayVideo(ActionEvent event) {
        String button_ID = playV.getId();
        System.out.println(videoTitle+"   "+date);


    }

    public void setData(VideoInf videoInf){
        //Image img = new Image(getClass().getResourceAsStream(videoInf.))
        videoTitle.setText(videoInf.getTitle());
        date.setText(videoInf.getDate());
        subNotsub.setText(videoInf.getSubnotsub());
        playV.setId(videoInf.getTitle());
        deleteV.setId(videoInf.getTitle());
        //Size.setText(videoInf.getSize());
    }

    public void delfile(String title){
        try {
            // create a new file object
            File directory = new File("C:\\Users\\Mohamed Ben Arrouch\\OneDrive\\Desktop\\project-vt-files\\"+title+"");

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

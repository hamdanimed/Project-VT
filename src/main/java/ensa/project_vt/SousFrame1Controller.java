package ensa.project_vt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    void DeleteVideo(ActionEvent event) {
        System.out.println("this video was deleted");
    }

    @FXML
    void PlayVideo(ActionEvent event) {
        System.out.println("Watch video is done");
    }

    public void setData(VideoInf videoInf){
        //Image img = new Image(getClass().getResourceAsStream(videoInf.))
        videoTitle.setText(videoInf.getTitle());
        date.setText(videoInf.getDate());
        subNotsub.setText(videoInf.getSubnotsub());
        Size.setText(videoInf.getSize());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

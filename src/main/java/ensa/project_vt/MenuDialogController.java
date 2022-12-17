package ensa.project_vt;

import ensa.project_vt.YoutubeSearch.YoutubeApiThread;
import ensa.project_vt.YoutubeSearch.YoutubeVideo;
import ensa.project_vt.localVideo.localVideo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuDialogController {

@FXML
    ComboBox videoQuality;
@FXML
    ComboBox audioQuality;



boolean videoQualityChoosen;
boolean audioQualityChoosen;

public void setVideoQualityOptions(List<String> VideoOptions, List<String> AudioOptions){
videoQuality.getItems().addAll(VideoOptions);
audioQuality.getItems().addAll(AudioOptions);
}
    public boolean isVideoQualityChoosen() {
        return videoQuality.getValue() != null;
    }
    public boolean isAudioQualityChoosen() {
        return audioQuality.getValue() != null;
    }
    public String getVideoQuality(){
    return videoQuality.getValue().toString();
    }
    public String getAudioQuality(){
    return audioQuality.getValue().toString();
    }
}
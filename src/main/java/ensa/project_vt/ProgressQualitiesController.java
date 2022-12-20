package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.YoutubeDl;
import ensa.project_vt.GenerateSubtitles.YoutubeDlTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ProgressQualitiesController {

    @FXML
    ComboBox videoQualityCombo;
    @FXML
    ComboBox audioQualityCombo;

    @FXML
    Label progressMsg;

    @FXML
    Button nextBtn;
    @FXML
    Button cancelBtn;
    @FXML
    Button startBtn;
    @FXML
    private DialogPane dialogPane;

    private Stage stage;
    private Scene scene;
    private Parent root;

    boolean videoQualityChoosen;
    boolean audioQualityChoosen;

    public YoutubeDl youtubeDl;
    private Thread checkQualitiesThread;
    private String videoQuality="";
    private String audioQuality="";

    public void setYoutubeDl(YoutubeDl youtubeDl) {
        this.youtubeDl = youtubeDl;
    }
    //    public ProgressQualitiesController(YoutubeDl d) {
//        this.youtubeDl=d;
//    }

    public void initialize() {
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialogPane.lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);
        nextBtn.setDisable(true);
//        System.out.println(this.youtubeDl.getYoutubelink());

//        System.out.println(dialogPane.getUserData());

//        stage=(Stage) ((Node)dialogPane).getScene().getWindow();
//        System.out.println(stage.getUserData())
//        System.out.println(dialogPane.getScene().getWindow().getUserData());
//        System.out.println(root.getScene().getWindow().getUserData());
//        System.out.println(root.getDialogPane().getScene().getWindow().getUserData());

    }

    @FXML
    public void start(ActionEvent event){
        System.out.println(youtubeDl.getYoutubelink());
        startBtn.setDisable(true);
        checkQualitiesThread=new Thread(new YoutubeDlTask(this.youtubeDl,this,"checkQuality"));
        checkQualitiesThread.setDaemon(true);
        checkQualitiesThread.start();
    }

    @FXML
    public void next(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("progressDownload.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setUserData(this.youtubeDl);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        System.out.println("check qualities , cancel buttton clicked");
        System.out.println(this.youtubeDl.getYoutubelink());
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You're about to Cancel t");
        alert.setContentText("Do really want to exit ?");
        if(alert.showAndWait().get()== ButtonType.OK){
            stage=(Stage) dialogPane.getScene().getWindow();
            stage.close();
        }
    }
    @FXML
    public void selectVideoQuality(ActionEvent event){
        this.videoQuality=(String) videoQualityCombo.getValue();
        String[] qualityArray=this.videoQuality.substring(1,videoQuality.length()-1).split(",");
        this.youtubeDl.setVideoQuality(qualityArray[0]);
        if(videoQualityCombo.getValue()!=null && audioQualityCombo.getValue()!=null){
            nextBtn.setDisable(false);
        }
    }
    @FXML
    public void selectAudioQuality(ActionEvent event){
        this.audioQuality=(String) audioQualityCombo.getValue();
        String[] qualityArray=this.audioQuality.substring(1,audioQuality.length()-1).split(",");
        this.youtubeDl.setAudioQuality(qualityArray[0]);
        if(videoQualityCombo.getValue()!=null && audioQualityCombo.getValue()!=null){
            nextBtn.setDisable(false);
        }

    }

    public void setVideoQualityOptions(List<String> VideoOptions, List<String> AudioOptions){
        videoQualityCombo.getItems().addAll(VideoOptions);
        audioQualityCombo.getItems().addAll(AudioOptions);
    }
    public Label getProgressMsg() {
        return progressMsg;
    }

    public void setProgressMsg(Label progressMsg) {
        this.progressMsg = progressMsg;
    }
    public boolean isVideoQualityChoosen() {
        return videoQualityCombo.getValue() != null;
    }
    public boolean isAudioQualityChoosen() {
        return audioQualityCombo.getValue() != null;
    }
    public String getVideoQuality(){
        return videoQualityCombo.getValue().toString();
    }
    public String getAudioQuality(){
        return audioQualityCombo.getValue().toString();
    }


}
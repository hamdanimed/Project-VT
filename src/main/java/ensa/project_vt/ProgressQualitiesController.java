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
import javafx.scene.image.ImageView;
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
    ImageView loadingImage;

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

    public DataObject dataObject;
    public YoutubeDl youtubeDl;
    private Thread checkQualitiesThread;
    private String videoQuality="";
    private String audioQuality="";
    private YoutubeDlTask task;

    public void setYoutubeDl(YoutubeDl youtubeDl) {

        this.youtubeDl = youtubeDl;
    }
    //    public ProgressQualitiesController(YoutubeDl d) {
//        this.youtubeDl=d;
//    }

    public void initialize() {
//        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
//        Node closeButton = dialogPane.lookupButton(ButtonType.CLOSE);
//        closeButton.managedProperty().bind(closeButton.visibleProperty());
//        closeButton.setVisible(false);
        nextBtn.setDisable(true);
        loadingImage.setVisible(false);
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
        //get data and initialize objects
        this.dataObject=(DataObject) dialogPane.getScene().getWindow().getUserData();
        this.youtubeDl=dataObject.youtubeDl;

        startBtn.setDisable(true);
        loadingImage.setVisible(true);

        //start the task
        this.task=new YoutubeDlTask(this.dataObject,this,"checkQuality");
        checkQualitiesThread=new Thread(task);
        checkQualitiesThread.setDaemon(true);
        checkQualitiesThread.start();
    }

    @FXML
    public void next(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("progressDownload.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setUserData(dataObject);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void cancel(ActionEvent event) throws IOException {
//        System.out.println("check qualities , cancel buttton clicked");
//        System.out.println(this.youtubeDl.getYoutubelink());

        if(task!=null && task.isRunning()){
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("You're about to Cancel t");
            alert.setContentText("Do really want to exit ?");
            if(alert.showAndWait().get()== ButtonType.OK){
                //first method for canceling (somehow works) , doesnt work for download dialog for some reason
    //            if(checkQualitiesThread!=null && checkQualitiesThread.isAlive()){
    //                checkQualitiesThread.interrupt();
    //            }
                //second method for canceling (just to have a unified method with the download)
                if(task!=null && task.isRunning()){
                    task.sendCancelSignal();

                }
                stage=(Stage) dialogPane.getScene().getWindow();
                stage.close();
            }
        }else{
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
    public ImageView getloadingImage() {
        return loadingImage;
    }

    public Button getStartBtn() {
        return startBtn;
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
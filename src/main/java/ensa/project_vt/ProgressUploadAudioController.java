package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.Speechmatics;
import ensa.project_vt.GenerateSubtitles.SpeechmaticsTask;
import ensa.project_vt.GenerateSubtitles.YoutubeDl;
import ensa.project_vt.GenerateSubtitles.YoutubeDlTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.chrono.ThaiBuddhistEra;

public class ProgressUploadAudioController {

    @FXML
    private DialogPane dialogPane;
    @FXML
    private Label speedLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label sizeLabel;
    @FXML
    private Label percentageLabel;
    @FXML
    private ProgressBar progressBar;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private YoutubeDl youtubeDl;
    private Speechmatics speechmatics;
    public DataObject dataObject;
    private SpeechmaticsTask task;
    private Thread sendAudioThread;
    @FXML
    public void initialize(){
//        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
//        Node closeButton = dialogPane.lookupButton(ButtonType.CLOSE);
//        closeButton.managedProperty().bind(closeButton.visibleProperty());
//        closeButton.setVisible(false);

    }

    @FXML
    public void start(ActionEvent event){
        this.dataObject= (DataObject) dialogPane.getScene().getWindow().getUserData();
        this.youtubeDl=this.dataObject.youtubeDl;
        this.speechmatics=this.dataObject.speechmatics;

        task=new SpeechmaticsTask(this.youtubeDl,this.speechmatics,this,"sendAudio");
        sendAudioThread=new Thread(task);
        sendAudioThread.setDaemon(true);
        sendAudioThread.start();
    }
     @FXML
    public void finish(ActionEvent event) throws IOException {
        System.out.println("upload audio , finish buttton clicked");
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You're about to exit");
        alert.setContentText("Do really want to exit ?");
        if(alert.showAndWait().get()== ButtonType.OK){
            stage=(Stage) dialogPane.getScene().getWindow();
            stage.close();
        }
    }
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        System.out.println("upload audio , cancel buttton clicked");
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You're about to Cancel t");
        alert.setContentText("Do really want to exit ?");
        if(alert.showAndWait().get()== ButtonType.OK){
            stage=(Stage) dialogPane.getScene().getWindow();
            stage.close();
        }
     }

    public void setProgress(String speed,String time,String percentage,String size){
        percentage=percentage.concat("%");
        System.out.println(percentage);
        this.speedLabel.setText(speed);
        this.timeLabel.setText(time);
        this.percentageLabel.setText(percentage);
        this.sizeLabel.setText(size);
        double value=Double.valueOf(percentage.substring(0,percentage.length()-1));
//        double value=Double.valueOf(percentage);
        this.progressBar.setProgress(value/100.0);
    }
}

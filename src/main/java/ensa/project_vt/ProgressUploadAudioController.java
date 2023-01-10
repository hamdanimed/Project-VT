package ensa.project_vt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

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
    @FXML
    private Button startBtn;
    @FXML
    private Button finishBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label errorMsgLabel;

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
        finishBtn.setDisable(true);
        errorMsgLabel.setVisible(false);
    }

    @FXML
    public void start(ActionEvent event){
        this.dataObject= (DataObject) dialogPane.getScene().getWindow().getUserData();

        startBtn.setDisable(true);

        task=new SpeechmaticsTask(this.dataObject,this,"sendAudio");
        sendAudioThread=new Thread(task);
        sendAudioThread.setDaemon(true);
        sendAudioThread.start();
    }
     @FXML
    public void finish(ActionEvent event) throws IOException {
//        System.out.println("upload audio , finish buttton clicked");
//        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirmation");
//        alert.setHeaderText("Are you sure you want to Exit ?");
////        alert.setContentText("Do really want to exit ?");
//        if(alert.showAndWait().get()== ButtonType.OK){
            stage=(Stage) dialogPane.getScene().getWindow();
            stage.close();
//        }
    }
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to CANCEL ?");
        if(alert.showAndWait().get()== ButtonType.OK){
            if(task!=null && task.isRunning()){
                task.sendCancelSignal();
            }
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
        this.progressBar.setProgress(value/100.0);
    }

    public Button getStartBtn() {
        return startBtn;
    }

    public Button getFinishBtn() {
        return finishBtn;
    }

    public Label getErrorMsgLabel() {
        return errorMsgLabel;
    }
}

package ensa.project_vt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;


public class ProgressDownloadController {
    @FXML
    private Label speedLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label percentageLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label errorMsgLabel;
    @FXML
    private Label sizeLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Stage stage;
    @FXML
    private AnchorPane scenePane;
    @FXML
    public Label videoIdLabel;
    @FXML
    private DialogPane dialogPane;
    @FXML
    private Button nextBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button startBtn;
    private Scene scene;
    private Parent root;

    public Thread downloadVideoAndAudioThread;
    public YoutubeDlTask task;
    public YoutubeDl youtubeDl;
    public DataObject dataObject;
    @FXML
    public void initialize(){
        nextBtn.setDisable(true);
        errorMsgLabel.setVisible(false);
    }

    @FXML
    public void start(ActionEvent event){
        this.dataObject=(DataObject) dialogPane.getScene().getWindow().getUserData();

        startBtn.setDisable(true);
        errorMsgLabel.setVisible(false);
        
        task=new YoutubeDlTask(this.dataObject,this,"downloadVideoAndAudio");
        downloadVideoAndAudioThread=new Thread(task);
        downloadVideoAndAudioThread.setDaemon(true);
        downloadVideoAndAudioThread.start();
    }
    @FXML
    public void next(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("progressUploadAudio.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        dataObject=task.getDataObject();
        stage.setUserData(dataObject);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        if(task!=null && task.isRunning()){
            Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(videoIdLabel.getText());
            alert.setHeaderText("Are you sure you want to CANCEL ?");
//            alert.setContentText("Do really want to exit ?");
            if(alert.showAndWait().get()== ButtonType.OK){
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

    public void setProgress(String speed,String time,String percentage,String size){

        this.speedLabel.setText(speed);
        this.timeLabel.setText(time);
        this.percentageLabel.setText(percentage);
        this.sizeLabel.setText(size);
        double value=Double.valueOf(percentage.substring(0,percentage.length()-1));
        this.progressBar.setProgress(value/100.0);
    }

    public Button getNextBtn() {

        return nextBtn;
    }

    public Button getStartBtn() {
        return startBtn;
    }

    public Label getErrorMsgLabel() {
        return errorMsgLabel;
    }

    public void setNextBtn(Button nextBtn) {
        this.nextBtn = nextBtn;
    }
}

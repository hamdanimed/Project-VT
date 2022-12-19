package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.DataFile;
import ensa.project_vt.GenerateSubtitles.YoutubeDl;
import ensa.project_vt.GenerateSubtitles.YoutubeDlTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class ProgressController {
    @FXML
    private Label speedLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label percentageLabel;
    @FXML
    private Label titleLabel;
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

    public Thread downloadVideoAndAudioThread;

    public YoutubeDl youtubeDl;

    @FXML
    private DialogPane dialogPane;

    @FXML
    public void initialize(){
        ((Thread)dialogPane.getUserData()).start();
    }
    public void setProgress(String speed,String time,String percentage,String size){

        this.speedLabel.setText(speed);
        this.timeLabel.setText(time);
        this.percentageLabel.setText(percentage);
        this.sizeLabel.setText(size);
        double value=Double.valueOf(percentage.substring(0,percentage.length()-1));
        this.progressBar.setProgress(value/100.0);
    }

    public void closeDialog(){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(videoIdLabel.getText());
        alert.setHeaderText("You're about to Cancel the Download");
        alert.setContentText("do reeeeally want to exit ?");
        if(alert.showAndWait().get()== ButtonType.OK){
            stage=(Stage) scenePane.getScene().getWindow();
            ((Thread)dialogPane.getUserData()).interrupt();
            stage.close();

        }

    }
}

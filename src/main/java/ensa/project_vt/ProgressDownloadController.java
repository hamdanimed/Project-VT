package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.YoutubeDl;
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
    private Scene scene;
    private Parent root;

    @FXML
    public void initialize(){
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialogPane.lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

    }

    @FXML
    public void next(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("progressUploadAudio.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        System.out.println("download , cancel buttton clicked");
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(videoIdLabel.getText());
        alert.setHeaderText("You're about to Cancel t");
        alert.setContentText("Do really want to exit ?");
        if(alert.showAndWait().get()== ButtonType.OK){
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




}

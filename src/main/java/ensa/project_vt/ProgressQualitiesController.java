package ensa.project_vt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ProgressQualitiesController {

    @FXML
    ComboBox videoQuality;
    @FXML
    ComboBox audioQuality;

    @FXML
    Label progressMsg;

    @FXML
    Button nextBtn;
    @FXML
    Button cancelBtn;

    @FXML
    private DialogPane dialogPane;

    private Stage stage;
    private Scene scene;
    private Parent root;

    boolean videoQualityChoosen;
    boolean audioQualityChoosen;

    @FXML
    public void initialize(){
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = dialogPane.lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        

        final Button btOk = (Button) dialogPane.lookupButton(ButtonType.CLOSE);
        btOk.addEventFilter(
                ActionEvent.ACTION,
                event -> {
                    Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("You're about to Cancel t");
                    alert.setContentText("Do really want to exit ?");
                    if(alert.showAndWait().get()== ButtonType.OK){
//                        stage=(Stage) dialogPane.getScene().getWindow();
//                        stage.close();
                        event.consume();
                    }
                }
        );
    }

    @FXML
    public void next(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("progressDownload.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void cancel(ActionEvent event) throws IOException {
        System.out.println("check qualities , cancel buttton clicked");
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("You're about to Cancel t");
        alert.setContentText("Do really want to exit ?");
        if(alert.showAndWait().get()== ButtonType.OK){
            stage=(Stage) dialogPane.getScene().getWindow();
            stage.close();
        }
    }

    public void setVideoQualityOptions(List<String> VideoOptions, List<String> AudioOptions){
        videoQuality.getItems().addAll(VideoOptions);
        audioQuality.getItems().addAll(AudioOptions);
    }
    public Label getProgressMsg() {
        return progressMsg;
    }

    public void setProgressMsg(Label progressMsg) {
        this.progressMsg = progressMsg;
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
package ensa.project_vt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProgressUploadAudioController {

    @FXML
    private DialogPane dialogPane;

    private Stage stage;
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

}

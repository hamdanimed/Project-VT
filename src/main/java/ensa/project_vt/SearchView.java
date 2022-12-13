package ensa.project_vt;
import org.apache.commons.lang3.StringEscapeUtils;



import javafx.application.Application;
import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

public class SearchView extends Application {
    private Button browse;
    @Override
    public void start(Stage stage) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(SearchView.class.getResource("search-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            stage.setTitle("Search");
            stage.setScene(scene);
            SearchViewController searchViewController = fxmlLoader.getController();
            searchViewController.setStage(stage);
            stage.show();



    }

    public static void main(String[] args) {
        launch();
    }
}
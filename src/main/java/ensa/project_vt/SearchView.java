package ensa.project_vt;

import javafx.application.Application;
import javafx.css.Style;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class SearchView extends Application {
    @Override
    public void start(Stage stage) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(SearchView.class.getResource("search-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            stage.setTitle("Search");
            stage.setScene(scene);
            stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}
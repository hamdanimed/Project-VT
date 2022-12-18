package ensa.project_vt.YoutubeSearch;

import ensa.project_vt.SearchViewController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.List;

public class YoutubeApiThread  extends Thread  {
    private String query;
    @FXML
    private ListView<YoutubeVideo> listView;
    @FXML
    private Pane progressArea;
    @FXML
    Text textInfo;

    public YoutubeApiThread(String query, ListView<YoutubeVideo> listView, Pane progressArea, Text textInfo) {
        this.query = query;
        this.listView = listView;
        this.progressArea=progressArea;
        this.textInfo=textInfo;
    }

    @Override
    public void run() {

        String apiKey = "AIzaSyC34VqBS3fiCsJyd2fX1P2fx5yBIQnimTY";
        VisitYoutube client = new VisitYoutube(apiKey);

        try {
            List<YoutubeVideo> videos = client.Search(query, "snippet", "video", 10);
            ObservableList<YoutubeVideo> data = FXCollections.observableArrayList(videos);
            Platform.runLater(() -> {
                // Update UI here.
                listView.setVisible(true);
                listView.setItems(data);
                textInfo.setVisible(true);
                textInfo.setText("Results :");
                progressArea.setVisible(false);


            });

        } catch (IOException e) {
            System.out.println("Execption was invoked in YoutubeApiThread");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }




    }
}

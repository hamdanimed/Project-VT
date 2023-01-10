package ensa.project_vt;

import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ResultCell extends ListCell<YoutubeVideo>{
    @FXML
    private Label titleLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private ImageView imageView;
    /*@FXML
    private Pane pane;

     */

    @FXML
    private HBox hbox;
    public ResultCell(){


        loadFXML();
    }
    // load the fxml view for the result item
    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("result_cell.fxml"));
            loader.setController(this);
            loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // To call when adding the youtubeVideo to the list view
    @Override
    protected void updateItem(YoutubeVideo youtubeVideo, boolean empty) {
        super.updateItem(youtubeVideo, empty);

        if(empty || youtubeVideo == null) {
            setText(null);
            setContentDisplay(null);
        }
        else {
            Image image = new Image(youtubeVideo.getThumbnailUrl());
            imageView.setImage(image);

            titleLabel.setText(youtubeVideo.getVideoTitle());
            durationLabel.setText(youtubeVideo.getDuration());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setText(null);
            setGraphic(hbox);
        }
    }
}



package ensa.project_vt;

import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ResultCell extends ListCell<Result>{
    @FXML
    private Label titleLabel;
    @FXML
    private Label durationLabel;
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

    // To call when adding the item to the list view
    @Override
    protected void updateItem(Result item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setContentDisplay(null);
        }
        else {
            titleLabel.setText(item.getVideoTitle());
            durationLabel.setText(item.getDuration());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setText(null);
            setGraphic(hbox);
        }
    }
}



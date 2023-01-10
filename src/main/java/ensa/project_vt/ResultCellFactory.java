package ensa.project_vt;

import ensa.project_vt.ResultCell;
import ensa.project_vt.YoutubeVideo;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

// A factory to create result cells
public class ResultCellFactory implements Callback<ListView<YoutubeVideo>, ListCell<YoutubeVideo>> {
    @Override
    public ListCell<YoutubeVideo> call(ListView<YoutubeVideo> resultListView) {
        return new ResultCell();
    }
}


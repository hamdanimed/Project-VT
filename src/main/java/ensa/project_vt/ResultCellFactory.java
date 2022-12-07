package ensa.project_vt;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

// A factory to create result cells
public class ResultCellFactory implements Callback<ListView<Result>, ListCell<Result>> {

    @Override
    public ListCell<Result> call(ListView<Result> resultListView) {
        return new ResultCell();
    }
}


module ensa.project_vt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires org.apache.commons.lang3;


    opens ensa.project_vt to javafx.fxml;
    exports ensa.project_vt;
    exports ensa.project_vt.YoutubeSearch;
    opens ensa.project_vt.YoutubeSearch to javafx.fxml;
}
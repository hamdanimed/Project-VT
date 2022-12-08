module ensa.project_vt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens ensa.project_vt to javafx.fxml;
    exports ensa.project_vt;
}
module ensa.project_vt {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires com.google.gson;
    requires org.apache.commons.lang3;
    requires org.apache.commons.io;
    requires javafx.media;


    opens ensa.project_vt to javafx.fxml;
    exports ensa.project_vt;

}
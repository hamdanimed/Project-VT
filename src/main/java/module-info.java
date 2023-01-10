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
//    exports ensa.project_vt.controllers;
//    opens ensa.project_vt.controllers to javafx.fxml;
    exports ensa.project_vt.threading;
    opens ensa.project_vt.threading to javafx.fxml;
    exports ensa.project_vt.dataClasses;
    opens ensa.project_vt.dataClasses to javafx.fxml;
    exports ensa.project_vt.jobClasses;
    opens ensa.project_vt.jobClasses to javafx.fxml;
//    exports ensa.project_vt.jobClasses;
//    opens ensa.project_vt.jobClasses to javafx.fxml;
//    exports ensa.project_vt.YoutubeSearch;
//    opens ensa.project_vt.YoutubeSearch to javafx.fxml;
//    exports ensa.project_vt.GenerateSubtitles;
//    opens ensa.project_vt.GenerateSubtitles to javafx.fxml;
}
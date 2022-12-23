package ensa.project_vt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.*;

public class VideoPlayer extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(VideoPlayer.class.getResource("mediaplayer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        VideoPlayerController controller = fxmlLoader.getController();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) ->{
            switch(event.getCode())
            {
                case SPACE:
                    controller.playVideo();
                    break;
                case L:
                    controller.next();
                    break;
                case J:
                    controller.prev();

                    break;
            }
        });

        stage.setTitle("Media Player");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch();
//        SrtParser sp = new SrtParser("C:\\Users\\HP\\Desktop\\Java\\Player\\src\\main\\resources\\ma\\ensa\\player\\subs.srt",24000.0);


    }
}
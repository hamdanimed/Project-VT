package ensa.project_vt;

//Animations
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class VideoPlayerController implements Initializable {
    @FXML
    private MediaPlayer mediaPlayer;
    @FXML
    private MediaView mediaView;
    @FXML
    private Media mediaVideo;
    @FXML
    private AnchorPane videoPlayer;
    @FXML
    private VBox controlBar;
    @FXML
    private Button playBtn;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Slider timeSlider;
    @FXML
    private Button muteBtn,fullScreenBtn,editBtn;
    @FXML
    private Label closedCaptions;
    @FXML
    private Label captionEditLabel;
    @FXML
    private TextArea captionEditText;
    @FXML
    private VBox editBox;
    @FXML
    private HBox editHBox;
    @FXML
    private ProgressBar timeProgress;
    @FXML
    private Label timeLabel,totalLabel;
    @FXML
    private TextArea CaptionEditText;
    @FXML
    private VBox captionBox;


    private Image imgPlay,imgPause,imgMute,imgUnmute,imgReplay,imgFullScreen,imgEditMode;
    private ImageView playIV,pauseIV,muteIV,unmuteIV,replayIV,fullScreenIV,editModeIV;
    private boolean isPlaying,isMute,isOver,isEditMode,isFullScreen;
    private double currentVolume;
    private SrtParser sp;

    private int captionIndex;
    private Caption actualCaption,nextCaption,captionInit;
    private TranslateTransition translateEditBox,translateVideo;
    private ScaleTransition scaleVideo;
    private FadeTransition fadeIn,fadeOut;
    private SimpleDateFormat sdf;

    @Override
    public void initialize(URL url,ResourceBundle resourceBundle)
    {
//        mediaVideo = new Media(new File("D:\\Series\\Emily In Paris S3\\[EgyBest].Emily.In.Paris.S03E02.WEB-DL.720p.x264.mp4").toURI().toString());
        mediaVideo = new Media(new File("src\\main\\resources\\ensa\\project_vt\\video\\video.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(mediaVideo);
        mediaView.setMediaPlayer(mediaPlayer);
        sp = new SrtParser("src\\main\\resources\\ensa\\project_vt\\subs2.srt",mediaVideo.getDuration().toMillis());
        sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                totalLabel.setText("/"+timeString(mediaVideo.getDuration().toMillis()));
            }
        });

        // Images
        imgPlay = new Image(new File("src/main/resources/ensa/project_vt/UI/play.png").toURI().toString());
        imgPause = new Image(new File("src/main/resources/ensa/project_vt/UI/pause.png").toURI().toString());
        imgMute = new Image(new File("src/main/resources/ensa/project_vt/UI/mute.png").toURI().toString());
        imgUnmute = new Image(new File("src/main/resources/ensa/project_vt/UI/unmute.png").toURI().toString());
        imgReplay = new Image(new File("src/main/resources/ensa/project_vt/UI/replay.png").toURI().toString());
        imgFullScreen = new Image(new File("src/main/resources/ensa/project_vt/UI/full-screen.png").toURI().toString());
        imgEditMode = new Image(new File("src/main/resources/ensa/project_vt/UI/edit-captions.png").toURI().toString());
        // ImageView's
        playIV = makeIcon(playIV,imgPlay);
        pauseIV = makeIcon(pauseIV,imgPause);
        muteIV = makeIcon(muteIV,imgMute);
        unmuteIV = makeIcon(unmuteIV,imgUnmute);
        replayIV = makeIcon(replayIV,imgReplay);
        fullScreenIV = makeIcon(fullScreenIV,imgFullScreen);
        editModeIV = makeIcon(editModeIV,imgEditMode);
        fullScreenBtn.setGraphic(fullScreenIV);
        editBtn.setGraphic(editModeIV);
        playBtn.setFocusTraversable(false);
        fullScreenBtn.setFocusTraversable(false);
        editBtn.setFocusTraversable(false);
        muteBtn.setFocusTraversable(false);


        playBtn.setGraphic(pauseIV);
        mediaPlayer.play();
        isPlaying=true;
        isMute=false;
        isOver=false;
        isEditMode=false;
        isFullScreen=false;
        currentVolume=1;
        muteBtn.setGraphic(unmuteIV);


        initCaption();


        // Initialize animations
        translateEditBox = new TranslateTransition(Duration.millis(200),editBox);
        translateVideo = new TranslateTransition(Duration.millis(200),videoPlayer);
        scaleVideo = new ScaleTransition(Duration.millis(200),videoPlayer);
        //Animation of control bar
        fadeIn = new FadeTransition(Duration.millis(200),controlBar);
        fadeOut = new FadeTransition(Duration.millis(200),controlBar);
        fadeIn.setFromValue(0);fadeIn.setToValue(1);
        fadeOut.setFromValue(1);fadeOut.setToValue(0);
        editHBox.setClip(new Rectangle(320,700));
        editHBox.setTranslateX(250);

        // Control Bar animations
        videoPlayer.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {fadeIn.play();}
        });
        videoPlayer.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent){fadeOut.play();}
        });
//        Bindings.bindBidirectional(closedCaptions.textProperty(), captionEditText.textProperty());
        // volume
        Bindings.bindBidirectional(mediaPlayer.volumeProperty(),volumeSlider.valueProperty());
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if(volumeSlider.getValue()==0){muteBtn.setGraphic(muteIV);}
                else {muteBtn.setGraphic(unmuteIV);}

            }
        });

        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                isOver=true;
                isPlaying=false;
                playBtn.setGraphic(replayIV);
                mediaPlayer.seek(Duration.millis(0));
                mediaPlayer.pause();
                initCaption();

            }
        });


        // Progress bar slider
        mediaPlayer.totalDurationProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldDuration, Duration newDuration) {
                timeSlider.setMax(newDuration.toMillis());
            }
        });
        timeSlider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean wasChanging, Boolean isChanging) {
                    mediaPlayer.seek(new Duration(timeSlider.getValue()));
            }
        });
        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                double currentTime = mediaPlayer.getCurrentTime().toMillis();
                timeProgress.setProgress(mediaPlayer.getCurrentTime().toMillis()/mediaVideo.getDuration().toMillis());
                if(Math.abs(currentTime-newValue.doubleValue())>500)
                {
                    if(playBtn.getGraphic().equals(replayIV)) playBtn.setGraphic(playIV);
                    mediaPlayer.seek(Duration.millis(newValue.doubleValue()));
                    findCaption();
                    loadCaption();
                }
                timeLabel.setText(timeString(timeSlider.getValue()));
            }
        });


        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldTime, Duration newValue) {

                if(!timeSlider.isValueChanging())
                {
                    isOver=false;
                    timeSlider.setValue(mediaPlayer.getCurrentTime().toMillis());
                    if(newValue.toMillis()>nextCaption.getStart() && nextCaption.getId()!=actualCaption.getId())
                    {

                        captionIndex++;
                        System.out.println("captionIndex: "+captionIndex);
                        closedCaptions.setText(nextCaption.getText());
                        actualCaption = nextCaption;
                        if(sp.getCaptions().containsKey(captionIndex+1)) nextCaption = sp.getCaptions().get(captionIndex+1);
                        loadCaption();
                    }
                    if(newValue.toMillis()>actualCaption.getEnd())
                    {
                        closedCaptions.setText("");
                    }

                }
            }
        });
        fullScreenBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {

                if(isEditMode) editMode();
                if(!isFullScreen) {
                    enterFullScreen(mouseEvent);
                }
                else{
                    exitFullScreen(mouseEvent);
                }
                isFullScreen = !isFullScreen;
            }
        });


        //TODO
//        captionEditText.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
//                System.out.println(t1);
//            }
//        });





    }

    @FXML
    protected void playVideo()
    {
        if(isOver)
        {
            mediaPlayer.play();
            playBtn.setGraphic(pauseIV);
            isPlaying=true;
            isOver=false;
            initCaption();
        }
        else {
            if(!isPlaying)
            {
                mediaPlayer.play();
                playBtn.setGraphic(pauseIV);
            }
            else {
                mediaPlayer.pause();
                playBtn.setGraphic(playIV);

            }
        }
        isPlaying = !isPlaying;
    }
    @FXML
    protected void mute()
    {
        if(isMute || volumeSlider.getValue()==0)
        {
            isMute = true;
            mediaPlayer.setVolume(currentVolume);
            muteBtn.setGraphic(unmuteIV);
        }
        else
        {
            currentVolume = mediaPlayer.getVolume();
            mediaPlayer.setVolume(0);
            muteBtn.setGraphic(muteIV);
        }
        isMute = !isMute;
    }
    @FXML
    void saveCaptions(ActionEvent event) {
        sp.editCaption(captionEditText.getText(),captionIndex);
        try {
            Files.writeString(Paths.get("src\\main\\resources\\ensa\\project_vt\\subs2.srt"),sp.format(), StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("can't save file");
        }
        closedCaptions.setText(captionEditText.getText());
        System.out.println("Saved");
    }


    @FXML
    void loadCaption(MouseEvent event) {
        loadCaption();
    }
    public void loadCaption() {
        if(actualCaption!= null && actualCaption.getId()!=-1)
        {
            captionEditText.setText(actualCaption.getText());
            String start = sdf.format(new Date((long)actualCaption.getStart()));
            String end = sdf.format(new Date((long)actualCaption.getEnd()));
            captionEditLabel.setText(start+" -> "+end);
        }

    }


    @FXML
    public void editMode()
    {
        if(isFullScreen)
        {
            Stage stage = (Stage) fullScreenBtn.getScene().getWindow();
            stage.setFullScreen(false);
            videoPlayer.setPrefHeight(481);
            videoPlayer.setPrefWidth(854);
            mediaView.setFitHeight(481);
            mediaView.setFitWidth(854);
            captionBox.setLayoutY(481-150);
            captionBox.setPrefWidth(854);
            controlBar.setLayoutY(481-51);
            controlBar.setPrefWidth(854);
            isFullScreen = false;
        }
        if(!scaleVideo.getStatus().equals(Animation.Status.RUNNING))
        {
            if(!isEditMode)
            {
                scaleVideo.setToX(0.8);
                scaleVideo.setToY(0.8);
                translateVideo.setByX(-200);
                scaleVideo.play();
                translateVideo.play();
                translateEditBox.setByX(-300);
                translateEditBox.play();
            }
            else
            {
                scaleVideo.setToX(1);
                scaleVideo.setToY(1);
                translateVideo.setByX(200);
                scaleVideo.play();
                translateVideo.play();
                translateEditBox.setByX(+300);
                translateEditBox.play();
            }
            isEditMode = !isEditMode;

        }
    }
    public void enterFullScreen(MouseEvent mouseEvent)
    {
        if (isEditMode) editMode();
        Button btn = (Button) mouseEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setFullScreen(true);
        videoPlayer.setPrefHeight(stage.getScene().getHeight());
        videoPlayer.setPrefWidth(stage.getScene().getWidth());
        mediaView.setFitHeight(stage.getScene().getHeight());
        mediaView.setFitWidth(stage.getScene().getWidth());
        captionBox.setLayoutY(stage.getScene().getHeight()-150);
        captionBox.setPrefWidth(stage.getScene().getWidth());
        controlBar.setLayoutY(stage.getScene().getHeight()-51);
        controlBar.setPrefWidth(stage.getScene().getWidth());
    }
    public void exitFullScreen(MouseEvent mouseEvent)
    {
        Button btn = (Button) mouseEvent.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setFullScreen(false);
        videoPlayer.setPrefHeight(481);
        videoPlayer.setPrefWidth(854);
        mediaView.setFitHeight(481);
        mediaView.setFitWidth(854);
        captionBox.setLayoutY(481-150);
        captionBox.setPrefWidth(854);
        controlBar.setLayoutY(481-51);
        controlBar.setPrefWidth(854);
    }
    public void initCaption()
    {
        captionIndex = -1;
        actualCaption = new Caption(-1);
        nextCaption = sp.getCaptions().get(0);
        captionInit = new Caption(-1);
    }


    public ImageView makeIcon(ImageView iv,Image img)
    {
        iv = new ImageView(img);
        iv.setFitHeight(20);
        iv.setFitWidth(20);
        return iv;
    }
    public void findCaption()
    {
        HashMap<Integer,Caption> result = sp.search(mediaPlayer.getCurrentTime().toMillis(),0,sp.getCaptions().size());
        if(result==null)
        {
            closedCaptions.setText("");
        }
        else
        {
            if(result.containsKey(0))
            {

                closedCaptions.setText("");
                nextCaption=result.get(0);
                captionIndex = nextCaption.getId()-1;
                if(captionIndex==0) actualCaption = captionInit;
                else actualCaption = sp.getCaptions().get(captionIndex);
            }
            else {
                closedCaptions.setText(result.get(1).getText());
                actualCaption = result.get(1);
                captionIndex=actualCaption.getId();
                nextCaption = sp.getCaptions().get(captionIndex+1);
            }

        }
    }
    public String timeString(double time)
    {
        String seconds = String.format("%02d",(int) (time/1000) %60);
        String minutes = String.format("%02d",(int) ((time/(1000*60)) %60)) ;
        String hours = String.format("%02d",(int) ((time/(1000*60*60)) %24)) ;
        return ((hours.equals("00"))?"":hours+":")+minutes+":"+seconds;

    }
    public Boolean isFieldFocused()
    {
        return captionEditText.isFocused();
    }

    // Keyboard Shortcuts
    public void exitFullScreenSC()
    {
        Stage stage = (Stage) fullScreenBtn.getScene().getWindow();
        stage.setFullScreen(false);
        videoPlayer.setPrefHeight(481);
        videoPlayer.setPrefWidth(854);
        mediaView.setFitHeight(481);
        mediaView.setFitWidth(854);
        captionBox.setLayoutY(481-150);
        captionBox.setPrefWidth(854);
        controlBar.setLayoutY(481-51);
        controlBar.setPrefWidth(854);
        isFullScreen = false;
    }
    public void next()
    {
        mediaPlayer.seek(Duration.millis(mediaPlayer.getCurrentTime().toMillis()+5000));
        findCaption();
        loadCaption();
        System.out.println("next");
    }
    public void prev()
    {
        mediaPlayer.seek(Duration.millis(mediaPlayer.getCurrentTime().toMillis()-5000));
        findCaption();
        loadCaption();
    }
}

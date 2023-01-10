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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class VideoPlayerController {
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
    private Label errorLabel;
    @FXML
    private VBox captionBox;
    @FXML
    private ImageView backBtn;
    @FXML
    private Button saveCaptionBtn;


    private Image imgPlay,imgPause,imgMute,imgUnmute,imgReplay,imgFullScreen,imgEditMode;
    private ImageView playIV,pauseIV,muteIV,unmuteIV,replayIV,fullScreenIV,editModeIV;
    private boolean isPlaying,isMute,isOver,isEditMode,isFullScreen,isCaptionLoading;
    private double currentVolume;
    private SrtParser sp;
    private Boolean isVideoFound, isSRTFound;
    private int captionIndex;
    private Caption actualCaption,nextCaption,captionInit;
    private TranslateTransition translateEditBox,translateVideo;
    private ScaleTransition scaleVideo;
    private FadeTransition fadeIn,fadeOut;
    private SimpleDateFormat sdf;
    private String videoPath;
    private String srtPath;
    private String previousInterface;


    public void intermediateFunction(String videoPath,String srtPath,String interfaceFxmlName){
        setVideoPath(videoPath);
        setSrtPath(srtPath);
        System.out.println(videoPath+"  "+srtPath);
        setPreviousInterface(interfaceFxmlName);
        initializeFunction();
    }

    public void initializeFunction()
    {
        isVideoFound = false;
        backBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                System.out.println("back pressed");
                Stage stage=null;
                Scene scene = null;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(previousInterface));
                    stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                    scene = new Scene(fxmlLoader.load(), 1200, 700);
                    stage.setTitle("Project VT");
                    if(previousInterface.equals("search-view.fxml")){
                        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                        stage.setScene(scene);
                        SearchViewController searchViewController = fxmlLoader.getController();
                        searchViewController.setStage(stage);
                        stage.show();
                    }else{
                        stage.setScene(scene);
                    }
                    stage.show();
                    if(isVideoFound) mediaPlayer.dispose();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

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
        captionEditText.setFocusTraversable(false);
        saveCaptionBtn.setFocusTraversable(false);


        playBtn.setGraphic(pauseIV);
        isPlaying=true;
        isMute=false;
        isOver=false;
        isEditMode=false;
        isFullScreen=false;
        isCaptionLoading = false;
        currentVolume=1;
        muteBtn.setGraphic(unmuteIV);




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
        try{

        mediaVideo = new Media(new File(videoPath).toURI().toString());
        mediaPlayer = new MediaPlayer(mediaVideo);
        mediaView.setMediaPlayer(mediaPlayer);
        isVideoFound = true;

        }
        catch (Exception e)
        {
            errorLabel.setText("Fichier non trouvé");
            return;
        }

        mediaPlayer.play();

        sp = new SrtParser(srtPath,mediaVideo.getDuration().toMillis());
        sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        initCaption();
        if(sp.getCaptions().isEmpty())
        {
            editBtn.setVisible(false);
            editBtn.setDisable(true);
        }
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                totalLabel.setText("/"+timeString(mediaVideo.getDuration().toMillis()));
                setShortcuts();
            }
        });


        // Control Bar animations
        videoPlayer.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {fadeIn.play();}
        });
        videoPlayer.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent){fadeOut.play();}
        });

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

        //Feature: video paused automatically after clicking on field to edit
        captionEditText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1 && isPlaying) playVideo();
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
                    if (isPlaying) playVideo();
                    System.out.println("event seek");
                    if(playBtn.getGraphic().equals(replayIV)) playBtn.setGraphic(playIV);
                    System.out.println("newValue.doubleValue() = " + newValue.doubleValue());
                    mediaPlayer.seek(Duration.millis(newValue.doubleValue()));
                    findCaption(newValue.doubleValue());
                    System.out.println("math.abs setText ");
                    loadCaption();
                    if(!isPlaying) playVideo();
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
                    if(!sp.getCaptions().isEmpty())
                    {
                        if(newValue.toMillis()>nextCaption.getStart() && nextCaption.getId()!=actualCaption.getId())
                        {

                            captionIndex++;
                            actualCaption = nextCaption;
                            if(sp.getCaptions().containsKey(captionIndex+1)) nextCaption = sp.getCaptions().get(captionIndex+1);
                            closedCaptions.setText(actualCaption.getText());
                            System.out.println("isValueChanging setText");
                            loadCaption();
                        }
                        if(newValue.toMillis()>actualCaption.getEnd() && newValue.toMillis()< nextCaption.getStart())
                        {
                            closedCaptions.setText("");
                        }

                    }

                }
            }
        });
        closedCaptions.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(t1.equals("")) closedCaptions.setVisible(false);
                else closedCaptions.setVisible(true);
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
    }


    public void setShortcuts(){
        videoPlayer.getScene().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) ->{
            if(!isEditMode)
            {
                switch(event.getCode())
                {
                    case SPACE:
                        playVideo();
                        break;
                    case L:
                        next();
                        break;
                    case J:
                        prev();

                        break;
                    case ESCAPE:
                        exitFullScreenSC();
                        break;
                }

            }
        });
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

    //old function
//    @FXML
//    void saveCaptions(ActionEvent event) {
//
//        //Validation
//        if(!sp.getCaptions().isEmpty())
//        {
//            String[] parts = captionEditText.getText().split("\n");
//            String validText = "";
//            for (int i = 0; i < parts.length; i++) {
//                if(!parts[i].equals("")) {
//                    validText+=parts[i];
//                    if(i != parts.length-1) validText+="\n";
//                }
//            }
//
//            captionEditText.setText(validText);
//            sp.editCaption(validText,captionIndex);
//            try {
//                Files.writeString(Paths.get(srtPath),sp.format(), StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
//            } catch (IOException e) {
//                System.out.println("can't save file");
//            }
//            closedCaptions.setText(captionEditText.getText());
//            System.out.println("Saved");
//        }
//    }

    //new function
    @FXML
    void saveCaptions(ActionEvent event) {

        //Validation
        if(!sp.getCaptions().isEmpty())
        {
            if(captionEditText.getText().equals(""))
            {
                captionEditText.setText(actualCaption.getText());

            }
            else {
                String[] parts = captionEditText.getText().split("\n");
                System.out.println(Arrays.toString(parts));
                String validText = "";
                if(parts.length == 0 )
                {
                    captionEditText.setText(actualCaption.getText());
                    return;
                }
                for (int i = 0; i < parts.length; i++) {
                    if(parts[i].equals(""))
                    {
                        captionEditText.setText(actualCaption.getText());
                        return;
                    }
                    if(!parts[i].equals("")) {
                        validText+=parts[i];
                        if(i != parts.length-1) validText+="\n";
                    }
                }

                captionEditText.setText(validText);
                sp.editCaption(validText,captionIndex);
                try {
                    Files.writeString(Paths.get(srtPath),sp.format(), StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
                } catch (IOException e) {
                    System.out.println("can't save file");
                }
                closedCaptions.setText(captionEditText.getText());
                System.out.println("Saved");

            }
        }
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
        if(sp.getCaptions().isEmpty()) nextCaption = new Caption(-1);
        else nextCaption = sp.getCaptions().get(0);
        captionInit = new Caption(-1);
    }


    public void findCaption(Double time)
    {
        if(sp.getCaptions().isEmpty())
        {
            actualCaption = captionInit;
            nextCaption = captionInit;
        }
        else{

            HashMap<Integer,Caption> result = sp.search(time,0,sp.getCaptions().size());
            System.out.println(result);
            if(result==null)
            {
                closedCaptions.setText("");
            }
            else
            {
                if(result.containsKey(0))
                {
                    closedCaptions.setText("");
                    if(result.get(0)==null)
                    {
                        captionIndex = sp.getCaptions().size()-1;
                        actualCaption = sp.getCaptions().get(captionIndex);
                        nextCaption = actualCaption;
                    }
                    else
                    {
                        nextCaption = result.get(0);
                        captionIndex = nextCaption.getId()-1;
                        if(captionIndex==-1){
                            actualCaption = captionInit;
                        }
                        else actualCaption = sp.getCaptions().get(captionIndex);

                    }
                }
                else {
                    actualCaption = result.get(1);
                    captionIndex=actualCaption.getId();
                    if(sp.getCaptions().containsKey(captionIndex+1))
                        nextCaption = sp.getCaptions().get(captionIndex+1);
                    else
                        nextCaption = actualCaption;
                    closedCaptions.setText(actualCaption.getText());
                }

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
        if(isPlaying) playVideo();
        Double newValue = timeSlider.getValue()+5000;
        timeSlider.setValue(newValue);
        if(!isPlaying) playVideo();
    }
    public void prev()
    {
        if (isPlaying) playVideo();
        Double newValue = timeSlider.getValue()-5000;
        timeSlider.setValue(newValue);
        if (!isPlaying) playVideo();
    }


    public ImageView makeIcon(ImageView iv,Image img)
    {
        iv = new ImageView(img);
        iv.setFitHeight(20);
        iv.setFitWidth(20);
        return iv;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setSrtPath(String srtPath) {
        this.srtPath = srtPath;
    }

    public void setPreviousInterface(String previousInterface) {
        this.previousInterface = previousInterface;
    }
}

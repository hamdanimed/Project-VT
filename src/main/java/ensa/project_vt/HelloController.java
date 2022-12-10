package ensa.project_vt;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
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
    private Button muteBtn;
    @FXML
    private Label closedCaptions;

    private Image imgPlay,imgPause,imgMute,imgUnmute;
    private ImageView playIV,pauseIV,muteIV,unmuteIV;
    private boolean isPlaying,isMute;
    private double videoLength;

    private double captionStart,captionEnd;
    private int captionIndex;
    private Caption actualCaption,nextCaption;

    @Override
    public void initialize(URL url,ResourceBundle resourceBundle)
    {
        mediaVideo = new Media(new File("src\\main\\resources\\ensa\\project_vt\\video\\video.mp4").toURI().toString());
        mediaPlayer = new MediaPlayer(mediaVideo);
        mediaView.setMediaPlayer(mediaPlayer);

        SrtParser sp = new SrtParser("src\\main\\resources\\ensa\\project_vt\\subs.srt",mediaVideo.getDuration().toMillis());


        // Images
        imgPlay = new Image(new File("src/main/resources/ensa/project_vt/UI/play.png").toURI().toString());
        imgPause = new Image(new File("src/main/resources/ensa/project_vt/UI/pause.png").toURI().toString());
        imgMute = new Image(new File("src/main/resources/ensa/project_vt/UI/mute.png").toURI().toString());
        imgUnmute = new Image(new File("src/main/resources/ensa/project_vt/UI/unmute.png").toURI().toString());
        // ImageView's
        playIV = makeIcon(playIV,imgPlay);
        pauseIV = makeIcon(pauseIV,imgPause);
        muteIV = makeIcon(muteIV,imgMute);
        unmuteIV = makeIcon(unmuteIV,imgUnmute);

        playBtn.setGraphic(pauseIV);
        mediaPlayer.play();
        isPlaying=true;
        isMute=false;
        muteBtn.setGraphic(unmuteIV);

        //Initialize captions
        captionIndex = 0;
        actualCaption = new Caption();
        nextCaption = sp.getCaptions().get(1);

        //Animation of control bar
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200),controlBar);
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200),controlBar);
        fadeIn.setFromValue(0);fadeIn.setToValue(1);
        fadeOut.setFromValue(1);fadeOut.setToValue(0);
        videoPlayer.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                fadeIn.play();
            }
        });
        videoPlayer.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent){
                fadeOut.play();
            }
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
                if(true)
                {
                    mediaPlayer.seek(new Duration(timeSlider.getValue()));
                }
            }
        });


        timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                double currentTime = mediaPlayer.getCurrentTime().toMillis();
                if(Math.abs(currentTime-newValue.doubleValue())>500)
                {
                    mediaPlayer.seek(Duration.millis(newValue.doubleValue()));
                    HashMap<Integer,Caption> result = sp.find(newValue.doubleValue());
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
                            nextCaption=result.get(0);
                            captionIndex = nextCaption.getId()-1;
                            actualCaption = sp.getCaptions().get(captionIndex);
                        }
                        else {
                            closedCaptions.setText(result.get(1).getText());
                            actualCaption = result.get(1);
                            captionIndex=actualCaption.getId();
                            nextCaption = sp.getCaptions().get(captionIndex+1);
                        }

                    }


                }


            }
        });
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration oldTime, Duration newValue) {
                if(!timeSlider.isValueChanging())
                {
                    timeSlider.setValue(mediaPlayer.getCurrentTime().toMillis());
                    if(newValue.toMillis()>nextCaption.getStart())
                    {
                        captionIndex++;
                        closedCaptions.setText(nextCaption.getText());
                        actualCaption = nextCaption;
                        if(sp.getCaptions().containsKey(captionIndex+1)) nextCaption = sp.getCaptions().get(captionIndex+1);
                    }
                    if(newValue.toMillis()>actualCaption.getEnd())
                    {
                        closedCaptions.setText("");
                    }

                }
            }
        });
        System.out.println(sp.find(6000));






    }

    @FXML
    protected void playVideo()
    {
        if(!isPlaying)
        {
            mediaPlayer.play();
            playBtn.setGraphic(pauseIV);
        }
        else {
            mediaPlayer.pause();
            playBtn.setGraphic(playIV);

        }
        isPlaying = !isPlaying;
    }
    @FXML
    protected void mute()
    {
        if(isMute || volumeSlider.getValue()==0)
        {
            System.out.println("mute");
            mediaPlayer.setVolume(1);
            muteBtn.setGraphic(unmuteIV);
        }
        else
        {
            System.out.println("unmute");
            mediaPlayer.setVolume(0);
            muteBtn.setGraphic(muteIV);
        }
        isMute = !isMute;
        mediaPlayer.seek(new Duration(5800));
    }

    public ImageView makeIcon(ImageView iv,Image img)
    {
        iv = new ImageView(img);
        iv.setFitHeight(20);
        iv.setFitWidth(20);
        return iv;
    }

}
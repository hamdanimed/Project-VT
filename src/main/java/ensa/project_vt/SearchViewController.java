package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.Speechmatics;
import ensa.project_vt.GenerateSubtitles.YoutubeDl;
import ensa.project_vt.GenerateSubtitles.YoutubeDlTask;
import ensa.project_vt.YoutubeSearch.VisitYoutube;
import ensa.project_vt.YoutubeSearch.YoutubeApiThread;
import ensa.project_vt.YoutubeSearch.YoutubeVideo;

import ensa.project_vt.localVideo.localVideo;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.*;

public class SearchViewController {
    private Stage stage;
    private Parent root;
    private Scene scene;
    private YoutubeVideo selectedVideo;

    @FXML
    private TextField searchField;
    @FXML
    private Button search;
    @FXML
    private Text mainText;
    @FXML
    private Pane pane;
    private String searchInput;
    @FXML
    private ListView<YoutubeVideo> listView;
    @FXML
    private Button browse;
    @FXML
    private ImageView imageView;
    @FXML
    Label videoLinkLabel;
    @FXML
    Label videoTitleLabel;

    @FXML
    Label videoDurationLabel;
    @FXML
    Text textInfo;
    @FXML
    ImageView Back;
    @FXML
    Pane progressArea;
    @FXML
    Label operation;
    @FXML
    Label operationProgress;
    @FXML
    Pane stepsPane;

    //YoutubeDl , Speechmatics Variables
    private String speechmaticsConfigFilePath;
    private String youtubeDlConfigFilePath;
    private String youtubeDlExePath;
    private String appFolder;
    private YoutubeDl youtubeDl;
    private Speechmatics speechmatics;

    public void initializeYoutubeDlandSpeechmaticsObjects(){
        File myObjForInfo = new File("src/main/resources/ensa/project_vt/generate_subtitles/speechmatics-config-standard.json");
        if (myObjForInfo.exists()) {
            this.speechmaticsConfigFilePath= myObjForInfo.getAbsolutePath();
        } else {
            System.out.println("The speechmatics-config-standard.json file does not exist.");
        }
        myObjForInfo = new File("src/main/resources/ensa/project_vt/generate_subtitles/youtube-dl-config.conf");
        if (myObjForInfo.exists()) {
            this.youtubeDlConfigFilePath= myObjForInfo.getAbsolutePath();
        } else {
            System.out.println("The youtube-dl-config.conf file does not exist.");
        }
        myObjForInfo = new File("src/main/resources/ensa/project_vt/generate_subtitles/youtube-dl.exe");
        if (myObjForInfo.exists()) {
            this.youtubeDlExePath= myObjForInfo.getAbsolutePath();
        } else {
            System.out.println("The youtube-dl.exe file does not exist.");
        }

        this.appFolder="C:\\Users\\hp\\PC\\project-vt-files\\";
        this.youtubeDl=new YoutubeDl(appFolder,youtubeDlConfigFilePath,youtubeDlExePath) ;
        this.speechmatics=new Speechmatics(appFolder,speechmaticsConfigFilePath) ;

    }

    public void Back(){
        if(pane.isVisible()){
            pane.setVisible(false);
            listView.setVisible(true);
            textInfo.setText("Results");
        }else{
            HandleDialogs();
        }
    }

    public void HandleDialogs(){
        try {
            // load the fxml for the popup window
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("quality-popup.fxml"));
            DialogPane dialogPane = fxmlLoader.load(); // load the dialog view

            MenuDialogController menuDialogController = fxmlLoader.getController(); // load the controller for the dialog
            youtubeDl.setYoutubelink("https://www.youtube.com/watch?v=-EbzDqtZEh4");
            Thread checkQualityThread = new Thread(new YoutubeDlTask(youtubeDl,menuDialogController,"checkQuality"));
            checkQualityThread.start();
//            List<String> videoQualities = new ArrayList<>(Arrays.asList("1080P","720P","480P"));
//            List<String> audioQualities = new ArrayList<>(Arrays.asList("wav","mp3"));
            // add quality options
//            menuDialogController.setVideoQualityOptions(videoQualities,audioQualities);

            Dialog<ButtonType> qualitiesDialog = new Dialog<>();
            qualitiesDialog.setDialogPane(dialogPane);
            qualitiesDialog.setTitle("Video & audio quality");

            Dialog<ButtonType> downloadingDialog=new Dialog<>();
            downloadingDialog.setTitle("Downloading Progress");
//            downloadingDialog.setDialogPane(dialogPane);

            Dialog<ButtonType> uploadAudioDialog= new Dialog<>();
            uploadAudioDialog.setTitle("Uploading the Audio");
//            downloadingDialog.setDialogPane(dialogPane);

            Optional<ButtonType> clickedButton = qualitiesDialog.showAndWait();
            Optional<ButtonType> clickedButton2= Optional.of(ButtonType.NO);
            // if you click on Finish
            if(clickedButton.get()==ButtonType.FINISH){
                // Check if both qualities were choosen
                    if(menuDialogController.isAudioQualityChoosen() && menuDialogController.isVideoQualityChoosen()) {
                        // get the video & audio quality values from menuDialogController
                        // set the video & audio quality to the global selectedVideo variable
                        selectedVideo.setVideoQuality(menuDialogController.getVideoQuality());
                        selectedVideo.setAudioQuality(menuDialogController.getAudioQuality());
                        System.out.println(selectedVideo.getYtVideoQuality()+" : "+selectedVideo.getYtAudioQuality());
                    }
            }

            //NOT WORKING CODE ,NOT GOOD CODE
            System.out.println(clickedButton.get());
            if(clickedButton.get()==ButtonType.NEXT){
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("progress.fxml"));
                dialogPane=fxmlLoader.load();
                downloadingDialog.setDialogPane(dialogPane);
            }
            if(qualitiesDialog.isShowing()){
                qualitiesDialog.close();
            }
            clickedButton2 = downloadingDialog.showAndWait();
            System.out.println("clicked2"+clickedButton2.get());
            if(clickedButton2.get()==ButtonType.NEXT){
                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("progressUploadAudio.fxml"));
                dialogPane=fxmlLoader.load();
                uploadAudioDialog.setDialogPane(dialogPane);
                if(downloadingDialog.isShowing()){
                    downloadingDialog.close();
                }
                uploadAudioDialog.showAndWait();
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void displayInfo(YoutubeVideo video){
        Image image = new Image(video.getThumbnailUrl());
        imageView.setImage(image);
        videoTitleLabel.setText(video.getVideoTitle());
        videoDurationLabel.setText(video.getDuration());
        videoLinkLabel.setText(video.getUrl());
        textInfo.setVisible(true);
        textInfo.setText("Your Video");
    }
    public void handleMouseClick(MouseEvent e) throws IOException {
        if(listView.getSelectionModel().getSelectedItem() != null){
            selectedVideo = listView.getSelectionModel().getSelectedItem();
            System.out.println("clicked on " + selectedVideo.getVideoTitle());
            System.out.println(selectedVideo.getYtVideoQuality());
            System.out.println(selectedVideo.getDuration());
            System.out.println(selectedVideo.getUrl());
            listView.setVisible(false);
            displayInfo(selectedVideo);
            pane.setVisible(true);
        }
    }

    @FXML
    private void initialize() {
        initializeYoutubeDlandSpeechmaticsObjects();
        textInfo.setVisible(false);
        pane.setVisible(false);
        listView.setVisible(false);
        progressArea.setVisible(false);
        listView.setCellFactory(resultListView -> new ResultCell());

    }
    private void createVideoInstance(File file){
        Media mediaFile = new Media(file.toURI().toString());

        MediaPlayer mediaPlayer = new MediaPlayer(mediaFile);

        mediaPlayer.setOnReady(new Runnable() {

            @Override
            public void run() {

                System.out.println("Duration: "+mediaFile.getDuration().toSeconds());
                localVideo localVideo = new localVideo("id"+file.getName(),file.getName(),String.valueOf(mediaFile.getDuration().toSeconds()),file.getPath());
                // after creating an instance of localVideo , call the method to create the preview view
            }
        });
    }



    public void setStage(Stage stage) {
        this.stage = stage;
        browse.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Video Files", "*.mp4");
            fileChooser.getExtensionFilters().add(extFilter);
            // get the file selected
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null) {


                createVideoInstance(file);

                // create a new video object with the information from the file
                String title = file.getName();
                System.out.println(file);

            }else {
                System.out.println("No file was selected.");
            }
        });
    }
    // set an event handler for ENTER key in search bar

    //method to be called when search button is clicked
    public void search(ActionEvent a) throws Exception {

        pane.setVisible(false);
        progressArea.setVisible(true);
        operation.setText("Fetching Data from Youtube ");


        // check if the input is empty and return
        searchInput = searchField.getText();
        if(searchInput.isEmpty()){
            mainText.setText("Type a link for a video or a keyword");
            return;
        }
        // get the type of input : is it a link or a keyword ?
        String type = getInputType(searchInput);
        switch (type) {
            case "yt-link" -> System.out.println("It's a youtube link");
//            case "yt-link" -> {
//                mainText.setVisible(false);
//
//                YoutubeApiThread apiThread = new YoutubeApiThread(searchInput,listView,progressArea,textInfo);
//                apiThread.start();
//                apiThread.interrupt();
//            }
            case "link" -> {
                mainText.setText("Oops .. this is not a youtube link");
            }
            case "keyword" -> {
                mainText.setVisible(false);

                YoutubeApiThread apiThread = new YoutubeApiThread(searchInput,listView,progressArea,textInfo);
                apiThread.start();
                apiThread.interrupt();
            }
            default -> System.out.println("Invalid input");
        }
    }

    public String getInputType(String input){
        // check if input is a valid url
        if(isValidURL(input)){
            // check if it's a yt url
            if(isYtUrl(input))
            {
                return "yt-link";
            }
            else return "link";
        }
        else {
            return "keyword";
        }
    }
    public boolean isValidURL(String url){
        String regex = "((http|https)://)(www.)?"
                + "[a-zA-Z0-9@:%._\\+~#?&//=]"
                + "{2,256}\\.[a-z]"
                + "{2,6}\\b([-a-zA-Z0-9@:%"
                + "._\\+~#?&//=]*)";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        // Find match between given string
        // and regular expression
        // using Pattern.matcher()
        Matcher m = p.matcher(url);
        return m.matches();
    }
    // return true if the argument is url
    public boolean isYtUrl(String url){
        String regex = "^((?:https?:)?\\/\\/)?((?:www|m)\\.)?((?:youtube\\.com|youtu.be))(\\/(?:[\\w\\-]+\\?v=|embed\\/|v\\/)?)([\\w\\-]+)(\\S+)?$";
        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(url);
        return m.matches();
    }
    public String getSearchInput(){
        return this.searchInput;
    }

    public Text getMainText() {
        return mainText;
    }

    public ListView<YoutubeVideo> getListView() {
        return listView;
    }
    public void play(){
        System.out.println("I play the video : " + videoTitleLabel.getText()+" the link is : "+videoLinkLabel.getText());
    }
    public void save(){
        System.out.println("I save the video : " + videoTitleLabel.getText()+" the link is : "+videoLinkLabel.getText());
    }
    @FXML
    public void transcript() throws IOException {
        System.out.println("I transcript the video : " + selectedVideo.getVideoTitle()+" the link is : "+selectedVideo.getUrl());
        //test loading a fxml on top of another for the popup
        stepsPane=FXMLLoader.load(getClass().getResource("steps.fxml"));
        youtubeDl.setYoutubelink(selectedVideo.getUrl());
//        System.out.println(youtubeDl.checkAvailableQualities());
//        youtubeDl.downloadVideoAndAudio();
//        YoutubeDlTask task=new YoutubeDlTask("");
//        new Thread(task).start();
        Thread downloadVideoAndAudio = new Thread(new YoutubeDlTask(youtubeDl,new Object(),"downloadVideoAndAudio"));
        Thread checkQualityThread = new Thread(new YoutubeDlTask(youtubeDl,new Object(),"checkQuality"));
        checkQualityThread.start();
        HandleDialogs();
        //this doesnt invoke the downloadVideoAndAudioThread
//        if(!checkQualityThread.isAlive()){
//            downloadVideoAndAudio.start();
//        }
    }

}
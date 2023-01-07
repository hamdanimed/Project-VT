package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.*;
import ensa.project_vt.YoutubeSearch.YoutubeApiThread;
import ensa.project_vt.YoutubeSearch.YoutubeVideo;

import ensa.project_vt.localVideo.localVideo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.regex.*;
import org.apache.commons.io.FilenameUtils;

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
    @FXML
    Button stopBtn;

    //YoutubeDl , Speechmatics Variables
    private String speechmaticsConfigFilePath;
    private String youtubeDlConfigFilePath;
    private String youtubeDlExePath;
    private String appFolder;
    private YoutubeDl youtubeDl;
    private Speechmatics speechmatics;
    private FFmpeg ffmpeg;
    private DataFile dataFile;

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

//        this.appFolder="C:\\Users\\hp\\PC\\project-vt-files\\";
        this.appFolder="src\\main\\resources\\ensa\\project_vt\\project-vt-files\\";
        this.youtubeDl=new YoutubeDl(appFolder,youtubeDlConfigFilePath,youtubeDlExePath) ;
        this.speechmatics=new Speechmatics(appFolder,speechmaticsConfigFilePath) ;
        this.ffmpeg=new FFmpeg(appFolder);
        this.dataFile=new DataFile(appFolder);
        this.youtubeDl.setYoutubelink("https://www.youtube.com/watch?v=-EbzDqtZEh4");
    }

    public void Back(ActionEvent event) {
        if(pane.isVisible()){
            pane.setVisible(false);
            listView.setVisible(true);
            textInfo.setText("Results");
        }
        else{
            Scene scene = null;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(SearchView.class.getResource("Frame1-view.fxml"));
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(fxmlLoader.load(), 1200, 700);
                stage.setTitle("Home");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            HandleDialogsForVideoFromSearch();
        }
    }

    public void launchProgressQualities(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("progressQualities.fxml"));
            DialogPane dialogPane = fxmlLoader.load(); // load the dialog view
            ProgressQualitiesController qualitiesController=fxmlLoader.getController();
            qualitiesController.dataObject=new DataObject(this.youtubeDl,this.speechmatics,this.dataFile);
            qualitiesController.setYoutubeDl(this.youtubeDl);
//            ProgressQualitiesController p=fxmlLoader.getController();

            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
//            dialog.getDialogPane().getScene().getWindow().setUserData("userData");
            dialog.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getSubtitles(ActionEvent event){
//        this.youtubeDl.videoPath="C:\\Users\\hp\\PC\\project-vt-files\\-EbzDqtZEh4\\-EbzDqtZEh4.mp4";
//        this.youtubeDl.videoTitle="-EbzDqtZEh4";
//        DataObject dataObject=new DataObject(this.youtubeDl,this.speechmatics,this.dataFile);
//        dataObject.videoType="ytb";
//        SpeechmaticsTask task=new SpeechmaticsTask(dataObject,this,"getSubtitles");
//        Thread thread=new Thread(task);
//        thread.start();

        Scene scene = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SearchView.class.getResource("mediaplayer.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load(), 1200, 700);
            stage.setTitle("Media Player");
            stage.setScene(scene);
            VideoPlayerController videoPlayerController = fxmlLoader.getController();
//            videoPlayerController.setVideoPath("src\\main\\resources\\ensa\\project_vt\\project-vt-files\\UelDrZ1aFeY\\UelDrZ1aFeY.mp4");
            stage.show();
            videoPlayerController.intermediateFunction("src\\main\\resources\\ensa\\project_vt\\project-vt-files\\UelDrZ1aFeY\\UelDrZ1aFeY.mp4");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void launchProgressUpload(String audioPath,String videoPath){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("progressUploadAudio.fxml"));
            DialogPane dialogPane = fxmlLoader.load(); // load the dialog view

            YoutubeDl uploadYoutubeDl=this.youtubeDl;
            uploadYoutubeDl.audioPath=audioPath;
            uploadYoutubeDl.videoPath=videoPath;

//            dialog = new Dialog<>();
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);

            DataObject dataObject=new DataObject(uploadYoutubeDl,this.speechmatics,this.dataFile);
            dataObject.videoType="local";
            dialog.getDialogPane().getScene().getWindow().setUserData(dataObject);
            dialog.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void HandleDialogsForVideoFromSearch(){
        try {
            //check if the video is already downloaded ; in the database (data.json)
//            if(dataFile.isVideoDownloaded(this.youtubeDl.videoId,"ytb") != "null"){
//                Alert alert=new Alert(Alert.AlertType.ERROR);
//                alert.showAndWait();
//                return ;
//            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("progressQualities.fxml"));
            DialogPane dialogPane = fxmlLoader.load(); // load the dialog view

//            ProgressQualitiesController qualitiesController=fxmlLoader.getController();
//            qualitiesController.dataObject=new DataObject(this.youtubeDl,this.speechmatics,this.dataFile);
//            qualitiesController.setYoutubeDl(this.youtubeDl);

//            dialog = new Dialog<>();
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);

            dialog.getDialogPane().getScene().getWindow().setUserData(new DataObject(this.youtubeDl,this.speechmatics,this.dataFile));

            dialog.show();
        }
        catch (IOException e) {
            e.printStackTrace();
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
        this.stage.resizableProperty().setValue(false);
        browse.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Video Files", "*.mp4");
            fileChooser.getExtensionFilters().add(extFilter);
            // get the file selected
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null) {


                // create a new video object with the information from the file
                createVideoInstance(file);

                //converting to audio
                String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
                this.ffmpeg.setVideo(fileNameWithOutExt,file.getAbsolutePath());

                FFmpegTask convertingTask=new FFmpegTask(ffmpeg,this.dataFile,this);
                Thread convertingThread=new Thread(convertingTask);
                convertingThread.setDaemon(true);
                convertingThread.start();
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
        youtubeDl.setYoutubelink(selectedVideo.getUrl());
        youtubeDl.videoTitle=selectedVideo.getVideoTitle();
        HandleDialogsForVideoFromSearch();
    }

}
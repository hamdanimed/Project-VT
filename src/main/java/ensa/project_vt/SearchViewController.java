package ensa.project_vt;

//import ensa.project_vt.localVideo.localVideo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
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
    public ImageView loadingGif;
    @FXML
    public Text warningMessage;
    @FXML
    public Label msgLabel;
    @FXML
    private Button search;
    @FXML
    private Text linkLabel;
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
    TextFlow videoLinkTextFlow = new TextFlow();
    @FXML
    Label videoTitleLabel;

    @FXML
    TextFlow textFlow = new TextFlow();
    @FXML
    Label videoDurationLabel;
    @FXML
    Text textInfo;
    @FXML
    ImageView back;
    @FXML
    VBox playArea;
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

    private YoutubeVideo videoDisplayedOnPane=null;


    @FXML
    private void initialize() {
        initializeYoutubeDlandSpeechmaticsObjects();
        msgLabel.setVisible(false);
        loadingGif.setVisible(false);
        warningMessage.setVisible(false);
        textInfo.setVisible(false);
        pane.setVisible(false);
        listView.setVisible(false);
        progressArea.setVisible(false);
        listView.setCellFactory(resultListView -> new ResultCell());
        searchField.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // perform action here
                System.out.println("Searching ");
                try {
                    search(e);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }
    public void initializeYoutubeDlandSpeechmaticsObjects(){
        File myObjForInfo = new File("src/main/resources/ensa/project_vt/project-vt-files/speechmatics-config-standard.json");
        if (myObjForInfo.exists()) {
            this.speechmaticsConfigFilePath= myObjForInfo.getAbsolutePath();
        } else {
            System.out.println("The speechmatics-config-standard.json file does not exist.");
        }
        myObjForInfo = new File("src/main/resources/ensa/project_vt/project-vt-files/youtube-dl-config.conf");
        if (myObjForInfo.exists()) {
            this.youtubeDlConfigFilePath= myObjForInfo.getAbsolutePath();
        } else {
            System.out.println("The youtube-dl-config.conf file does not exist.");
        }
        myObjForInfo = new File("src/main/resources/ensa/project_vt/project-vt-files/youtube-dl.exe");
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
    //handel Click on Browse
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
                String fileNameWithOutExt = FilenameUtils.removeExtension(file.getName());
                this.ffmpeg.setVideo(fileNameWithOutExt,file.getAbsolutePath());

            }else {
                System.out.println("No file was selected.");
            }
        });
    }


    //method to be called when search button is clicked
    public void search(ActionEvent a) throws Exception {
        // check if the input is empty and return
        searchInput = searchField.getText();
        textInfo.setVisible(false);
        if(searchInput.isEmpty()){
            warningMessage.setVisible(true);
            mainText.setVisible(false);
            return;

        }
        else {
            pane.setVisible(false);
            progressArea.setVisible(true);
            operation.setText("Fetching Data from Youtube ");
        }
        // get the type of input : is it a link or a keyword ?
        String type = getInputType(searchInput);
        switch (type) {
//            case "yt-link" -> System.out.println("It's a youtube link");
            case "yt-link" -> {
                mainText.setVisible(false);

                YoutubeApiThread apiThread = new YoutubeApiThread(searchInput,listView,progressArea,textInfo);
                apiThread.start();
                apiThread.interrupt();
            }
            case "link" -> {
                mainText.setText("Oops .. this is not a youtube link");
            }
            case "keyword" -> {
                mainText.setVisible(false);
                warningMessage.setVisible(false);
                YoutubeApiThread apiThread = new YoutubeApiThread(searchInput,listView,progressArea,textInfo);
                apiThread.start();
                apiThread.interrupt();
            }
            default -> System.out.println("Invalid input");
        }
    }

    public void play(ActionEvent event){
        System.out.println("I play the video : " + videoTitleLabel.getText()+" the link is : "+videoLinkLabel.getText());
        //need a check of exitance
        Scene scene = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("mediaplayer.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load(), 1200, 700);
            stage.setTitle("Media Player");
            stage.setScene(scene);
            VideoPlayerController videoPlayerController = fxmlLoader.getController();
            stage.show();

            String srtPath="";
            String videoPath="";
            //check if subtitled or not
            if(videoDisplayedOnPane.getId().length()!=0){
                srtPath=dataFile.isSubtitled(videoDisplayedOnPane.getId());
                videoPath=dataFile.getPath(videoDisplayedOnPane.getId());
            }else{
                srtPath=dataFile.isSubtitled(videoDisplayedOnPane.getUrl());
                videoPath=dataFile.getPath(videoDisplayedOnPane.getUrl());
            }
            videoPlayerController.intermediateFunction("src\\main\\resources\\ensa\\project_vt\\project-vt-files\\UelDrZ1aFeY\\UelDrZ1aFeY.mp4","src\\main\\resources\\ensa\\project_vt\\project-vt-files\\UelDrZ1aFeY\\UelDrZ1aFeY.srt","search-view.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void transcript() throws IOException {
        if(dataFile.isVideoDownloaded(this.ffmpeg.videoTitle) != "null"){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Video was already converted to audio ");
            alert.setContentText("Go to the home interface");
            alert.showAndWait();
            return ;
        }
        if(videoDisplayedOnPane.videoType.equals("local")){
            FFmpegTask convertingTask=new FFmpegTask(ffmpeg,this.dataFile,this,"convertToAudio");
            Thread convertingThread=new Thread(convertingTask);
            convertingThread.setDaemon(true);
            convertingThread.start();
        }else{
            youtubeDl.setYoutubelink(selectedVideo.getUrl());
            youtubeDl.videoTitle=selectedVideo.getVideoTitle();
            launchProgressQualities();
        }
    }
    public void back(ActionEvent event) {
        if(pane.isVisible()){
            if(!selectedVideo.videoType.equals("local")) {
                pane.setVisible(false);
                listView.setVisible(true);
                textInfo.setText("Results");
            }
            else {
                pane.setVisible(false);
                mainText.setVisible(true);
                textInfo.setVisible(false);
            }
        }
        else{
            Scene scene = null;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home.fxml"));
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
    public void handleMouseClickOnListItem(MouseEvent e) throws IOException {
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
    public void launchProgressQualities(){
        try {
//            check if the video is already downloaded ; in the database (data.json)
            if(dataFile.isVideoDownloaded(this.youtubeDl.videoId) != "null"){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Video is already downloaded or converted to audio [for local videos]");
                alert.setContentText("Go to the home interface");
                alert.showAndWait();
                return ;
            }

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("progressQualities.fxml"));
            DialogPane dialogPane = fxmlLoader.load(); // load the dialog view

            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);

            dialog.getDialogPane().getScene().getWindow().setUserData(new DataObject(this.youtubeDl,this.speechmatics,this.dataFile,this.ffmpeg));

            dialog.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public void launchProgressUpload(String audioPath,String videoId,String videoTitle,String videoPath,String videoType){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("progressUploadAudio.fxml"));
            DialogPane dialogPane = fxmlLoader.load(); // load the dialog view

            YoutubeDl uploadYoutubeDl=this.youtubeDl;
            uploadYoutubeDl.audioPath=audioPath;
            uploadYoutubeDl.videoPath=videoPath;
            uploadYoutubeDl.videoTitle=videoTitle;
            uploadYoutubeDl.videoId=videoId;

//            dialog = new Dialog<>();
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);

            DataObject dataObject=new DataObject(uploadYoutubeDl,this.speechmatics,this.dataFile,this.ffmpeg);
            dataObject.videoType=videoType;
            dialog.getDialogPane().getScene().getWindow().setUserData(dataObject);
            dialog.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayInfo(YoutubeVideo video){
        /*videoDisplayedOnPane=video;
        System.out.println(videoDisplayedOnPane.toString());
        if(video.getThumbnailUrl()!=null) {
            Image image = new Image(video.getThumbnailUrl());
            imageView.setImage(image);
        }
        videoTitleLabel.setText(video.getVideoTitle());
        textFlow.setPrefWidth(70);
        videoDurationLabel.setText(video.getDuration());
        videoLinkLabel.setText(video.getUrl());
        videoLinkTextFlow.setPrefWidth(70);
        textInfo.setVisible(true);
        textInfo.setText("Your Video");

         */
        videoDisplayedOnPane=video;
        System.out.println(videoDisplayedOnPane.toString());
        if(video.getThumbnailUrl()!=null) {
            imageView.setVisible(true);
            Image image = new Image(video.getThumbnailUrl());
            imageView.setImage(image);
            videoTitleLabel.setPrefWidth(pane.getPrefWidth()-3*25-imageView.getFitWidth());
            textFlow.setPrefWidth(70);
            textFlow.setLayoutX(175);
        }else if(video.getThumbnailUrl()==null){
            imageView.setVisible(false);
            videoTitleLabel.setPrefWidth(pane.getPrefWidth()-2*30);
            textFlow.setLayoutX(25);
        }
        videoTitleLabel.setText(video.getVideoTitle());
        videoDurationLabel.setText(video.getDuration());
        videoLinkLabel.setText(video.getUrl());
        videoLinkTextFlow.setPrefWidth(70);
        textInfo.setVisible(true);
        textInfo.setText("Your Video");

    }
    private void createVideoInstance(File file){
        Media mediaFile = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(mediaFile);

        mediaPlayer.setOnReady(new Runnable() {
            // had to create the video instance here and render it becasue it's the only way to get the video duration
            @Override
            public void run() {

                System.out.println("happens");
                String title = file.getName();
                //String duration = String.valueOf(mediaFile.getDuration().toSeconds());
                Duration duration = Duration.ofMillis((long)mediaFile.getDuration().toMillis());
                // Get the number of hours, minutes, and seconds in the duration
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;
                long seconds = duration.getSeconds() % 60;
                String formattedDuration = String.format("%dh %dm %ds", hours, minutes, seconds);
                String path = file.getPath();
                selectedVideo = new YoutubeVideo();
                selectedVideo.setVideoTitle(title);
                selectedVideo.setDuration(formattedDuration);
                selectedVideo.setUrl(path);
                linkLabel.setText("Path :");
                selectedVideo.videoType="local";
                displayInfo(selectedVideo);
                pane.setVisible(true);
                listView.setVisible(false);


            }
        });
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
    public void save(){
        System.out.println("I save the video : " + videoTitleLabel.getText()+" the link is : "+videoLinkLabel.getText());
    }

}
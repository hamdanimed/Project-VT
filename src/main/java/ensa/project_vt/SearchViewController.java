package ensa.project_vt;

import ensa.project_vt.GenerateSubtitles.DataFile;
import ensa.project_vt.GenerateSubtitles.Speechmatics;
import ensa.project_vt.GenerateSubtitles.YoutubeDl;
import ensa.project_vt.GenerateSubtitles.YoutubeDlTask;
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

import java.io.File;
import java.io.IOException;
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
    @FXML
    Button stopBtn;

    //YoutubeDl , Speechmatics Variables
    private String speechmaticsConfigFilePath;
    private String youtubeDlConfigFilePath;
    private String youtubeDlExePath;
    private String appFolder;
    private YoutubeDl youtubeDl;
    private Speechmatics speechmatics;
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

        this.appFolder="C:\\Users\\hp\\PC\\project-vt-files\\";
        this.youtubeDl=new YoutubeDl(appFolder,youtubeDlConfigFilePath,youtubeDlExePath) ;
        this.speechmatics=new Speechmatics(appFolder,speechmaticsConfigFilePath) ;
        this.dataFile=new DataFile(appFolder);
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

//    Thread checkQualityThread = new Thread(new YoutubeDlTask(youtubeDl,menuDialogController,"checkQuality"));
//    Thread downloadVideoAndAudioThread=new Thread(new YoutubeDlTask(youtubeDl,progressController,"downloadVideoAndAudio"));
//            youtubeDl.setYoutubelink("https://www.youtube.com/watch?v=-EbzDqtZEh4");
//            youtubeDl.setYoutubelink("https://www.youtube.com/watch?v=Pw-0pbY9JeU");
//            ProgressQualitiesController menuDialogController = fxmlLoader.getController(); // load the controller for the dialog

    public void HandleDialogs(){
        try {
            // load the fxml for the popup window
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("progressQualities.fxml"));
            DialogPane dialogPane = fxmlLoader.load(); // load the dialog view


//            checkQualityThread.start();


            Dialog<ButtonType> qualitiesDialog = new Dialog<>();

            qualitiesDialog.setDialogPane(dialogPane);
            qualitiesDialog.setTitle("Video & audio quality");

            qualitiesDialog.setOnCloseRequest(e->{
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("You're about to Cancel t");
                alert.setContentText("Do really want to exit ?");
                if(alert.showAndWait().get()== ButtonType.OK){
                    e.consume();
                }
            });

            Dialog<ButtonType> downloadingDialog=new Dialog<>();
            downloadingDialog.setTitle("Downloading Progress");


            Optional<ButtonType> clickedButtonQualities = qualitiesDialog.showAndWait();
            Optional<ButtonType> clickedButtonDownloading= Optional.of(ButtonType.NO);

            //handle qualitiesDialog
            if(clickedButtonQualities.isPresent() && clickedButtonQualities.get()==ButtonType.NEXT){
//                checkQualityThread.interrupt();

                fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("progress.fxml"));
                dialogPane=fxmlLoader.load();

                ProgressDownloadController progressController=fxmlLoader.getController();
                progressController.videoIdLabel.setText(youtubeDl.videoId);
                progressController.youtubeDl=youtubeDl;


//                dialogPane.setUserData(downloadVideoAndAudioThread);
                downloadingDialog.setDialogPane(dialogPane);

                //if the video is already downloaded
                if(dataFile.isVideoDownloaded(youtubeDl.videoId,"ytb")!="null"){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setContentText("The video has already been downloaded");
                    alert.showAndWait();
                }
                else if (dataFile.isVideoDownloaded(youtubeDl.videoId,"ytb")=="null") {

//                    downloadVideoAndAudioThread.setDaemon(true);
//                    downloadVideoAndAudioThread.start();

                    downloadingDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
                    Node closeButton = downloadingDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
                    closeButton.managedProperty().bind(closeButton.visibleProperty());
                    closeButton.setVisible(false);

                    downloadingDialog.showAndWait();

                }

            }
            else if (clickedButtonQualities.isPresent() && clickedButtonQualities.get()==ButtonType.CANCEL) {
//                checkQualityThread.interrupt();
                qualitiesDialog.close();
            }
            //handle downloadingDialog
//            if(clickedButtonDownloading.isPresent() && clickedButtonDownloading.get()==ButtonType.NEXT){
//                downloadingDialog.close();
//            }
//            else if(clickedButtonDownloading.isPresent() && clickedButtonDownloading.get()==ButtonType.CANCEL){
//                checkQualityThread.interrupt();
//                downloadingDialog.close();
//            }

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

//import javafx.application.Application;
//        import javafx.concurrent.Task;
//        import javafx.event.ActionEvent;
//        import javafx.geometry.Pos;
//        import javafx.scene.Scene;
//        import javafx.scene.control.Alert;
//        import javafx.scene.control.Button;
//        import javafx.scene.control.ButtonBar.ButtonData;
//        import javafx.scene.control.ButtonType;
//        import javafx.scene.control.Dialog;
//        import javafx.scene.control.ProgressIndicator;
//        import javafx.scene.layout.GridPane;
//        import javafx.scene.layout.Region;
//        import javafx.scene.layout.StackPane;
//        import javafx.scene.layout.VBox;
//        import javafx.scene.text.Text;
//        import javafx.stage.Stage;
//        import javafx.stage.Window;
//
//public class DialogTest extends Application {
//
//    Region veil;
//    ProgressIndicator indicator;
//
//    IntField startingNumber = new IntField(0, 999999, 0);
//    IntField endingNumber = new IntField(startingNumber.getValue(), 999999, startingNumber.getValue() + 1);
//    ButtonType printButtonType = new ButtonType("Print", ButtonData.OK_DONE);
//    Stage stage;
//
//    @Override
//    public void start(Stage primaryStage) {
//        stage = primaryStage;
//        Button button = new Button("Print Checks");
//
//        VBox box = new VBox(10, button);
//        box.setAlignment(Pos.CENTER);
//
//        veil = new Region();
//        veil.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3);");
//        veil.setVisible(false);
//
//        indicator = new ProgressIndicator();
//        indicator.setMaxHeight(60);
//        indicator.setMinWidth(60);
//        indicator.setVisible(false);
//
//        StackPane root = new StackPane(box, veil, indicator);
//
//        root.setAlignment(Pos.CENTER);
//
//        Scene scene = new Scene(root, 400, 400);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        button.setOnAction((event) -> {
//            Dialog<ButtonType> dialog
//                    = getCheckPrintDialog(primaryStage, "Enter starting check number");
//            dialog.showAndWait()
//                    .filter(result -> result == printButtonType)
//                    .ifPresent(result -> {
//                        // this is for this example only, normaly you already have this value
//                        endingNumber.setValue(startingNumber.getValue() + 1);
//                        printChecks(startingNumber.getValue(), endingNumber.getValue());
//                    });
//        });
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    public <R extends ButtonType> Dialog getCheckPrintDialog(Window owner, String title) {
//        Dialog<R> dialog = new Dialog<>();
//        dialog.initOwner(owner);
//        dialog.setTitle(title);
//        dialog.getDialogPane().getButtonTypes().addAll(printButtonType, ButtonType.CANCEL);
//
//        Button btOk = (Button) dialog.getDialogPane().lookupButton(printButtonType);
//        btOk.addEventFilter(ActionEvent.ACTION, event -> {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Print Checks? Are you sure?", ButtonType.YES, ButtonType.NO);
//            alert.showAndWait()
//                    .filter(result -> result == ButtonType.NO)
//                    .ifPresent(result -> event.consume());
//        });
//
//        GridPane grid = new GridPane();
//        grid.setHgap(10);
//        grid.setVgap(10);
//
//        Text from = new Text("Starting Number:");
//        grid.add(from, 0, 0);
//
//        grid.add(startingNumber, 1, 0);
//
//        dialog.getDialogPane().setContent(grid);
//        return dialog;
//    }
//
//    private void printChecks(int from, int to) {
//
//        Task<Void> task = new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//                Thread.sleep(5000);
//                return null;
//            }
//        };
//
//        task.setOnSucceeded((event) -> {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Has the last check, the number: " + endingNumber.getValue() + "?", ButtonType.YES, ButtonType.NO);
//            alert.initOwner(stage);
//            Button btnNo = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
//            btnNo.addEventFilter(ActionEvent.ACTION, e -> {
//                Dialog<ButtonType> newEndNum = new Dialog<>();
//                newEndNum.setTitle("Enter the ending check number");
//                newEndNum.initOwner(stage);
//                newEndNum.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
//                GridPane grid = new GridPane();
//                grid.setHgap(10);
//                grid.setVgap(10);
//
//                Text toUser = new Text("Ending Number:");
//                grid.add(toUser, 0, 0);
//
//                grid.add(endingNumber, 1, 0);
//
//                newEndNum.getDialogPane().setContent(grid);
//                newEndNum.showAndWait().filter(result -> result == ButtonType.CANCEL)
//                        .ifPresent(result -> e.consume());
//            });
//            alert.showAndWait();
//        });
//        veil.visibleProperty().bind(task.runningProperty());
//        indicator.visibleProperty().bind(task.runningProperty());
//        new Thread(task).start();
//    }
//
//}
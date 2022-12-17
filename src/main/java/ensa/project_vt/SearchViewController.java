package ensa.project_vt;

import ensa.project_vt.YoutubeSearch.VisitYoutube;
import ensa.project_vt.YoutubeSearch.YoutubeVideo;
import ensa.project_vt.localVideo.localVideo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
import java.util.List;
import java.util.regex.*;

public class SearchViewController {
    private Stage stage;
    private Parent root;
    private Scene scene;
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

    public void Back(){
        if(pane.isVisible()){
            pane.setVisible(false);
            listView.setVisible(true);
            textInfo.setText("Results");
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
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem().getVideoTitle());
        YoutubeVideo video = listView.getSelectionModel().getSelectedItem();

        listView.setVisible(false);
        displayInfo(video);
        pane.setVisible(true);
    }

    @FXML
    private void initialize() {
        textInfo.setVisible(false);
        pane.setVisible(false);
        listView.setVisible(false);
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

            }
        });
    }
    // set an event handler for ENTER key in search bar

    //method to be called when search button is clicked
    public void search(ActionEvent a) throws Exception {
        pane.setVisible(false);
        textInfo.setVisible(true);
        textInfo.setText("Results :");

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
            case "link" -> {
                mainText.setText("Oops .. this is not a youtube link");
            }
            case "keyword" -> {
                String apiKey = "AIzaSyC34VqBS3fiCsJyd2fX1P2fx5yBIQnimTY";
                VisitYoutube client = new VisitYoutube(apiKey);
                //remove the main text
                mainText.setVisible(false);
                listView.setVisible(true);
                //after getting search results
                //TO-DO : get data from yt api
                List<YoutubeVideo> videos = client.Search(searchInput, "snippet", "video", 10);
                ObservableList<YoutubeVideo> data = FXCollections.observableArrayList(videos);
                listView.setItems(data);
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

}
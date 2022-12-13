package ensa.project_vt;

import ensa.project_vt.YoutubeSearch.VisitYoutube;
import ensa.project_vt.YoutubeSearch.YoutubeVideo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane parent;
    private String searchInput;
    @FXML
    private ListView<YoutubeVideo> listView;
    @FXML
    private Button browse;
    @FXML
    private ImageView imageView;

    // To handle mouse click on an item of the list
    @FXML
    /*public void handleMouseClick(MouseEvent arg0) {
        System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem().getVideoTitle());
        YoutubeVideo ytVid = listView.getSelectionModel().getSelectedItem();
        // get information about the video to be displayed in a preview
        String videoTitle = ytVid.getVideoTitle();
        String videoUrl = ytVid.getUrl();
        String videoDuration = ytVid.getDuration();
        // pass those parameters to a method to display them in preview
    }

     */

    public void handleMouseClick(MouseEvent e) throws IOException {
        //System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem().getVideoTitle());
        YoutubeVideo video = listView.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("video_info.fxml"));
        root = loader.load();
        VideoInfo videoInfo = loader.getController();
        videoInfo.displayInfo(video);

        stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("InfoStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void initialize() {
        listView.setVisible(false);
        listView.setCellFactory(resultListView -> new ResultCell());

    }
    /*private void createVideoInstance(File file){
        Media mediaFile = new Media(file.toURI().toString());

        MediaPlayer mediaPlayer = new MediaPlayer(mediaFile);

        mediaPlayer.setOnReady(new Runnable() {

            @Override
            public void run() {

                System.out.println("Duration: "+mediaFile.getDuration().toSeconds());
                YoutubeVideo youtubeVideo = new YoutubeVideo(file.getName(),String.valueOf(mediaFile.getDuration().toSeconds()));

            }
        });
    }

     */

    public void setStage(Stage stage) {
        this.stage = stage;
        browse.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Video Files", "*.mp4");
            fileChooser.getExtensionFilters().add(extFilter);
            // get the file selected
            File file = fileChooser.showOpenDialog(stage);
            if(file!=null) {
// create a new video object with the information from the file
                String title = file.getName();

            }
        });
    }
    //method to be called when search button is clicked
    public void search(ActionEvent a) throws Exception {

        searchInput = searchField.getText();
        // check if the input is empty and return
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
package ensa.project_vt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.regex.*;

public class SearchViewController {
   @FXML
    private TextField searchField;
   @FXML
    private Button search;
   private String searchInput;
   //method to be called when search button is clicked
   public void search(ActionEvent a){
       searchInput = searchField.getText();
       // check if the input is empty and return
    if(searchInput.isEmpty()){
        System.out.println("Type a keyword or link");
        return;
    }
       // get the type of input : is it a link or a keyword ?
       String type = getInputType(searchInput);
       switch (type) {
           case "yt-link" -> System.out.println("It's a youtube link");
           case "link" -> System.out.println("It's not a youtube link");
           case "keyword" -> System.out.println("Searching for keyword now");
           default -> System.out.println("Invalid input");
       }

   }
   public void createResultView(String title,String duration){

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
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

   }
   public void createResultView(String title,String duration){

   }
   public String getInputType(String input){
            // check if input is a valid url
            if(isValidURL(input)){
                // check if it's a yt url

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
       // If the string is empty
       // return false
       if (url == null) {
           return false;
       }
       // Find match between given string
       // and regular expression
       // using Pattern.matcher()
       Matcher m = p.matcher(url);
       return m.matches();
   }
   // return true if the argument is url
   public boolean isYtUrl(){

   }
   public String getSearchInput(){
       return this.searchInput;
   }

}
package ensa.project_vt;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class VisitYoutube {

    private String apiKey;
    private UrlCreate Url;
    private  HttpURLConnection youtubeVisitor;
    public VisitYoutube(){

    }

    public  VisitYoutube(String apiKey){
        this.apiKey = apiKey;
        this.Url = new UrlCreate(this.apiKey);
    }


    private String StreamToString(InputStream stream)
    {
        StringBuilder output = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)))
        {
            int c = 0;
            while ((c = reader.read()) != -1)
            {
                output.append((char) c);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return output.toString();
    }
    public  List<YoutubeVideo> Search(String keyword, String part, String type, int maxResults) throws IOException {
        String requestUrl = this.Url.SearchUrl(keyword,part,type,maxResults);
        URL url = new URL(requestUrl);
        this.youtubeVisitor = (HttpURLConnection) url.openConnection();
        this.youtubeVisitor.setRequestProperty("accept","application/json");
        String response = this.StreamToString(this.youtubeVisitor.getInputStream());

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

        JsonArray contents = jsonObject.get("items").getAsJsonArray();
        List<YoutubeVideo> videos = new ArrayList<>();
        for(JsonElement jsonElement : contents){
            JsonObject videoJsonObject = jsonElement.getAsJsonObject();
            if(videoJsonObject.has("snippet")){
                JsonObject videoId = videoJsonObject.get("id").getAsJsonObject();
                JsonObject videoData = videoJsonObject.get("snippet").getAsJsonObject();

                YoutubeVideo video = YoutubeVideo.getVideoInfo(videoId,videoData);
                videos.add(video);
            }
        }
        return videos;
    }
    public String GetVideoDetails(String videoId) throws IOException {
        String requestUrl =this.Url.VideoDetailUrl(videoId);
        URL url = new URL(requestUrl);
        this.youtubeVisitor = (HttpURLConnection) url.openConnection();
        this.youtubeVisitor.setRequestProperty("accept","application/json");
        String response = this.StreamToString(this.youtubeVisitor.getInputStream());
        return response;
    }

}


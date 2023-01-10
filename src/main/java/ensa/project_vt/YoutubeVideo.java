package ensa.project_vt;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4;

public class YoutubeVideo {


    private String id="";
    public String videoType="";
    private String videoTitle;
    private String duration;

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    private String url;
    private String thumbnailUrl;
    private String videoQuality;
    private String audioQuality;

    // for online video results
    public YoutubeVideo(String id, String url, String title,String duration, String thumbnailUrl) {
        this.id = id;
        this.url = url;
        this.videoTitle = title;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
    }
    public YoutubeVideo(){
        // for creating a local video
    }

    public void setVideoQuality(String videoQuality) {
        this.videoQuality = videoQuality;
    }

    public static YoutubeVideo getVideoInfo(JsonObject videoId, JsonObject videoInfo) throws IOException {
        String id = videoId.get("videoId").getAsString();
        //String title = videoInfo.get("title").getAsString();
        String title = unescapeHtml4(videoInfo.get("title").getAsString());

        String thumbnailUrl=videoInfo.get("thumbnails").getAsJsonObject().get("medium").getAsJsonObject().get("url").getAsString();
        String url = "https://www.youtube.com/watch?v=" + id;


        //to get duration
        VisitYoutube client = new VisitYoutube("AIzaSyDF0TDdiIrYK4_izUOOgOSSnNQQZFACOrI");
        String videoInfoDuration = client.GetVideoDetails(id);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(videoInfoDuration, JsonObject.class);
        JsonArray contents = jsonObject.getAsJsonArray("items").getAsJsonArray();
        JsonObject info = contents.get(0).getAsJsonObject();
        String duration = info.get("contentDetails").getAsJsonObject().get("duration").getAsString();
        //System.out.println(duration +" "+duration.replace("PT","").replace("H",":").replace("M",":").replace("S",""));
        String formated = duration.replace("PT","").replace("H","h ").replace("M","min ").replace("S","s");

        return new YoutubeVideo(id, url,title,formated, thumbnailUrl);
    }


    public String getVideoTitle() {
        return videoTitle;
    }

    public String getDuration() {
        return duration;
    }

    public String getUrl() {
        return url;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }


    public void setAudioQuality(String audioQuality) {
        this.audioQuality = audioQuality;
    }

    public String getYtVideoQuality() {
        return videoQuality;
    }

    public String getYtAudioQuality() {
        return audioQuality;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "YoutubeVideo{" +
                "id='" + id + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", duration='" + duration + '\'' +
                ", url='" + url + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", videoQuality='" + videoQuality + '\'' +
                ", audioQuality='" + audioQuality + '\'' +
                '}';
    }
}
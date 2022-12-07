package ensa.project_vt;

public class Result {

    private String videoTitle;
    private String duration;
    private String url;

    public Result(String videoTitle, String duration, String url) {
        this.videoTitle = videoTitle;
        this.duration = duration;
        this.url = url;
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
}

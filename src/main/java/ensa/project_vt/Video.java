package ensa.project_vt;

public class Video {
    protected String id;
    protected String videoTitle;
    protected String duration;
    public Video(String id,String videoTitle,String duration){
        this.id=id;
        this.videoTitle=videoTitle;
        this.duration=duration;
    }

    public String getId() {
        return id;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getDuration() {
        return duration;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}

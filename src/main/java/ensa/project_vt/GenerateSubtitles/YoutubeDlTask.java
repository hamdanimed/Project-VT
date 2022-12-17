package ensa.project_vt.GenerateSubtitles;

import javafx.concurrent.Task;

public class YoutubeDlTask extends Task<Integer> {
    private String action;
    private YoutubeDl youtubeDlObject;

    public YoutubeDlTask(YoutubeDl youtubeDlObject,String action) {
        super();
        this.action=action;
        this.youtubeDlObject=youtubeDlObject;
    }

    @Override
    protected Integer call() throws Exception {

        switch (this.action) {
            case "checkQuality" -> {
                System.out.println("checkQuality");
                this.youtubeDlObject.checkAvailableQualities();
            }
            case "downloadVideoAndAudio" -> {
                System.out.println("downloadVideoAndAudio");
                this.youtubeDlObject.downloadVideoAndAudio();
            }
            default -> {
                System.out.println("Task was Invoked");
                return 1;
            }
        }
        return 0;
    }

}

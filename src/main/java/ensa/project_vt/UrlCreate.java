package ensa.project_vt;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UrlCreate {
    private final String baseUrl = "https://www.googleapis.com/youtube/v3";
    private String baseSearchUrl = this.baseUrl + "/search/?key=";
    private String baseVideoDetailUrl = this.baseUrl + "/videos?key=";

    public UrlCreate(String apiKey)
    {
        this.baseSearchUrl += apiKey;
        this.baseVideoDetailUrl += apiKey;
    }

    public String SearchUrl(String query, String part, String type, int maxResults)
    {
        String encodedQuery;

        encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        String searchUrl = this.baseSearchUrl +
                "&q=" + encodedQuery +
                "&part=" + part +
                "&type=" + type +
                "&maxResults=" + maxResults;

        return searchUrl;
    }
    public String VideoDetailUrl(String videoId)
    {
        return this.baseVideoDetailUrl +
                "&id=" + videoId +
                "&part=contentDetails";
    }
}

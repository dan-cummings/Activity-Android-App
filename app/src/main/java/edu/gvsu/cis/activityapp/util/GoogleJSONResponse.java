package edu.gvsu.cis.activityapp.util;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kyle Flynn on 11/27/2017.
 */

public class GoogleJSONResponse {
    @SerializedName("debug_info")
    private List<String> debug_info;

    @SerializedName("html_attributions")
    private List<String> html_attributions;

    @SerializedName("next_page_token")
    private String next_page_token;

    @SerializedName("results")
    private List<GooglePlacesResults> results;

    public List<String> getDebugInfo() {
        return debug_info;
    }

    public List<String> getHtmlAttributions() {
        return html_attributions;
    }

    public String getNextPageToken() {
        return next_page_token;
    }

    public List<GooglePlacesResults> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }

    @SerializedName("status")
    private String status;
}

package edu.gvsu.cis.activityapp.util;

/**
 * Created by Kyle Flynn on 11/20/2017.
 */

public class HttpRequest {

    private String completeUrl;

    public HttpRequest(RequestBuilder request) {
        this.completeUrl = this.buildUrl(request);
    }

    private String buildUrl(RequestBuilder request) {
        // https://maps.googleapis.com/maps/api/place/nearbysearch/
        String output = request.getBaseUrl();
        output += request.getQuery() + "?";
        for (String param : request.getParams().keySet()) {
            output += param + "=" + request.getParams().get(param) + "&";
        }
        if (!request.getParams().isEmpty()) {
            output = output.substring(0, output.length() - 1);
        }
        return output;
    }

    public String toString() {
        return this.completeUrl;
    }

}

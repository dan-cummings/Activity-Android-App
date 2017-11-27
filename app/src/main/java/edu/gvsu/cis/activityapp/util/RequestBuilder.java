package edu.gvsu.cis.activityapp.util;

import java.util.HashMap;

/**
 * Created by Kyle Flynn on 11/20/2017.
 */

public class RequestBuilder {

    private final String baseUrl;
    private String query;
    private HashMap<String, String> params;

    public RequestBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
        this.query = "";
        this.params = new HashMap<>();
    }

    public RequestBuilder addParam(String param, String value) {
        this.params.put(param, value);
        return this;
    }

    public RequestBuilder setQuery(String query) {
        this.query = query;
        return this;
    }

    public HashMap<String, String> getParams() {
        return this.params;
    }

    public String getQuery() {
        return this.query;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public HttpRequest build() {
        return new HttpRequest(this);
    }

}

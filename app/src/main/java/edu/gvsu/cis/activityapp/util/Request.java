package edu.gvsu.cis.activityapp.util;

/**
 * Created by daniel on 12/7/17.
 */

public class Request {

    String from;
    String to;
    String group;
    String _key;

    public Request() {}

    public Request(String f, String t, String g) {
        this.from = f;
        this.to = t;
        this.group = g;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

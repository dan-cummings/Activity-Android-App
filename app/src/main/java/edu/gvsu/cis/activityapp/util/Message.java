package edu.gvsu.cis.activityapp.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Created by daniel on 12/3/17.
 */

public class Message {

    String message;
    String user;
    String time;

    public Message() {

    }

    public Message(String m, String u) {
        this.message = m;
        this.user = u;
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        this.time = fmt.print(DateTime.now());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

package edu.gvsu.cis.activityapp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 11/20/17.
*/
public class Chat {

    String eventName;
    String lastMessage;
    String sender;
    Map<String, Boolean> members = new HashMap<>();
    String _key;

    public Chat() {}

    public Chat(String eventName, String message, String sender) {
        this.eventName = eventName;
        this.lastMessage = message;
        this.sender = sender;
    }
    public Map<String, Boolean> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Boolean> members) {
        this.members = members;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String mEventName) {
        this.eventName = mEventName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String mLastMessage) {
        this.lastMessage = mLastMessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String mSender) {
        this.sender = mSender;
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String k) {
        this._key = k;
    }
}
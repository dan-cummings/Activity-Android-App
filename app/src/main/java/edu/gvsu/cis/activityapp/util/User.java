package edu.gvsu.cis.activityapp.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 12/2/17.
 */

public class User {

    String name;
    Map<String, Boolean> chats = new HashMap<>();
    Map<String, Boolean> groups = new HashMap<>();


    public User() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Boolean> getChats() {
        return chats;
    }

    public void setChats(Map<String, Boolean> chats) {
        this.chats = chats;
    }

    public Map<String, Boolean> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Boolean> groups) {
        this.groups = groups;
    }
}

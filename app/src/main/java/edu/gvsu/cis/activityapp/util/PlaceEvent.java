package edu.gvsu.cis.activityapp.util;

import com.google.android.gms.location.places.Place;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 11/20/17.
 */

@Parcel
public class PlaceEvent {

    String _key;
    String mName;
    String mOwner;
    String mUid;
    String date;
    String time;
    Map<String, Boolean> members = new HashMap<>();
    String placeId;

    public PlaceEvent() { }

    public PlaceEvent (String name, String owner, String uid, Map<String, Boolean> mem,
                       String place, String d, String t) {
        this.mName = name;
        this.mOwner = owner;
        this.mUid = uid;
        this.placeId = place;
        this.members = mem;
        this.date = d;
        this.time = t;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public Map<String, Boolean> getMembers() {
        return this.members;
    }

    public void setMembers(Map<String, Boolean> m) {
        this.members = members;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmOwner() {
        return mOwner;
    }

    public void setmOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setKey(String k) { this._key = k;}

    public String getKey() { return this._key;}
}

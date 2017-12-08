package edu.gvsu.cis.activityapp.util;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daniel on 11/20/17.
 */

@Parcel
public class PlaceEvent {

    String _key;
    String name;
    String owner;
    String mUid;
    String date;
    String time;
    Map<String, Boolean> members = new HashMap<>();
    String placeId;
    double lat;
    double lng;

    public PlaceEvent() { }

    public PlaceEvent (String name, String owner, String uid, Map<String, Boolean> mem,
                       String place, String d, String t) {
        this.name = name;
        this.owner = owner;
        this.mUid = uid;
        this.placeId = place;
        this.members = mem;
        this.date = d;
        this.time = t;

    }

    public double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

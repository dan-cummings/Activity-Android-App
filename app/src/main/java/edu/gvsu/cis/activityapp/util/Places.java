package edu.gvsu.cis.activityapp.util;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 11/20/17.
 */
public class Places {

    public static List<PlaceEvent> EVENTS = new ArrayList<>();

    public static void addEvent(PlaceEvent event) {
        EVENTS.add(event);
    }

    public static class PlaceEvent {

        String mName;
        String mOwner;
        String mUid;
        String placeId;

        public PlaceEvent (String name, String owner, String uid) {
            this.mName = name;
            this.mOwner = owner;
            this.mUid = uid;
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
    }
}

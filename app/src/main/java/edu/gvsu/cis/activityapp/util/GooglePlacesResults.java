package edu.gvsu.cis.activityapp.util;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kyle Flynn on 11/27/2017.
 */

public class GooglePlacesResults {
    public String getFormattedAddress() {
        return formatted_address;
    }

    public String getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getRating() {
        return rating;
    }

    public String getReference() {
        return reference;
    }

    public List<String> getTypes() {
        return types;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("formatted_address")
    private String formatted_address;

    @SerializedName("icon")
    private String icon;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("rating")
    private Double rating;

    @SerializedName("reference")
    private String reference;

    @SerializedName("types")
    private List<String> types;

    public class Geometry {
        @SerializedName("location")
        private PlacesLocation location;

        public PlacesLocation getLocation() {
            return location;
        }
    }

    public class PlacesLocation {

        @SerializedName("lat")
        private double lat;

        @SerializedName("lng")
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}

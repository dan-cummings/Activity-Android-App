package edu.gvsu.cis.activityapp.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.Task;

/**
 * Created by Kyle Flynn on 10/25/2017.
 */

public class MapManager {

    private static MapManager instance;

    private AppCompatActivity mActivity;

    private GoogleMap mGoogleMap;

    private GeoDataClient mGeoClient;
    private PlaceDetectionClient mPlacesClient;

    private boolean mLocationEnabled;

    public void init(AppCompatActivity activity) {
        this.mActivity = activity;
        this.mLocationEnabled = false;
        this.mGeoClient = Places.getGeoDataClient(activity, null);
        this.mPlacesClient = Places.getPlaceDetectionClient(activity, null);
    }

    @SuppressLint("MissingPermission")
    public GoogleMap initMap(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setMyLocationEnabled(isLocEnabled());
        map.getUiSettings().setMyLocationButtonEnabled(isLocEnabled());
        map.getUiSettings().setIndoorLevelPickerEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(true);

        this.mGoogleMap = map;

        return this.mGoogleMap;
    }

    /*** METHOD THAT CREATES A SINGLETON OF THIS CLASS ***/
    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }

    @SuppressLint("MissingPermission")
    public Task<PlaceLikelihoodBufferResponse> getCurrentPlace() {
        return mPlacesClient.getCurrentPlace(null);
    }

    public GoogleMap getMap() {
        return this.mGoogleMap;
    }

    public void setLocEnabled(boolean enabled) {
        this.mLocationEnabled = enabled;
    }

    public boolean isLocEnabled() {
        return this.mLocationEnabled;
    }

    public AppCompatActivity getActivity() {
        return this.mActivity;
    }

}

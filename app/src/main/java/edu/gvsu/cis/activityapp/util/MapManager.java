package edu.gvsu.cis.activityapp.util;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
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
        this.mGoogleMap = map;
        this.mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        this.mGoogleMap.setMyLocationEnabled(isLocEnabled());
        this.mGoogleMap.getUiSettings().setMyLocationButtonEnabled(isLocEnabled());
        this.mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        this.mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        this.mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
        this.mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
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

    public Task<PlaceBufferResponse> getPlaceByID(String id) {
        return this.mGeoClient.getPlaceById(id);
    }

    public Task<PlacePhotoMetadataResponse> getPlacePhoto(String id) {
        return this.mGeoClient.getPlacePhotos(id);
    }

    public Task<PlacePhotoResponse> getBitmapPhoto(PlacePhotoMetadata metadata) {
        return this.mGeoClient.getPhoto(metadata);
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

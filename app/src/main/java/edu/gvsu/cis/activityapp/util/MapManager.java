package edu.gvsu.cis.activityapp.util;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Kyle Flynn on 10/25/2017.
 */

public class MapManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static MapManager instance;

    private AppCompatActivity mActivity;

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGAC;
    private PlaceDetectionApi mPlaceAPI;
    private LocationRequest mLocRequest;
    private Location mCurrentLocation;

    private boolean mLocationEnabled;

    public void init(AppCompatActivity activity) {
        this.mActivity = activity;

        mGAC = new GoogleApiClient.Builder(mActivity)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocRequest = new LocationRequest();

        mPlaceAPI = Places.PlaceDetectionApi;

        this.mLocationEnabled = false;
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

    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }

    public void connect() {
        mGAC.connect();
    }

    public GoogleMap getMap() {
        return this.mGoogleMap;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (isLocEnabled()) {
//            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("GOOGLE APIS CONNECTED SUSPENDED: CODE " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("GOOGLE APIS HAS FAILED TO CONNECT: " + connectionResult.getErrorMessage());
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mCurrentLocation = location;
//        getPlaces(this.mCurrentLocation);
    }

    public void setLocEnabled(boolean enabled) {
        this.mLocationEnabled = enabled;
    }

    public boolean isLocEnabled() {
        return this.mLocationEnabled;
    }

    public Location getLocation() {
        return mCurrentLocation;
    }


}

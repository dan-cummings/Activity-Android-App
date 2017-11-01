package edu.gvsu.cis.activityapp.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import edu.gvsu.cis.activityapp.fragments.MapFragment;

/**
 * Created by Kyle Flynn on 10/25/2017.
 */

public class ActivityMapManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private AppCompatActivity mActivity;
    private Context mContext;

    private GoogleMap mGoogleMap;

    private GoogleApiClient mGAC;
    private PlaceDetectionApi mPlaceAPI;

    private LocationRequest mLocRequest;
    private Location mCurrentLocation;

    private boolean mLocationEnabled;

    public ActivityMapManager(AppCompatActivity activity) {
        this.mActivity = activity;
        this.mContext = activity.getBaseContext();

        mGAC = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocRequest = new LocationRequest();
        mLocRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocRequest.setInterval(2 * 1000);
        mLocRequest.setFastestInterval(2000);

        mPlaceAPI = Places.PlaceDetectionApi;

        this.mLocationEnabled = false;
    }

    public void connect() {
        mGAC.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (isLocationEnabled()) {
            startLocationUpdates();
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
        getPlaces(this.mCurrentLocation);
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
    }

    public void setLocationEnabled(boolean enabled) {
        this.mLocationEnabled = enabled;
    }

    public boolean isLocationEnabled() {
        return this.mLocationEnabled;
    }

    public Location getLocation() {
        return mCurrentLocation;
    }

    public GoogleApiClient getAPIClient() {
        return mGAC;
    }

    public GoogleMap getMap() {
        return mGoogleMap;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGAC, mLocRequest, this);
    }

    /***********************************************************************/

    @SuppressLint("MissingPermission")
    private void getPlaces(Location location) {
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGAC, null);
        result.setResultCallback((likelyPlaces) -> {
            if (likelyPlaces.getCount() > 0) {
                System.out.println("LIKELY PLACES: " + likelyPlaces.get(0).getPlace().getName());
            }
        });
    }

    private void placeMarkers(String[] placeNames, String[] placeAddresses, String[] placeAttributes, LatLng[] placeCoords) {
        for (int i = 0; i < placeNames.length; i++) {
            LatLng markerLatLng = placeCoords[i];
            String markerSnippet = placeAddresses[i];
            if (placeAttributes[i] != null) {
                markerSnippet = markerSnippet + "\n" + placeAttributes[i];
            }
            if (mGoogleMap != null) {
                mGoogleMap.addMarker(new MarkerOptions()
                        .title(placeNames[i])
                        .position(markerLatLng)
                        .snippet(markerSnippet));
            }
        }
    }

}

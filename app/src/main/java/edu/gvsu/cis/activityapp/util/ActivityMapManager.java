package edu.gvsu.cis.activityapp.util;

import android.Manifest;
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
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import edu.gvsu.cis.activityapp.fragments.MapFragment;

/**
 * Created by Kyle Flynn on 10/25/2017.
 */

public class ActivityMapManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private AppCompatActivity mActivity;
    private Context mContext;

    private GoogleApiClient mGAC;
    private LocationRequest mLocRequest;
    private Location mCurrentLocation;

    private boolean mLocationEnabled;

    public ActivityMapManager(AppCompatActivity activity) {
        this.mActivity = activity;
        this.mContext = activity.getBaseContext();

        mGAC = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocRequest = new LocationRequest();
        mLocRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocRequest.setInterval(2 * 1000);
        mLocRequest.setFastestInterval(2000);

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

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGAC, mLocRequest, this);
    }

}

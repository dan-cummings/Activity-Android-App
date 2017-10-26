package edu.gvsu.cis.activityapp.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Kyle Flynn on 10/25/2017.
 */

public class ActivityMapManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGAC;
    private LocationRequest mLocRequest;
    private FusedLocationProviderClient mFusedLocationProvider;

    private Location mCurrentLocation;

    private Context mContext;

    public ActivityMapManager(Context context) {
        this.mContext = context;

        mGAC = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocRequest = new LocationRequest();
        mLocRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocRequest.setInterval(2 * 1000);
        mLocRequest.setFastestInterval(2000);

        mFusedLocationProvider = LocationServices.getFusedLocationProviderClient(context);
    }

    public void connect() {
        mGAC.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("GOOGLE APIS HAS CONNECTED.");
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

    public Location getLocation() {
        return mCurrentLocation;
    }

}

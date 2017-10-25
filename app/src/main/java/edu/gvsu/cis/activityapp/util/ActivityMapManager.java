package edu.gvsu.cis.activityapp.util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Kyle Flynn on 10/25/2017.
 */

public class ActivityMapManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGAC;

    public ActivityMapManager(Context context) {
        mGAC = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
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
}

package edu.gvsu.cis.activityapp.fragments;

import android.location.Location;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Timer;
import java.util.TimerTask;

import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.activities.MainActivity;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    /*
    *
    * Okay.. So Google Places API comes with 3 components... PlacePicker, GeoDataAPI, and PlaceDetectionAPI...
    * We do not want the PlacePicker, since it provides immutable code, meaning we cannot add our own places
    * to this UI. Supposedly, we can use the GeoDataAPI to get places around the user, and the PlaceDetectionAPI
    * to retrieve information on the user's current location. GeoDataAPI seems like it will be our friend.
    * Google Places API Web Service Key: AIzaSyAvHvPQ4a4OtjyEC0IJnqavqWxfKoA2kpU
    */

    private GoogleMap mGoogleMap;
    private Location mLastKnownLocation;

    private MapView mMapView;
    private View mView;

    private MainActivity mActivity;

    private Timer mTimer;

    public MapFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Here it's safe to access the activity object.
        if (getActivity() instanceof MainActivity) {
            // This is the main activity instance and we can access the MapManager.
            mActivity = (MainActivity) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.map);

        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        boolean enabled = mActivity.getMapManager().isLocationEnabled();

        try {
            mGoogleMap.setMyLocationEnabled(enabled);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(enabled);
            mActivity.getMapManager().setGoogleMap(mGoogleMap);
            startUpdateThread();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    private void startUpdateThread() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // We can't update the map UI in this thread...
                // In order to do this, we have to communicate with the main UI thread.
                // https://developer.android.com/training/multiple-threads/communicate-ui.html
                updateMap();
            }
        }, 0, 5000);
    }

    private void updateMap() {
        mLastKnownLocation = mActivity.getMapManager().getLocation();
        if (mLastKnownLocation != null) {
            // doStuff()
            System.out.println("Location updated!");
        } else {
            System.out.println("Location is null...");
        }
    }

}
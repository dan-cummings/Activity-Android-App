package edu.gvsu.cis.activityapp.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;

import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.util.MapManager;

import static android.app.Activity.RESULT_OK;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    /*
    * Okay.. So Google Places API comes with 3 components... PlacePicker, GeoDataAPI, and PlaceDetectionAPI...
    * We do not want the PlacePicker, since it provides immutable code, meaning we cannot add our own places
    * to this UI. Supposedly, we can use the GeoDataAPI to get places around the user, and the PlaceDetectionAPI
    * to retrieve information on the user's current location. GeoDataAPI seems like it will be our friend.
    * Google Places API Web Service Key: AIzaSyAvHvPQ4a4OtjyEC0IJnqavqWxfKoA2kpU
    */

    private MapManager mMapManager;

    private Location mLastKnownLocation;

    private MapView mMapView;
    private View mView;
    private Button mBtnChange;

    private Timer mTimer;

    public MapFragment() {}

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMapManager = MapManager.getInstance();
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
        mBtnChange = (Button) view.findViewById(R.id.btn_change_loc);

        mBtnChange.setOnClickListener((touch) -> openPlacePicker());

        // This view CONTAINS the map, and is NOT the map.
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());

        GoogleMap mMap = mMapManager.initMap(googleMap);

        try {
            mMap.setOnMapClickListener(this::handleTouch);
            mMap.setOnMarkerClickListener(this::handleMarkerTouch);
//            startUpdateThread();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    private void handleTouch(LatLng touch) {
//        mMapManager.getMap().addMarker(new MarkerOptions().position(touch).title("Activity Location"));
//        mMapManager.getCurrentPlace().addOnCompleteListener((result) -> {
//            if (result.isSuccessful()) {
//                PlaceLikelihoodBufferResponse response = result.getResult();
//                for (PlaceLikelihood placeLikelihood : response) {
//                    Log.i("MAP MANAGER", String.format("Place '%s' has likelihood: %g",
//                            placeLikelihood.getPlace().getName(),
//                            placeLikelihood.getLikelihood()));
//                }
//                response.release();
//            } else {
//                Toast.makeText(getContext(), result.getException().getMessage(), Toast.LENGTH_LONG).show();
//                System.out.println(result.getException().getMessage());
//            }
//        });
    }

    private void placeMarker(LatLng location, String title, float color) {
        mMapManager.getMap().addMarker(new MarkerOptions().position(location).title(title).icon(BitmapDescriptorFactory.defaultMarker(color)));
    }

    private void moveCamera(LatLng location) {
        mMapManager.getMap().moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    public void openPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(mMapManager.getActivity()), 1);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                mMapManager.getMap().clear();
                Place place = PlacePicker.getPlace(getContext(), data);
                placeMarker(place.getLatLng(), place.getName().toString(), BitmapDescriptorFactory.HUE_AZURE);
                moveCamera(place.getLatLng());
            }
        }
    }

    private boolean handleMarkerTouch(Marker marker) {
        /*
         * true if the listener has consumed the event (i.e., the default behavior should not occur);
         * false otherwise (i.e., the default behavior should occur).
         * The default behavior is for the camera to move to the marker and an info window to appear.
         */
        return false;
    }

//    private void startUpdateThread() {
//        mTimer = new Timer();
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                // We can't update the map UI in this thread...
//                // In order to do this, we have to communicate with the main UI thread.
//                // https://developer.android.com/training/multiple-threads/communicate-ui.html
////                updateMap();
//            }
//        }, 0, 5000);
//    }
//
//    private void updateMap() {
//        mLastKnownLocation = mMapManager.getLocation();
//        if (mLastKnownLocation != null) {
//            // doStuff()
//            System.out.println("Location updated!");
//        } else {
//            System.out.println("Location is null...");
//        }
//    }

}
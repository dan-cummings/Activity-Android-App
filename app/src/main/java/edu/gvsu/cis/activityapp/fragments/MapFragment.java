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
import com.google.android.gms.location.places.Places;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;

import cz.msebera.android.httpclient.Header;
import edu.gvsu.cis.activityapp.R;
import edu.gvsu.cis.activityapp.services.GooglePlacesProvider;
import edu.gvsu.cis.activityapp.util.GoogleJSONResponse;
import edu.gvsu.cis.activityapp.util.GooglePlacesResults;
import edu.gvsu.cis.activityapp.util.HttpRequest;
import edu.gvsu.cis.activityapp.util.MapManager;
import edu.gvsu.cis.activityapp.util.RequestBuilder;

import static android.app.Activity.RESULT_OK;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    /*
    * Okay.. So Google PlaceEvent API comes with 3 components... PlacePicker, GeoDataAPI, and PlaceDetectionAPI...
    * We do not want the PlacePicker, since it provides immutable code, meaning we cannot add our own places
    * to this UI. Supposedly, we can use the GeoDataAPI to get places around the user, and the PlaceDetectionAPI
    * to retrieve information on the user's current location. GeoDataAPI seems like it will be our friend.
    * Google PlaceEvent API Web Service Key: AIzaSyAvHvPQ4a4OtjyEC0IJnqavqWxfKoA2kpU
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
        mMapManager.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14.0f));
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
                HttpRequest nearbyPlaces = new HttpRequest(
                        new RequestBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                                .setQuery("json")
                                .addParam("location", place.getLatLng().latitude + "," + place.getLatLng().longitude)
                                .addParam("radius", "1609")
//                                .addParam("type", "point_of_interest")
                                .addParam("key", "AIzaSyAvHvPQ4a4OtjyEC0IJnqavqWxfKoA2kpU"));
                GooglePlacesProvider.get(nearbyPlaces.toString(), null, getNearbyPlaces());
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

    public JsonHttpResponseHandler getNearbyPlaces() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                GoogleJSONResponse mapper = gson.fromJson(response.toString(), GoogleJSONResponse.class);

                List<GooglePlacesResults> results = mapper.getResults();
                if(results != null){
                    for(GooglePlacesResults r : results){
                        double placeLat = r.getGeometry().getLocation().getLat();
                        double placeLng = r.getGeometry().getLocation().getLng();
                        LatLng placeLoc = new LatLng(placeLat, placeLng);

                        placeMarker(placeLoc, r.getName(), BitmapDescriptorFactory.HUE_ORANGE);
                    }
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray places) {
                System.out.println("ARRAY OBJECT RESPONSE");
                System.out.println(places.toString());
            }
        };
    }

}
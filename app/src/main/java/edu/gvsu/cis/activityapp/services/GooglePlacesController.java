package edu.gvsu.cis.activityapp.services;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kyle Flynn on 11/3/2017.
 */

public class GooglePlacesController implements Callback<List<Place>> {

    private final String BASE_URL = "https://ww.google.com/";

    private List<Place> places;

    public void init() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GooglePlaces placesAPI = retrofit.create(GooglePlaces.class);

        Call<List<Place>> places = placesAPI.listRepos("");
        places.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
        if (response.isSuccessful()) {
            places = response.body();
            for (Place p : places) {
                System.out.println(p.getName());
            }
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<List<Place>> call, Throwable t) {
        t.printStackTrace();
    }

}

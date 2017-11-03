package edu.gvsu.cis.activityapp.services;

import com.google.android.gms.location.places.Place;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Kyle Flynn on 11/3/2017.
 */

public interface GooglePlaces {
    @GET("users/{user}/repos")
    Call<List<Place>> listRepos(@Path("user") String user);
}

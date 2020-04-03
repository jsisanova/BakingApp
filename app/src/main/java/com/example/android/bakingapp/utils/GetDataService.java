package com.example.android.bakingapp.utils;

import com.example.android.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

// Define the Endpoints inside of an interface using special retrofit annotations to encode details about the parameters and request method.
// Retrofit turns your HTTP API into a Java interface.
public interface GetDataService {

    // Every method must have an HTTP annotation that provides the request method and relative URL.
    // You can also specify query parameters in the URL.     @GET("users/list?sort=desc")
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getAllRecipes();
//    For requests from UI thread, we need to do it asynchonously - retrofit hides this from us, but we need to wrap it into Call object



//    eg. here the @GET("movie/{id}") is the id of path
//    @GET("movie/{id}")
//    Call<MoviesResponse> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
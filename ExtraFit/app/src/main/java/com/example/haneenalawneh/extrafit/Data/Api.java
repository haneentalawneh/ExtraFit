package com.example.haneenalawneh.extrafit.Data;

/**
 * Created by haneenalawneh on 11/12/17.
 */

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface Api {
    String BASE_URL = "https://gist.githubusercontent.com/haneentalawneh/";

    @GET("64f0a05303d7a627169acb856ea883e2/raw/835263a6163b58f041b51bfdaf51f2b347b84248/gistfile1.txt")
    Call<List<Exercise>> getExercises();

    @GET("dd1fc8495f5c91f9926a14381d33572e/raw/fc0ec7950ee0870e1d8aaaa7922c4cf35695c8e1/healthTips")
    Call<List<Tip>> getTips();

}

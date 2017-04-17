package com.eccentricyan.mypin.infra.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.Nullable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shiyanhui on 2017/04/17.
 */

public interface RestfulApi {
    @GET("{category}/list")
    Observable<JsonObject> articles(@Path("category")String category,
                                    @Nullable @Query("offset") int offset);
    @GET("top/params")
    Single<JsonObject> categories();
}


package com.eccentricyan.mypin.infra.api;

import com.google.gson.JsonObject;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.Nullable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by shiyanhui on 2017/04/17.
 */

public interface RestfulApi {
    @GET("{category}/list")
    Observable<JsonObject> articles(@Path("category")String category,
                                    @Nullable @Query("offset") int offset);
    @GET("top/params")
    Single<JsonObject> categories();

    @GET("{model}/{model_id}/{targets}/")
    Observable<JsonObject> searchTargets(@Path("model")String model,
                                         @Path("model_id")String model_id,
                                         @Path("targets")String targets,
                                         @Nullable @QueryMap(encoded=true) Map<String, String> options);
    @GET("me/{targets}/")
    Observable<JsonObject> searchMyTargets(@Path("targets")String targets,
                                         @Nullable @QueryMap(encoded=true) Map<String, String> options);
}


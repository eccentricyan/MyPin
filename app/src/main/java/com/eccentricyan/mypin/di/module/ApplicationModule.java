package com.eccentricyan.mypin.di.module;

import android.util.Log;

import com.eccentricyan.mypin.Application;
import com.eccentricyan.mypin.common.utils.NetUtils;
import com.eccentricyan.mypin.di.scope.ApplicationScope;
import com.eccentricyan.mypin.infra.api.RestfulApi;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.eccentricyan.mypin.common.defines.Defines.CACHE;
import static com.eccentricyan.mypin.common.defines.Defines.SITE_URL;

@Module
public class ApplicationModule {

    public static final String ENDPOINT = SITE_URL;

    @Provides
    @ApplicationScope
    public Realm realm() { return Realm.getDefaultInstance();}

    @Provides
    @ApplicationScope
    public RestfulApi restfulApi() {
        return restApi(okHttpclient());
    }

    public RestfulApi restApi(OkHttpClient okHttpClient) {
        synchronized (RestfulApi.class) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(RestfulApi.class);
        }
    }

    protected OkHttpClient okHttpclient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request.Builder request = chain.request().newBuilder();
            request.removeHeader("Pragma");
            if (isOnline()) {
                request.addHeader("cache-control", "public, max-age=" + 60);
            } else {
                Log.e("offline", "offline");
                request.header("cache-control", "public, only-if-cached, max-stale=" + 24 * 60 * 60);
            }
            return chain.proceed(request.build());
        });
        httpClient.writeTimeout(15 * 60 * 1000, TimeUnit.MILLISECONDS);
        httpClient.readTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        httpClient.connectTimeout(20 * 1000, TimeUnit.MILLISECONDS);

        return httpClient.cache(CACHE).build();
    }

    @Provides
    @ApplicationScope
    public EventBus eventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @ApplicationScope
    public Scheduler subscribeScheduler() {
        return Schedulers.io();
    }


    @Provides
    @ApplicationScope
    public Gson gson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    public Boolean isOnline() {
        return NetUtils.checkNet(Application.getInstance());
    }

}

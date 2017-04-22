package com.eccentricyan.mypin.presentation.card;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.util.Log;
import android.view.View;

import com.eccentricyan.mypin.common.command.ReplyCommand;
import com.eccentricyan.mypin.di.component.ActivityComponent;
import com.eccentricyan.mypin.domain.entities.Page;
import com.eccentricyan.mypin.presentation.base.BaseViewModel;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pinterest.android.pdk.PDKPin;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import icepick.State;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

import static com.eccentricyan.mypin.common.defines.Defines.PIN_FIELDS;

/**
 * Created by shiyanhui on 2017/04/19.
 */

public class CardListViewModel extends BaseViewModel{
    @State
    public String cursor;
    @State
    public ObservableInt progressVisibility;
    @State
    public ObservableInt recyclerViewVisibility;
    @State
    public ObservableInt infoTextVisibility;

    Map<String, String> data = new HashMap<>();

    public ObservableBoolean isRefreshing = new ObservableBoolean(false);

    public CardListViewModel(ActivityComponent component) {
        super(component);
        progressVisibility = new ObservableInt(View.INVISIBLE);
        recyclerViewVisibility = new ObservableInt(View.INVISIBLE);
        infoTextVisibility = new ObservableInt(View.VISIBLE);
        data.put("fields", PIN_FIELDS);
        data.put("access_token", token);
        loadCards(data, false);
    }

    private void loadCards(Map<String, String> data, Boolean refresh) {
        isRefreshing.set(true);
        progressVisibility.set(View.VISIBLE);
        recyclerViewVisibility.set(View.INVISIBLE);
        infoTextVisibility.set(View.INVISIBLE);
        Log.e("data", data.toString());
        Observable<JsonObject> observable = api.searchMyTargets("pins", data);
        compositeDisposable.add(
                observable
                        .map(jsonObject -> {
                            Log.e("jsonObject", jsonObject.toString());
                            cursor = gson.fromJson(jsonObject.get("page"), Page.class).cursor;
                            return (List<PDKPin>)gson.fromJson(jsonObject.get("data"), new TypeToken<List<PDKPin>>(){}.getType());
                        })
                        .compose(lifecycleProvider.bindToLifecycle())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate(() -> progressVisibility.set(View.INVISIBLE))
                        .subscribeOn(subscribeScheduler)
                        .subscribe(models -> {
                            Log.e("models", gson.toJson(models));
                                    eventBus.post(new CardsChangeEvent(models, refresh));
                                    if (!models.isEmpty()) {
                                        recyclerViewVisibility.set(View.VISIBLE);
                                    } else {

                                    }
                                },
                                error -> {
                                    Log.e("onError: ",  error.getMessage());
                                    isRefreshing.set(false);
                                },
                                () -> {
                                    isRefreshing.set(false);
                                }));
    }

    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand(new Action(){
        @Override
        public void run() {
            if (isRefreshing.get()) return;
            data.put("cursor", cursor);
            loadCards(data, false);
        }
    });

    public final ReplyCommand<Integer> onRefreshCommand = new ReplyCommand(new Action(){
        @Override
        public void run() {
            if (isRefreshing.get()) return;
            data.remove("cursor");
            loadCards(data, true);
        }
    });


}
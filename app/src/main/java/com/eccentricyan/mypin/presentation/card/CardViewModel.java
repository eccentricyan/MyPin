package com.eccentricyan.mypin.presentation.card;

import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.ObservableInt;
import android.view.View;

import com.eccentricyan.mypin.common.command.ReplyCommand;
import com.eccentricyan.mypin.di.component.ActivityComponent;
import com.eccentricyan.mypin.domain.entities.Card;
import com.eccentricyan.mypin.presentation.base.BaseViewModel;
import com.pinterest.android.pdk.PDKPin;

import icepick.State;
import io.reactivex.Observable;

/**
 * Created by shiyanhui on 2017/04/19.
 */

public class CardViewModel extends BaseViewModel {
    private PDKPin pin;
    @State
    public ObservableInt progressVisibility;
    @State
    public ObservableInt recyclerViewVisibility;
    @State
    public ObservableInt infoTextVisibility;

    public CardViewModel(ActivityComponent component, PDKPin pin) {
        super(component);
        progressVisibility = new ObservableInt(View.INVISIBLE);
        recyclerViewVisibility = new ObservableInt(View.INVISIBLE);
        infoTextVisibility = new ObservableInt(View.VISIBLE);
        this.pin = pin;
    }

    @Bindable
    public String getUsername() {
        return pin.creator.getFirstName();
    }
    @Bindable
    public String getLink() {
        return pin.getLink();
    }

    @Bindable
    public String getImage() {
        return pin.image.original.url;
    }

    @Bindable
    public int getWidth() {
        return pin.image.original.width;
    }

    @Bindable
    public int getHeight() {
        return pin.image.original.height;
    }

    public void setCard(PDKPin pin) {
        this.pin = pin;
        super.notifyChange();
    }

    //command
    public ReplyCommand itemClickCommand = new ReplyCommand(() -> {
//        Intent intent = new Intent(context, CardDetailActivity.class);
//        intent.putExtra(CardDetailActivity.EXTRA_CARD, Parcels.wrap(card));
//        context.startActivity(intent);
    });

//    public Observable<Card> refreshCard() {
//        Map<String, String> data = new HashMap<>();
//        return api.find("cards", card.id, data)
//                .map(jsonObject -> gson.fromJson(jsonObject.get("card"), Card.class))
//                .compose(lifecycleProvider.bindToLifecycle())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doAfterTerminate(() -> progressVisibility.set(View.INVISIBLE))
//                .subscribeOn(subscribeScheduler);
//    }

}

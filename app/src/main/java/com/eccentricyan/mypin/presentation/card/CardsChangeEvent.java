package com.eccentricyan.mypin.presentation.card;

import com.eccentricyan.mypin.domain.entities.Card;
import com.pinterest.android.pdk.PDKPin;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shiyanhui on 2017/04/19.
 */

public class CardsChangeEvent {
    public final List<PDKPin> pins;
    public final boolean refresh;
    public CardsChangeEvent(List<PDKPin> pins, boolean refresh) {
        this.pins = pins;
        this.refresh = refresh;
    }
}
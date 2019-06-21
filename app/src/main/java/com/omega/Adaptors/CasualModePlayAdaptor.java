package com.omega.Adaptors;

import android.content.Context;

import java.util.Collections;

public class CasualModePlayAdaptor extends SimpleFlashCardViewerAdapter {

    public CasualModePlayAdaptor(Context context) {
        super(context);
    }

    public void shuffle() {
        Collections.shuffle(dataSet);
        notifyDataSetChanged();
    }
}

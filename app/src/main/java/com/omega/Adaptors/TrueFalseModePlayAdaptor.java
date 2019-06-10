package com.omega.Adaptors;

import android.content.Context;

import com.omega.Database.FlashCards;


public class TrueFalseModePlayAdaptor extends SimpleFlashCardViewerAdapter {

    public TrueFalseModePlayAdaptor(Context c) {
        super(c);
    }

    public FlashCards removeItemAtPos(int pos) {
        FlashCards cardToReturn = dataSet.get(pos);
        dataSet.remove(pos);
        notifyItemRemoved(pos);
        return cardToReturn;
    }

    public boolean isDataSetEmpty() {
        return dataSet.isEmpty();
    }

}

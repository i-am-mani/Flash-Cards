package com.omega.Adaptors;

import android.content.Context;

import androidx.annotation.NonNull;

public class TitleReverseMatchPlayAdaptor extends SimpleFlashCardViewerAdapter {

    public TitleReverseMatchPlayAdaptor(Context c) {
        super(c);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayModeViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.detachListener();
    }
}

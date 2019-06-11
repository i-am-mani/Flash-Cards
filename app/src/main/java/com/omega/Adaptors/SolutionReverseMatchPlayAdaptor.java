package com.omega.Adaptors;

import android.content.Context;

import androidx.annotation.NonNull;

public class SolutionReverseMatchPlayAdaptor extends SimpleFlashCardViewerAdapter {


    public SolutionReverseMatchPlayAdaptor(Context c) {
        super(c);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayModeViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.detachListener();
    }
}

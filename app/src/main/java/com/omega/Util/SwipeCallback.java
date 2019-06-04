package com.omega.Util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeCallback extends ItemTouchHelper.SimpleCallback {


    ColorDrawable backgroundDelete = new ColorDrawable(Color.RED);
    ColorDrawable backgroundEdit = new ColorDrawable(Color.GREEN);

    OnSwiped onSwiped;

    public SwipeCallback(OnSwiped ImpOnSwiped) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        onSwiped = ImpOnSwiped;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.RIGHT) {
            onSwiped.deleteItem(viewHolder.getAdapterPosition());
        } else if (direction == ItemTouchHelper.LEFT) {
            onSwiped.editItem(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        backgroundDelete.setAlpha(50);
        backgroundEdit.setAlpha(50);
        if (dX > 0) { // Swiping to the right
            backgroundDelete.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX),
                    itemView.getBottom());

            backgroundDelete.draw(c);

        } else if (dX < 0) { // Swiping to the left
            backgroundEdit.setBounds(itemView.getRight() + ((int) dX),
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
            backgroundEdit.draw(c);

        } else { // view is unSwiped
            backgroundEdit.setBounds(0, 0, 0, 0);
            backgroundDelete.setBounds(0, 0, 0, 0);
            backgroundEdit.draw(c);
            backgroundDelete.draw(c);
        }
    }

    public interface OnSwiped {
        void deleteItem(int adapterPosition);

        void editItem(int adapterPosition);
    }


}
package com.omega.Adaptors;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omega.Database.Groups;
import com.omega.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupsAdaptor extends RecyclerView.Adapter<GroupsAdaptor.GroupsViewHolder> {

    private Context mContext;
    LayoutInflater layoutInflater;
    List<Groups> filterList;
    List<Groups> groupsList;
    String TAG = GroupsAdaptor.class.getSimpleName();
    GroupsAdaptorListenerInterface itemListener;
    private int mPosition;

    public GroupsAdaptor(Context context,GroupsAdaptorListenerInterface adaptorListenerInterface){
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        itemListener = adaptorListenerInterface;
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mainView = layoutInflater.inflate(R.layout.item_checkout_groups, parent, false);
        GroupsViewHolder groupsViewHolder = new GroupsViewHolder(mainView);
        return groupsViewHolder;
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder holder, int position) {
        if (filterList != null) {
            holder.onBind(holder, position);

        }
        else{
            holder.tvGroupDescription.setText("No Group Found!");
        }
    }


    @Override
    public int getItemCount() {
        if (filterList != null) {
            return filterList.size();
        }
        else{
            return 0;
        }
    }

    public void setDataSet(List<Groups> dataSet) {
        groupsList = dataSet;
        filterList = new ArrayList<>(dataSet); //i.e default no search keyword
        notifyDataSetChanged();
    }

    public Groups getItemAtPosition(int pos) {
        return filterList.get(pos);
    }

    public void refresh(int pos) {
        notifyItemChanged(pos);
    }

    public void removeItemFromDataSet(int pos) {
        groupsList.remove(pos);
        filterList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void filter(String query) {
        if (groupsList != null) {
            if (filterList == null) {
                filterList = new ArrayList<>();
            }
            filterList.clear(); // clear existing search list
            for (Groups groups : groupsList) { // add matching items with query
                Log.d(TAG, "filter: group name " + groups.getGroupName());
                if (groups.getGroupName().contains(query)) {
                    filterList.add(groups);
                }
            }
            Log.d(TAG, "filter: " + filterList + "group list " + groupsList);
            notifyDataSetChanged();
        }
    }

    public interface GroupsAdaptorListenerInterface {
        void onItemClick(View view, String groupName);

        void onPlayButtonClicked(View view, String groupName);

        void getNumberOfFlashCards(String groupName, TextView textView);

        void deleteGroup(int pos);

        void editGroup(int pos);

        void exportFlashCardsOfGroup(int pos);
    }

    public class GroupsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.text_checkout_group_name)
        TextView tvGroupName;
        @BindView(R.id.text_checkout_group_description)
        TextView tvGroupDescription;
        @BindView(R.id.text_number_of_flashcards)
        TextView tvNumberOfFlashCards;
        @BindView(R.id.button_options)
        ImageButton btnOptions;

        public GroupsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        private void onBind(GroupsViewHolder holder, int position) {
            String name = filterList.get(position).getGroupName();
            String description = filterList.get(position).getGroupDescription();

            holder.tvGroupName.setText(name);
            holder.tvGroupDescription.setText(description);
            itemListener.getNumberOfFlashCards(name, holder.tvNumberOfFlashCards);
            mPosition = position;
        }


        @Override
        public void onClick(View itemView) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            int nItems = Integer.valueOf(tvNumberOfFlashCards.getText().toString());

            if (nItems > 0) {
                onNonEmptyFlashCards(itemView);
            } else {

                onEmptyFlashCards(itemView);
            }
        }

        private void onNonEmptyFlashCards(View itemView) {
            itemView.animate().translationXBy(1500).setDuration(100).withEndAction(() -> {
                itemListener.onItemClick(itemView, tvGroupName.getText().toString());
            });
        }

        private void onEmptyFlashCards(View itemView) {
            ObjectAnimator rotateUp = ObjectAnimator.ofFloat(itemView, "rotation", 20);
            ObjectAnimator rotateDown = ObjectAnimator.ofFloat(itemView, "rotation", -20);
            ObjectAnimator rotateCenter = ObjectAnimator.ofFloat(itemView, "rotation", 0);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(rotateUp).before(rotateDown);
            animatorSet.play(rotateDown).before(rotateCenter);
            animatorSet.setDuration(200);
            animatorSet.start();

            Toast.makeText(mContext, "No FlashCards Found", Toast.LENGTH_SHORT).show();

        }

        @OnClick(R.id.image_button_edit_flashcards)
        public void goToPlayMode(View v) {
            v.animate().translationXBy(1500).setDuration(200).withEndAction(() -> {
                itemListener.onPlayButtonClicked(v, tvGroupName.getText().toString());
            });
        }

        @OnClick(R.id.button_options)
        public void showPopMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(mContext, btnOptions);
            popupMenu.inflate(R.menu.menu_checkout_fragment);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.menu_item_delete:
                            itemListener.deleteGroup(getAdapterPosition());
                            return true;
                        case R.id.menu_item_export:
                            itemListener.exportFlashCardsOfGroup(getAdapterPosition());
                            return true;
                        case R.id.menu_item_edit_group:
                            itemListener.editGroup(getAdapterPosition());
                            return true;
                    }

                    return false;
                }
            });
        }
    }

}
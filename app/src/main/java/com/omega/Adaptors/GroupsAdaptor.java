package com.omega.Adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    LayoutInflater layoutInflater;
    List<Groups> filterList;
    List<Groups> groupsList;
    String TAG = GroupsAdaptor.class.getSimpleName();
    GroupsAdaptorListenerInterface itemListener;

    public GroupsAdaptor(Context context,GroupsAdaptorListenerInterface adaptorListenerInterface){
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
            String name = filterList.get(position).getGroupName();
            String description = filterList.get(position).getGroupDescription();
            holder.tvGroupName.setText(name);
            holder.tvGroupDescription.setText(description);
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
    }

    public class GroupsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.text_checkout_group_name)
        TextView tvGroupName;
        @BindView(R.id.text_checkout_group_description)
        TextView tvGroupDescription;

        public GroupsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + getAdapterPosition());

            v.animate().translationXBy(1500).setDuration(100).withEndAction(() -> {
                itemListener.onItemClick(v, tvGroupName.getText().toString());
            });
        }

        @OnClick(R.id.image_button_start_play_mode)
        public void goToPlayMode(View v) {
            v.animate().translationXBy(1500).setDuration(200).withEndAction(() -> {
                itemListener.onPlayButtonClicked(v, tvGroupName.getText().toString());
            });
        }
    }

}

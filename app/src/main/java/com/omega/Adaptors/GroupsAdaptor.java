package com.omega.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omega.Database.Groups;
import com.omega.R;

import java.util.List;

public class GroupsAdaptor extends RecyclerView.Adapter<GroupsAdaptor.GroupsViewHolder> {

    LayoutInflater layoutInflater;
    List<Groups> groupsList;

    public GroupsAdaptor(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mainView = layoutInflater.inflate(R.layout.item_checkout_groups, parent, false);
        GroupsViewHolder groupsViewHolder = new GroupsViewHolder(mainView);
        return groupsViewHolder;
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder holder, int position) {
        if (groupsList != null) {
            holder.tvGroupName.setText(groupsList.get(position).getGroupName());
            holder.tvGroupDescription.setText(groupsList.get(position).getGroupDescription());
        }
        else{
            holder.tvGroupDescription.setText("No Group Found!");
        }
    }

    @Override
    public int getItemCount() {
        if (groupsList != null) {
            return groupsList.size();
        }
        else{
            return 0;
        }
    }

    public class GroupsViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupName;
        TextView tvGroupDescription;
        public GroupsViewHolder(View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.text_checkout_group_name);
            tvGroupDescription = itemView.findViewById(R.id.text_checkout_group_description);
        }
    }

    public void setDataSet(List<Groups> dataSet) {
        groupsList = dataSet;
        notifyDataSetChanged();
    }
}

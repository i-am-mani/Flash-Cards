package com.omega.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.omega.Adaptors.GroupsAdaptor;
import com.omega.Database.Groups;
import com.omega.R;
import com.omega.Util.EqualSpaceItemDecoration;
import com.omega.Util.FlashCardViewModel;
import com.omega.Util.ISwitchToFragment;
import com.omega.Util.SwipeCallback;

public class CheckoutFlashCardFragment extends Fragment {

    FlashCardViewModel flashCardViewModel;
    GroupsAdaptor groupsAdaptor;
    ISwitchToFragment switchToFragment;

    String newGroup;
    String newDesc;

    String TAG = CheckoutFlashCardFragment.class.getSimpleName();

    public CheckoutFlashCardFragment(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            switchToFragment = (ISwitchToFragment) context;
        } catch (ClassCastException c) {
            c.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        flashCardViewModel = ViewModelProviders.of(this).get(FlashCardViewModel.class);
        flashCardViewModel.getAllGroups().observe(this, groups -> groupsAdaptor.setDataSet(groups));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewGroup = inflater.inflate(R.layout.fragment_check_out_flash_cards, container, false);
        getActivity().setTitle("Checkout Groups");
        initializeVariables(viewGroup);
        return viewGroup;
    }

    private void initializeVariables(View viewGroup) {
        RecyclerView rvGroups = viewGroup.findViewById(R.id.recycler_view_group);
        groupsAdaptor = new GroupsAdaptor(getActivity(), new GroupsItemImpl() );
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvGroups.setLayoutManager(linearLayoutManager);
        rvGroups.setAdapter(groupsAdaptor);
        rvGroups.addItemDecoration(new EqualSpaceItemDecoration(40));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeCallback(new OnSwipeDeleteItem()));
        itemTouchHelper.attachToRecyclerView(rvGroups);
    }


    class GroupsItemImpl implements GroupsAdaptor.GroupsAdaptorListenerInterface {
        @Override
        public void onItemClick(View view,String groupName) {
            switchToFragment.switchToCreateFlashCard(groupName);
        }
    }

    class OnSwipeDeleteItem implements SwipeCallback.OnSwiped {

        @Override
        public void deleteItem(int adapterPosition) {
            Groups group = groupsAdaptor.deleteRowFromDataSet(adapterPosition);
            flashCardViewModel.deleteGroup(group);
            Log.d(TAG, "deleteItem: Row Deleted");
        }

        @Override
        public void editItem(int adapterPosition) {
            Log.d(TAG, "editItem: Left Swiped");
            String groupName = flashCardViewModel.getAllGroups().getValue().get(adapterPosition).getGroupName();

            Dialog dialog = new Dialog(getActivity());
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_edit_group);
            dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Button btnConfirmEdit = dialog.findViewById(R.id.button_confirm_edit);
            btnConfirmEdit.setOnClickListener(v -> {
                newDesc = ((TextInputEditText) dialog.findViewById(R.id.edit_text_new_description)).getText().toString();
                flashCardViewModel.updateGroup(groupName, newDesc);
                dialog.dismiss();
            });
            dialog.setOnCancelListener(dialog1 -> groupsAdaptor.refresh(adapterPosition));
            dialog.show();
        }
    }

}


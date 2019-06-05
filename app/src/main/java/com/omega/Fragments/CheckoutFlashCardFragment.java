package com.omega.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
    private String newName;

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

            Groups group = groupsAdaptor.getItemAtPosition(adapterPosition);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Confirmation Alert");
            alertDialog.setMessage("Are you sure you want to delete item ? ");

            alertDialog.setPositiveButton("Yes", (dialog, which) -> {
                flashCardViewModel.deleteGroup(group);
            });
            alertDialog.setNegativeButton("Undo", (dialog, which) -> {
                groupsAdaptor.refresh(adapterPosition);
            });

            alertDialog.setOnDismissListener(dialog -> groupsAdaptor.refresh(adapterPosition));

            AlertDialog dialog = alertDialog.create();
            dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
            dialog.show();
        }

        @Override
        public void editItem(int adapterPosition) {
            Log.d(TAG, "editItem: Left Swiped");
            Groups group = groupsAdaptor.getItemAtPosition(adapterPosition);

            Dialog dialog = new Dialog(getActivity());
            dialog.setCanceledOnTouchOutside(true);
            dialog.setContentView(R.layout.dialog_edit);

            ((TextView) (dialog.findViewById(R.id.text_dialog_title))).setText("Edit Group");

            TextInputEditText etDesc = dialog.findViewById(R.id.edit_text_new_description);
            TextInputEditText etName = dialog.findViewById(R.id.edit_text_new_group_name);

            etName.setText(group.getGroupName());
            etDesc.setText(group.getGroupDescription());

            dialog.getWindow().setBackgroundDrawableResource(R.color.DarkModePrimaryDarkColor);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Button btnConfirmEdit = dialog.findViewById(R.id.button_confirm_edit);
            btnConfirmEdit.setOnClickListener(v -> {
                newDesc = etDesc.getText().toString();
                newName = etName.getText().toString();

                group.setGroupDescription(newDesc);
                group.setGroupName(newName);

                flashCardViewModel.updateGroup(group);
                dialog.dismiss();
                groupsAdaptor.refresh(adapterPosition);
            });
            dialog.setOnCancelListener(dialog1 -> groupsAdaptor.refresh(adapterPosition));
            dialog.show();
        }
    }

}


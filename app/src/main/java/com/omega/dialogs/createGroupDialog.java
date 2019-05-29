package com.omega.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import com.omega.R;

public class createGroupDialog extends Dialog {
    Activity context;
    Button btnCreateGroup ;

    public createGroupDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_group);
        btnCreateGroup = context.findViewById(R.id.button_create_group);
    }


}

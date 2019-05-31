package com.omega.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.omega.R;

import java.util.function.Consumer;

public class SingleEditTextDialog extends Dialog {
    private Activity context;
    private Button btnConfirm;
    private TextView tvTitle;
    private EditText etReply;

    public SingleEditTextDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_group);

        btnConfirm = this.findViewById(R.id.button_create_group);
        tvTitle = this.findViewById(R.id.text_title);
        etReply = this.findViewById(R.id.edit_text_group_name);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setButtonText(String name){
        btnConfirm.setText(name);
    }

    public void setOnClick(Consumer<View> onClick) {
        tvTitle.setOnClickListener(v -> {
            onClick.accept(v);
        });
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setEditTextHint(String hint){
        etReply.setText(hint);
    }

}

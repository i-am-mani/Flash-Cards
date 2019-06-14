package com.omega.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.omega.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO: document your custom view class.
 */
public class RichEditText extends LinearLayout {

    @BindView(R.id.edit_text_description)
    TextInputEditText editText;

    private String TAG = "RichEditText";


    public RichEditText(Context context) {
        super(context);
        init();
    }


    public RichEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        View mainView = inflate(getContext(), R.layout.rich_edit_text, this);
        ButterKnife.bind(this, mainView);
    }

    @OnClick(R.id.button_bold)
    public void onSetSelectedBold(View view) {
        Editable text = editText.getText();
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        text.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setText(text);
        Log.d("RichText", "onSetSelectedBold: " + Html.toHtml(text));
    }

    @OnClick(R.id.button_italic)
    public void onSetSelectItalic(View view) {
        Editable text = editText.getText();
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        text.setSpan(new StyleSpan(Typeface.ITALIC), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setText(text);
        Log.d("RichText", "onSetSelectedBold: " + Html.toHtml(text));
    }

    @OnClick(R.id.button_superscript)
    public void onSetSelectedSuperScript(View view) {
        Editable text = editText.getText();
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        text.setSpan(new SuperscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setText(text);
        Log.d("RichText", "onSetSelectedBold: " + Html.toHtml(text));
    }

    @OnClick(R.id.button_subscript)
    public void onSetSelectedSubscript(View view) {
        Editable text = editText.getText();
        int start = editText.getSelectionStart();
        int end = editText.getSelectionEnd();
        text.setSpan(new SubscriptSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setText(text);
        Log.d("RichText", "onSetSelectedBold: " + Html.toHtml(text));
    }

    public String getTextAsHTML() {
        Editable text = editText.getText();
        String s = Html.toHtml(text);
        return s;
    }

}

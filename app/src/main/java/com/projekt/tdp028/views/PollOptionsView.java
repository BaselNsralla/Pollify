package com.projekt.tdp028.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.projekt.tdp028.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class PollOptionsView extends LinearLayout implements View.OnClickListener {

    public PollOptionsView(Context context) {
        super(context);
        inflate(context, R.layout.poll_options_view, this);
    }

    public PollOptionsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.poll_options_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Button btn = findViewById(R.id.add_poll_option);
        btn.setOnClickListener(this);
        addDefaultOptions();
        addDefaultOptions();
    }

    private void addDefaultOptions() {
        RelativeLayout editTextContainer = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.edit_text_poll_option, this, false);
        ImageButton imageButton = (ImageButton) editTextContainer.findViewById(R.id.id_close_button);
        imageButton.setVisibility(View.GONE);
        addView(editTextContainer, getChildCount()-1);
    }


    @Override
    public void onClick(View v) {
        int childCount = getChildCount();
        int position   = childCount-1;
        RelativeLayout editTextContainer = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.edit_text_poll_option, this, false);
        ImageButton imageButton = (ImageButton) editTextContainer.findViewById(R.id.id_close_button);
        imageButton.setOnClickListener(new CloseClickListener(position));

        addView(editTextContainer, position);
        editTextContainer.requestFocus();

        InputMethodManager imgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.showSoftInput(editTextContainer, 0);
        imgr.toggleSoftInput(InputMethodManager.RESULT_SHOWN, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void refreshClickListeners() {
        for (int i = 2; i < getChildCount() -1; ++i) {
            RelativeLayout editTextContainer = (RelativeLayout)getChildAt(i);
            ImageButton imageButton = (ImageButton) editTextContainer.findViewById(R.id.id_close_button);
            int position = i;
            imageButton.setOnClickListener(new CloseClickListener(position));
        }
    }

    public List<String> getOptionsTexts() {
        List<String> list = new ArrayList<String>();

        for(int i = 0; i < getChildCount()-1; ++i) {
            RelativeLayout editTextContainer = (RelativeLayout)getChildAt(i);
            EditText editText = (EditText) editTextContainer.findViewById(R.id.id_option_editText);
            String text = editText.getText().toString();
            if(text.length() == 0) { continue; }
            list.add(text);
        }

        return list;
    }

    class CloseClickListener implements View.OnClickListener{
        int position;
        public CloseClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            removeViewAt(position);
            refreshClickListeners();
        }
    }

}

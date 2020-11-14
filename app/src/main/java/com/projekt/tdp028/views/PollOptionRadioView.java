package com.projekt.tdp028.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.projekt.tdp028.R;
import com.projekt.tdp028.models.firebase.PollOption;

import java.util.ArrayList;
import java.util.List;


public class PollOptionRadioView extends RadioGroup {

    LayoutInflater inflater;
    public OnPollOptionRadioViewListener onPollOptionListener;
    private List<String> pollIdList = new ArrayList<String>();

    public PollOptionRadioView(Context context) {
        super(context);
        this.inflater = LayoutInflater.from(getContext());
        setHapticFeedbackEnabled(true);
    }

    public PollOptionRadioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.inflater = LayoutInflater.from(getContext());
    }

    public void activate() {
        setActive(true);
    }

    private void setActive(boolean active) {
        for (int i = 0; i < getChildCount(); ++i) {
            getChildAt(i).setClickable(active);
        }
    }

    public void setPollOptions(List<PollOption> pollOptions) {
        for (PollOption pollOption: pollOptions) {
            RadioButton radioButton = (RadioButton) this.inflater.inflate(R.layout.radio_button_poll_option, null);
            radioButton.setText(pollOption.getText());
            radioButton.setClickable(false);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                    setActive(false);
                    onPollOptionListener.onPollOptionClicked(pollOption.getId());
                }
            });

            pollIdList.add(pollOption.getId());
            addView(radioButton, getChildCount());// TODO: sort first
        }
    }

    public void setPick(String pollOptionId) {
        setActive(false);
        int index = pollIdList.indexOf(pollOptionId);
        ((RadioButton)getChildAt(index)).setChecked(true);
    }

    public interface OnPollOptionRadioViewListener {
        void onPollOptionClicked(String optionId);
    }
}

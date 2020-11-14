package com.projekt.tdp028.FeedList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.projekt.tdp028.R;

public class CardInfoViewGroup extends FrameLayout {

    private String data;
    private TextView tv;
    private Context context;


    public CardInfoViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        View v = inflate(context, R.layout.component_card_info, this);
        tv = (TextView) v.findViewById(R.id.location_text);
        this.context = context;
    }

    public void setData(String data) {
        this.data = data;
         tv.setText(data);
    }



}

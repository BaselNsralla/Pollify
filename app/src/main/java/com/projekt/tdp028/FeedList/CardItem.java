package com.projekt.tdp028.FeedList;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.projekt.tdp028.Constants.DensityCalculator;
import com.projekt.tdp028.R;

public abstract class CardItem extends FrameLayout {

    private TextView title_text_view;
    private Context context;

    public CardItem(Context context) {
        super(context);
        int height = DensityCalculator.inDp(context, 150);
        setElevation(5.0f);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height);
        setLayoutParams(params);
        inflateXMLView(context);
    }

    final private void inflateXMLView(Context context) {
        inflate(context, R.layout.component_card_list_item, this);
        setBackgroundForContainer((ViewGroup)findViewById(R.id.card_list_container));
        title_text_view = (TextView) findViewById(R.id.card_title);
    }

    protected void setBackgroundForContainer(ViewGroup view) {}

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    final public void setData(String str) {
        title_text_view.setText(str);
    }


}

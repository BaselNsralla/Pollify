package com.projekt.tdp028.FeedList;

import android.content.Context;
import android.view.ViewGroup;

import com.projekt.tdp028.R;

public class OwnerCardItem extends CardItem {
    public OwnerCardItem(Context context) {
        super(context);
    }

    @Override
    protected void setBackgroundForContainer(ViewGroup view) {
        super.setBackgroundForContainer(view);
        view.setBackground(getResources().getDrawable(R.drawable.corner_radius_material_primary));
    }
}

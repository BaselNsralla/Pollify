package com.projekt.tdp028.Constants;

import android.content.Context;

public class DensityCalculator {
    public static int inDp(Context context, int size) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int dp = (int) (size * scale + 0.5f);
        return dp;
    }

}

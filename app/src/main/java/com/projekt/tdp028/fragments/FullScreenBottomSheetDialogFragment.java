package com.projekt.tdp028.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.projekt.tdp028.R;

public class FullScreenBottomSheetDialogFragment extends BottomSheetDialogFragment {
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.setBackgroundColor(0x00FFFFFF);
            bottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        View view = getView();
        view.post(() -> {
            View parent = (View) view.getParent(); // En coordinator view som jag får av specific fragment subclassing
            View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            bottomSheet.setBackgroundColor(0x00FFFFFF);
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) (parent).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();
            BottomSheetBehavior bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setPeekHeight(view.getMeasuredHeight());
            ((View)bottomSheet.getParent()).setBackgroundColor(Color.TRANSPARENT);
        });
    }
}

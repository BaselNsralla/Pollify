package com.projekt.tdp028.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class FlexButton extends androidx.appcompat.widget.AppCompatButton {
    public FlexButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlexButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FlexButton(Context context) {
        super(context);
        //mScaleDetector = new ScaleGestureDetector(getContext());
    }

    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;
    private ScaleGestureDetector mScaleDetector;

    float mLastTouchX;
    float mLastTouchY;
    float mPosX;
    float mPosY;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    float deltaX;
    float deltaY;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        final int action = MotionEventCompat.getActionMasked(ev);
        float rawX = ev.getRawX();
        float rawY = ev.getRawY();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                mPosX = getX();
                mPosY = getY();

                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
                ////deltaX = rawX - layoutParams.leftMargin;
                ////deltaY = rawY - layoutParams.rightMargin;

                deltaX = rawX - getX();
                deltaY = rawY - getY();

                mLastTouchX = x;
                mLastTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - mLastTouchX;
                final float dy = y - mLastTouchY;

                mPosX += dx;
                mPosY += dy;

                //params.setMargins((int)(rawX-deltaX), (int)(rawY-deltaY),0, 0);
;
                setY(rawY-deltaY);
                setX(rawX-deltaX);

                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                // MÄT hur lång tid det tog o kolla hur mycket den rörde på sig
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }

                break;
            }
        }
        return true;
    }
}

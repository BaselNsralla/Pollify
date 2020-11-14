package com.projekt.tdp028.animations;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class PollAnimation {

    public static class ScaleAnimator extends ValueAnimator {
        private float from;
        private float to;
        private View view;
        private PollAnimationListener pListener = null;

        public ScaleAnimator(View v, float from, float to) {
            super();

            this.from = from;
            this.to   = to;
            this.view = v;
            setup();

        }

        private void setup() {
            setFloatValues(this.from, this.to);
            setInterpolator(new AccelerateDecelerateInterpolator());
            setDuration(100);
            addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    view.setScaleX(animatedValue);
                    view.setScaleY(animatedValue);
                    view.requestLayout();
                    if (pListener != null) {
                        if (animatedValue == to) {
                            pListener.onAnimationEnd();
                        }
                    }
                }
            });
        }


        public void start(final PollAnimationListener listener) {
            pListener = listener;
            super.start();
        }


        public void reverse(final PollAnimationListener listener) {
            pListener = listener;
            super.reverse();
        }
    }

/*
    static ValueAnimator attachScaleAnimator(final View v, final float from, final float to, final PollAnimationListener listener) {
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(from, to);
        scaleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimator.setDuration(100);
        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                v.setScaleX(animatedValue);
                v.setScaleY(animatedValue);
                v.requestLayout();
                if (animatedValue == to) { listener.onAnimationEnd(); }
            }
        });

        return scaleAnimator;
    }

    static ValueAnimator attachScaleAnimator(final View v, final float from, final float to) {
        return attachScaleAnimator(v, from, to, new PollAnimationListener() {
            @Override
            public void onAnimationEnd() { }
        });
    }
  */


    public interface PollAnimationListener {
        void onAnimationEnd();
    }
}

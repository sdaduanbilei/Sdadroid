package com.scorpio.frame.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by scorpio on 14-11-27.
 */
public class CustomAnim {
    public static void FavAnimation(View view) {
        RotateAnimation animation = new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(80);
        animation.setRepeatCount(2);
        view.startAnimation(animation);
    }

    public static void Translate(View view) {
        Animation animation = new TranslateAnimation(1.0f, 1.0f, 1.0f, 0);
        animation.setDuration(100);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }
}

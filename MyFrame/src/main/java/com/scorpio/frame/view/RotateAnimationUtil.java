package com.scorpio.frame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

/**
 * Created by scorpio on 15-1-9.
 */
public class RotateAnimationUtil extends RotateAnimation{
    private long mElapsedAtPause = 0;
    private boolean mPaused = false;

    public RotateAnimationUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateAnimationUtil(float fromDegrees, float toDegrees) {
        super(fromDegrees, toDegrees);
    }

    public RotateAnimationUtil(float fromDegrees, float toDegrees, float pivotX, float pivotY) {
        super(fromDegrees, toDegrees, pivotX, pivotY);
    }

    public RotateAnimationUtil(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue) {
        super(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
    }

    @Override
    public boolean getTransformation(long currentTime, Transformation outTransformation) {
        if (mPaused && mElapsedAtPause ==0){
            mElapsedAtPause = currentTime -getStartTime();
        }
        if (mPaused){
            setStartTime(currentTime-mElapsedAtPause);
        }
        return super.getTransformation(currentTime, outTransformation);
    }

    public void  pause(){
        mElapsedAtPause = 0 ;
        mPaused = true ;
    }

    public void resume(){
        mPaused =false ;
    }
}

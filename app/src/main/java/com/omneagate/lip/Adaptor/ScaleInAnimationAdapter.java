package com.omneagate.lip.Adaptor;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by USER1 on 16-06-2016.
 */
public class ScaleInAnimationAdapter extends AnimationAdapter {

    private static final float DEFAULT_SCALE_FROM = .5f;
    private final float mFrom;


    @Override
    protected Animator[] getAnimators(View view) {

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f);
        return new ObjectAnimator[]{scaleX, scaleY};
    }

    public ScaleInAnimationAdapter(RecyclerView.Adapter adapter) {
        this(adapter, DEFAULT_SCALE_FROM);
    }

    public ScaleInAnimationAdapter(RecyclerView.Adapter adapter, float from) {
        super(adapter);
        mFrom = from;
    }
}

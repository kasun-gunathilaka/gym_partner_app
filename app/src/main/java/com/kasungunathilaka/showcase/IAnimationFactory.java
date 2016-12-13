package com.kasungunathilaka.showcase;

import android.graphics.Point;
import android.view.View;

/**
 * Created by nirmal on 6/29/2016.
 */
public interface IAnimationFactory {

    void fadeInView(View target, long duration, AnimationStartListener listener);

    void fadeOutView(View target, long duration, AnimationEndListener listener);

    void animateTargetToPoint(MaterialShowcaseView showcaseView, Point point);

    public interface AnimationStartListener {
        void onAnimationStart();
    }

    public interface AnimationEndListener {
        void onAnimationEnd();
    }
}

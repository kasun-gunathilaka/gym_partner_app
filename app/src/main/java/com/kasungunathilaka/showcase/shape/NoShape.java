package com.kasungunathilaka.showcase.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kasungunathilaka.showcase.target.Target;

/**
 * Created by nirmal on 6/29/2016.
 */
public class NoShape implements Shape {

    @Override
    public void updateTarget(Target target) {
        // do nothing
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int x, int y, int padding) {
        // do nothing
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}

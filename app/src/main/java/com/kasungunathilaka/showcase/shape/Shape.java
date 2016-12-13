package com.kasungunathilaka.showcase.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.kasungunathilaka.showcase.target.Target;

/**
 * Created by nirmal on 6/29/2016.
 */
public interface Shape {

    void draw(Canvas canvas, Paint paint, int x, int y, int padding);

    int getWidth();

    int getHeight();

    void updateTarget(Target target);

}

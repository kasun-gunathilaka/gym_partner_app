package com.kasungunathilaka.showcase.target;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by nirmal on 6/29/2016.
 */
public interface Target {
    Target NONE = new Target() {
        @Override
        public Point getPoint() {
            return new Point(1000000, 1000000);
        }

        @Override
        public Rect getBounds() {
            Point p = getPoint();
            return new Rect(p.x - 190, p.y - 190, p.x + 190, p.y + 190);
        }
    };

    Point getPoint();

    Rect getBounds();
}

package com.dk.microgis.gis.parmatransfer;

import com.dk.microgis.base.Point2D;

public class AdjPoint2D extends Point2D {
    public Double adjX;
    public Double adjY;

    public AdjPoint2D(Double x, Double y) {
        super(x, y);
    }

    public AdjPoint2D(Integer x, Integer y) {
        super(x, y);
    }


    public AdjPoint2D(String name, Double x, Double y) {
        super(name, x, y);
    }

    public AdjPoint2D(Point2D point2D) {
        super(point2D);
    }

    public AdjPoint2D() {
    }
}

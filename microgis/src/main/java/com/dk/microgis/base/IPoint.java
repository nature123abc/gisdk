package com.dk.microgis.base;

/**
 * @author hq
 * @date 2021-04-15 12:04
 * @desc
 */
public interface IPoint<T extends Point2D> {
    boolean isSamePoint(T p);
}

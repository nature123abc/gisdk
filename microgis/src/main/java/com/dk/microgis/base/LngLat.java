package com.dk.microgis.base;



/**
 * @author hq
 * @date 2020-12-17 14:25
 * @desc
 */
public class LngLat {
    public double lng;
    public double lat;
    public String name;


    public LngLat(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

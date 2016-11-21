package com.alsa.domain;

/**
 * Created by alsa on 21.11.2016.
 */
public class Basestamp {
    public long timestamp;
    public String base;

    public Basestamp(long timestamp, String base) {
        this.timestamp = timestamp;
        this.base = base;
    }
}

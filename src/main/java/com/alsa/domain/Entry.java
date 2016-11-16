package com.alsa.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by alsa on 03.11.2016.
 */

@Entity
public class Entry {
    @Id
    @GeneratedValue
    public long id;
    @Column(unique=true)
    public String prntscr;
    public String url;
    public Date timestamp;

    public Entry() {
    }

    public Entry(String prntscr, String url) {
        this.prntscr = prntscr;
        this.url = url;
    }
}

package com.alsa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by alsa on 16.11.2016.
 */
@Entity
public class Base {
    @Id
    @GeneratedValue
    public long id;
    public String base;

}

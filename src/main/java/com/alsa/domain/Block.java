package com.alsa.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by alsa on 03.11.2016.
 */

@Entity
public class Block {
    @Id
    @GeneratedValue
    public long id;
    @Column(unique=true)
    public String base;
    public BlockStatus status;
    public Date processedTime;
}

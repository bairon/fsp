package com.alsa.service;

import com.alsa.domain.Block;

/**
 * Created by alsa on 03.11.2016.
 */
public interface BlockService {
    Block create();

    Block save(Block block);
}

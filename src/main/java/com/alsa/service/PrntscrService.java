package com.alsa.service;

import com.alsa.domain.PrntscrResponse;

import java.io.InputStream;

/**
 * Created by alsa on 16.11.2016.
 */
public interface PrntscrService {
    PrntscrResponse uploadImage(InputStream imageStream);
}

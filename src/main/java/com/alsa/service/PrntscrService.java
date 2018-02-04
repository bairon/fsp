package com.alsa.service;

import com.alsa.domain.PrntscrResponse;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by alsa on 16.11.2016.
 */
public interface PrntscrService {
    PrntscrResponse uploadImage(InputStream imageStream) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException;
}

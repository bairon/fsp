package com.alsa.service;

import com.alsa.domain.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by alsa on 03.11.2016.
 */
public interface EntryService {
    Page<Entry> listAllByPage(Pageable p);

    Entry save(Entry entry);

    int newcount(long since);
}

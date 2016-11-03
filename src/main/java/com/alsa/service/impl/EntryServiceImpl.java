package com.alsa.service.impl;

import com.alsa.domain.Entry;
import com.alsa.repository.EntryRepository;
import com.alsa.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by alsa on 03.11.2016.
 */
@Service
public class EntryServiceImpl implements EntryService {

    @Autowired
    private EntryRepository entryRepository;

    @Override
    public Page<Entry> listAllByPage(Pageable p) {
        return entryRepository.findAll(p);
    }

    @Override
    public Entry save(Entry entry) {
        return entryRepository.save(entry);
    }

}

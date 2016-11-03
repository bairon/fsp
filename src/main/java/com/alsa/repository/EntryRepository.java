package com.alsa.repository;

import com.alsa.domain.Entry;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by alsa on 03.11.2016.
 */
public interface EntryRepository extends PagingAndSortingRepository<Entry, Long> {
}

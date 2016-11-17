package com.alsa.repository;

import com.alsa.domain.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by alsa on 03.11.2016.
 */
public interface EntryRepository extends PagingAndSortingRepository<Entry, Long> {

    @Query("select count(e) from Entry e where e.timestamp > ?1")
    int countOlder(long since);
}

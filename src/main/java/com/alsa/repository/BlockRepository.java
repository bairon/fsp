package com.alsa.repository;

import com.alsa.domain.Block;
import com.alsa.domain.BlockStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by alsa on 03.11.2016.
 */
public interface BlockRepository extends CrudRepository<Block, Long> {
    List<Block> findFirstByStatus(BlockStatus status, Sort sort);
    List<Block> findFirstByBase(String base);
    void deleteByProcessedTimeLessThan(long timestamp);

}

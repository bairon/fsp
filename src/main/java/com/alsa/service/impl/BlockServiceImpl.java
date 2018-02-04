package com.alsa.service.impl;

import com.alsa.Utils;
import com.alsa.domain.Base;
import com.alsa.domain.Basestamp;
import com.alsa.domain.Block;
import com.alsa.domain.BlockStatus;
import com.alsa.repository.BaseRepository;
import com.alsa.repository.BlockRepository;
import com.alsa.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.alsa.WebConstants.HOUR;

/**
 * Created by alsa on 03.11.2016.
 */
@Service
public class BlockServiceImpl implements BlockService {
    @Autowired
    BlockRepository blockRepository;

    @Autowired
    BaseRepository baseRepository;

    private LinkedList<Basestamp> baseStamps = new LinkedList<>();
    private long gap;

    @Override

    public synchronized Block create() {
        Block result = null;
        List<Block> processingBlocks = blockRepository.findFirstByStatus(BlockStatus.PROCESSING, new Sort("processedTime"));
        if (processingBlocks.size() > 0 && System.currentTimeMillis() - processingBlocks.get(0).processedTime > 1000 * 60 * 5) {
            processingBlocks.get(0).processedTime = System.currentTimeMillis();
            result = processingBlocks.get(0);
        } else {
            Base base = baseRepository.findOne(1L);
            if (base == null)
                throw new IllegalStateException("Base not set");
            while(!blockRepository.findFirstByBase(base.base).isEmpty()) {
                base.base = Utils.plusOneBase36(base.base);
            }
            Block newBlock = new Block();
            newBlock.processedTime = System.currentTimeMillis();
            newBlock.base = base.base;
            newBlock.status = BlockStatus.PROCESSING;
            result = newBlock;
            base.base = Utils.plusOneBase36(base.base);
            Utils.withRole("ROLE_ADMIN");
            baseRepository.save(base);
            Utils.withRole("ROLE_USER");
            updateGap(base);
        }
        if (result != null) {
            blockRepository.save(result);
        }
        return result;
    }

    private void updateGap(Base base) {
        Iterator<Basestamp> basestampIterator = baseStamps.iterator();
        while(basestampIterator.hasNext()) {
            Basestamp basestamp = basestampIterator.next();
            int compare = basestamp.base.compareTo(base.base);
            if (compare <= 0) {
                basestampIterator.remove();
            }
        }
        if (baseStamps.size() > 0) {
            Basestamp first = baseStamps.getFirst();
            gap = System.currentTimeMillis() - first.timestamp;
            if (gap < 0) gap = 0;
        } else {
            gap = 0;
        }
    }

    @Override
    public Block save(Block block) {
        block.status = BlockStatus.PROCESSED;
        block.processedTime = System.currentTimeMillis();
        return blockRepository.save(block);
    }

    @Override
    @Transactional
    public void clean() {
        Utils.withRole("ROLE_ADMIN");
        blockRepository.deleteByProcessedTimeLessThan(System.currentTimeMillis() - HOUR);
        Utils.clearRole();
    }

    @Override
    public synchronized void submitBaseTimestamp(long timestamp, String base) {
        try {
            if (base != null) {
                baseStamps.add(new Basestamp(timestamp, base));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public long gap() {
        return gap / 60000;
    }

    @Override
    public synchronized void overtake() {
        if (baseStamps.size() > 0) {
            Basestamp last = baseStamps.getLast();
            Utils.withRole("ROLE_ADMIN");
            Base base = new Base();
            base.id = 1L;
            base.base = last.base;
            baseRepository.save(base);
            Utils.withRole("ROLE_USER");
            baseStamps.clear();
            gap = 0;
        }
    }
}

package com.alsa.service.impl;

import com.alsa.Utils;
import com.alsa.domain.Block;
import com.alsa.domain.BlockStatus;
import com.alsa.repository.BlockRepository;
import com.alsa.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by alsa on 03.11.2016.
 */
@Service
public class BlockServiceImpl implements BlockService {
    @Autowired
    BlockRepository blockRepository;

    @Override

    public synchronized Block create() {
        Block result = null;
        List<Block> processingBlocks = blockRepository.findAllByStatus(BlockStatus.PROCESSING, new Sort("processedTime"));
        if (processingBlocks.size() > 0 && System.currentTimeMillis() - processingBlocks.get(0).processedTime.getTime() > 1000 * 60 * 5) {
            processingBlocks.get(0).processedTime = new Date();
            result = processingBlocks.get(0);
        } else {
            Iterable<Block> bases = blockRepository.findAll(new Sort(Sort.Direction.DESC, "base"));
            Block latestBlock = bases.iterator().next();
            Block newBlock = new Block();
            newBlock.processedTime = new Date();
            newBlock.base = Utils.plusOneBase36(latestBlock.base);
            newBlock.status = BlockStatus.PROCESSING;
            result = newBlock;
        }
        blockRepository.save(result);
        return result;
    }

    @Override
    public Block save(Block block) {
        block.status = BlockStatus.PROCESSED;
        block.processedTime = new Date();
        return blockRepository.save(block);
    }

}

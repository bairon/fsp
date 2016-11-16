package com.alsa.service.impl;

import com.alsa.Utils;
import com.alsa.domain.Base;
import com.alsa.domain.Block;
import com.alsa.domain.BlockStatus;
import com.alsa.repository.BaseRepository;
import com.alsa.repository.BlockRepository;
import com.alsa.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    BaseRepository baseRepository;

    @Override

    public synchronized Block create() {
        Block result = null;
        List<Block> processingBlocks = blockRepository.findAllByStatus(BlockStatus.PROCESSING, new Sort("processedTime"));
        if (processingBlocks.size() > 0 && System.currentTimeMillis() - processingBlocks.get(0).processedTime.getTime() > 1000 * 60 * 5) {
            processingBlocks.get(0).processedTime = new Date();
            result = processingBlocks.get(0);
        } else {
            Base base = baseRepository.findOne(1L);
            base.base = Utils.plusOneBase36(base.base);
            baseRepository.save(base);
            Block newBlock = new Block();
            newBlock.processedTime = new Date();
            newBlock.base = base.base;
            newBlock.status = BlockStatus.PROCESSING;
            result = newBlock;
        }
        if (result != null) {
            blockRepository.save(result);
        }
        return result;
    }

    @Override
    public Block save(Block block) {
        block.status = BlockStatus.PROCESSED;
        block.processedTime = new Date();
        return blockRepository.save(block);
    }

}

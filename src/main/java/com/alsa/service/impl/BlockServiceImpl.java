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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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

    @Override

    public synchronized Block create() {
        Block result = null;
        List<Block> processingBlocks = blockRepository.findAllByStatus(BlockStatus.PROCESSING, new Sort("processedTime"));
        if (processingBlocks.size() > 0 && System.currentTimeMillis() - processingBlocks.get(0).processedTime > 1000 * 60 * 5) {
            processingBlocks.get(0).processedTime = System.currentTimeMillis();
            result = processingBlocks.get(0);
        } else {
            Base base = baseRepository.findOne(1L);
            if (base == null || base.base == null || base.base.length() == 0)
                throw new IllegalStateException("Base not set");
            while(!blockRepository.findAllByBase(base.base).isEmpty()) {
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
        }
        if (result != null) {
            blockRepository.save(result);
        }
        return result;
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
}

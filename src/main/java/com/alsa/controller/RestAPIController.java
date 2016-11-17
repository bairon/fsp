package com.alsa.controller;

import com.alsa.WebConstants;
import com.alsa.domain.Block;
import com.alsa.domain.Entry;
import com.alsa.service.BlockService;
import com.alsa.service.EntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by alsa on 03.11.2016.
 */

@RestController
public class RestAPIController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestAPIController.class);

    @Autowired
    private EntryService entryService;
    @Autowired
    private BlockService blockService;

    @RequestMapping(value = WebConstants.CREATE_BLOCK, method = RequestMethod.GET)
    public Block createBlock()
    {
        return blockService.create();
    }

    @RequestMapping(value = WebConstants.POST_ENTRY, method = RequestMethod.POST)
    public Entry postEntry(@RequestBody Entry entry, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().iterator().next().toString());
        }
        try {
            return entryService.save(entry);
        } catch (Throwable t) {
            return null;
        }
    }
    @RequestMapping(value = WebConstants.POST_BLOCK, method = RequestMethod.POST)
    public Block postBlock(@RequestBody Block block, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RuntimeException(bindingResult.getAllErrors().iterator().next().toString());
        }
        return blockService.save(block);

    }
    @RequestMapping(value = WebConstants.ENTRIES, method = RequestMethod.GET)
    public Page<Entry> entries(Pageable pageable) {
        return entryService.listAllByPage(pageable);
    }

    @RequestMapping(value = WebConstants.ENTRIES_NEWCOUNT, method = RequestMethod.GET)
    public int entries(long since) {
        return entryService.newcount(since);
    }

}

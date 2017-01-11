package com.alsa;

import com.alsa.domain.Base;
import com.alsa.domain.PrntscrResponse;
import com.alsa.repository.BaseRepository;
import com.alsa.repository.BlockRepository;
import com.alsa.repository.EntryRepository;
import com.alsa.service.BlockService;
import com.alsa.service.EntryService;
import com.alsa.service.PrntscrService;
import com.alsa.worker.PrntscrSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import static com.alsa.WebConstants.HOUR;
import static com.alsa.WebConstants.MINUTE;

@SpringBootApplication
public class FspApplication {

    @Autowired
    BaseRepository baseRepository;

    @Autowired
    BlockRepository blockRepository;

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    PrntscrService prntscrService;

    @Autowired
    BlockService blockService;

    @Autowired
    EntryService entryService;

    @Autowired
    PrntscrSearch prntscrSearch;

    public static void main(String[] args) {
        SpringApplication.run(FspApplication.class, args);
    }

    @PostConstruct
    public void init() {
        Utils.withRole("ROLE_USER", "ROLE_ADMIN");
        Base b = baseRepository.findOne(1L);
        if (b == null) {
            b = new Base();
            b.id = 1L;
            b.base = getCurrentBase();
            if (b.base != null && b.base.length() > 0) {
                baseRepository.save(b);
            }
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    blockService.clean();
                    entryService.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, HOUR, HOUR);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String currentBase;
                int counter = 0;
                do {
                    currentBase = getCurrentBase();
                    long currentTimestamp = System.currentTimeMillis();
                    blockService.submitBaseTimestamp(currentTimestamp, currentBase);
                    Utils.sleep(5000);
                } while (currentBase == null && counter++ < 20);
            }
        }, MINUTE, 5 * MINUTE);
        Utils.clearRole();
        //new Thread(prntscrSearch).start();
    }

    private synchronized String getCurrentBase() {
        String result = null;
        InputStream stream = getClass().getClassLoader().getResourceAsStream("static/img/space.png");
        PrntscrResponse prntscrResponse = prntscrService.uploadImage(stream);
        if (prntscrResponse != null) {
            if (prntscrResponse.status.equalsIgnoreCase("success")) {
                String url = prntscrResponse.data;
                if (url != null && url.trim().length() > 0) {
                    String prntscrid = url.substring(url.lastIndexOf("/") + 1);
                    result = prntscrid.substring(0, prntscrid.length() - 1);
                }
            } else {
                System.out.println(prntscrResponse.data);
            }
        }
        System.out.println("getCurrentBase() returning " + result);
        return result;
    }
}

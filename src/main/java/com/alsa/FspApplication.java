package com.alsa;

import com.alsa.domain.Base;
import com.alsa.domain.Entry;
import com.alsa.domain.PrntscrResponse;
import com.alsa.repository.BaseRepository;
import com.alsa.repository.BlockRepository;
import com.alsa.repository.EntryRepository;
import com.alsa.service.BlockService;
import com.alsa.service.EntryService;
import com.alsa.service.PrntscrService;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.alsa.WebConstants.HOUR;

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

	public static void main(String[] args) {
		SpringApplication.run(FspApplication.class, args);
	}

	@PostConstruct
	public void init() {
		Utils.withRole("ROLE_ADMIN");
		Base b = new Base();
		b.id = 1L;
		b.base = getCurrentBase();
		if (b.base != null && b.base.length() > 0) {
			baseRepository.save(b);
		}
		Utils.clearRole();
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
	}

	private String getCurrentBase() {
		InputStream stream = getClass().getClassLoader().getResourceAsStream("static/img/space.png");
		PrntscrResponse prntscrResponse = prntscrService.uploadImage(stream);
		if (prntscrResponse != null) {
			if (prntscrResponse.status.equalsIgnoreCase("success")) {
				String url = prntscrResponse.data;
				if (url != null && url.trim().length() > 0) {
					String prntscrid = url.substring(url.lastIndexOf("/") + 1);
					return prntscrid.substring(0, prntscrid.length() - 1);
				}
			} else {
				System.out.println(prntscrResponse.data);
			}
		}
		return null;
	}
}

package com.alsa;

import com.alsa.domain.Entry;
import com.alsa.repository.BlockRepository;
import com.alsa.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class FspApplication {

	@Autowired
	public EntryRepository entryRepository;

	@Autowired
	public BlockRepository blockRepository;

	public static void main(String[] args) {
		SpringApplication.run(FspApplication.class, args);
	}
	@PostConstruct
	public void init() {
		entryRepository.save(new Entry("d2tr1y", "http://image.prntscr.com/image/e32168d3872643209a8ba5866288f2bd.png"));
		entryRepository.save(new Entry("d2tse3", "http://image.prntscr.com/image/ef8ae9964cc64c5d8616a3e41538155d.png"));
	}
}

package com.alsa;

import com.alsa.domain.Base;
import com.alsa.repository.BaseRepository;
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

import javax.annotation.PostConstruct;
import java.io.*;

@SpringBootApplication
public class FspApplication {

	@Autowired
	BaseRepository baseRepository;

	@PostConstruct
	public void init() {
		Base b = new Base();
		b.id = 1L;
		b.base = getCurrentBase();
		//baseRepository.save(b);
	}

	private String getCurrentBase() {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost("https://prntscr.com/upload.php");
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		InputStream stream = getClass().getClassLoader().getResourceAsStream("static/img/space.png");
		String responseString = null;
		try {
			builder.addBinaryBody(
					"image",
					stream,
					ContentType.create("image/png"),
					"space.png"
			);
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			StatusLine statusLine = response.getStatusLine();
			System.out.println(statusLine.toString());
			HttpEntity responseEntity = response.getEntity();
			BasicResponseHandler basicResponseHandler = new BasicResponseHandler();
			responseString = basicResponseHandler.handleEntity(responseEntity);
			System.out.println(responseString);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseString;
	}

	public static void main(String[] args) {
		SpringApplication.run(FspApplication.class, args);
	}
}

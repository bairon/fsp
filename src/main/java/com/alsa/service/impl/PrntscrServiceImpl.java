package com.alsa.service.impl;

import com.alsa.domain.PrntscrResponse;
import com.alsa.service.PrntscrService;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by alsa on 16.11.2016.
 */
@Service
public class PrntscrServiceImpl implements PrntscrService {

    public static final String HTTPS_PRNTSCR_COM_UPLOAD_PHP = "https://prntscr.com/upload.php";

    @Override
    public PrntscrResponse uploadImage(InputStream imageStream) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost(HTTPS_PRNTSCR_COM_UPLOAD_PHP);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();


        try {
            builder.addBinaryBody(
                    "image",
                    imageStream,
                    ContentType.create("image/png"),
                    "space.png"
            );
            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(uploadFile);
            if (response == null) return null;
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity == null) return null;
            String responseString = new BasicResponseHandler().handleEntity(responseEntity);
            PrntscrResponse prntscrResponse = new GsonBuilder().create().fromJson(responseString, PrntscrResponse.class);
            return prntscrResponse;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}

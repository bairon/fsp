package com.alsa.service.impl;

import com.alsa.domain.PrntscrResponse;
import com.alsa.service.PrntscrService;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by alsa on 16.11.2016.
 */
@Service
public class PrntscrServiceImpl implements PrntscrService {

    public static final String HTTPS_PRNTSCR_COM_UPLOAD_PHP = "https://prntscr.com/upload.php";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";


    @Override
    public PrntscrResponse uploadImage(InputStream imageStream) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RequestConfig externalRequestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000).setSocketTimeout(3000).build();
        SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
        sslContextBuilder.loadTrustMaterial(new TrustSelfSignedStrategy());
        SSLContext sslContext = sslContextBuilder.build();
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, new org.apache.http.conn.ssl.DefaultHostnameVerifier());
        HttpClientBuilder externalHttpClientBuilder = HttpClientBuilder.create().setSSLSocketFactory(sslSocketFactory).addInterceptorLast(new RequestAcceptEncoding()).addInterceptorLast(new ResponseContentEncoding())
                .setDefaultRequestConfig(externalRequestConfig).setUserAgent(USER_AGENT);
        CloseableHttpClient httpClient = externalHttpClientBuilder.build();
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

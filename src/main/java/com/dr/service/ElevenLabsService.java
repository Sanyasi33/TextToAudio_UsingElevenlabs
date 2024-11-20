package com.dr.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class ElevenLabsService {

    @Value("${elevenlabs.apiKey}")
    private String ELEVANLABS_API_KEY;
    @Value("${elevenlabs.apiUrl}")
    private String ELEVANLABS_API_URL;

    public byte[] convertTextToSpeech(String text) throws Exception {
        OkHttpClient client = new OkHttpClient();

        String requestJson = "{\"text\":\"" + text + "\", \"voice\":\"default\", \"voice_settings\":{\"stability\":0.5, \"similarity_boost\":0.75}}";
        RequestBody body = RequestBody.create(requestJson, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(ELEVANLABS_API_URL)
                .addHeader("Accept", "application/json")
                .addHeader("xi-api-key", ELEVANLABS_API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response Code: " + response.code());
            if (response.isSuccessful() && response.body() != null) {
                return response.body().bytes();
            } else {
                System.err.println("Request failed with code: " + response.code());
                System.err.println("Response message: " + response.message());
                if (response.body() != null) {
                    System.err.println("Response body: " + response.body().string());
                }
                return new byte[0];
            }
        }
    }
}

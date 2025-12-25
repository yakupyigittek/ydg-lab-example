package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ExternalRequestTest {

    @Test
    @DisplayName("example.com'a GET isteği 200 dönmeli")
    void exampleDotComShouldReturn200() {
        @SuppressWarnings("resource")
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://example.com"))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        int attempts = 0;
        int maxAttempts = 3;
        long backoffMillis = 1000;
        Exception lastException = null;
        HttpResponse<Void> response = null;
        while (attempts < maxAttempts) {
            try {
                response = client.send(request, HttpResponse.BodyHandlers.discarding());
                break; // başarılı
            } catch (Exception e) {
                lastException = e;
                attempts++;
                if (attempts < maxAttempts) {
                    try {
                        Thread.sleep(backoffMillis * attempts); // artan bekleme
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        fail("Bekleme kesildi: " + ie.getMessage());
                        return;
                    }
                }
            }
        }

        if (response == null) {
            fail("HTTP isteği başarısız oldu (" + maxAttempts + " deneme): " + (lastException != null ? lastException.getMessage() : "bilinmeyen hata"));
            return;
        }

        // Testi kasıtlı olarak başarısız yapmak için beklenen kodu 201'e çekiyoruz
        assertEquals(201, response.statusCode(), "Beklenen durum kodu 201 (kasıtlı), gelen: " + response.statusCode());
    }
}

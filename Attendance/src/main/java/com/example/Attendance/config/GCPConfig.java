package com.example.Attendance.config;

import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GCPConfig {

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileName;

    @Bean
    public Storage storage() {
        String key = String.format("classpath:%s.json", keyFileName);
        try (InputStream keyFile = ResourceUtils.getURL(key).openStream();) {
            return StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(keyFile))
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.GCP_SETTING_ERROR);
        }
    }
}
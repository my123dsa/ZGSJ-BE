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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class GCPConfig {

    @Value("${GCP_JSON}")
    private String credentialsJson;


    @Bean
    public Storage storage() {

//        String key = String.format("classpath:%s.json", keyFileName);
//        try (InputStream keyFile = ResourceUtils.getURL(key).openStream();) {

        // GCP를 사용하려면 key가 필요한데 기본적으로 key는 json파일로 줘서 위와 같이 사용해야하는데
        // 우리는 ci/cd자동화 때문에 String으로 하는게 필요했음 -> 이에 따라 아래의 코드를 이용해 키스트림으로 뽑아내 사용할 수 있었다~
            try (ByteArrayInputStream keyFile = new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))) {


            return StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(keyFile))
                    .build()
                    .getService();
        } catch (IOException e) {
            throw new CustomException(ErrorCode.GCP_SETTING_ERROR);
        }
    }
}
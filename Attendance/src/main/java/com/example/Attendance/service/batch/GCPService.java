package com.example.Attendance.service.batch;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GCPService {
    private final String bucketName;
    private final Storage storage;

    public GCPService(@Value("${spring.cloud.gcp.storage.bucket}") String bucketName, Storage storage) {
        this.bucketName = bucketName;
        this.storage = storage;
    }

    public String uploadObject(byte[] pdfContent) {
        UUID uuid = UUID.randomUUID();

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uuid.toString())
                .setContentType("application/pdf")
                .build();
        storage.create(blobInfo, pdfContent);

        return String.format("https://storage.cloud.google.com/%s/%s", bucketName, uuid); //pdf url
    }
}

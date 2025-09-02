package com.satnam.codesapi.user_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

@Service
public class S3Service {

    private final S3Client s3;
    private final String bucket;
    private final String region;
    private final AwsBasicCredentials awsCreds;

    public S3Service(
            @Value("${aws.access-key}") String accessKey,
            @Value("${aws.secret-key}") String secretKey,
            @Value("${aws.region}") String region,
            @Value("${s3.bucket}") String bucket
    ) {
        this.bucket = bucket;
        this.region = region;
        this.awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    /**
     * Upload file to S3
     */
    public void uploadFile(String key, InputStream inputStream, long contentLength, String contentType) {
        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                // Keep private (use pre-signed URL to access)
                .acl(software.amazon.awssdk.services.s3.model.ObjectCannedACL.PRIVATE)
                .build();

        s3.putObject(putReq, RequestBody.fromInputStream(inputStream, contentLength));
    }

    /**
     * Delete file from S3
     */
    public void deleteFile(String key) {
        DeleteObjectRequest delReq = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3.deleteObject(delReq);
    }

    /**
     * Generate pre-signed URL (valid for specified duration)
     */
    public URL getFileUrl(String key) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofMinutes(15)) // valid for 15 minutes
                    .build();

            return presigner.presignGetObject(presignRequest).url();
        }
    }
}

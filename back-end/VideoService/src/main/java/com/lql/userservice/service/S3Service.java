package com.lql.userservice.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 amazonS3;
    public S3Object getObject(String bucketName, String objectKey) {
        return amazonS3.getObject(new GetObjectRequest(bucketName, objectKey));
    }

    public Mono<PutObjectResult> uploadObject(String bucketName, String objectKey, File file) {
        return Mono.just(amazonS3.putObject(bucketName, objectKey, file));
    }
    public String generateUrl(String bucketName, String objectKey, Date expiresDate) {
        return amazonS3.generatePresignedUrl(bucketName, objectKey, expiresDate, HttpMethod.GET).toString();
    }


    public void deleteObject(String bucketName, String objectKey) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
    }
}

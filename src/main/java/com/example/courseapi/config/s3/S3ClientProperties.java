package com.example.courseapi.config.s3;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Data
@ConfigurationProperties("aws.s3")
public class S3ClientProperties {
    private boolean mock;
    private String region;
    private String bucketName;
}

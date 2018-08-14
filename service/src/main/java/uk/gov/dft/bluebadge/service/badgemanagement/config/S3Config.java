package uk.gov.dft.bluebadge.service.badgemanagement.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class S3Config {
  @Value("${amazonProperties.endpointUrl}")
  private String endpointUrl;
  @Value("${amazonProperties.bucketName}")
  private String bucketName;
  @Value("${amazonProperties.accessKey}")
  private String accessKey;
  @Value("${amazonProperties.secretKey}")
  private String secretKey;

  @Bean
  public AmazonS3Client amazonS3Client(){
    return AmazonS3ClientBuilder.standard()
        .withRegion(clientRegion)
        .withCredentials(new ProfileCredentialsProvider())
        .build();
  }

}

package uk.gov.dft.bluebadge.service.badgemanagement.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class S3Config {
  @Value("${amazon.profile:default}")
  private String profile;

  @Value("${amazon.s3bucket}")
  private String s3Bucket;

  @Value("${amazon.thumbnail-height-px:300}")
  private Integer thumbnailHeight;

  @Bean
  public AmazonS3 amazonS3() {
    return AmazonS3ClientBuilder.standard()
        .withCredentials(new ProfileCredentialsProvider(profile))
        .build();
  }
}

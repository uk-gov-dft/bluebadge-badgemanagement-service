package uk.gov.dft.bluebadge.service.badgemanagement.service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class PhotoServiceTest {

  public void setup(){
    AmazonS3ClientBuilder.standard()
        .withRegion(clientRegion)
        .withCredentials(new ProfileCredentialsProvider())
        .build();
  }
}
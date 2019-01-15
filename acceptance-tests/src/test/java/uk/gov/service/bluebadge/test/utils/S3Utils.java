package uk.gov.service.bluebadge.test.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.net.URISyntaxException;

@SuppressWarnings({"unused", "WeakerAccess"})
public class S3Utils {
  private final AmazonS3 s3;

  public S3Utils() {
    this.s3 = AmazonS3ClientBuilder.defaultClient();
  }

  public int getNumberOfFilesInABucket(String bucketName) throws Exception {
    if (s3.doesBucketExistV2(bucketName)) {
      ObjectListing result = s3.listObjects(bucketName);
      while (result.isTruncated()) {
        result = s3.listNextBatchOfObjects(result);
      }
      return result.getObjectSummaries().size();
    } else {
      throw new Exception("Bucket `" + bucketName + "` doesn't exist");
    }
  }

  public void removeObject(String bucket, String key) {
    s3.deleteObject(bucket, key);
  }

  public void cleanBucket(String bucketName) {
    for (S3ObjectSummary objectSummary : s3.listObjects(bucketName).getObjectSummaries()) {
      s3.deleteObject(bucketName, objectSummary.getKey());
    }
  }

  public String putObject(String bucket, String fileName, String s3key) throws URISyntaxException {
    File f = new File(this.getClass().getResource(fileName).toURI());
    if (!s3.doesObjectExist(bucket, s3key)) {
      s3.putObject(bucket, s3key, f);
    }
    return s3key;
  }

  public String putObject(String bucket, String fileName) throws URISyntaxException {
    return putObject(bucket, fileName, fileName);
  }

  public boolean objectExists(String bucket, String objectKey) {
    return s3.doesObjectExist(bucket, objectKey);
  }
}

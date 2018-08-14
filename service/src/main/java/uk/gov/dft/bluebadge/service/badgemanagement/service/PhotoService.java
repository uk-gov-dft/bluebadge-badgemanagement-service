package uk.gov.dft.bluebadge.service.badgemanagement.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class PhotoService {

  private final AmazonS3Client amazonS3Client;
  private final String s3BucketName;

  @Autowired
  public PhotoService(AmazonS3Client amazonS3Client, String s3BucketName) {
    this.amazonS3Client = amazonS3Client;
    this.s3BucketName = s3BucketName;
  }

  public String uploadPhoto(MultipartFile multipartFile) throws IOException {
    File uploadFile = convertMultiPartToFile(multipartFile);
    String originalFilename = multipartFile.getOriginalFilename();

    PutObjectRequest request = new PutObjectRequest(s3BucketName, originalFilename, uploadFile);
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType("plain/text");
    metadata.addUserMetadata("x-amz-meta-title", "someTitle");
    request.setMetadata(metadata);

    PutObjectResult putObjectResult = amazonS3Client.putObject(request);
    putObjectResult.
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();
    return convFile;
  }
}

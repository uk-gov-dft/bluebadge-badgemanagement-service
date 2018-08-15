package uk.gov.dft.bluebadge.service.badgemanagement.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.service.exception.InternalServerException;
import uk.gov.dft.bluebadge.service.badgemanagement.config.S3Config;

@Slf4j
@Service
public class PhotoService {

  private static final String FILE_PATH_TEMPLATE_ORIGINAL =
      "{badgeNumber}/artefacts/{uuid}-original.jpg";
  private static final String FILE_PATH_TEMPLATE_THUMBNAIL =
      "{badgeNumber}/artefacts/{uuid}-thumbnail.jpg";
  private final AmazonS3 amazonS3;
  private S3Config s3Config;

  @Autowired
  public PhotoService(AmazonS3 amazonS3, S3Config s3Config) {
    this.amazonS3 = amazonS3;
    this.s3Config = s3Config;
  }

  S3KeyNames getS3KeyNames(String badgeNumber) {
    Assert.notNull(badgeNumber, "badge number required to generate s3 key");
    S3KeyNames names = new S3KeyNames();
    String uuid = UUID.randomUUID().toString();
    String path = StringUtils.replace(FILE_PATH_TEMPLATE_ORIGINAL, "{uuid}", uuid);
    names.setOriginalKeyName(StringUtils.replace(path, "{badgeNumber}", badgeNumber));

    path = StringUtils.replace(FILE_PATH_TEMPLATE_THUMBNAIL, "{uuid}", uuid);
    names.setThumbnailKeyName(StringUtils.replace(path, "{badgeNumber}", badgeNumber));

    names.setOriginalUrl("/" + s3Config.getS3bucket() + "/" + names.getOriginalKeyName());
    names.setThumbnameUrl("/" + s3Config.getS3bucket() + "/" + names.getThumbnailKeyName());

    return names;
  }

  S3KeyNames photoUpload(String imageAsBase64, String badgeNumber) {
    Assert.notNull(imageAsBase64, "Require image.");
    Assert.notNull(badgeNumber, "Need badge number for storage path");

    S3KeyNames names = getS3KeyNames(badgeNumber);

    byte[] rawData = Base64.getDecoder().decode(imageAsBase64);
    InputStream originalAsByteInputStream = new ByteArrayInputStream(rawData);
    ImageIO.setUseCache(false);

    BufferedImage originalImage;
    try {
      originalImage = ImageIO.read(originalAsByteInputStream);
    } catch (IOException e) {
      log.error("Could not process file.", e);
      Error error = new Error();
      error.setMessage("File storage failed, Could not parse file.");
      throw new InternalServerException(error);
    }

    try {
      // Save original image
      uploadImage(originalImage, names.getOriginalKeyName(), originalImage.getHeight());

      // Save thumbnail
      uploadImage(originalImage, names.getThumbnailKeyName(), s3Config.getThumbnailHeight());

      log.info("Photo upload successful, badge {}.", badgeNumber);
    } catch (AmazonServiceException e) {
      // The call was transmitted successfully, but Amazon S3 couldn't process
      // it, so it returned an error response.
      log.error("S3 could not complete putobject.", e);
      Error error = new Error();
      error.setMessage("File storage failed, s3 storage could not process request.");
      throw new InternalServerException(error);
    } catch (SdkClientException e) {
      // Amazon S3 couldn't be contacted for a response, or the client
      // couldn't parse the response from Amazon S3.
      log.error("Could not connect to s3", e);
      Error error = new Error();
      error.setMessage("File storage failed, s3 storage could not be contacted.");
      throw new InternalServerException(error);
    }

    return names;
  }

  void uploadImage(BufferedImage originalImage, String s3Key, int height) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType("image/jpeg");
    ByteArrayInputStream streamToWriteToS3 =
        getInputStreamForSizedBufferedImage(originalImage, height);
    PutObjectRequest request =
        new PutObjectRequest(s3Config.getS3bucket(), s3Key, streamToWriteToS3, metadata);
    PutObjectResult result = amazonS3.putObject(request);
    log.debug("Original Image: wrote {} bytes to s3.", result.getMetadata().getContentLength());
  }

  ByteArrayInputStream getInputStreamForSizedBufferedImage(BufferedImage sourceImage, int height) {
    BufferedImage outputImage =
        new BufferedImage(
            sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
    outputImage
        .createGraphics()
        .drawImage(sourceImage, 0, 0, getProportionalWidth(sourceImage, height), height, null);

    ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();

    try {
      ImageIO.write(outputImage, "jpg", imageOutputStream);
    } catch (IOException e) {
      log.error("Could not process file - resizing.", e);
      Error error = new Error();
      error.setMessage("File storage failed, Could not resize file.");
      throw new InternalServerException(error);
    }
    return new ByteArrayInputStream(imageOutputStream.toByteArray());
  }

  int getProportionalWidth(BufferedImage sourceImage, int height) {
    // No change if 100% (Avoid any rounding)
    if (sourceImage.getHeight() == height) {
      return sourceImage.getWidth();
    }

    float ratio = (float) height / (float) sourceImage.getHeight();

    return Math.round(ratio * sourceImage.getWidth());
  }
}

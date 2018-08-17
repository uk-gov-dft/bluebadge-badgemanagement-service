package uk.gov.dft.bluebadge.service.badgemanagement.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import uk.gov.dft.bluebadge.common.api.model.Error;
import uk.gov.dft.bluebadge.common.service.ImageProcessingUtils;
import uk.gov.dft.bluebadge.common.service.exception.InternalServerException;
import uk.gov.dft.bluebadge.service.badgemanagement.config.S3Config;

@Slf4j
@Service
class PhotoService {

  private static final String FILE_PATH_TEMPLATE_ORIGINAL =
      "{parentId}/artefacts/{uuid}-original.jpg";
  private static final String FILE_PATH_TEMPLATE_THUMBNAIL =
      "{parentId}/artefacts/{uuid}-thumbnail.jpg";
  private static final String IMAGE_JPEG = "image/jpeg";
  private final AmazonS3 amazonS3;
  private final S3Config s3Config;

  @Autowired
  PhotoService(
      AmazonS3 amazonS3, S3Config s3Config) {
    this.amazonS3 = amazonS3;
    this.s3Config = s3Config;
  }

  /**
   * Retrieves the S3 key for a given badge number. Also returns the url to store in database.
   *
   * @param parentId Identifier of parent object e.g. badge number.
   * @return Keys and urls.
   */
  S3KeyNames generateS3KeyNames(String parentId, String bucket) {
    Assert.notNull(parentId, "parentId (badge number) required to generate s3 key");
    S3KeyNames names = new S3KeyNames();
    String uuid = UUID.randomUUID().toString();
    String path = StringUtils.replace(FILE_PATH_TEMPLATE_ORIGINAL, "{uuid}", uuid);
    names.setOriginalKeyName(StringUtils.replace(path, "{parentId}", parentId));

    path = StringUtils.replace(FILE_PATH_TEMPLATE_THUMBNAIL, "{uuid}", uuid);
    names.setThumbnailKeyName(StringUtils.replace(path, "{parentId}", parentId));

    names.setOriginalUrl("/" + bucket + "/" + names.getOriginalKeyName());
    names.setThumbnameUrl("/" + bucket + "/" + names.getThumbnailKeyName());

    return names;
  }

  /**
   * Store photo and thumbnail in S3.
   *
   * @param imageAsBase64 Source as encoded string.
   * @param parentId Parent context object id, e.g. badge number.
   * @return Urls to access stored images.
   */
  S3KeyNames photoUpload(String imageAsBase64, String parentId) {
    Assert.notNull(imageAsBase64, "Require image.");
    Assert.notNull(parentId, "Need parentId (badge number) for storage path");

    S3KeyNames names = generateS3KeyNames(parentId, s3Config.getS3Bucket());
    BufferedImage originalImage =
        ImageProcessingUtils.getBufferedImageFromBase64(imageAsBase64, parentId);

    try {
      // Save original image
      uploadImage(originalImage, names.getOriginalKeyName(), originalImage.getHeight());

      // Save thumbnail
      uploadImage(originalImage, names.getThumbnailKeyName(), s3Config.getThumbnailHeight());

      log.info("Photo upload successful, badge {}.", parentId);
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

  /**
   * Upload a single image to S3 after resizing it.
   *
   * @param originalImage Image.
   * @param s3Key S3 key to store at.
   * @param height Height to size to.
   */
  private void uploadImage(BufferedImage originalImage, String s3Key, int height) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(IMAGE_JPEG);
    ByteArrayInputStream streamToWriteToS3 =
        ImageProcessingUtils.getInputStreamForSizedBufferedImage(originalImage, height);
    PutObjectRequest request =
        new PutObjectRequest(s3Config.getS3Bucket(), s3Key, streamToWriteToS3, metadata);
    PutObjectResult result = amazonS3.putObject(request);
    log.debug("image written to s3 key: {}. Aws result: {}", s3Key, result.getETag());
  }
}

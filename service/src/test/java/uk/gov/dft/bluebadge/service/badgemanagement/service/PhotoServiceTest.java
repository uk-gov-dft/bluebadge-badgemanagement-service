package uk.gov.dft.bluebadge.service.badgemanagement.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import uk.gov.dft.bluebadge.common.service.exception.InternalServerException;
import uk.gov.dft.bluebadge.service.badgemanagement.config.S3Config;

public class PhotoServiceTest {

  private final String BADGE_NUMBER = "KKKKKK";
  private final String IMAGE_JPG_BASE64_GOOD =
      "/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRo"
          + "fHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjI"
          + "yMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAAQABgDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwI"
          + "EAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmN"
          + "kZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb"
          + "3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobH"
          + "BCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqO"
          + "kpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDLs2+z3C3AbAXgqO4NWtN02TU"
          + "b9JlXFvHIWJPc5NP0rSzdv5sgxEnXPeuysreJIlESYQ9MCtIVnTVkeTgcO2ryQ+OEemM9cUVcERJBJKgdqK53q7nrrQ//2Q==";

  private PhotoService photoService;

  @Mock AmazonS3Client amazonS3Client;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(amazonS3Client.putObject(any())).thenReturn(new PutObjectResult());
    S3Config config = new S3Config();

    String BUCKET = "bucket";
    config.setS3Bucket(BUCKET);
    config.setThumbnailHeight(300);
    config.setSignedUrlDurationMs(1000);
    photoService = new PhotoService(amazonS3Client, config);
  }

  @Test
  public void photoUpload() {
    // upload method orchestrates rather than actually doing much, so just give it a run
    photoService.photoUpload(IMAGE_JPG_BASE64_GOOD, BADGE_NUMBER);
    String IMAGE_GIF_BASE64 =
        "R0lGODlhOAHwAPAAAAAAAP///ywAAAAAOAHwAAAC/kSMp8nrDZ+MdNqKr858+w5+YkiOZhOk6sq2"
            + "7gvH8kzX9o3n+s73/j8rCU/EobGIPCqTzKXTBIxKp9Sq9YrN4p7cprcL/orD5LFGi06r1+y2e1WOm+X0ub2OF733/L7/v5YneEc4aFiIOAS4yNjo"
            + "+KhyKJlIOWlZKQapucnZKXUJihk6Klpq4ImaqrqaQupqCvsqi8Raa3vbN6sby7vriwscLGz1W9x7bHw4vMzcXJOMHA09beZsfW1NrS3NvR2CDR6O"
            + "691dTn6uIK6+7onubg4PzT5Pz/h+H58/W8/f74YPUJ/AS/4KGsQSMOHAhXkOOnz4Q6FEhhTDQLyI0cbE/o0VOx7JCDJkC44kPZrsIDJlyJIsT7pE"
            + "oTImxJY0X7qUifNgzZ02qeX82Y+n0J4CgRqdNzQp0X1Hm4ZTCnVpNKdUr0W9KnVS1a3MsHrNiomr2GBfy4KVMzatLbNsz1ZTCzdV27lul8S924mu"
            + "3rpE8PqFtDcwX5R/Cy8SjHiwBcOM/SR+rDhB48lvIFtWTDlzoMicLz/RDFqL586kP4Y+XWW0aqmoW38qDXs1Yde0eciOjbtC7d06bvs2yTu4xtzE"
            + "f58SjjyG8eLEkzt3sTx60efUWzGXXrN6dezXSWunzj08t+/PxZs/Rt75+fWw0idnDz+se+Hx6yOaT7+7fXz4/oPv109Uf7z9R2AmAtZWYIJMHIgg"
            + "gApuwyBtDzrYUYSuTYihHhailmGHZ2x4mociSgBiiBSOuEuJoaF4IjwqgsYiiy9qFmOLr8yYWY0j4kiZjjZOxWNjPnoYpJA/DslLkYwhmaGShjF5"
            + "ZHtO/gXlhFNSGWWV8l15l5YKcomXl1lSAmaXY4qJVplwoXkmIWqu2SabFr2ZlpxxvkWnWHbal+dYe975WZ9c/RmfoIMCiihHhm5FaKK0LEpVo+xB"
            + "GqmjkkJBaVOXnpepppZ+yl+nRm0Kqgeijloqqbqd+pOqqS7Gak6ucherrK/O6kCtOOEqna4y8XprOr6qBOxywxIbgyywx6ZUbLDLitTsb8+ulGy1"
            + "v0wLUrSvYpuRtrdxi5G3pYJ7kbirkTuTteoyha5O675LSrsOmfupvO7Se5m9BuHrqL4F8Zuvv0HBSzBBAvMDMKIHI1xww8osTE/CkEEcscMWN0Qx"
            + "OxLfmbHGF3+cZsfibJyYyOqQ3KbJI4PM8pwqY1MAADs=";
    photoService.photoUpload(IMAGE_GIF_BASE64, BADGE_NUMBER);
    String IMAGE_PNG_BASE64_GOOD =
        "iVBORw0KGgoAAAANSUhEUgAAABgAAAAQCAYAAAAMJL+VAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAD"
            + "aklEQVQ4jT2Uy3JkRQxEjx5VdV9tt+1p20NAEDCw4h/4DD6Jv2RJBBHAMIwf3X1vVYlFm9lLmSllSjL8+kuMJZEssz2fyDHww8OPfH/4gL8W/vn9"
            + "M7//9gdLuUJIrK3y519/cZUrP//0yIeHHUNOnF6fcAt2cyFnMA3Or0d8LhkRgR6gwvH1xMdPn3jcb+xspquhPpDKTG9CbCDi5OQs48IwDIwl4xqY"
            + "VdyF3jeiN0op6JInEo4GZDXaVnl6euL1+YnaVpQgJUMkWE9HzsdnTIS76yuWeWTIBXcnF6d4QhGidVprqILrqWE9AFAraBEGM2I70zji1igeSH1l"
            + "fflEPZ3YDQMPdwsWnbadkVCIjmXDFNwS5gVaxbVCQlFVXJ15KZSeOb88s5VCVEHjCM1IcmKcjaurwv3NTMkCBL02TBsqgoggqqSUsOR46gk0UMBC"
            + "mccRq87Lv/8wDoYdhfXlM0ueOOwHDnc3zPPE/c3ILguDNugbomBm9DjTe6U10JTwLRqCUcwwc6Zpwqrx8e8nXp4/osfg/PrETRIeb97x4Zt7Sk7c"
            + "zBlvR1w7XTqmgbvRukE3VJ3j8Yyvc4beed1Wcq3UT0FqgHW27QXWlet94f1h5LuHPXcFxiTkdiQ4IdJIppgqvZ1REbyMiARDmfETGyoKArU2Tu1E"
            + "78LQg2mc2JWR27Rwv1/YDU6RjdwFo6OpIw7uFw8jhIhLYEARMTx6R4FkitZKX1eyJJZS2I8T76drHpdb9qkwaiap4nIxNyJQkTfwoNZGrfULyTQt"
            + "uHVIIhQxVIKkwnWeeJx33JUdh901h3mhhFLaRYgLIEqHy5ECvXdqrWzbdiFWBcAzirXAo5N7MKlzO4wc5oXH+YbbYWRQxWpHCVQdM4gIzBLihoig"
            + "KrhfknSpsUv0vYNuDYvGLJm7YeR+nLlJI++mmVEUbx2NhouiVDoBAiGK9A5vk6SUMDPcM2bGtm14buA9mHDezTu+3t/y1bxnZwlfK7RGRJCSk1wI"
            + "Gq13zAwRo78RXNRegAHaW5+X1ilh7Nw5zAvf3h64n64otfP5jz+J1rCS8OSYCL13QgURI6VE753+Rvj/Wlq7mA3gC871OPGwu+L91S1TGP3pyHpe"
            + "6acVqRVVY/BEGQbWtoII4zhRu+CevxgdEZcnJ07JiVor/wFTTXuxHEJ3MgAAAABJRU5ErkJggg==";
    photoService.photoUpload(IMAGE_PNG_BASE64_GOOD, BADGE_NUMBER);
  }

  @Test
  public void getS3KeyNames() {
    S3KeyNames names = photoService.generateS3KeyNames(BADGE_NUMBER);
    Assert.assertTrue(names.getOriginalKeyName().startsWith("KKKKKK/artefacts/"));
    Assert.assertTrue(names.getOriginalKeyName().endsWith("-original.jpg"));
    Assert.assertTrue(names.getThumbnailKeyName().startsWith("KKKKKK/artefacts/"));
    Assert.assertTrue(names.getThumbnailKeyName().endsWith("-thumbnail.jpg"));
  }

  @Test(expected = InternalServerException.class)
  public void awsDown_putImage() {
    when(amazonS3Client.putObject(any())).thenThrow(AmazonServiceException.class);
    photoService.photoUpload(IMAGE_JPG_BASE64_GOOD, BADGE_NUMBER);
  }

  @Test(expected = InternalServerException.class)
  public void awsClientError_putImage() {
    when(amazonS3Client.putObject(any())).thenThrow(SdkClientException.class);
    photoService.photoUpload(IMAGE_JPG_BASE64_GOOD, BADGE_NUMBER);
  }

  @Test
  public void getUrl() throws MalformedURLException {
    when(amazonS3Client.generatePresignedUrl(any())).thenReturn(new URL("http://www.google.com/"));
    Assert.assertEquals("http://www.google.com/", photoService.generateSignedS3Url("abc"));
  }

  @Test(expected = InternalServerException.class)
  public void awsDown_getUrl() {
    when(amazonS3Client.generatePresignedUrl(any())).thenThrow(AmazonServiceException.class);
    photoService.generateSignedS3Url("abc");
  }

  @Test(expected = InternalServerException.class)
  public void awsClientError_getUrl() {
    when(amazonS3Client.generatePresignedUrl(any())).thenThrow(SdkClientException.class);
    photoService.generateSignedS3Url("abc");
  }

  @Test
  public void deletePhoto() {
    photoService.deletePhoto("BadgeNo", "imageKey");
    verify(amazonS3Client, times(1)).deleteObject(any(), Mockito.eq("imageKey"));
  }

  @Test(expected = NullPointerException.class)
  public void deletePhoto_whenNullImageKey_thenNullPointer() {
    photoService.deletePhoto("BadgeNo", null);
    verify(amazonS3Client, never()).deleteObject(any(), any());
  }
}

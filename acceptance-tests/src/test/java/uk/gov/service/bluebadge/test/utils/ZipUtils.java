package uk.gov.service.bluebadge.test.utils;

public class ZipUtils {

  public boolean isZip(byte[] bytes){
    return bytes[0] == 80 && bytes[1] == 75 && bytes[2] == 3 && bytes[3] == 4;
  }
}

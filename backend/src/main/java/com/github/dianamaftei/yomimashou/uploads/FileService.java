package com.github.dianamaftei.yomimashou.uploads;

import static com.google.common.io.ByteStreams.toByteArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

  @Value("${file.path}")
  private String filePath;

  private static final String DESTINATION_PATH = "uploads";

  public String upload(MultipartFile file, String prefix) throws IOException {
    File fileToUpload =
        File.createTempFile(prefix, getFileExtension(file.getOriginalFilename()),
            new File(filePath + File.separator + DESTINATION_PATH));

    assert fileToUpload.createNewFile();
    try (FileOutputStream fos = new FileOutputStream(fileToUpload)) {
      fos.write(file.getBytes());
    }
    return fileToUpload.getName();
  }

  private String getFileExtension(String originalFilename) {
    String[] split = originalFilename.split("\\.");
    if (split.length == 0) {
      throw new FileException("Invalid image name.");
    }
    String extension = split[split.length - 1];

    if (!extension.toLowerCase().endsWith("png") &&
        !extension.toLowerCase().endsWith("jpg") &&
        !extension.toLowerCase().endsWith("jpeg") &&
        !extension.toLowerCase().endsWith("bmp")) {
      throw new FileException(
          "Unsupported file format");
    }

    return "." + extension;
  }

  public void getFile(String fileName, HttpServletResponse response) {
    Path file = Paths.get(filePath, DESTINATION_PATH, fileName);

    response.setContentType("image/jpeg");

    try (InputStream in = Files.newInputStream(file, StandardOpenOption.READ)) {
      byte[] byteArray = toByteArray(in);
      response.getOutputStream().write(byteArray);
    } catch (Exception exception) {
      throw new FileException("Could not read file", exception);
    }
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }
}

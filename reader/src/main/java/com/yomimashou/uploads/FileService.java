package com.yomimashou.uploads;

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

  @Value("${path.uploads}")
  private String uploadsPath;

  public String upload(final MultipartFile file, final String prefix) throws IOException {
    final File fileToUpload = File.createTempFile(prefix,
        getFileExtension(file.getOriginalFilename()), new File(uploadsPath));

    assert fileToUpload.createNewFile();
    try (final FileOutputStream fos = new FileOutputStream(fileToUpload)) {
      fos.write(file.getBytes());
    }
    return fileToUpload.getName();
  }

  private String getFileExtension(final String originalFilename) {
    final String[] split = originalFilename.split("\\.");
    if (split.length == 0) {
      throw new FileException("Invalid image name.");
    }
    final String extension = split[split.length - 1];

    if (!extension.toLowerCase().endsWith("png") &&
        !extension.toLowerCase().endsWith("gif") &&
        !extension.toLowerCase().endsWith("jpg") &&
        !extension.toLowerCase().endsWith("jpeg") &&
        !extension.toLowerCase().endsWith("bmp")) {
      throw new FileException("Unsupported file format");
    }

    return "." + extension;
  }

  public void getFile(final String fileName, final HttpServletResponse response) {
    final Path file = Paths.get(uploadsPath, fileName);

    response.setContentType("image/jpeg");

    try (final InputStream in = Files.newInputStream(file, StandardOpenOption.READ)) {
      final byte[] byteArray = toByteArray(in);
      response.getOutputStream().write(byteArray);
    } catch (final Exception exception) {
      throw new FileException("Could not read file", exception);
    }
  }

  public String getUploadsPath() {
    return uploadsPath;
  }

  public void setUploadsPath(final String uploadsPath) {
    this.uploadsPath = uploadsPath;
  }
}

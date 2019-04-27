package com.github.dianamaftei.yomimashou.uploads;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileController {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

  private static final String PREFIX = "text";

  private final FileService fileService;

  @Autowired
  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping()
  public void add(@RequestParam("file") MultipartFile file) throws IOException {
    fileService.upload(file, PREFIX);
  }

  @GetMapping(value = "/{fileName}")
  public ResponseEntity getFile(@PathVariable String fileName, HttpServletResponse response) {

    try {
      fileService.getFile(fileName, response);
      return new ResponseEntity(HttpStatus.OK);
    } catch (Exception exception) {
      LOGGER.error("Error serving media file: " + fileName);
      return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
  }
}

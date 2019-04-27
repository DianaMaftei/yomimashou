package com.github.dianamaftei.yomimashou.text;

import com.github.dianamaftei.yomimashou.uploads.FileService;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/text")
public class TextController {

  private final TextParserService textParserService;
  private final TextService textService;
  private final FileService fileService;
  private final static String MEDIA_UPLOAD_PREFIX = "text_";

  @Autowired
  public TextController(TextParserService textParserService, TextService textService,
      FileService fileService) {
    this.textParserService = textParserService;
    this.textService = textService;
    this.fileService = fileService;
  }

  @PostMapping()
  public Text add(@RequestPart("text") Text text, @RequestPart("file") MultipartFile file)
      throws IOException {
    String imageName = fileService.upload(file, MEDIA_UPLOAD_PREFIX);
    text.setImageFileName(imageName);
    return this.textService.add(text);
  }

  @GetMapping()
  public List<Text> getAll() {
    List<Text> texts = this.textService.getAll();
    texts.forEach(text -> text.setContent(null));
    return texts;
  }

  @GetMapping("/{id}")
  public Text getById(@PathVariable Long id) {
    return this.textService.getById(id);
  }

  @PostMapping(value = "/parse/words")
  public Set<String> getWords(@RequestBody String text) {
    if (text != null && text.length() > 0) {
      return textParserService.parseWords(text);
    }
    return Collections.emptySet();
  }

  @PostMapping(value = "/parse/names")
  public Set<String> getNames(@RequestBody String text) {
    if (text != null && text.length() > 0) {
      return textParserService.parseNames(text);
    }
    return Collections.emptySet();
  }

}

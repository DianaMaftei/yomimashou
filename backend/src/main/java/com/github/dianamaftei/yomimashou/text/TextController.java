package com.github.dianamaftei.yomimashou.text;

import com.github.dianamaftei.appscommon.model.Text;
import com.github.dianamaftei.yomimashou.uploads.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/text")
public class TextController {

  private final TextParserService textParserService;
  private final TextService textService;
  private final FileService fileService;
  private static final String MEDIA_UPLOAD_PREFIX = "text_";

  @Autowired
  public TextController(TextParserService textParserService, TextService textService,
      FileService fileService) {
    this.textParserService = textParserService;
    this.textService = textService;
    this.fileService = fileService;
  }

  @PostMapping()
  public Text add(@RequestPart("text") Text text, @RequestPart(value = "file", required = false) MultipartFile file)
      throws IOException {
    if (file != null) {
      String imageName = fileService.upload(file, MEDIA_UPLOAD_PREFIX);
      text.setImageFileName(imageName);
    }

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
    Optional<Text> text = this.textService.getById(id);
    if(!text.isPresent()) {
      throw new RuntimeException("text not found");
    }
    return text.get();
  }

  @PostMapping(value = "/parse/words")
  public Set<String> getWords(@RequestBody String text) {
    return textParserService.parseWords(text);
  }

  @PostMapping(value = "/parse/names")
  public Set<String> getNames(@RequestBody String text) {
    return textParserService.parseNames(text);
  }
}

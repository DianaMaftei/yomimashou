package com.yomimashou.text;

import com.yomimashou.appscommon.model.Text;
import com.yomimashou.uploads.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/text")
public class TextController {

    private final TextService textService;
    private final FileService fileService;
    private static final String MEDIA_UPLOAD_PREFIX = "text_";

    @Autowired
    public TextController(TextService textService, FileService fileService) {
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
        if (!text.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Missing text");
        }
        return text.get();
    }

    @PostMapping(value = "/analyze")
    public AnalyzedText analyze(@RequestBody Text text) {
        if (CollectionUtils.isEmpty(text.getParsedWords()) && StringUtils.isBlank(text.getParsedKanji())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No words or kanji found to parse");
        }
        return textService.analyze(text);
    }

}

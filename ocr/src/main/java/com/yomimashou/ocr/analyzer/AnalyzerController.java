package com.yomimashou.ocr.analyzer;

import com.yomimashou.ocr.analyzer.textbox.TextBox;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ocr")
@CrossOrigin
public class AnalyzerController {

    private final AnalyzerService analyzerService;

    public AnalyzerController(AnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    @PostMapping("/full")
    public String analyzeFullImage(@RequestParam("file") List<MultipartFile> files) {
        return analyzerService.analyzeFullPages(files);
    }

    @PostMapping("/snippet")
    public String analyzeImageSnippet(@RequestPart(value = "file") MultipartFile file) {
        return analyzerService.analyzeImageSnippet(file);
    }

    @PostMapping("/manga")
    public List<TextBox> analyzeManga(@RequestPart(value = "file") MultipartFile file) {
        return analyzerService.analyzeManga(file);
    }

}

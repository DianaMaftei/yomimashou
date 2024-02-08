package com.yomimashou.ocr.analyzer;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyzerServiceTest {

    @Mock
    private Tesseract tesseract;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private AnalyzerService analyzerService;

    @Test
    void analyzeImageSnippet() throws IOException, TesseractException {
        File snippetNoFurigana = new ClassPathResource("data/snippetNoFurigana.jpg").getFile();
        BufferedImage bi = ImageIO.read(snippetNoFurigana);
        MultipartFile multipartFile = new MockMultipartFile("snippetNoFurigana.jpg",
                new FileInputStream(snippetNoFurigana));

        when(imageService.cleanImageAndRemoveFurigana(any(BufferedImage.class))).thenReturn(bi);
        when(tesseract.doOCR(any(BufferedImage.class))).thenReturn("ocr result");

        String ocrResult = analyzerService.analyzeImageSnippet(multipartFile);

        assertEquals("ocr result", ocrResult);
    }
}

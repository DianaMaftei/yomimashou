package com.yomimashou.ocr.analyzer;

import com.google.cloud.vision.v1.*;
import com.yomimashou.ocr.analyzer.exceptions.AnalyzerException;
import com.yomimashou.ocr.analyzer.textbox.Box;
import com.yomimashou.ocr.analyzer.textbox.TextBox;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AnalyzerService {

    private final ImageService imageService;
    private final Tesseract tesseract;
    private final CloudVisionTemplate cloudVisionTemplate;

    public AnalyzerService(ImageService imageService, Tesseract tesseract, CloudVisionTemplate cloudVisionTemplate) {
        this.imageService = imageService;
        this.tesseract = tesseract;
        this.cloudVisionTemplate = cloudVisionTemplate;
    }

    public String analyzeFullPages(List<MultipartFile> files) {
        StringBuilder textFromImages = new StringBuilder();

        List<BufferedImage> cleanImages = files.stream().map(file -> cleanImage(file, true)).collect(Collectors.toList());
        cleanImages.stream()
                .map(bufferedImage -> this.cloudVisionTemplate.extractTextFromImage(getResource(bufferedImage)))
                .forEach(textFromImages::append);

        return textFromImages.toString();
    }

    public String analyzeImageSnippet(MultipartFile file) {
        BufferedImage image = cleanImage(file, true);
        String text = null;
        try {
            text = tesseract.doOCR(image);
        } catch (TesseractException e) {
            throw new AnalyzerException("Unable to do snippet image ocr", e);
        }

        return text;
    }

    public List<TextBox> analyzeManga(MultipartFile multipartFile) {
        BufferedImage image = multipartFileToImage(multipartFile);

        AnnotateImageResponse response = this.cloudVisionTemplate.analyzeImage(
                getResource(image), Feature.Type.DOCUMENT_TEXT_DETECTION);

        TextAnnotation fullTextAnnotation = response.getFullTextAnnotation();

        return extractTextBoxes(fullTextAnnotation);
    }

    private List<TextBox> extractTextBoxes(TextAnnotation textAnnotation) {
        Page page = textAnnotation.getPages(0);
        List<Block> blocks = page.getBlocksList();

        List<TextBox> textBoxes = new ArrayList<>();

        blocks.forEach(block -> {
            Box box = extractBox(block);
            String text = extractText(block);
            textBoxes.add(new TextBox(text, box));
        });

        return textBoxes;
    }

    private String extractText(Block block) {
        StringBuilder text = new StringBuilder();
        List<Paragraph> paragraphs = block.getParagraphsList();
        paragraphs.forEach(paragraph -> {
            int lastYPos = paragraph.getBoundingBox().getVertices(0).getY();
            boolean newRow;
            List<Word> words = paragraph.getWordsList();
            for (Word word : words) {
                int wordPosY = word.getBoundingBox().getVertices(0).getY();
                newRow = wordPosY < lastYPos;
                if (newRow) {
                    text.append("\n");
                }
                List<Symbol> symbols = word.getSymbolsList();
                symbols.forEach(symbol -> text.append(symbol.getText()));
                lastYPos = wordPosY;
            }
        });

        return text.toString();
    }

    private Box extractBox(Block block) {
        List<Vertex> vertices = block.getBoundingBox().getVerticesList();
        Box box = new Box();
        box.setX(vertices.get(0).getX());
        box.setY(vertices.get(0).getY());
        box.setWidth(vertices.get(1).getX() - vertices.get(0).getX());
        box.setHeight(vertices.get(3).getY() - vertices.get(0).getY());
        return box;
    }

    private BufferedImage cleanImage(MultipartFile file, boolean removeFurigana) {
        BufferedImage image = multipartFileToImage(file);
        if (image.getColorModel() == null) {
            throw new AnalyzerException("Invalid file");
        }

        if (image.getColorModel().getPixelSize() > 1) {
            try {
                if (removeFurigana) {
                    image = imageService.cleanImageAndRemoveFurigana(image);
                } else {
                    image = imageService.cleanImage(image);
                }
            } catch (IOException e) {
                throw new AnalyzerException("Unable to clean image", e);
            }
        }
        return image;
    }

    private BufferedImage multipartFileToImage(MultipartFile multipartFile) {
        BufferedImage bufferedImage = null;
        try (InputStream is = new ByteArrayInputStream(multipartFile.getBytes())) {
            bufferedImage = ImageIO.read(is);
        } catch (IOException e) {
            throw new AnalyzerException("Could not get image from file", e);
        }
        return bufferedImage;
    }

    private Resource getResource(BufferedImage bufferedImage) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", os);
        } catch (IOException e) {
            throw new AnalyzerException("Unable to write image to stream", e);
        }
        return new ByteArrayResource(os.toByteArray());
    }

}

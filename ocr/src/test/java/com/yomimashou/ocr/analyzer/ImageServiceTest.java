package com.yomimashou.ocr.analyzer;

import net.sourceforge.lept4j.Pix;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImageServiceTest {
    private ImageService imageService = new ImageService();

    @Test
    void cleanImage() throws IOException {
        File snippetNoFurigana = new ClassPathResource("data/snippetNoFurigana.jpg").getFile();
        BufferedImage original = ImageIO.read(snippetNoFurigana);
        ColorModel originalColorModel = original.getColorModel();

        BufferedImage clean = imageService.cleanImage(original);
        ColorModel cleanColorModel = clean.getColorModel();

        // check that output image has been scaled
        assertEquals(Math.round(original.getHeight() * 3.5), clean.getHeight());
        assertEquals(Math.round(original.getWidth() * 3.5), clean.getWidth());

        // check that output image has been grayscaled and binarized
        assertEquals(24, originalColorModel.getPixelSize());
        assertEquals(1, cleanColorModel.getPixelSize());
    }
}

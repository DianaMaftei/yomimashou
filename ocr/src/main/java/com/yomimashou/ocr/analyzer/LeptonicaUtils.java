package com.yomimashou.ocr.analyzer;

import net.sourceforge.lept4j.Box;
import net.sourceforge.lept4j.Boxa;
import net.sourceforge.lept4j.Leptonica;
import net.sourceforge.lept4j.Pix;
import net.sourceforge.lept4j.util.LeptUtils;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.sourceforge.lept4j.ILeptonica.*;

//Collection of methods to try and localize text in manga
public class LeptonicaUtils {

    private Leptonica leptInstance = Leptonica.INSTANCE;

    private Pix getPixFromFile(String imageName) throws IOException {
        File file = new ClassPathResource("data/" + imageName).getFile();
        BufferedImage image = ImageIO.read(file);
        return LeptUtils.convertImageToPix(image);
    }

    public void writePixToFile(Pix pix, String imageName) throws IOException {
        BufferedImage bufferedImage = LeptUtils.convertPixToImage(pix);
        File outputfile = new File(new ClassPathResource(".").getFile() + "clean_" + imageName + ".png");
        ImageIO.write(bufferedImage, "png", outputfile);
    }

    private Boxa getConnectedComponentsFromBinarizedPix(Pix binarizedPix) {
        return leptInstance.pixConnCompBB(binarizedPix, 8);
    }

    private Boxa overlapConnectedComponents(Boxa boxa) {
        return leptInstance.boxaCombineOverlaps(boxa, null);
    }

    private Pix contrastAndInvert(Pix grayscale) {
        Pix contrasted = leptInstance.pixContrastTRC(null, grayscale, 0.5f);
        Pix edges = leptInstance.pixSobelEdgeFilter(contrasted, L_ALL_EDGES);
        Pix binary = leptInstance.pixThresholdToBinary(edges, 20);
        leptInstance.pixInvert(binary, binary);

        LeptUtils.dispose(contrasted);
        LeptUtils.dispose(edges);

        return binary;
    }

    private List<Box> filterBoxesByMinAndMaxSize(Pix pix, Boxa boxa) {
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < boxa.n; i++) {
            Box box = leptInstance.boxaGetBox(boxa, i, L_CLONE);
            if (box != null) {
                if (hasMinimumSizeRequired(pix, box) && hasMaximumSizeRequired(pix, box)) {
                    boxes.add(box);
                }
            }
        }

        return boxes;
    }

    private boolean hasMaximumSizeRequired(Pix pix, Box box) {
        return ((double) box.w / pix.w) * 100 < 10 && ((double) box.h / pix.w) * 100 < 10;
    }

    private boolean hasMinimumSizeRequired(Pix pix, Box box) {
        double areaOfImage = pix.h * pix.w;

        boolean hasNecessaryProportions = !(box.w * 5 < box.h) && !(box.h * 5 < box.w);
        boolean hasNecessaryArea = ((box.w * box.h) / areaOfImage) * 100 > 0.002;

        return hasNecessaryProportions && hasNecessaryArea;
    }

    private List<Box> extendMargins(List<Box> boxes) {
        Boxa boxa;
        boxa = listToBoxa(boxes);

        int maxSizeToIncrease = 10;

        boxes = new ArrayList<>();
        for (int i = 0; i < boxa.n; i++) {
            Box box = leptInstance.boxaGetBox(boxa, i, L_CLONE);
            int sizeToIncrease = Math.min(Math.min(box.h, box.w) / 2, maxSizeToIncrease);
            box = leptInstance.boxAdjustSides(box, box, -sizeToIncrease, sizeToIncrease, -sizeToIncrease, sizeToIncrease);
            boxes.add(box);
        }

        return boxes;
    }

    private List<Box> filterByMedian(String imageName, Pix pix, List<Box> boxes, List<Double> areas) throws IOException {
        double[] filteredAreas = areas.stream().filter(area -> area > 3 && area < 100).mapToDouble(Double::doubleValue).toArray();
        Arrays.sort(filteredAreas);

        int middle = filteredAreas.length / 2;
        double medianValue = 0;
        if (filteredAreas.length % 2 == 1)
            medianValue = filteredAreas[middle];
        else
            medianValue = (filteredAreas[middle - 1] + filteredAreas[middle]) / 2;

        double maxScale = medianValue * 4.0;
        double minScale = medianValue * 0.15;

        return boxes.stream().filter(box -> Math.sqrt(box.w * box.h) > minScale && Math.sqrt(box.w * box.h) < maxScale).collect(Collectors.toList());
    }

    private List<Box> filterOutBoxesWithBlackPixelsOnEdges(Pix pix, Boxa boxa) {
        List<Box> boxes;
        boxes = new ArrayList<>();

        for (int i = 0; i < boxa.n; i++) {
            Box box = leptInstance.boxaGetBox(boxa, i, L_CLONE);
            int startX = box.x - 1;
            int startY = box.y - 1;
            int endX = box.x + box.w;
            int endY = box.y + box.h;

            boolean marginContainsBlackPixel = false;

            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    if (y == box.y - 1 || y == box.y + box.h || x == box.x - 1 || x == box.x + box.w) {
                        IntBuffer intBuffer = IntBuffer.allocate(1);
                        leptInstance.pixGetPixel(pix, x, y, intBuffer);
                        if (intBuffer.get(0) == 1) { // 1 is black, 0 is white
                            marginContainsBlackPixel = true;
                            break;
                        }
                    }
                }
            }
            if (!marginContainsBlackPixel) {
                boxes.add(box);
            }
        }
        return boxes;
    }


    private void drawBoxes(String imageName, Pix pix, Boxa boxa, String attempt) throws IOException {
        Pix drawn = leptInstance.pixDrawBoxa(pix, boxa, 3, 0xff0000ff);
        writePixToFile(drawn, imageName);
    }

    private List<Box> boxaToList(Boxa boxa) {
        List<Box> boxes = new ArrayList<>();

        for (int i = 0; i < boxa.n; i++) {
            Box box = leptInstance.boxaGetBox(boxa, i, L_CLONE);
            if (box != null) {
                boxes.add(box);
            }
        }

        return boxes;
    }

    private Boxa listToBoxa(List<Box> boxes) {
        Boxa boxa = leptInstance.boxaCreate(boxes.size());
        for (Box box : boxes) {
            leptInstance.boxaAddBox(boxa, box, L_INSERT);
        }
        return boxa;
    }

}

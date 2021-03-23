package com.yomimashou.ocr.analyzer;

import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import net.sourceforge.lept4j.*;
import net.sourceforge.lept4j.util.LeptUtils;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.sourceforge.lept4j.ILeptonica.*;

@Component
public class ImageService {
    private Leptonica leptInstance;

    private LeptonicaUtils leptonicaUtils = new LeptonicaUtils();

    public ImageService() {
        leptInstance = Leptonica.INSTANCE;
    }

    public BufferedImage cleanImage(BufferedImage image) throws IOException {
        Pix originalPix = LeptUtils.convertImageToPix(image);

        Pix cleanedPix = performStandardImageCleaning(originalPix);

        BufferedImage cleanedImage = LeptUtils.convertPixToImage(cleanedPix);

        performResourceCleanup(originalPix, cleanedPix);

        return cleanedImage;
    }

    public BufferedImage cleanImageAndRemoveFurigana(BufferedImage image) throws IOException {
        Pix originalPix = LeptUtils.convertImageToPix(image);

        Pix cleanedPix = performStandardImageCleaning(originalPix);
        Pix noFuriganaPix = removeFurigana(cleanedPix);

        BufferedImage cleanedImage = LeptUtils.convertPixToImage(noFuriganaPix);

        performResourceCleanup(originalPix, cleanedPix, noFuriganaPix);

        return cleanedImage;
    }

    private Pix performStandardImageCleaning(Pix pix) {
        Pix pixGray = convertToGrayscale(pix);
        Pix pixScaled = scale(pixGray);
        Pix pixBlur = blur(pixScaled);
        Pix pixBinarized = binarize(pixBlur);

        performResourceCleanup(pixGray, pixScaled, pixBlur);

        return pixBinarized;
    }

    public Pix convertToGrayscale(Pix pix) {
        Pix pixGray = null;
        if (pix.d ==32) {
            pixGray = leptInstance.pixConvertRGBToGray(pix, 0.0f, 0.0f, 0.0f);
        } else if (pix.d == 24) {
            Pix pix32 = leptInstance.pixConvert24To32(pix);
            pixGray = leptInstance.pixConvertRGBToGray(pix32, 0.0f, 0.0f, 0.0f);
            LeptUtils.dispose(pix32);
        } else {
            pixGray = leptInstance.pixConvertTo8(pix, 0);
        }
        return pixGray;
    }

    private Pix scale(Pix pix) {
        return leptInstance.pixScaleGrayLI(pix, 3.5f, 3.5f);
    }

    private Pix blur(Pix pix) {
        return leptInstance.pixUnsharpMaskingGray(pix, 5, 2.5f);
    }

    private Pix binarize(Pix pix) {
        PointerByReference ppixb = new PointerByReference();

        leptInstance.pixOtsuAdaptiveThreshold(pix, 2000, 2000, 0, 0, 0.0f, null, ppixb);
        return new Pix(ppixb.getValue());
    }

    /**
     * Erase the furigana from a VERTICAL text block
     *
     * Get all the connected components and merge overlapping ones
     * Extend the lower margin of the boxes by their height and a half and then merge the overlapping ones again.
     * The aim is to have boxes that span the full height of a text line.
     * Check the average width of the boxes, furigana lines will have a smaller width than the regular lines,
     * usually halfwidth. Remove all small boxes from the image
     * @param pix
     * @return Pix
     */
    private Pix removeFurigana(Pix pix) {
        Boxa conectedComponents = leptInstance.pixConnCompBB(pix, 4);
        Boxa combinedOverlaps = leptInstance.boxaCombineOverlaps(conectedComponents, null);

        List<Integer> boxWidths = new ArrayList<>();

        List<Box> boxesWithIncreasedHeightList = new ArrayList<>();
        for (int i = 0; i < combinedOverlaps.n; i++) {
            Box box = leptInstance.boxaGetBox(combinedOverlaps, i, L_CLONE);
            boxWidths.add(box.w);
            Box adjustedBox = leptInstance.boxAdjustSides(null, box, 0, 0, 0, (int) (box.h * 1.5));
            LeptUtils.dispose(box);
            boxesWithIncreasedHeightList.add(adjustedBox);
        }

        double averageWidth = boxWidths.stream().mapToInt((x) -> x).summaryStatistics().getAverage();

        Boxa listToBoxa = listToBoxa(boxesWithIncreasedHeightList);
        Boxa boxesWithIncreasedHeightBoxa = leptInstance.boxaCombineOverlaps(listToBoxa, null);

        List<Box> boxesToRemoveList = new ArrayList<>();

        for (int i = 0; i < boxesWithIncreasedHeightBoxa.n; i++) {
            Box box = leptInstance.boxaGetBox(boxesWithIncreasedHeightBoxa, i, L_CLONE);
            if (box != null) {
                if (box.w < averageWidth) {
                    boxesToRemoveList.add(box);
                }
            }
        }

        Boxa boxesToRemoveBoxa = listToBoxa(boxesToRemoveList);
        Pix cleanedPix = leptInstance.pixSetBlackOrWhiteBoxa(pix, boxesToRemoveBoxa, L_SET_WHITE);

        performResourceCleanup(conectedComponents, combinedOverlaps, listToBoxa, boxesWithIncreasedHeightBoxa, boxesToRemoveBoxa);

        return cleanedPix;
    }

    private Boxa listToBoxa(List<Box> boxes) {
        Boxa boxa = leptInstance.boxaCreate(boxes.size());
        for (Box box : boxes) {
            leptInstance.boxaAddBox(boxa, box, L_INSERT);
        }
        return boxa;
    }

    private void performResourceCleanup(Structure... structures) {
        Arrays.stream(structures).forEach(LeptUtils::dispose);
    }
}

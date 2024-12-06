package bg.sofia.uni.fmi.mjt.imagekit;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LuminosityGrayscaleTest {

    private final ImageAlgorithm grayscale = new LuminosityGrayscale();
    private static final int MASK = 255;
    private static final int SHIFT_FOR_RED = 16;
    private static final int SHIFT_FOR_GREEN = 8;
    private static final double MULTIPLY_RED = 0.21;
    private static final double MULTIPLY_GREEN = 0.72;
    private static final double MULTIPLY_BLUE = 0.07;

    @Test
    void testProcessWithNullImage() {
        assertThrows(IllegalArgumentException.class, () -> grayscale.process(null),
                "The image can not be null!");
    }

    private int getGrayPixel(int rgb) {
        int red = (rgb >> SHIFT_FOR_RED) & MASK;
        int green = (rgb >> SHIFT_FOR_GREEN) & MASK;
        int blue = rgb & MASK;

        assertEquals(green, red, "Red and green must be equal!");
        assertEquals(green, blue, "Green and blue must be equal!");
        assertEquals(red, blue, "Red and blue must be equal!");

        return red;
    }

    @Test
    void testProcessWithValidImage() {
        BufferedImage bufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        bufferedImage.setRGB(0, 0, new Color(255, 0, 0).getRGB()); //set red color
        bufferedImage.setRGB(0, 1, new Color(0, 255, 0).getRGB()); //set green color
        bufferedImage.setRGB(1, 0, new Color(0, 0, 255).getRGB()); //set blue color
        bufferedImage.setRGB(1, 1, new Color(255, 255, 255).getRGB()); //set white color

        int redToGray = (int) (MULTIPLY_RED * 255 + MULTIPLY_GREEN * 0 + MULTIPLY_BLUE * 0);
        int greenToGray = (int) (MULTIPLY_RED * 0 + MULTIPLY_GREEN * 255 + MULTIPLY_BLUE * 0);
        int blueToGray = (int) (MULTIPLY_RED * 0 + MULTIPLY_GREEN * 0 + MULTIPLY_BLUE * 255);
        int whiteToGray = (int) (MULTIPLY_RED * 255 + MULTIPLY_GREEN * 255 + MULTIPLY_BLUE * 255);

        BufferedImage processedImage = grayscale.process(bufferedImage);

        int processedRed = getGrayPixel(processedImage.getRGB(0, 0));
        int processedGreen = getGrayPixel(processedImage.getRGB(0, 1));
        int processedBlue = getGrayPixel(processedImage.getRGB(1, 0));
        int processedWhite = getGrayPixel(processedImage.getRGB(1,1));

        assertEquals(redToGray, processedRed, "Red is not correctly converted");
        assertEquals(greenToGray, processedGreen, "Green is not correctly converted");
        assertEquals(blueToGray, processedBlue, "Blue is not correctly converted");
        assertEquals(whiteToGray, processedWhite, "White is not correctly converted");
    }
}

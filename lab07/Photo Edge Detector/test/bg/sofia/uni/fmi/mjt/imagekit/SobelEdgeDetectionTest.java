package bg.sofia.uni.fmi.mjt.imagekit;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection.SobelEdgeDetection;
import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SobelEdgeDetectionTest {

    private BufferedImage image;
    private static final int MASK = 255;
    private final ImageAlgorithm sobelAlg = new SobelEdgeDetection(new LuminosityGrayscale());

    @BeforeEach
    void setUp() {
        image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);

        image.setRGB(0, 0, Color.BLACK.getRGB());
        image.setRGB(0, 1, Color.BLACK.getRGB());
        image.setRGB(0, 2, Color.BLACK.getRGB());

        image.setRGB(1, 0, Color.WHITE.getRGB());
        image.setRGB(1, 1, Color.WHITE.getRGB());
        image.setRGB(1, 2, Color.WHITE.getRGB());

        image.setRGB(2, 0, Color.WHITE.getRGB());
        image.setRGB(2, 1, Color.WHITE.getRGB());
        image.setRGB(2, 2, Color.WHITE.getRGB());
    }

    @Test
    void testProcessWithValidImage() {
        BufferedImage processedImage = sobelAlg.process(image);

        int expectedEdgeDetection = processedImage.getRGB(1, 1) & MASK;
        int firstRow = processedImage.getRGB(1, 0) & MASK;
        int lastRow = processedImage.getRGB(1, 2) & MASK;

        assertEquals(255, expectedEdgeDetection, "An edge should be detected!");
        assertEquals(0, firstRow, "An edge should not be detected!");
        assertEquals(0, lastRow, "An edge should not be detected!");
    }

    @Test
    void testProcessWithNullImage() {
        assertThrows(IllegalArgumentException.class, () -> sobelAlg.process(null),
                "IllegalArgumentException must be thrown when a null image is passed!");
    }
}

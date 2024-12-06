package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.image.BufferedImage;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {

    ImageAlgorithm grayScaleAlg;

    private static final int[][] GX = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    private static final int[][] GY = {
            {-1, -2, -1},
            { 0,  0,  0},
            { 1,  2,  1}
    };

    private static final int MASK = 255;
    private static final int SHIFT_FOR_GREEN = 8;
    private static final int SHIFT_FOR_RED = 16;

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        grayScaleAlg = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayscaleImage = grayScaleAlg.process(image);
        BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int gx = 0;
                int gy = 0;

                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int pixel = grayscaleImage.getRGB(x + j, y + i) & MASK;
                        gx += GX[i + 1][j + 1] * pixel;
                        gy += GY[i + 1][j + 1] * pixel;
                    }
                }

                int magnitude = (int) Math.min(MASK, Math.sqrt(gx * gx + gy * gy));
                int newColor = (magnitude << SHIFT_FOR_RED) | (magnitude << SHIFT_FOR_GREEN) | magnitude;
                edgeImage.setRGB(x, y, newColor);
            }
        }
        return edgeImage;
    }
}

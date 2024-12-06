package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.image.BufferedImage;

public class LuminosityGrayscale implements GrayscaleAlgorithm {

    private static final int MASK = 255;
    private static final int SHIFT_FOR_GREEN = 8;
    private static final int SHIFT_FOR_RED = 16;
    private static final double MULTIPLY_RED = 0.21;
    private static final double MULTIPLY_GREEN = 0.72;
    private static final double MULTIPLY_BLUE = 0.07;

    public LuminosityGrayscale() {

    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("The image cannot be null!");
        }
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                int red = (rgb >> SHIFT_FOR_RED) & MASK;
                int green = (rgb >> SHIFT_FOR_GREEN) & MASK;
                int blue = rgb & MASK;

                int luminosity = (int) (MULTIPLY_RED * red + MULTIPLY_GREEN * green + MULTIPLY_BLUE * blue);
                int grayRgb = (luminosity << SHIFT_FOR_RED) | (luminosity << SHIFT_FOR_GREEN) | luminosity;
                resultImage.setRGB(i, j, grayRgb);
            }
        }
        return resultImage;
    }
}

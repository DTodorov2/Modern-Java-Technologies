package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;

public class LocalFileSystemImageManager implements FileSystemImageManager {

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        }
        return fileName.substring(lastIndexOfDot + 1).toUpperCase();
    }

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        if (imageFile == null) {
            throw new IllegalArgumentException("The file can not be null!");
        }

        if (!imageFile.exists() || !imageFile.isFile()) {
            throw new IOException("The file does not exist!");
        }

        String fileExtension = getFileExtension(imageFile.getName());
        if (!SupportedFileFormats.contains(fileExtension)) {
            throw new IOException("Not supported file extension!");
        }

        BufferedImage image = ImageIO.read(imageFile);
        if (image == null) {
            throw new IOException("Can not read file!");
        }

        return image;
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        if (imagesDirectory == null) {
            throw new IllegalArgumentException("he directory can not be null!");
        }

        if (!imagesDirectory.exists() || !imagesDirectory.isDirectory()) {
            throw new IOException("Not valid directory!");
        }

        File[] imageArr = imagesDirectory.listFiles();
        if (imageArr == null) {
            throw new IOException("The directory is empty!");
        }

        List<BufferedImage> imageList = new LinkedList<>();
        for (File file : imageArr) {
            String fileExtension = getFileExtension(file.getName());
            if (!SupportedFileFormats.contains(fileExtension)) {
                throw new IOException("The directory contains files with invalid extension");
            }
            BufferedImage currentBufferedImage = ImageIO.read(file);
            imageList.add(currentBufferedImage);
        }

        return imageList;
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("The image can not be null!");
        }
        if (imageFile == null) {
            throw new IllegalArgumentException("The file can not be null!");
        }
        if (imageFile.exists()) {
            throw new IOException("The file already exists!");
        }
        File parentDir = imageFile.getParentFile();
        if (!parentDir.exists()) {
            throw new IOException("The parent directory does not exist!");
        }

        String fileExtension = getFileExtension(imageFile.getName());

        if (!ImageIO.write(image, fileExtension, imageFile)) {
            throw new IOException("The file can not be saved!");
        }
    }
}

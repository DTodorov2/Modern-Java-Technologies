package bg.sofia.uni.fmi.mjt.imagekit;

import bg.sofia.uni.fmi.mjt.imagekit.filesystem.FileSystemImageManager;
import bg.sofia.uni.fmi.mjt.imagekit.filesystem.LocalFileSystemImageManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LocalFileSystemImageManagerTest {

    private FileSystemImageManager fileSystem;
    private File tempDirectory;
    private File validImage1;
    private File validImage2;
    private final BufferedImage buffImage1 = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

    @BeforeEach
    void setUp() throws IOException {
        fileSystem = new LocalFileSystemImageManager();

        tempDirectory = new File("test/bg/sofia/uni/fmi/mjt/testDirectory");
        tempDirectory.mkdirs();

        validImage1 = new File(tempDirectory, "testImage1.png");
        ImageIO.write(buffImage1, "png", validImage1);

        validImage2 = new File(tempDirectory, "testImage2.png");
        ImageIO.write(buffImage1, "png", validImage2);
    }

    @AfterEach
    void tearDown() {
        File[] files = tempDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }

        tempDirectory.delete();

    }
    @Test
    void testLoadImageWithInvalidFile() {
        File invalidFile = new File("doesNotExist.png");
        assertThrows(IOException.class, () -> fileSystem.loadImage(invalidFile),
                "IOException must be thrown when non-existing file is passed as an argument!");
    }

    @Test
    void testLoadImageWithDirectory() {
        assertThrows(IOException.class, () -> fileSystem.loadImage(tempDirectory),
                "IOException must be thrown when a non-regular file is passed as an argument!");
    }

    @Test
    void testLoadImageWithUnsupportedFormat() {
        File unsupportedFormatFile = new File("notSupported.random");
        assertThrows(IOException.class, () -> fileSystem.loadImage(unsupportedFormatFile),
                "IOException must be thrown when a non-supported format is passed as an argument!");
    }

    @Test
    void testLoadImageWithNull() {
        assertThrows(IllegalArgumentException.class, () -> fileSystem.loadImage(null),
                "IllegalArgumentException must be thrown when a null file is passed as an argument!");
    }

    @Test
    void testLoadImageWithValidImage() throws IOException {
        BufferedImage image = fileSystem.loadImage(validImage2);
        assertNotNull(image, "The image must be loaded successfully!");
    }

    @Test
    void testLoadImagesFromDirectoryWithValidDirectory() throws IOException {
        assertEquals(2, fileSystem.loadImagesFromDirectory(tempDirectory).size(),
                "2 images must be loaded but are not!");
    }

    @Test
    void testLoadImagesFromDirectoryWithNonExistingDirectory() {
        File nonExistingDirectory = new File("nonExisingDirectory");
        assertThrows(IOException.class, () -> fileSystem.loadImagesFromDirectory(nonExistingDirectory),
                "IOException must be thrown when the directory does not exist!");
    }

    @Test
    void testLoadImagesFromDirectoryWithRegularFile() {
        assertThrows(IOException.class, () -> fileSystem.loadImagesFromDirectory(validImage1),
                "IOException must be thrown when a non-directory file is passed as an argument!");
    }

    @Test
    void testLoadImagesFromDirectoryContainingNonSupportedFormatFiles() throws IOException {
        File notSupporedFormat = new File(tempDirectory,"notSupportedFormat.random");
        ImageIO.write(buffImage1, "png", notSupporedFormat);
        assertThrows(IOException.class, () -> fileSystem.loadImagesFromDirectory(tempDirectory),
                "IOException must be thrown when a directory with non-supported format images is passed!");
    }

    @Test
    void testLoadImagesFromDirectoryWithNull() {
        assertThrows(IllegalArgumentException.class, () -> fileSystem.loadImagesFromDirectory(null),
                "IllegalArgumentException must be thrown when a null directory is passed!");
    }

    @Test
    void testSaveImageWithValidImage() throws IOException {
        File newFile = new File(tempDirectory,"validImage.png");
        fileSystem.saveImage(buffImage1, newFile);
        assertTrue(newFile.exists(), "The image must be saved successfully!");
    }

    @Test
    void testSaveImageWithNullImage() {
        assertThrows(IllegalArgumentException.class, () -> fileSystem.saveImage(null, validImage1),
                "IllegalArgumentException must be thrown when a null image is passed!");
    }

    @Test
    void testSaveImageWithNullFile() {
        assertThrows(IllegalArgumentException.class, () -> fileSystem.saveImage(buffImage1, null),
                "IllegalArgumentException must be thrown when a null file is passed!");
    }

    @Test
    void testSaveImageWithNullFileAndImage() {
        assertThrows(IllegalArgumentException.class, () -> fileSystem.saveImage(null, null),
                "IllegalArgumentException must be thrown when a null file and a null image are passed!");
    }

    @Test
    void testSaveImageWithExistingFile() {
        assertThrows(IOException.class, () -> fileSystem.saveImage(buffImage1, validImage1),
                "IllegalArgumentException must be thrown when an existing file is passed!");
    }

    @Test
    void testSaveImageWithNotExistingParent() {
        File notExistingParentFile = new File("NotExistingParent/testImage1.png");
        assertThrows(IOException.class, () -> fileSystem.saveImage(buffImage1, notExistingParentFile),
                "IllegalArgumentException must be thrown when a not-existing parent file is passed!");
    }
}

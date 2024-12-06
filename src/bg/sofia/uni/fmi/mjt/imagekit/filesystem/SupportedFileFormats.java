package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

public enum SupportedFileFormats {
    JPEG("JPEG"),
    PNG("PNG"),
    BMP("BMP");

    private final String format;

    SupportedFileFormats(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public static boolean contains(String format) {
        for (SupportedFileFormats fileFormat : SupportedFileFormats.values()) {
            if (fileFormat.getFormat().equals(format)) {
                return true;
            }
        }
        return false;
    }
}

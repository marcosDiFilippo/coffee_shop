package services;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileService {
    
    private static final String STORAGE_DIR = "src/storage/products/";

    public FileService() {
        try {
            Path path = Paths.get(STORAGE_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String saveProductImage(File originalFile) {
        if (originalFile == null || !originalFile.exists()) {
            return null;
        }

        try {
            BufferedImage image = ImageIO.read(originalFile);
            if (image == null) {
                return null;
            }

            String fileName = UUID.randomUUID().toString() + ".png";
            File outputFile = new File(STORAGE_DIR + fileName);
            
            ImageIO.write(image, "png", outputFile);
            
            return outputFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

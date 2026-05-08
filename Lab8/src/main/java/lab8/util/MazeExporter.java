package lab8.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/* HOMEWORK - Export maze to PNG */
public class MazeExporter {
    
    public static void exportToPng(Canvas canvas, File file) throws IOException {
        WritableImage writableImage = new WritableImage(
            (int) canvas.getWidth(), 
            (int) canvas.getHeight()
        );
        canvas.snapshot(null, writableImage);
        ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
    }
}

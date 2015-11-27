package torgen.utils;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.WritableImage;
import org.testfx.util.WaitForAsyncUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public interface FxImageComparison {
    /**
     * Asserts that the node under test produces the same snapshot than the reference one, using a tolerance thresold.
     *
     * @param referenceSnapshot The path of the reference snapshot (a png picture).
     * @param nodeUnderTest     The node under test.
     * @param tolerance         The tolerance threshold: the percentage (in [0;100]) of the pixels that can differ.
     * @throws IOException When cannot read/write snapshots.
     */
    default void assertSnapshotsEqual(final String referenceSnapshot, final Node nodeUnderTest,
                                      final double tolerance) throws IOException {
        WritableImage writableImage = new WritableImage((int) nodeUnderTest.getScene().getWidth(), (int) nodeUnderTest.getScene().getHeight());
        BufferedImage buffer1 = null;
        BufferedImage buffer2 = null;

        Platform.runLater(() -> nodeUnderTest.snapshot(null, writableImage));
        WaitForAsyncUtils.waitForFxEvents();

        try {
            buffer1 = ImageIO.read(new File(referenceSnapshot));
            buffer2 = SwingFXUtils.fromFXImage(writableImage, null);
            assertEquals("The two snapshots differ", 0d, computeSnapshotSimilarity(buffer2, buffer1), tolerance);
        } finally {
            if (buffer1 != null) {
                buffer1.flush();
            }
            if (buffer2 != null) {
                buffer2.flush();
            }
        }
    }

    /**
     * From https://stackoverflow.com/questions/7292208/image-comparison-in-java
     */
    default double computeSnapshotSimilarity(BufferedImage image1, BufferedImage image2) throws IOException {
        int totalNoOfPixels = 0;
        int image1PixelColor;
        int red;
        int blue;
        int green;
        int image2PixelColor;
        int red2;
        int blue2;
        int green2;
        float differenceRed;
        float differenceGreen;
        float differenceBlue;
        float differenceForThisPixel;
        double nonSimilarPixels = 0d;

        // A digital image is a rectangular grid of pixels, Dimensions with/Height = 1366/728 pixels.
        // Colours are usually expressed in terms of a combination of red, green and blue values.
        for (int row = 0, width=image1.getWidth(); row < width; row++) {
            for (int column = 0, height = image1.getHeight(); column < height; column++) {
                image1PixelColor = image1.getRGB(row, column);
                red = (image1PixelColor & 0x00ff0000) >> 16;
                green = (image1PixelColor & 0x0000ff00) >> 8;
                blue = image1PixelColor & 0x000000ff;

                image2PixelColor = image2.getRGB(row, column);
                red2 = (image2PixelColor & 0x00ff0000) >> 16;
                green2 = (image2PixelColor & 0x0000ff00) >> 8;
                blue2 = image2PixelColor & 0x000000ff;

                if (red != red2 || green != green2 || blue != blue2) {
                    differenceRed = red - red2 / 255f;
                    differenceGreen = (green - green2) / 255f;
                    differenceBlue = (blue - blue2) / 255f;
                    differenceForThisPixel = (differenceRed + differenceGreen + differenceBlue) / 3f;
                    nonSimilarPixels += differenceForThisPixel;
                }
                totalNoOfPixels++;

                if (image1PixelColor != image2PixelColor) {
                    image2.setRGB(row, column, Color.GREEN.getGreen());
                }
            }
        }

        return nonSimilarPixels / totalNoOfPixels;
    }
}

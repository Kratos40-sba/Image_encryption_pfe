import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtils {
    public static List<int[][]> decompose(File file) throws IOException {
        int[][] SubPic;
        List<int[][]> Blocks = new ArrayList<>();
        BufferedImage bufferedImage = ImageIO.read(file);
        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();
        for (int i = 0; i < h; i = i + 8) {
            for (int j = 0; j < w; j = j + 8) {
                SubPic = new int[8][8];
                // Sub-matrix construction
                for (int x = j, sx = 0; x < (j + 8); x++, sx++) {
                    for (int y = i, sy = 0; y < (i + 8); y++, sy++) {
                        int temp = bufferedImage.getRGB(x, y);
                        Color c = new Color(temp);
                        SubPic[sy][sx] = Math.floorMod((byte) c.getRed(), 256);
                    }
                }
                Blocks.add(SubPic);
            }
        }
        return Blocks;
    }

    public static void compose(List<int[][]> blocks, File file, int width, int height) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        int s = 0;
        for (int i = 0; i < width; i = i + 8) {
            for (int j = 0; j < height; j = j + 8) {
                for (int x = j, sx = 0; x < (j + 8); x++, sx++) {
                    for (int y = i, sy = 0; y < (i + 8); y++, sy++) {
                        Color color = new Color(blocks.get(s)[sy][sx], blocks.get(s)[sy][sx], blocks.get(s)[sy][sx], 255);
                        bufferedImage.setRGB(x, y, color.getRGB());
                    }
                }
                s++;
            }
        }
        ImageIO.write(bufferedImage, "BMP", file);
    }

    public static int[][] sub_image_to_matrix(File file) throws IOException {
        int[][] matrix = new int[8][8];
        BufferedImage image = ImageIO.read(file);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int temp = image.getRGB(i, j);
                Color color = new Color(temp);
                matrix[i][j] = color.getRed();
            }
        }
        return matrix;
    }

}

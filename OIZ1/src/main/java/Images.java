import java.awt.*;
import java.io.File;

import static java.util.Arrays.sort;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;

class Images {

    public static final String FORMAT_NAME = "jpeg";
    public static final String PATH = "/home/galayy/IdeaProjects/OIZ/OIZ1/src/main/resources/";

    public static void main(String[] a) throws Throwable {
        var file = new File(PATH + "in.jpeg");
        var pixel = new Color[9];

        var R = new int[9];
        var B = new int[9];
        var G = new int[9];
        var output = new File(PATH + "FINAL.jpeg");
        var img = read(file);
        for (var i = 1; i < img.getWidth() - 1; i++)
            for (var j = 1; j < img.getHeight() - 1; j++) {
                pixel[0] = new Color(img.getRGB(i - 1, j - 1));
                pixel[1] = new Color(img.getRGB(i - 1, j));
                pixel[2] = new Color(img.getRGB(i - 1, j + 1));
                pixel[3] = new Color(img.getRGB(i, j + 1));
                pixel[4] = new Color(img.getRGB(i + 1, j + 1));
                pixel[5] = new Color(img.getRGB(i + 1, j));
                pixel[6] = new Color(img.getRGB(i + 1, j - 1));
                pixel[7] = new Color(img.getRGB(i, j - 1));
                pixel[8] = new Color(img.getRGB(i, j));

                for (var k = 0; k < 9; k++) {
                    R[k] = pixel[k].getRed();
                    B[k] = pixel[k].getBlue();
                    G[k] = pixel[k].getGreen();
                }

                sort(R);
                sort(G);
                sort(B);
                img.setRGB(i, j, new Color(R[4], B[4], G[4]).getRGB());
            }
        write(img, FORMAT_NAME, output);
    }

}

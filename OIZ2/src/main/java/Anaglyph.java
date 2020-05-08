import org.ejml.simple.SimpleMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_USHORT_555_RGB;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;

public class Anaglyph {

    public static final String FORMAT_NAME = "jpg";
    public static final String PATH = "/home/galayy/IdeaProjects/OIZ/OIZ2/src/main/resources/";

    private static final double[][][] COLOR_MATRIX = {{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 1, 0}, {0, 0, 1}}};

    private static final double[][][] HALF_COLOR_MATRIX = {{{0.229, 0.587, 0.114}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 1, 0}, {0, 0, 1}}};

    private static final double[][][] OPTIMIZED_MATRIX = {{{0, 0.45, 1.05}, {0, 0, 0}, {0, 0, 0}},
            {{0, 0, 0}, {0, 1, 0}, {0, 0, 1}}};

    public static void main(String[] args) throws IOException {
        var left = new File(PATH + "left.jpg");
        var right = new File(PATH + "right.jpg");

        var leftImage = read(left);
        var rightImage = read(right);
        var output = new File(PATH + "FINAL.jpg");
        var resultImage = new BufferedImage(leftImage.getWidth(), leftImage.getHeight(), TYPE_USHORT_555_RGB);

        for (int i = 0; i < leftImage.getWidth(); i++) {
            for (int j = 0; j < leftImage.getHeight(); j++) {
                var leftImageRGB = new Color(leftImage.getRGB(i, j));
                var leftArrayRGB = new double[]{leftImageRGB.getRed(), leftImageRGB.getGreen(), leftImageRGB.getBlue()};

                var rightImageRGB = new Color(rightImage.getRGB(i, j));
                var rightArrayRGB = new double[]{rightImageRGB.getRed(), rightImageRGB.getGreen(), rightImageRGB.getBlue()};

                var leftRGB = new SimpleMatrix(1, leftArrayRGB.length);
                leftRGB.setRow(0, 0, leftArrayRGB);
                var rightRGB = new SimpleMatrix(1, rightArrayRGB.length);
                rightRGB.setRow(0, 0, rightArrayRGB);

                var newRGB = createMatrixFromVector(createNewRGB(OPTIMIZED_MATRIX[0], leftArrayRGB)).plus(
                        createMatrixFromVector(createNewRGB(OPTIMIZED_MATRIX[1], rightArrayRGB)));

                resultImage.setRGB(i, j, new Color((int) newRGB.get(0, 0), (int) newRGB.get(0, 1),
                        (int) newRGB.get(0, 2)).getRGB());
            }
        }
        write(resultImage, FORMAT_NAME, output);
    }

    private static double[] createNewRGB(final double[][] predefinedMatrix, final double[] imageVectorRGB) {
        var R = createMatrixFromVector(predefinedMatrix[0]).dot(createMatrixFromVector(imageVectorRGB));
        var G = createMatrixFromVector(predefinedMatrix[1]).dot(createMatrixFromVector(imageVectorRGB));
        var B = createMatrixFromVector(predefinedMatrix[2]).dot(createMatrixFromVector(imageVectorRGB));

        var vectorRGB = new double[3];
        vectorRGB[0] = R > 255 ? 255 : R;
        vectorRGB[1] = G > 255 ? 255 : G;
        vectorRGB[2] = B > 255 ? 255 : B;
        return vectorRGB;
    }

    private static SimpleMatrix createMatrixFromVector(final double[] vector) {
        var matrix = new SimpleMatrix(1, vector.length);
        matrix.setRow(0, 0, vector);
        return matrix;
    }

}

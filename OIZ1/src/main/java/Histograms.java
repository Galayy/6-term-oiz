import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;

public class Histograms extends JFrame {

    public Histograms(final String in, final String out) {
        super("..:: Histogram Equalization ::..");
        try {
            this.setLayout(new GridLayout(2, 2, 10, 10));

            var img1 = new JPanel();
            var img2 = new JPanel();

            var f1 = new File(in);
            var f2 = new File(out);

            var image1 = getGrayscaleImage(read(f1));
            img1.add(new JLabel(new ImageIcon(image1)));

            var image2 = equalize(image1);
            write(image2, Images.FORMAT_NAME, f2);
            img2.add(new JLabel(new ImageIcon(image2)));
            this.add(img1);
            this.add(img2);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private BufferedImage equalize(final BufferedImage src) {
        var nImg = new BufferedImage(src.getWidth(), src.getHeight(), TYPE_BYTE_GRAY);
        var wr = src.getRaster();
        var er = nImg.getRaster();
        var totpix = wr.getWidth() * wr.getHeight();
        var histogram = new int[256];

        for (var x = 0; x < wr.getWidth(); x++) {
            for (var y = 0; y < wr.getHeight(); y++) {
                histogram[wr.getSample(x, y, 0)]++;
            }
        }

        var chistogram = new int[256];
        chistogram[0] = histogram[0];
        for (var i = 1; i < 256; i++) {
            chistogram[i] = chistogram[i - 1] + histogram[i];
        }

        var arr = new float[256];
        for (var i = 0; i < 256; i++) {
            arr[i] = (float) ((chistogram[i] * 255.0) / (float) totpix);
        }

        for (var x = 0; x < wr.getWidth(); x++) {
            for (var y = 0; y < wr.getHeight(); y++) {
                var nVal = (int) arr[wr.getSample(x, y, 0)];
                er.setSample(x, y, 0, nVal);
            }
        }
        nImg.setData(er);
        return nImg;
    }

    private BufferedImage getGrayscaleImage(final BufferedImage src) {
        BufferedImage gImg = new BufferedImage(src.getWidth(), src.getHeight(), TYPE_BYTE_GRAY);
        WritableRaster wr = src.getRaster();
        WritableRaster gr = gImg.getRaster();
        for (int i = 0; i < wr.getWidth(); i++) {
            for (int j = 0; j < wr.getHeight(); j++) {
                gr.setSample(i, j, 0, wr.getSample(i, j, 0));
            }
        }
        gImg.setData(gr);
        return gImg;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java HistogramEqualization <input file name> <output file name>");
        } else {
            var he = new Histograms(Images.PATH + "in.jpeg", args[1]);
            he.setSize(1024, 500);
            he.setVisible(true);
            he.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

}

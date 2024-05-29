import java.awt.AWTException;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UsageDemo {

	public static void main(String[] args) throws IOException, KernelException, HeadlessException, AWTException {
		BufferedImage img = Core.getImage("D:\\main_drive\\AllData\\Uploads\\Important_Media\\Self.png");
		BufferedImage imgor = Core.deepCopy(img);
		img = Core.grayscl(img);
		img = Core.applyKernel(img, "GaussianLaplacian5", 1);
		img = Core.applyLuminanceFilter(img, 0.2);
		img = Core.deleteOrphans(img, 3, Color.GREEN, Color.black);
		img = Core.deleteOrphans(img, 4, Color.GREEN, Color.black);
		img = Core.deleteOrphans(img, 5, Color.GREEN, Color.black);
		img = Core.deleteOrphans(img, 6, Color.GREEN, Color.black);
		img = Core.deleteOrphans(img, 7, Color.GREEN, Color.black);
		img = Core.max(img, imgor);
		ImageDisplay.display(img);

		/*-
		img = Core.crop(img, 413, 380, 110, 185);
		img = Core.grayscl(img);
		img = Core.applyLuminanceFilter(img, lum -> {
			double a = 16;
			int d = (int) (Math.floor(lum * a) * 255 / a);
			Color col = new Color(d, d, d).brighter();
			return col.getRGB();
		});
		System.out.println(img.getWidth() + " " + img.getHeight());
		ImageDisplay.display(img);
		 * */

		/*-BufferedImage img = Core.getImage("E:\\Drive\\AllData\\Mine\\Others\\lion.jpg");
		img = Core.grayscl(img);
		BufferedImage map = Core.getImage("E:\\Drive\\AllData\\Mine\\Others\\SDXL\\sdxl-1.jpg");
		new ImageDisplay(Core.invert(Core.applyWeightMap(img,
				Core.getGrayscaleMap(Core.resize(Core.invert(Core.grayscl(map)), img.getWidth(), img.getHeight())))))
				.display();*/

		/*-BufferedImage img = Core.browseImage();
		for (int s = 100; s < 2000; s += 100) {
			BufferedImage newimg = Core.fitTo(img, s);
			// new ImageDisplay(img).display("Original image");
			BufferedImage edge = Core.grayscl(newimg);
			// new ImageDisplay(edge).display("Grayscale");
			edge = Core.applyKernel(edge, "GaussianLaplacian5", 1);
			// new ImageDisplay(edge).display("Gaussian Edge Detection");
			edge = Core.applyLuminanceFilter(edge, 0.25, Color.black, Color.white);
			// new ImageDisplay(edge).display("Luminance Filter");
			// edge = Core.deleteOrphans(edge, 10, Color.black, Color.white);
			new ImageDisplay(edge).display("Orphans Deleted " + s);
		}

		// img = Core.min(edge, img);
		// new ImageDisplay(img).display("Overlay");*/

	}

}

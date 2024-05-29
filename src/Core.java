import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Core {
	public static BufferedImage browseImage(String comment) throws IOException {
		FileDialog fd = new FileDialog((JFrame) null, comment, FileDialog.LOAD);
		fd.setVisible(true);
		String fileName = fd.getFile();
		System.out.println("Selected image: " + fileName + "\n");
		if (fileName == null) {
			System.err.println("No image selected.");
		}
		fd.dispose();
		BufferedImage img = ImageIO.read(new File(fd.getDirectory() + fileName));
		return img;
	}

	public static BufferedImage deepCopy(BufferedImage img) {
		BufferedImage b = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		Graphics2D g = b.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return b;
	}

	public static BufferedImage applyKernel(BufferedImage img, String kerName, int repeat) throws KernelException {
		return applyKernel(img, DefaultKernels.getKernel(kerName), repeat);
	}

	public static BufferedImage applyKernel(BufferedImage img, String... kerNames) throws KernelException {
		BufferedImage out = deepCopy(img);
		for (String s : kerNames) {
			out = Core.applyKernel(out, s, 1);
		}
		return out;
	}

	public static BufferedImage applyKernel(BufferedImage img, Kernel kernel) throws KernelException {
		return applyKernel(img, kernel, 1);
	}

	public static BufferedImage applyKernel(BufferedImage img, Kernel kernel, int repeat) {
		System.out.println("Applying kernel \"" + kernel.name + "\" ...");
		BufferedImage out = img;
		for (int i = 0; i < repeat; i++) {
			out = convolute(out, kernel);
			System.out.println(" " + (i + 1) + " out of " + repeat);
		}
		System.out.println("Kernel applied\n-------------------------------");
		return out;
	}

	public static BufferedImage resize(BufferedImage img, int newWidth, int newHeight) {
		Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		BufferedImage out = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		out.createGraphics().drawImage(tmp, 0, 0, null);
		return out;
	}

	public static BufferedImage resize(BufferedImage img, double resizeRatio) {
		return resize(img, (int) (img.getWidth() * resizeRatio), (int) (img.getHeight() * resizeRatio));
	}

	public static BufferedImage fitTo(BufferedImage img, double width) {
		return Core.resize(img, width / Math.max(img.getWidth(), img.getHeight()));

	}

	public static BufferedImage avg(BufferedImage... imgs) {
		if (imgs.length == 0) {
			throw new Error("Empty image list.");
		}
		BufferedImage out = new BufferedImage(imgs[0].getWidth(), imgs[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < out.getWidth(); x++) {
			for (int y = 0; y < out.getHeight(); y++) {
				int[] rgb = { 0, 0, 0 };
				for (BufferedImage img : imgs) {
					Color col = new Color(img.getRGB(x, y));
					rgb[0] += col.getRed();
					rgb[1] += col.getGreen();
					rgb[2] += col.getBlue();
				}
				for (int i = 0; i < 3; i++) {
					rgb[i] /= imgs.length;
				}
				out.setRGB(x, y, new Color(rgb[0], rgb[1], rgb[2]).getRGB());
			}
		}
		return out;
	}

	public static BufferedImage channelwiseMax(BufferedImage... imgs) {
		if (imgs.length == 0) {
			throw new Error("Empty image list.");
		}
		BufferedImage out = new BufferedImage(imgs[0].getWidth(), imgs[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < out.getWidth(); x++) {
			for (int y = 0; y < out.getHeight(); y++) {
				int[] rgb = { 0, 0, 0 };
				for (BufferedImage img : imgs) {
					Color col = new Color(img.getRGB(x, y));
					rgb[0] = Math.max(col.getRed(), rgb[0]);
					rgb[1] = Math.max(col.getGreen(), rgb[1]);
					rgb[2] = Math.max(col.getBlue(), rgb[2]);
				}
				out.setRGB(x, y, new Color(rgb[0], rgb[1], rgb[2]).getRGB());
			}
		}
		return out;
	}

	public static BufferedImage max(BufferedImage... imgs) {
		if (imgs.length == 0) {
			throw new Error("Empty image list.");
		}
		BufferedImage out = new BufferedImage(imgs[0].getWidth(), imgs[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < out.getWidth(); x++) {
			for (int y = 0; y < out.getHeight(); y++) {
				double maxLum = Double.NEGATIVE_INFINITY;
				Color maxCol = null;
				for (BufferedImage img : imgs) {
					Color col = new Color(img.getRGB(x, y));
					double lum = calculateLuminance(col);
					if (lum > maxLum) {
						maxCol = col;
						maxLum = lum;
					}
				}
				out.setRGB(x, y, maxCol.getRGB());
			}
		}
		return out;
	}

	public static BufferedImage channelwiseMin(BufferedImage... imgs) {
		if (imgs.length == 0) {
			throw new Error("Empty image list.");
		}
		BufferedImage out = new BufferedImage(imgs[0].getWidth(), imgs[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < out.getWidth(); x++) {
			for (int y = 0; y < out.getHeight(); y++) {
				int[] rgb = { 255, 255, 255 };
				for (BufferedImage img : imgs) {
					Color col = new Color(img.getRGB(x, y));
					rgb[0] = Math.min(col.getRed(), rgb[0]);
					rgb[1] = Math.min(col.getGreen(), rgb[1]);
					rgb[2] = Math.min(col.getBlue(), rgb[2]);
				}
				out.setRGB(x, y, new Color(rgb[0], rgb[1], rgb[2]).getRGB());
			}
		}
		return out;
	}

	public static BufferedImage min(BufferedImage... imgs) {
		if (imgs.length == 0) {
			throw new Error("Empty image list.");
		}
		BufferedImage out = new BufferedImage(imgs[0].getWidth(), imgs[0].getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < out.getWidth(); x++) {
			for (int y = 0; y < out.getHeight(); y++) {
				double minLum = Double.POSITIVE_INFINITY;
				Color minCol = null;
				for (BufferedImage img : imgs) {
					Color col = new Color(img.getRGB(x, y));
					double lum = calculateLuminance(col);
					if (lum < minLum) {
						minCol = col;
						minLum = lum;
					}
				}
				out.setRGB(x, y, minCol.getRGB());
			}
		}
		return out;
	}

	public static BufferedImage grayscl(BufferedImage img) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < out.getWidth(); x++) {
			for (int y = 0; y < out.getHeight(); y++) {
				Color col = new Color(img.getRGB(x, y));
				int gr = (int) (calculateLuminance(col) * 255);
				Color gray = new Color(gr, gr, gr);
				out.setRGB(x, y, gray.getRGB());
			}
		}
		return out;
	}

	public static BufferedImage invert(BufferedImage img) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < out.getWidth(); x++) {
			for (int y = 0; y < out.getHeight(); y++) {
				int input = img.getRGB(x, y);
				out.setRGB(x, y, (0xFFFFFFFF - input) + (0xFF000000 & input));
			}
		}
		return out;
	}

	public static double[][] getGrayscaleMap(BufferedImage img) {
		double[][] out = new double[img.getWidth()][img.getHeight()];
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				Color col = new Color(img.getRGB(x, y));
				out[x][y] = calculateLuminance(col);
			}
		}
		return out;
	}

	public static BufferedImage applyWeightMap(BufferedImage img, double[][] weights) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < out.getWidth(); x++) {
			for (int y = 0; y < out.getHeight(); y++) {
				out.setRGB(x, y, getWeightedColor(img.getRGB(x, y), weights[x][y]).getRGB());
			}
		}
		return out;
	}

	public static BufferedImage erratic(BufferedImage img, double i) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < out.getWidth(); x++) {
			for (int y = 0; y < out.getHeight(); y++) {
				out.setRGB(x, y,
						img.getRGB((int) (Math.max(0, Math.min(x - i + Math.random() * (i / 2), out.getWidth()))),
								(int) (Math.max(0, Math.min(y - i + Math.random() * (i / 2), out.getHeight())))));
			}
		}
		return out;
	}

	public static Color getWeightedColor(int rgb, double weight) {
		return getWeightedColor(new Color(rgb), weight);
	}

	public static Color getWeightedColor(Color col, double weight) {
		return new Color((int) (col.getRed() * weight), (int) (col.getGreen() * weight),
				(int) (col.getBlue() * weight));
	}

	private static BufferedImage convolute(BufferedImage img, Kernel ker) {
		int n = ker.matrix.length;
		BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		double r = 0;
		double g = 0;
		double b = 0;
		Color col = null;
		for (int x = (n - 1) / 2; x < img.getWidth() - (n - 1) / 2; x++) {
			for (int y = (n - 1) / 2; y < img.getHeight() - (n - 1) / 2; y++) {
				r = 0;
				g = 0;
				b = 0;
				for (int i = -(n - 1) / 2; i < (n - 1) / 2 + 1; i++) {
					for (int j = -(n - 1) / 2; j < (n - 1) / 2 + 1; j++) {
						double kerVal = ker.matrix[i + (n - 1) / 2][j + (n - 1) / 2];
						if (kerVal != 0) {
							col = new Color(img.getRGB(x + i, y + j));
							r += col.getRed() * kerVal;
							g += col.getGreen() * kerVal;
							b += col.getBlue() * kerVal;
						}
					}
				}
				if (r > 255) {
					r = 255;
				}
				if (g > 255) {
					g = 255;
				}
				if (b > 255) {
					b = 255;
				}
				if (r < 0) {
					r = 0;
				}
				if (g < 0) {
					g = 0;
				}
				if (b < 0) {
					b = 0;
				}
				newImg.setRGB(x, y, new Color((int) r, (int) g, (int) b).getRGB());
			}
		}
		return newImg;
	}

	public static double calculateLuminance(int rgb) {
		return calculateLuminance(new Color(rgb));
	}

	public static double calculateLuminance(Color col) {
		return (0.2126 * col.getRed() + 0.7152 * col.getGreen() + 0.0722 * col.getBlue()) / 255;
	}

	public static BufferedImage applyLuminanceFilter(BufferedImage img, double minLuminance, Color col) {
		return applyLuminanceFilter(img, minLuminance, col, Color.black);
	}

	public static BufferedImage applyLuminanceFilter(BufferedImage img, double minLuminance) {
		return applyLuminanceFilter(img, minLuminance, Color.green, Color.black);
	}

	public static BufferedImage applyLuminanceFilter(BufferedImage img, LuminanceToRGB func) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				out.setRGB(x, y, func.run(calculateLuminance(img.getRGB(x, y))));
			}
		}
		return out;
	}

	public static BufferedImage applyLuminanceFilter(BufferedImage img, double minLuminance, Color highlightColor,
			Color backColor) {
		return applyLuminanceFilter(img, lum -> (lum > minLuminance ? highlightColor : backColor).getRGB());
	}

	public static BufferedImage crop(BufferedImage img, int x, int y, int w, int h) {
		return img.getSubimage(x, y, w, h);

	}

	/** Can only be used when image has 2 colors, fgColor and bgColor */
	public static BufferedImage deleteOrphans(BufferedImage img, int side, int thr, Color fgColor, Color bgColor) {
		side += 2;
		int ct = 0;
		BufferedImage out = Core.deepCopy(img);
		for (int x = 0; x <= img.getWidth() - side; x++) {
			for (int y = 0; y <= img.getHeight() - side; y++) {
				loop: {
					ct = 0;
					for (int i = 0; i < side; i++) {
						int val = (i == 0 || i == side - 1) ? 1 : (side - 1);
						for (int j = 0; j < side; j += val) {
							if (img.getRGB(x + i, y + j) == fgColor.getRGB()) {
								ct++;
							}
							if (ct >= thr) {
								break loop;
							}
						}
					}
					for (int i = 0; i < side; i++) {
						for (int j = 0; j < side; j++) {
							out.setRGB(x + i, y + j, bgColor.getRGB());
						}
					}
				}
			}
		}
		return out;
	}

	public static BufferedImage deleteOrphans(BufferedImage img, int side, Color fgColor, Color bgColor) {
		return deleteOrphans(img, side, 1, fgColor, bgColor);
	}

	public static BufferedImage browseImage() throws IOException {
		return browseImage("Choose a file");
	}

	public static BufferedImage getImage(String filePath) throws IOException {
		return ImageIO.read(new File(filePath));
	}

	// REMOVED (FAILED IN BETA)
	// Can only be used after applying luminance filter
	/*-public static BufferedImage removeDenseLocations(BufferedImage img, int s, double minDensity, Color fgColor,
			Color bgColor) {
		BufferedImage out = img.getSubimage(0, 0, img.getWidth(), img.getHeight());
		for (int x = 0; x <= img.getWidth() - s; x++) {
			for (int y = 0; y <= img.getHeight() - s; y++) {
				int num = 0;
				for (int i = 0; i < s; i++) {
					for (int j = 0; j < s; j++) {
						if (img.getRGB(x + i, y + j) == fgColor.getRGB()) {
							num += 1;
						}
					}
				}
				double density = (double) num / (double) (s * s);
				if (density > minDensity) {
					for (int i = 0; i < s; i++) {
						for (int j = 0; j < s; j++) {
							out.setRGB(x + i, y + j, bgColor.getRGB());
						}
					}
				}
	
			}
		}
		return out;
	}*/

}

interface LuminanceToRGB {

	int run(double y);
}
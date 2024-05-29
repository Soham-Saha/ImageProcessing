import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ImageDisplay {
	private BufferedImage disp;
	private JFrame frm;
	private int xnow;
	private int ynow;
	private int x;
	private int y;
	private double zoom;
	private JPanel pnl;
	private BufferedImage imgNow;

	public ImageDisplay(BufferedImage img) {
		this.disp = Core.resize(img, 500 / (double) Math.max(img.getHeight(), img.getWidth()));
		x = 0;
		y = 0;
		zoom = 1;
		pnl = new JPanel();
	}

	public void display(String frmName) {
		frm = new JFrame(frmName);
		frm.setSize(disp.getWidth() + 17, disp.getHeight() + 40);
		frm.setResizable(false);
		frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frm.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				double rot = e.getPreciseWheelRotation();
				if ((zoom > 1 && Math.signum(rot) == 1) || (zoom <= 5 && Math.signum(rot) == -1)) {
					zoom -= 0.05 * e.getPreciseWheelRotation();
					validateLocation();
					refresh();
				}
			}
		});
		frm.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				updateCurrentLocation(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				x -= (xnow - e.getX());
				y -= (ynow - e.getY());
				validateLocation();
				updateCurrentLocation(e);
				refresh();
			}

			private void updateCurrentLocation(MouseEvent e) {
				xnow = e.getX();
				ynow = e.getY();
			}
		});
		frm.setVisible(true);
		refresh();
	}

	public static ImageDisplay display(BufferedImage img) {
		ImageDisplay imgdisp = new ImageDisplay(img);
		imgdisp.display();
		return imgdisp;
	}

	protected void validateLocation() {
		if (zoom <= 1) {
			zoom = 1;
			imgNow = Core.resize(disp, zoom);
		}
		if (x < disp.getWidth() - imgNow.getWidth()) {
			x = disp.getWidth() - imgNow.getWidth();
		}
		if (y < disp.getHeight() - imgNow.getHeight()) {
			y = disp.getHeight() - imgNow.getHeight();
		}
		if (x > 0) {
			x = 0;
		}

		if (y > 0) {
			y = 0;
		}
	}

	@SuppressWarnings("serial")
	private void refresh() {
		frm.remove(pnl);
		pnl = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(Color.gray);
				g.fillRect(0, 0, frm.getWidth(), frm.getHeight());
				imgNow = Core.resize(disp, zoom);
				g.drawImage(imgNow, x, y, null);
			}
		};
		frm.add(pnl);
		frm.invalidate();
		frm.validate();
		frm.repaint();

	}

	public void display() {
		display("Displayed Image");

	}
}

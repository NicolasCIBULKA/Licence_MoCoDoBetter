package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * ShapePanel contains all the graphical objects to paint and manage their
 * update (for now).
 * 
 * @author Yann Barrachina
 *
 */
public class ShapePanel extends JPanel {
	private JPanel mainPanel;

	// TODO : create a new class, an object containing the shape and its color, and
	// maybe other things, for having everything in the final ShapePanel arrayList

	private ArrayList<Rectangle2D> al = new ArrayList<Rectangle2D>();

	public ShapePanel() {
		mainPanel = this;
		mainPanel.setDoubleBuffered(true);
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

	}

	public void paint(Graphics g) {
		super.paint(g);

		for (Rectangle2D shape : al) {
			Graphics2D g2d = (Graphics2D) g;

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			g2d.setColor(new Color(0, 0, 200));
			g2d.fill(shape);
		}
	}

	/**
	 * Adds a shape to display into the shapePanel. The ShapePanel contains the
	 * ArrayList of shapes to paint.
	 * 
	 * @param rectangle a Rectangle2D object
	 */
	public void addShape(Rectangle2D rectangle) {
		al.add(rectangle);
	}
}

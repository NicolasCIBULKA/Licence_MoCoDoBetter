package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Will surely become the main GUI class after many crash tests
 * 
 * @author Yann Barrachina
 *
 */
public class MoveShapeTest extends JFrame {
	private Rectangle2D.Float myRect = new Rectangle2D.Float(50, 50, 50, 50);
	private Rectangle2D.Float myRect2 = new Rectangle2D.Float(200, 200, 50, 50);
	private MovingAdapter ma = new MovingAdapter();

	private Rectangle2D.Float tempRect;

	private static final Dimension PANEL_SIZE = new Dimension(300, 600);

	private JFrame theFrame;
	private ShapePanel sp;
	private JPanel jpl = new JPanel();

	private JLabel jlaR1 = new JLabel("Rect1");
	private JLabel jlaR2 = new JLabel("Rect2");
	private JLabel jlaXR1 = new JLabel("X : 50");
	private JLabel jlaYR1 = new JLabel("Y : 50");
	private JLabel jlaXR2 = new JLabel("X : 200");
	private JLabel jlaYR2 = new JLabel("Y : 200");

	public MoveShapeTest() {
		theFrame = this;
		initLayout();
	}

	public void initLayout() {
		setTitle("Moving and Scaling");

		GridLayout gl = new GridLayout(2, 1);
		Container contentPane = getContentPane();
		contentPane.setLayout(gl);

		sp = new ShapePanel();
		sp.addShape(myRect);

		addMouseMotionListener(ma);
		addMouseListener(ma);
		addMouseWheelListener(new ScaleHandler());

		GridLayout glInfo = new GridLayout(3, 2);
		jpl.setLayout(glInfo);
		jpl.add(jlaR1);
		jpl.add(jlaR2);
		jpl.add(jlaXR1);
		jpl.add(jlaXR2);
		jpl.add(jlaYR1);
		jpl.add(jlaYR2);

		contentPane.add(sp);
		contentPane.add(jpl);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(PANEL_SIZE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Fait bouger le rectangle à partir des coordonnées du curseur
	 *
	 */
	class MovingAdapter extends MouseAdapter {

		private int x;

		private int y;

		public void mousePressed(MouseEvent e) {
			x = e.getX();
			y = e.getY();

			if (myRect2.getBounds2D().contains(x, y)) {
				tempRect = myRect2;
				jlaR1.setText("Rect1");
				jlaR2.setText(">>>Rect2<<<");

			} else if (myRect.getBounds2D().contains(x, y)) {
				tempRect = myRect;
				jlaR2.setText("Rect2");
				jlaR1.setText(">>>Rect1<<<");

			} else {
				tempRect = null;
			}
		}

		public void mouseDragged(MouseEvent e) {

			if (tempRect != null) {
				int dx = e.getX() - x;
				int dy = e.getY() - y;

				float newX = tempRect.x + dx;
				float newY = tempRect.y + dy;

				if (newX > 0 && newX < 250) {
					tempRect.x += dx;
				}
				if (newY > 0 && newY < 250) {
					tempRect.y += dy;
				}

				jlaXR1.setText("X : " + myRect.x);
				jlaYR1.setText("Y : " + myRect.y);
				jlaXR2.setText("X : " + myRect2.x);
				jlaYR2.setText("Y : " + myRect2.y);
				repaint();

				x += dx;
				y += dy;
			}

			/*
			 * // Le curseur est-il dans le rectangle ? if
			 * (myRect2.getBounds2D().contains(x, y)) {
			 * 
			 * // Modifier la position du rectangle en conséquence myRect2.x += dx;
			 * myRect2.y += dy; repaint();
			 * 
			 * } // Agir sur la position du second rectabgle en premier car il est au
			 * premier plan else if(myRect.getBounds2D().contains(x, y)){ // Modifier la
			 * position du rectangle en conséquence myRect.x += dx; myRect.y += dy;
			 * repaint(); }
			 */

		}

	}

	/**
	 * Modifie la taille du rectangle selon le mouvement de la molette
	 *
	 */
	class ScaleHandler implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e) {

			int x = e.getX();
			int y = e.getY();

			// Le défilement est-il de type unitaire ? (flèches clavier ou molette)
			if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {

				// Le curseur est-il dans le rectangle ?
				if (myRect.getBounds2D().contains(x, y)) {

					// Récupérer la quantité de rotation
					float amount = e.getWheelRotation() * 5f;

					// Modifier les dimension du rectangle en conséquence
					myRect.width += amount;
					myRect.height += amount;
					repaint();
				}
			}
		}
	}

	public static void main(String[] args) {
		new MoveShapeTest();

	}
}

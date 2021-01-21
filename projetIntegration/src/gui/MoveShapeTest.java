package gui;

import java.awt.Color;
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


public class MoveShapeTest extends JPanel {
	private Rectangle2D.Float myRect = new Rectangle2D.Float(50, 50, 50, 50);
	private Rectangle2D.Float myRect2 = new Rectangle2D.Float(200, 200, 50, 50);
	private MovingAdapter ma = new MovingAdapter();

	private Rectangle2D.Float tempRect;

	private static final Dimension PANEL_SIZE = new Dimension(300, 600);

	private static JPanel jpl = new JPanel();

	private JLabel jlaR1 = new JLabel("Rect1");
	private JLabel jlaR2 = new JLabel("Rect2");
	private JLabel jlaXR1 = new JLabel("X : 50");
	private JLabel jlaYR1 = new JLabel("Y : 50");
	private JLabel jlaXR2 = new JLabel("X : 200");
	private JLabel jlaYR2 = new JLabel("Y : 200");

	public MoveShapeTest() {
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

	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d.setColor(new Color(0, 0, 200));
		g2d.fill(myRect);

		Graphics2D g2d2 = (Graphics2D) g;

		g2d2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d2.setColor(new Color(100, 0, 100));
		g2d2.fill(myRect2);
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

			int dx = e.getX() - x;
			int dy = e.getY() - y;

			float newX = tempRect.x + dx;
			float newY = tempRect.y + dy;

			if (tempRect != null) {
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
			x += dx;
			y += dy;
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
		JFrame frame = new JFrame("Moving and Scaling");
		MoveShapeTest m = new MoveShapeTest();
		m.setDoubleBuffered(true);
		m.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		GridLayout gl = new GridLayout(2, 1);
		frame.setLayout(gl);
		frame.add(m);
		frame.add(jpl);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(PANEL_SIZE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
}



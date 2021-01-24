package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

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
	private Rectangle2D.Float myRect = new Rectangle2D.Float(50.0f, 50.0f, 50.0f, 50.0f);

	private Rectangle2D.Float myRect2 = new Rectangle2D.Float(200.0f, 200.0f, 50.0f, 50.0f);

	private ShapeGroup selectedComponent;

	private Line2D.Float line = new Line2D.Float(20.0f, 20.0f, 80.0f, 80.0f);

	private MovingAdapter ma = new MovingAdapter();

	private static final Dimension PANEL_SIZE = new Dimension(800, 600);

//	private JFrame theFrame;
	private ShapePanel sp;
	private JPanel jpl = new JPanel();

	private JLabel jlaR1 = new JLabel("Rect1");
	private JLabel jlaR2 = new JLabel("Rect2");
	private JLabel jlaXR1 = new JLabel("X : 50");
	private JLabel jlaYR1 = new JLabel("Y : 50");
	private JLabel jlaXR2 = new JLabel("X : 200");
	private JLabel jlaYR2 = new JLabel("Y : 200");

	public MoveShapeTest() {
//		theFrame = this;
		initLayout();
	}

	public void initLayout() {
		setTitle("Moving and Scaling");

		GridLayout gl = new GridLayout(2, 1);
		Container contentPane = getContentPane();
		contentPane.setLayout(gl);

		sp = new ShapePanel();
		
		// TODO : remove fourth parameter, name will be selected after, not here
		sp.addShape(50.0f, 50.0f, true, "Entity");
		sp.addShape(400.0f, 100.0f, false, "Association");

		addMouseMotionListener(ma);
		addMouseListener(ma);
//		addMouseWheelListener(new ScaleHandler());

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
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Moves shqpes from cursor coordinates
	 *
	 */
	class MovingAdapter extends MouseAdapter {

		private int x;

		private int y;

		public void mousePressed(MouseEvent e) {
			x = e.getX();
			y = e.getY();
			
			selectedComponent = null;
			
			int index = 0 ;
			for (ShapeGroup component : sp.getAlComponents()) {
				System.out.println("Comp " + index + " entity ? " + component.isAnEntity());
				index++ ;
				
				if (component.getMainShape().contains(x, y)) {

					selectedComponent = component;
					jlaR1.setText(">>>Rect1<<<");

				}
				if(selectedComponent == null){
					jlaR1.setText("Rect1");
				}
			}

		}

		public void mouseDragged(MouseEvent e) {

			if (selectedComponent != null) {

				int dx = e.getX() - x;
				int dy = e.getY() - y;

				float newX = selectedComponent.getX() + dx;
				float newY = selectedComponent.getY() + dy;

				// Checking if component is at the border of the frame
				Dimension currentPanelSize = new Dimension(sp.getSize());
				float componentXLimit = (float) currentPanelSize.getWidth() - selectedComponent.getWidth();
				float componentYLimit = (float) currentPanelSize.getHeight() - selectedComponent.getHeight();

				if (newX > 0 && newX < componentXLimit) {

					selectedComponent.setGroupAbscissa(newX);

				}
				if (newY > 0 && newY < componentYLimit) {
					selectedComponent.setGroupOrdinate(newY);

				}

				jlaXR1.setText("X : " + selectedComponent.getX());
				jlaYR1.setText("Y : " + selectedComponent.getY());
				jlaXR2.setText("X : " + myRect2.x);
				jlaYR2.setText("Y : " + myRect2.y);
				repaint();

				x += dx;
				y += dy;
			}

		}

	}

	// TODO : scaling global du schéma de conception
	/**
	 * Modifie la taille du rectangle selon le mouvement de la molette
	 *
	 */
//	class ScaleHandler implements MouseWheelListener {
//		public void mouseWheelMoved(MouseWheelEvent e) {
//
//			int x = e.getX();
//			int y = e.getY();
//
//			// Le défilement est-il de type unitaire ? (flèches clavier ou molette)
//			if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
//
//				// Le curseur est-il dans le rectangle ?
//				if (myRect.getBounds2D().contains(x, y)) {
//
//					// Récupérer la quantité de rotation
//					float amount = e.getWheelRotation() * 5f;
//
//					// Modifier les dimension du rectangle en conséquence
//					myRect.width += amount;
//					myRect.height += amount;
//					repaint();
//				}
//			}
//		}
//	}

	public static void main(String[] args) {
		new MoveShapeTest();

	}
}

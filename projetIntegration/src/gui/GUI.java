package gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * Will surely become the main GUI class after many crash tests
 * 
 * @author Yann Barrachina
 *
 */
public class GUI extends JFrame {
	private Rectangle2D.Float myRect2 = new Rectangle2D.Float(200.0f, 200.0f, 50.0f, 50.0f);

	private ShapeGroup selectedComponent;

	private MovingAdapter ma = new MovingAdapter();

	private static final Dimension PANEL_SIZE = new Dimension(1920, 1080);

//	private JFrame theFrame;
	private ShapePanel sp;
	private JPanel jpl = new JPanel();

	private JLabel jlaR1 = new JLabel("Rect1");
	private JLabel jlaR2 = new JLabel("Rect2");
	private JLabel jlaXR1 = new JLabel("X : 50");
	private JLabel jlaYR1 = new JLabel("Y : 50");
	private JLabel jlaXR2 = new JLabel("X : 200");
	private JLabel jlaYR2 = new JLabel("Y : 200");
	private JButton newEntityButton = new JButton("nouv. entité");
	private JButton newAssociationButton = new JButton("nouv. asso.");

	// menuBar objects
	private JMenuBar jmb = new JMenuBar();
	
	private JMenu options = new JMenu("Mocodo Better");
	private JMenuItem about = new JMenuItem("À propos");
	private JMenuItem quit = new JMenuItem("Quitter");
	
	private JMenu file = new JMenu("Fichier");
	private JMenuItem newFile = new JMenuItem("Nouveau...");
	private JMenuItem openFile = new JMenuItem("Ouvrir...");
	private JMenuItem exportFile = new JMenuItem("Exporter au format SQL...");
	
	private JMenu edit = new JMenu("Edition");
	private JMenuItem undo = new JMenuItem("Annuler");
	private JMenuItem redo = new JMenuItem("Rétablir");
	
	private JMenu window = new JMenu("Fenêtre");
	private JMenuItem minimize = new JMenuItem("Réduire");
	private JMenuItem fullScreen = new JMenuItem("Plein écran");
	private JMenu views = new JMenu("Vues");
	private JMenuItem mcdView = new JMenuItem("Schéma conceptuel");
	private JMenuItem mldView = new JMenuItem("Schéma relationnel");
	
	private JMenu help = new JMenu("Aide");
	private JMenuItem userGuide = new JMenuItem("Manuel de l'utilisateur");

	public GUI() {
//		theFrame = this;
		initLayout();
		initActions();
	}

	public void initLayout() {
		setTitle("Mocodo Better");

		GridLayout gl = new GridLayout(2, 1);
		Container contentPane = getContentPane();
		contentPane.setLayout(gl);

		sp = new ShapePanel();

		addMouseMotionListener(ma);
		addMouseListener(ma);
//		addMouseWheelListener(new ScaleHandler());

		GridLayout glInfo = new GridLayout(3, 2);
		jpl.setLayout(glInfo);
		jpl.add(jlaR1);
		jpl.add(jlaR2);
		jpl.add(jlaXR1);
		jpl.add(newEntityButton);
		jpl.add(jlaYR1);
		jpl.add(newAssociationButton);

		contentPane.add(sp);
		contentPane.add(jpl);
		
		options.add(about) ;
		options.addSeparator();
		options.add(quit) ;
		
		file.add(newFile);
		file.add(openFile);
		file.addSeparator();
		file.add(exportFile);
		
		edit.add(undo);
		edit.add(redo);
		
		window.add(minimize);
		window.add(fullScreen);
		window.addSeparator();
		views.add(mcdView);
		views.add(mldView);
		window.add(views);
		
		help.add(userGuide);
		
		jmb.add(options) ;
		jmb.add(file) ;
		jmb.add(edit) ;
		jmb.add(window) ;
		jmb.add(help) ;
		setJMenuBar(jmb);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(PANEL_SIZE);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void initActions(){
		// Menu actions
		quit.addActionListener(new QuitAction());
		minimize.addActionListener(new MinimizeAction());
		fullScreen.addActionListener(new FullScreenAction());
		
		// Buttons actions
		newEntityButton.addActionListener(new AddEntityAction());
		newAssociationButton.addActionListener(new AddAssociationAction());
		
	}

	/**
	 * Moves shapes from cursor coordinates
	 *
	 */
	class MovingAdapter extends MouseAdapter {

		private int x;

		private int y;

		public void mousePressed(MouseEvent e) {
			x = e.getX();
			// TODO : y coordinate is offset about 23 to 25 pixels, may include the titlebar
			// of the frame
			y = e.getY() - 23;
			System.out.println("X : " + x + " Y : " + y);

			selectedComponent = null;

			for (ShapeGroup component : sp.getAlComponents()) {

				if (component.getMainShape().contains(x, y)) {
					System.out.println(" >> Table selected");
					selectedComponent = component;
					jlaR1.setText(">>>Rect1<<<");

				}
			}

			if (selectedComponent == null) {
				jlaR1.setText("Rect1");
//				Line2D.Float clickedLine = getClickedLine(x, y);
			}

		}

		public void mouseDragged(MouseEvent e) {

			if (selectedComponent != null) {

				int dx = e.getX() - x;
				int dy = e.getY() - y - 23;

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

		/**
		 * Returns the first line in the collection of lines that is close enough to
		 * where the user clicked, or null if no such line exists
		 *
		 */

		public Line2D.Float getClickedLine(int x, int y) {
//		int boxX = x - HIT_BOX_SIZE / 2;
//		int boxY = y - HIT_BOX_SIZE / 2 + 12;
			double boxX = x;
			double boxY = y;

			for (Line2D.Float line : sp.getAlLines()) {
				if (line.ptSegDist(boxX, boxY) < 10.0d) {
					System.out.println("--- " + line.x1 + " " + line.y1 + " " + line.x2 + " " + line.y2 + " ---");
					return line;
				}
			}
			return null;
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
	
	
	public class AddEntityAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			sp.addShapeGroup(50.0f, 50.0f, true);
	    }
	}

	
	public class AddAssociationAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			sp.addShapeGroup(50.0f, 50.0f, false);

	    }
	}
	
	public class MinimizeAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			setState(JFrame.ICONIFIED);
		}
	}
	
	public class FullScreenAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO : fullscreen
			
		}
	}
	
	public class QuitAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
    		System.exit(0) ;

	    }
	}

	
	public static void main(String[] args) {
		new GUI();

	}
}

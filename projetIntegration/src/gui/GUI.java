package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import data.Association;
import data.Entity;
import data.Node;
import process.MCDManaging;

/**
 * Will surely become the main GUI class after many crash tests
 * 
 * @author Yann Barrachina
 *
 */
public class GUI extends JFrame {
	
	private Rectangle2D.Float myRect2 = new Rectangle2D.Float(200.0f, 200.0f, 50.0f, 50.0f);
	
	private MCDManaging mcdManager = new MCDManaging();
	private ShapeGroup selectedComponent;
	private String cursorState = new String("selection");

	private MovingAdapter ma = new MovingAdapter();

	private static final Dimension PANEL_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private static final Dimension ICONPANEL_SIZE = new Dimension(PANEL_SIZE.width,60);
	private static final Dimension ICONBUTTON_SIZE = new Dimension(50,50);
	private static final int HEIGHT_DIFFERENCE = ICONPANEL_SIZE.height + 24;

//	private JFrame theFrame;
	private ShapePanel sp;
	private JPanel jpl = new JPanel();
	private JPanel iconPanel = new JPanel();

	private JLabel jlaR1 = new JLabel("Rect1");
//	private JLabel jlaR2 = new JLabel("Rect2");
	private JLabel jlaXR1 = new JLabel("X : 50");
	private JLabel jlaYR1 = new JLabel("Y : 50");
	private JLabel jlaXR2 = new JLabel("X : 200");
	private JLabel jlaYR2 = new JLabel("Y : 200");
	private JButton testButton = new JButton("test");
	private JButton selectionButton = new JButton("S");
	private JButton handButton = new JButton("H");
	private JButton newEntityButton = new JButton("+E");
	private JButton newAssociationButton = new JButton("+A");
	
	private Icon testIcon = new ImageIcon("");

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

		BorderLayout bl = new BorderLayout(3, 1);
		Container contentPane = getContentPane();
		contentPane.setLayout(bl);

		sp = new ShapePanel();

		addMouseMotionListener(ma);
		addMouseListener(ma);
//		addMouseWheelListener(new ScaleHandler());

		FlowLayout flIcon = new FlowLayout(FlowLayout.LEADING);
		iconPanel.setLayout(flIcon);
		
		iconPanel.setPreferredSize(ICONPANEL_SIZE);
		
		selectionButton.setPreferredSize(ICONBUTTON_SIZE);
		handButton.setPreferredSize(ICONBUTTON_SIZE);
		newEntityButton.setPreferredSize(ICONBUTTON_SIZE);
		newAssociationButton.setPreferredSize(ICONBUTTON_SIZE);
		selectionButton.setToolTipText("Selection");
		handButton.setToolTipText("Hand");
		newEntityButton.setToolTipText("New entity tool");
		newAssociationButton.setToolTipText("New association tool");
		iconPanel.add(selectionButton);
		iconPanel.add(handButton);
		iconPanel.add(newEntityButton);
		iconPanel.add(newAssociationButton);
		
		GridLayout glInfo = new GridLayout(3, 2);
		jpl.setLayout(glInfo);
		
		Dimension TEST_SIZE = new Dimension(PANEL_SIZE.width,200);
		jpl.setPreferredSize(TEST_SIZE);
		
		jpl.add(jlaR1);
		jpl.add(testButton);
		jpl.add(jlaXR1);
//		jpl.add(newEntityButton);
		jpl.add(jlaYR1);
//		jpl.add(newAssociationButton);

		contentPane.add(iconPanel, BorderLayout.NORTH);
		contentPane.add(sp, BorderLayout.CENTER);
		contentPane.add(jpl, BorderLayout.SOUTH);
		
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
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void initActions(){
		// Menu actions
		quit.addActionListener(new QuitAction());
		minimize.addActionListener(new MinimizeAction());
		fullScreen.addActionListener(new FullScreenAction());
		
		// Buttons actions
		selectionButton.addActionListener(new SelectionAction());
		handButton.addActionListener(new DragAction());
		newEntityButton.addActionListener(new AddEntityAction());
		newAssociationButton.addActionListener(new AddAssociationAction());
		testButton.addActionListener(new testAction());
		
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
			y = e.getY() - HEIGHT_DIFFERENCE;
			System.out.println("X : " + x + " Y : " + y);

			selectedComponent = null;
			switch(cursorState) {
			case "entity" :
				Node newNodeEntity = new Entity("Entite",null);
				mcdManager.addNode(newNodeEntity);
				
				sp.getComponentMap().put(sp.addShapeGroup(x, y, true), newNodeEntity);
				break;
				
			case "association" :
				Node newNodeAssociation = new Association("Association",null,null);
				mcdManager.addNode(newNodeAssociation);
				
				sp.getComponentMap().put(sp.addShapeGroup(x, y, false), newNodeAssociation);
				break;
				
			default :
				for (ShapeGroup component : sp.getComponentMap().keySet()) {

					if (component.getMainShape().contains(x, y)) {
						System.out.println(" >> Table selected");
						selectedComponent = component;
						jlaR1.setText(">>>Rect1<<<");
						jlaXR1.setText("X : " + selectedComponent.getX());
						jlaYR1.setText("Y : " + selectedComponent.getY());
					}
				}	
			}

			if (selectedComponent == null) {
				jlaR1.setText("Rect1");
//				Line2D.Float clickedLine = getClickedLine(x, y);
			}

		}

		public void mouseDragged(MouseEvent e) {

			if (selectedComponent != null && cursorState.equals("hand")) {

				int dx = e.getX() - x;
				int dy = e.getY() - y - HEIGHT_DIFFERENCE;

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
	
	public class SelectionAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			cursorState = "selection";
			selectionButton.setEnabled(false);
			handButton.setEnabled(true);
			newEntityButton.setEnabled(true);
			newAssociationButton.setEnabled(true);
	    }
	}
	
	public class DragAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			cursorState = "hand";
			selectionButton.setEnabled(true);
			handButton.setEnabled(false);
			newEntityButton.setEnabled(true);
			newAssociationButton.setEnabled(true);
			
	    }
	}
	
	public class AddEntityAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			cursorState = "entity";
			selectionButton.setEnabled(true);
			handButton.setEnabled(true);
			newEntityButton.setEnabled(false);
			newAssociationButton.setEnabled(true);
			
	    }
	}

	
	public class AddAssociationAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			cursorState = "association";
			selectionButton.setEnabled(true);
			handButton.setEnabled(true);
			newEntityButton.setEnabled(true);
			newAssociationButton.setEnabled(false);

	    }
	}
	
	public class testAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			testButton.setPressedIcon(null);
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

package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import data.Association;
import data.Attribute;
import data.Cardinality;
import data.Entity;
import data.Node;
import exceptions.ExistingEdgeException;
import exceptions.FileAlreadyExistException;
import exceptions.InvalidNodeLinkException;
import exceptions.NullNodeException;
import exceptions.SaveWasInteruptedException;
import process.MCDManaging;
import process.Saving;
import process.Loading;

/**
 * Main GUI class after many crash tests
 * 
 * @author Yann Barrachina
 *
 */
public class GUI extends JFrame {

	private Rectangle2D.Float myRect2 = new Rectangle2D.Float(200.0f, 200.0f, 50.0f, 50.0f);

	private ShapeGroup selectedComponent;
	private ShapePanel sp;
	private AttributePanel ap = new AttributePanel();

	private MCDManaging mcdManager = new MCDManaging();

	private String cursorState = new String("selection");

	private ClickManager clickManager = new ClickManager();

	private int uniqueSuffix = 0;

	FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("stdd Files", "stdd");
	private JFileChooser jfc = new JFileChooser();

	private static final Dimension PANEL_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private static final Dimension ICONPANEL_SIZE = new Dimension(PANEL_SIZE.width, 60);
	private static final Dimension ICONBUTTON_SIZE = new Dimension(50, 50);
	private static final int HEIGHT_DIFFERENCE = ICONPANEL_SIZE.height + 45;
	private static final Font APP_FONT = new Font(Font.DIALOG, Font.BOLD, 15);

	private JFrame theFrame;
	private JFrame configFrame;

	private JPanel jpl = new JPanel();
	private JPanel iconPanel = new JPanel();
	private JPanel a1 = new JPanel();
	private JPanel a2 = new JPanel();
	private JPanel a3 = new JPanel();
	private JPanel a4 = new JPanel();
	private JPanel a5 = new JPanel();
	private JPanel a6 = new JPanel();
	private JPanel a7 = new JPanel();
	private JPanel a8 = new JPanel();
	private JPanel topConfigPanel = new JPanel();
	private JPanel bodyConfigPanel = new JPanel();
	private JPanel bottomConfigPanel = new JPanel();

	private JLabel jlaR1 = new JLabel("Rect1");
//	private JLabel jlaR2 = new JLabel("Rect2");
	private JLabel jlaXR1 = new JLabel("X : 50");
	private JLabel jlaYR1 = new JLabel("Y : 50");
	private JLabel jlaXR2 = new JLabel("X : 200");
	private JLabel jlaYR2 = new JLabel("Y : 200");
	private JLabel jlaSoftwareName = new JLabel("Mocodo better");
	private JLabel jlaVersion = new JLabel("Version 0.6-19.02.21");
	private JLabel jlaDEV1 = new JLabel("Dev : Barrachina Yann");
	private JLabel jlaDEV2 = new JLabel("	   Cibulka Nicolas");
	private JLabel jlaDEV3 = new JLabel("      Coutenceau Etienne");
	private JLabel jlaDEV4 = new JLabel("      Lekbour Fatia");
	private JLabel nameConfigLabel = new JLabel("Nom :");

	private JTextField jtfConfigName = new JTextField();

	private JButton selectionButton = new JButton("S");
	private JButton handButton = new JButton("H");
	private JButton newEntityButton = new JButton("+E");
	private JButton newAssociationButton = new JButton("+A");
	private JButton newLinkButton = new JButton("E-A");
	private JButton applyConfigButton = new JButton("Appliquer");
	private JButton cancelConfigButton = new JButton("Annuler");

	private Icon testIcon = new ImageIcon("");

	// menuBar objects
	private JMenuBar jmb = new JMenuBar();

	private JMenu options = new JMenu("Mocodo Better");
	private JMenuItem about = new JMenuItem("À propos");
	private JMenuItem quit = new JMenuItem("Quitter");

	private JMenu file = new JMenu("Fichier");
	private JMenuItem newFile = new JMenuItem("Nouveau...");
	private JMenuItem openFile = new JMenuItem("Ouvrir...");
	private JMenuItem saveFile = new JMenuItem("Enregistrer...");
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
		theFrame = this;

		initLayout();
		initActions();
		jfc.setFileFilter(extensionFilter);
	}

	/**
	 * Initialize the main frame
	 */
	public void initLayout() {
		setTitle("Mocodo Better");

		selectedComponent = null;

		BorderLayout bl = new BorderLayout(3, 1);
		Container contentPane = getContentPane();
		contentPane.setLayout(bl);

		sp = new ShapePanel();

		addMouseMotionListener(clickManager);
		addMouseListener(clickManager);
//		addMouseWheelListener(new ScaleHandler());

		FlowLayout flIcon = new FlowLayout(FlowLayout.LEADING);
		iconPanel.setLayout(flIcon);

		iconPanel.setPreferredSize(ICONPANEL_SIZE);

		selectionButton.setPreferredSize(ICONBUTTON_SIZE);
		handButton.setPreferredSize(ICONBUTTON_SIZE);
		newEntityButton.setPreferredSize(ICONBUTTON_SIZE);
		newAssociationButton.setPreferredSize(ICONBUTTON_SIZE);
		newLinkButton.setPreferredSize(ICONBUTTON_SIZE);
		selectionButton.setToolTipText("Selection");
		handButton.setToolTipText("Hand");
		newEntityButton.setToolTipText("New entity tool");
		newAssociationButton.setToolTipText("New association tool");
		newLinkButton.setToolTipText("Linking tool");
		iconPanel.add(selectionButton);
		iconPanel.add(handButton);
		iconPanel.add(newEntityButton);
		iconPanel.add(newAssociationButton);
		iconPanel.add(newLinkButton);

		GridLayout glInfo = new GridLayout(3, 2);
		jpl.setLayout(glInfo);

		Dimension TEST_SIZE = new Dimension(PANEL_SIZE.width, 200);
		jpl.setPreferredSize(TEST_SIZE);

		jpl.add(jlaR1);
		jpl.add(jlaXR1);
//		jpl.add(newEntityButton);
		jpl.add(jlaYR1);
//		jpl.add(newAssociationButton);

		contentPane.add(iconPanel, BorderLayout.NORTH);
		contentPane.add(sp, BorderLayout.CENTER);
//		contentPane.add(jpl, BorderLayout.SOUTH);

		options.add(about);
		options.addSeparator();
		options.add(quit);

		file.add(newFile);
		file.add(openFile);
		file.add(saveFile);
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

		jmb.add(options);
		jmb.add(file);
//		jmb.add(edit);
		jmb.add(window);
		jmb.add(help);
		setJMenuBar(jmb);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(PANEL_SIZE);
		setResizable(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Initialize the About menu
	 */
	public void initAbout() {
		JFrame jfa = new JFrame("A propos");
		GridLayout gla = new GridLayout(8, 1);

		jfa.getContentPane().setLayout(gla);

		// GUI Name
		a1.setSize(200, 30);
		a1.setLayout(new FlowLayout(FlowLayout.CENTER));
		jlaSoftwareName.setFont(APP_FONT);
		a1.add(jlaSoftwareName);
		jfa.getContentPane().add(a1);

		a2.setSize(200, 30);
		jfa.getContentPane().add(a2);

		// Version
		a3.setSize(200, 30);
		a3.setLayout(new FlowLayout(FlowLayout.CENTER));
		a3.add(jlaVersion);
		jfa.getContentPane().add(a3);

		a4.setSize(200, 30);
		jfa.getContentPane().add(a4);

		// Dev Team
		a5.setSize(200, 30);
		a5.setLayout(new FlowLayout(FlowLayout.CENTER));
		a5.add(jlaDEV1);
		jfa.getContentPane().add(a5);
		a6.setSize(200, 30);
		a6.setLayout(new FlowLayout(FlowLayout.CENTER));
		a6.add(jlaDEV2);
		jfa.getContentPane().add(a6);
		a7.setSize(200, 30);
		a7.setLayout(new FlowLayout(FlowLayout.CENTER));
		a7.add(jlaDEV3);
		jfa.getContentPane().add(a7);
		a8.setSize(200, 30);
		a8.setLayout(new FlowLayout(FlowLayout.CENTER));
		a8.add(jlaDEV4);
		jfa.getContentPane().add(a8);
		jfa.setSize(200, 240);
		jfa.setResizable(false);
		jfa.setLocationRelativeTo(null);
		jfa.setVisible(true);
	}

	/**
	 * Initialize the GUI actions
	 */
	public void initActions() {
		// Menu actions
		about.addActionListener(new ActionAbout());
		openFile.addActionListener(new OpenAction());
		saveFile.addActionListener(new ActionSave());
		quit.addActionListener(new QuitAction());
		minimize.addActionListener(new MinimizeAction());
		fullScreen.addActionListener(new FullScreenAction());

		// Buttons actions
		selectionButton.addActionListener(new SelectionAction());
		handButton.addActionListener(new DragAction());
		newEntityButton.addActionListener(new AddEntityAction());
		newAssociationButton.addActionListener(new AddAssociationAction());
		newLinkButton.addActionListener(new AddLinkAction());
		applyConfigButton.addActionListener(new ApplyConfigAction());
		cancelConfigButton.addActionListener(new CancelConfigAction());

	}

	/**
	 * Let the user configure the parameters of an object through a dedicated
	 * window. Uses cancel and applyConfiguration buttons
	 * 
	 * @param shapeToConfigure the entity or association to configure
	 */
	public void displayObjectConfiguration() {
		if (selectedComponent.isAnEntity()) {
			configFrame = new JFrame("Paramètres de l'entité");
		} else {
			configFrame = new JFrame("Paramètres de l'association");
		}

		ap.setAttributeList(sp.getComponentMap().get(selectedComponent).getListAttribute());

		Container contentPane = configFrame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		bottomConfigPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		topConfigPanel.setPreferredSize(new Dimension(520, 50));
		bodyConfigPanel.setPreferredSize(new Dimension(520, 500));
		bottomConfigPanel.setPreferredSize(new Dimension(520, 50));

		jtfConfigName.setPreferredSize(new Dimension(150, 20));
		jtfConfigName.setText(selectedComponent.getGroupName());
		topConfigPanel.add(nameConfigLabel);
		topConfigPanel.add(jtfConfigName);

		contentPane.add(topConfigPanel, BorderLayout.PAGE_START);

		bodyConfigPanel.add(ap);
		contentPane.add(bodyConfigPanel, BorderLayout.CENTER);

		bottomConfigPanel.add(cancelConfigButton);
		bottomConfigPanel.add(applyConfigButton);
		contentPane.add(bottomConfigPanel, BorderLayout.PAGE_END);

		configFrame.setSize(520, 600);
		configFrame.setResizable(false);
		configFrame.setLocationRelativeTo(null);
		configFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		configFrame.setVisible(true);
	}

	/**
	 * Connects nodes in the given List of ShapeGroup. These are corresponding to
	 * the graphical shapes in the ShapePanel's componentMap. Fills the ShapePanel's
	 * linkMap, which contains all graphical relations between objects for drawing
	 * lines.
	 * 
	 * @param shapesToBeConnected List of shapes to connect
	 */
	public void linkTwoShapes(List<ShapeGroup> shapesToBeConnected) {
		try {
			// Connecting nodes, Jgraph part
			mcdManager.connectNodes(sp.getComponentMap().get(shapesToBeConnected.get(0)),
					sp.getComponentMap().get(shapesToBeConnected.get(1)),
					new Cardinality("1", "0", shapesToBeConnected.get(0).getGroupName()));

			// Connecting objects, graphical part
			// Depends on existing entries in the linkMap
			if (sp.getLinkMap().containsKey(shapesToBeConnected.get(0))) {

				System.out.println("[GUI]  Added a shape");
				sp.getLinkMap().get(shapesToBeConnected.get(0)).add(shapesToBeConnected.get(1));

			} else {
				System.out.println("[GUI]  Added a couple key-value");

				List<ShapeGroup> newLinkList = new ArrayList<ShapeGroup>();
				newLinkList.add(shapesToBeConnected.get(1));
				sp.getLinkMap().put(shapesToBeConnected.get(0), newLinkList);
			}
		} catch (NullNodeException | ExistingEdgeException | InvalidNodeLinkException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Moves shapes from cursor coordinates
	 *
	 */
	public class ClickManager extends MouseAdapter {

		private int x;
		private int y;

//		private List<ShapeGroup> connectedShapes = new ArrayList<ShapeGroup>();
		private List<ShapeGroup> shapesToConnect = new ArrayList<ShapeGroup>();

		public void mousePressed(MouseEvent e) {
			x = e.getX();

			/*
			 * y coordinate is offset about 23 to 25 pixels, may include the titlebar of the
			 * frame
			 */
			y = e.getY() - HEIGHT_DIFFERENCE;
			System.out.println("[GUI]  X : " + x + " Y : " + y);

			// Determining action to perform according to cursorState
			// Only determining when cusrsor is not disabled with "none" status
			if (!cursorState.equals("none")) {

				// If entity button is selected, creating an entity at desired coordinates
				if (cursorState.equals("entity")) {

					String newEntityName = "Entite" + uniqueSuffix;
					ArrayList<Attribute> entityAttributeList = new ArrayList<Attribute>();
					Node newNodeEntity = new Entity(newEntityName, entityAttributeList);
					mcdManager.addNode(newNodeEntity);
					sp.getComponentMap().put(sp.addShapeGroup(newEntityName, x, y, true), newNodeEntity);
					uniqueSuffix++;

					// If association button is selected, creating an association at desired
					// coordinates
				} else if (cursorState.equals("association")) {

					String newAssociationName = "Association" + uniqueSuffix;
					ArrayList<Attribute> associationAttributeList = new ArrayList<Attribute>();
					Node newNodeAssociation = new Association(newAssociationName, associationAttributeList);
					mcdManager.addNode(newNodeAssociation);
					sp.getComponentMap().put(sp.addShapeGroup(newAssociationName, x, y, false), newNodeAssociation);
					uniqueSuffix++;

				} else {

					// For all other states...

					selectedComponent = null;

					// Determining clicked component
					for (ShapeGroup component : sp.getComponentMap().keySet()) {

						if (component.getMainShape().contains(x, y)) {
							selectedComponent = component;
							System.out.println("[GUI]  pointing another component");
							System.out.println("[GUI]  Component entity type ? " + selectedComponent.isAnEntity());
							jlaR1.setText(">>> " + selectedComponent.getGroupName() + " <<<");
							jlaXR1.setText("X : " + selectedComponent.getX());
							jlaYR1.setText("Y : " + selectedComponent.getY());
						}
					}

					if (selectedComponent != null) {

						// If a component is clicked, displays the configuration frame
						if (cursorState.equals("selection")) {
							displayObjectConfiguration();
							cursorState = "none";
						}

						// If the linking tool is selected, performs a linking
						if (cursorState.equals("link")) {
							if (shapesToConnect.size() < 2) {
								shapesToConnect.add(selectedComponent);
								repaint();
							}
							// If two components are selected
							if (shapesToConnect.size() == 2) {
								boolean fisrtObjectType = shapesToConnect.get(0).isAnEntity();
								boolean secondObjectType = shapesToConnect.get(1).isAnEntity();

								// Links...
								if (fisrtObjectType != secondObjectType) {
									linkTwoShapes(shapesToConnect);
									repaint();

									// Or says to the user that he's a clumsy smurf
								} else {
									JOptionPane.showMessageDialog(theFrame,
											"Vous ne pouvez pas associer deux objets de même type !",
											"Erreur d'association", JOptionPane.WARNING_MESSAGE);
								}
								shapesToConnect.clear();
							}
						}
					}
				}
			}

			System.out.println("[GUI]  Liste de connection : " + shapesToConnect.size());

//			else {
//				List<Node> connectedNodes = Graphs.neighborListOf(mcdManager.getMCD().getMCDGraph(),
//						sp.getComponentMap().get(selectedComponent));
//				if (connectedNodes.size() != 0) {
//					for (ShapeGroup component : sp.getComponentMap().keySet()) {
//						for (Node node : connectedNodes) {
//							if (sp.getComponentMap().get(component) == node) {
//								connectedShapes.add(component);
//							}
//						}
//					}
//					System.out.println("[GUI]  Nombre de liens : " + connectedShapes.size());
//				}
//			}

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
		public void actionPerformed(ActionEvent e) {
			cursorState = "selection";
			selectionButton.setEnabled(false);
			handButton.setEnabled(true);
			newEntityButton.setEnabled(true);
			newAssociationButton.setEnabled(true);
			newLinkButton.setEnabled(true);
		}
	}

	public class DragAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!cursorState.equals("none")) {
				cursorState = "hand";
				selectionButton.setEnabled(true);
				handButton.setEnabled(false);
				newEntityButton.setEnabled(true);
				newAssociationButton.setEnabled(true);
				newLinkButton.setEnabled(true);
			}

		}
	}

	public class AddEntityAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!cursorState.equals("none")) {
				cursorState = "entity";
				selectionButton.setEnabled(true);
				handButton.setEnabled(true);
				newEntityButton.setEnabled(false);
				newAssociationButton.setEnabled(true);
				newLinkButton.setEnabled(true);
			}

		}
	}

	public class AddAssociationAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!cursorState.equals("none")) {
				cursorState = "association";
				selectionButton.setEnabled(true);
				handButton.setEnabled(true);
				newEntityButton.setEnabled(true);
				newAssociationButton.setEnabled(false);
				newLinkButton.setEnabled(true);
			}

		}
	}

	public class AddLinkAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!cursorState.equals("none")) {
				cursorState = "link";
				selectionButton.setEnabled(true);
				handButton.setEnabled(true);
				newEntityButton.setEnabled(true);
				newAssociationButton.setEnabled(true);
				newLinkButton.setEnabled(false);
			}

		}
	}

	public class ApplyConfigAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean nameExist = false;
			boolean isDefaultName = false;

			// Checking if the new name given by the user doesn't already exist
			for (Node node : sp.getComponentMap().values()) {
				if (node.getName().equals(jtfConfigName.getText())
						&& node != sp.getComponentMap().get(selectedComponent)) {
					JOptionPane
							.showMessageDialog(configFrame,
									"Un autre objet (entité ou association) porte déjà le nom « "
											+ jtfConfigName.getText() + " »",
									"Erreur de saisie", JOptionPane.WARNING_MESSAGE);
					nameExist = true;
				}
			}

			// Checking if the beggining of new name given by the user isn't equal to the
			// default names
			String configName = jtfConfigName.getText();
			if (configName.length() >= 6) {
				String splitted = configName.substring(0, 6);

				if (splitted.equals("Entite")) {
					JOptionPane.showMessageDialog(configFrame,
							"Le nom « Entite » est réservé, le nom de votre objet ne peut commencer par cette suite de caractères.",
							"Renommage obligatoire", JOptionPane.WARNING_MESSAGE);
					isDefaultName = true;
				}

				if (configName.length() >= 11 && !isDefaultName) {
					String splitted2 = configName.substring(0, 11);

					if (splitted2.equals("Association")) {
						JOptionPane.showMessageDialog(configFrame,
								"Le nom « Association » est réservé, le nom de votre objet ne peut commencer par cette suite de caractères.",
								"Renommage obligatoire", JOptionPane.WARNING_MESSAGE);
						isDefaultName = true;
					}
				}
			}

			// Applying changes to MCD and graphical component
			if (!nameExist && !isDefaultName) {
				ap.updateAttributeList();

				mcdManager.getNodeFromName(selectedComponent.getGroupName()).setListAttribute(ap.getAttributeList());
				mcdManager.getNodeFromName(selectedComponent.getGroupName()).setName(jtfConfigName.getText());

				sp.getComponentMap().get(selectedComponent).setName(jtfConfigName.getText());
				sp.getComponentMap().get(selectedComponent).setListAttribute(ap.getAttributeList());
				selectedComponent.setGroupName(jtfConfigName.getText());

				configFrame.dispose();
				cursorState = "selection";
				repaint();
			}
		}
	}

	public class CancelConfigAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			configFrame.setVisible(false);
			configFrame.dispose();
			cursorState = "selection";
		}
	}

	/**
	 * action of the about button to know more about the program
	 *
	 */
	class ActionAbout implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			initAbout();
		}
	}

	class OpenAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// JFileChooser opening
				jfc.setDialogType(JFileChooser.OPEN_DIALOG);
				jfc.setDialogTitle("Ouvrir un fichier .stdd");
				jfc.setAcceptAllFileFilterUsed(false);
				
				int returnVal = jfc.showOpenDialog(GUI.this);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					Map<String, ArrayList<Float>> coordinatesMap = new HashMap<String, ArrayList<Float>>();
					
					Loading loader = new Loading(file.getAbsolutePath());
					mcdManager.getMCD().setMCDGraph(loader.getMcdManager().getMCD().getMCDGraph());
					
					
					
				}
		}
	}

	class ActionSave implements ActionListener {
		public void actionPerformed(ActionEvent e) {
//			jfc.setDialogType(JFileChooser.SAVE_DIALOG);
//			jfc.setDialogTitle("Enregistrer le fichier");
//			jfc.setAcceptAllFileFilterUsed(false);
//			jfc.setApproveButtonText("Enregistrer");
//			
//			int returnVal = jfc.showSaveDialog(GUI.this);
//			
//			if (returnVal == JFileChooser.APPROVE_OPTION) {
//				File file = jfc.get;
//				
//			}
			
			Map<String, ArrayList<Float>> coordinatesMap = new HashMap<String, ArrayList<Float>>();
			for (ShapeGroup shape : sp.getComponentMap().keySet()) {
				ArrayList<Float> position = new ArrayList<Float>();
				position.add(shape.getX());
				position.add(shape.getY());
				position.add(shape.getWidth());
				position.add(shape.getHeight());

				coordinatesMap.put(shape.getGroupName(), position);
			}
			try {
				new Saving("/Users/ryzentosh/Fac/Cours L3 I/Semestre 6/Projet d'intégration/essai",
						mcdManager.getMCD(), coordinatesMap);
			} catch (SaveWasInteruptedException e1) {
				e1.printStackTrace();
			} catch (FileAlreadyExistException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * action of a button to minimize the window
	 *
	 */
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

	/**
	 * action of a button to quit
	 *
	 */
	public class QuitAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);

		}
	}

	public static void main(String[] args) {
		new GUI();
	}
}

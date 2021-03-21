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
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.AbstractGraphIterator;
import org.jgrapht.traverse.BreadthFirstIterator;

import data.Association;
import data.Attribute;
import data.Cardinality;
import data.Entity;
import data.Node;
import exceptions.EdgesNotLinkedException;
import exceptions.ExistingEdgeException;
import exceptions.FileAlreadyExistException;
import exceptions.InvalidNodeLinkException;
import exceptions.NullNodeException;
import exceptions.SQLTranscriptionException;
import exceptions.SaveWasInteruptedException;
import process.Loading;
import process.MCDManaging;
import process.MLDManaging;
import process.SQLCreation;
import process.Saving;

/**
 * Main GUIMacOS class after many crash tests
 * beautiful macOS version, dedicated shortcuts
 * 
 * @author Yann Barrachina
 *
 */
public class GUIMacOS extends JFrame {

	private Rectangle2D.Float myRect2 = new Rectangle2D.Float(200.0f, 200.0f, 50.0f, 50.0f);

	private ShapeGroup selectedComponent;
	private IconPanel iconPanel;
	private ShapePanel shapePanel;
	private AttributePanel attributePanel = new AttributePanel();
	private CardinalityPanel cardinalityPanel = new CardinalityPanel();

	private MCDManaging mcdManager = new MCDManaging();

	private ClickManager clickManager = new ClickManager();

	private int uniqueSuffix = 0;
	private boolean isDisplayingMCD = true;
	private String workingPath = null;

	private JFileChooser jfc = new JFileChooser();

	private static final Dimension PANEL_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	private static final Dimension ICONPANEL_SIZE = new Dimension(PANEL_SIZE.width, 60);
	private static final Dimension ICONBUTTON_SIZE = new Dimension(50, 50);
	private static final Dimension MAX_FRAME_SIZE = new Dimension(3000, 1500);

	private static final Font APP_FONT = new Font(Font.DIALOG, Font.BOLD, 15);

	private JFrame theFrame;
	private JFrame configFrame;

	private JPanel headPanel = new JPanel();
	private JPanel topConfigPanel = new JPanel();
	private JPanel bodyConfigPanel = new JPanel();
	private JPanel bottomConfigPanel = new JPanel();

	private JTabbedPane associationTabbedPane = new JTabbedPane();

	private JScrollPane jsp;

	private JLabel jlaR1 = new JLabel("Rect1");
//	private JLabel jlaR2 = new JLabel("Rect2");
	private JLabel jlaXR1 = new JLabel("X : 50");
	private JLabel jlaYR1 = new JLabel("Y : 50");
	private JLabel jlaXR2 = new JLabel("X : 200");
	private JLabel jlaYR2 = new JLabel("Y : 200");
	private JLabel jlaSoftwareName = new JLabel("Mocodo better");
	private JLabel jlaVersion = new JLabel("Version 0.8-10.03.21");
	private JLabel jlaDEV1 = new JLabel("Dev : Barrachina Yann");
	private JLabel jlaDEV2 = new JLabel("	   Cibulka Nicolas");
	private JLabel jlaDEV3 = new JLabel("      Coutenceau Etienne");
	private JLabel jlaDEV4 = new JLabel("      Lekbour Fatia");
	private JLabel nameConfigLabel = new JLabel("Nom :");

	private JTextField jtfConfigName = new JTextField();
	private JButton zoomButton = new JButton("+");
	private JButton dezoomButton = new JButton("-");

	private JButton applyConfigButton = new JButton("Appliquer");
	private JButton cancelConfigButton = new JButton("Annuler");

	// menuBar objects
	private JMenuBar jmb = new JMenuBar();

	private JMenu options = new JMenu("Mocodo Better");
	private JMenuItem about = new JMenuItem("À propos");
	private JMenuItem quit = new JMenuItem("Quitter");

	private JMenu file = new JMenu("Fichier");
	private JMenuItem newFile = new JMenuItem("Nouveau...");
	private JMenuItem openFile = new JMenuItem("Ouvrir...");
	private JMenuItem saveFile = new JMenuItem("Enregistrer");
	private JMenuItem saveFileAs = new JMenuItem("Enregistrer Sous...");
	private JMenuItem exportFile = new JMenuItem("Exporter au format SQL...");

	private JMenu edit = new JMenu("Edition");
	private JMenuItem undo = new JMenuItem("Annuler");
	private JMenuItem redo = new JMenuItem("Rétablir");

	private JMenu window = new JMenu("Fenêtre");
	private JMenuItem minimize = new JMenuItem("Réduire");
	private JMenu views = new JMenu("Vues");
	private JMenuItem mcdView = new JMenuItem("Schéma conceptuel");
	private JMenuItem mldView = new JMenuItem("Schéma relationnel");

	private JMenu help = new JMenu("Aide");
	private JMenuItem userGuide = new JMenuItem("Manuel de l'utilisateur");

	public GUIMacOS() {
		theFrame = this;

		initLayout();
		initActions();

	}

	/**
	 * Initialize the main frame
	 */
	public void initLayout() {
		setTitle("MoCoDo Better");

		selectedComponent = null;
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(3, 1));

		shapePanel = new ShapePanel();
		shapePanel.addMouseMotionListener(clickManager);
		shapePanel.addMouseListener(clickManager);

		iconPanel = new IconPanel();

		jsp = new JScrollPane(shapePanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		jsp.getVerticalScrollBar().setUnitIncrement(10);
		jsp.getHorizontalScrollBar().setUnitIncrement(10);

		headPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		headPanel.setPreferredSize(ICONPANEL_SIZE);

		zoomButton.setPreferredSize(ICONBUTTON_SIZE);
		zoomButton.setToolTipText("Zoomer");
		dezoomButton.setPreferredSize(ICONBUTTON_SIZE);
		dezoomButton.setToolTipText("Dézoomer");

		headPanel.add(iconPanel);
		headPanel.add(zoomButton);
		headPanel.add(dezoomButton);

		contentPane.add(headPanel, BorderLayout.NORTH);
		contentPane.add(jsp, BorderLayout.CENTER);

		// JMenuBar settings, making menus and shortcuts displays
		options.add(about);
		options.addSeparator();
		options.add(quit);
		
		quit.setAccelerator(KeyStroke.getKeyStroke(81, InputEvent.META_DOWN_MASK));

		file.add(newFile);
		file.add(openFile);
		file.add(saveFile);
		file.add(saveFileAs);
		file.addSeparator();
		file.add(exportFile);
		
		newFile.setAccelerator(KeyStroke.getKeyStroke(78, InputEvent.META_DOWN_MASK));
		openFile.setAccelerator(KeyStroke.getKeyStroke(79, InputEvent.META_DOWN_MASK));
		saveFile.setAccelerator(KeyStroke.getKeyStroke(83, InputEvent.META_DOWN_MASK));
		saveFileAs.setAccelerator(KeyStroke.getKeyStroke(83, InputEvent.META_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
		exportFile.setAccelerator(KeyStroke.getKeyStroke(69, InputEvent.META_DOWN_MASK));

		edit.add(undo);
		edit.add(redo);

		window.add(minimize);
		window.addSeparator();
		views.add(mcdView);
		views.add(mldView);
		window.add(views);
		
		mcdView.setAccelerator(KeyStroke.getKeyStroke(87, InputEvent.META_DOWN_MASK));
		mldView.setAccelerator(KeyStroke.getKeyStroke(88, InputEvent.META_DOWN_MASK));

		help.add(userGuide);

		jmb.add(options);
		jmb.add(file);
//		jmb.add(edit);
		jmb.add(window);
		jmb.add(help);
		setJMenuBar(jmb);

		// Final frame settings
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMaximumSize(MAX_FRAME_SIZE);
		setSize(PANEL_SIZE);
		setResizable(true);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		iconPanel.repaint();
	}

	/**
	 * Initialize the About menu
	 */
	public void initAbout() {
		JFrame jfa = new JFrame("A propos");
		JPanel a1 = new JPanel();
		JPanel a2 = new JPanel();
		JPanel a3 = new JPanel();
		JPanel a4 = new JPanel();
		JPanel a5 = new JPanel();
		JPanel a6 = new JPanel();
		JPanel a7 = new JPanel();
		JPanel a8 = new JPanel();

		jfa.getContentPane().setLayout(new GridLayout(8, 1));

		// GUIMacOS Name
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
	 * Initialize the GUIMacOS actions
	 */
	public void initActions() {
		// Menu actions
		about.addActionListener(new ActionAbout());
		newFile.addActionListener(new NewAction());
		openFile.addActionListener(new OpenAction());
		saveFile.addActionListener(new ActionSave());
		saveFileAs.addActionListener(new ActionSave());
		exportFile.addActionListener(new ExportAction());
		quit.addActionListener(new QuitAction());
		minimize.addActionListener(new MinimizeAction());

		zoomButton.addActionListener(new ZoomAction());
		dezoomButton.addActionListener(new DezoomAction());

		mcdView.addActionListener(new MCDAction());
		mldView.addActionListener(new MLDAction());

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

		bottomConfigPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		topConfigPanel.setPreferredSize(new Dimension(520, 50));
		bodyConfigPanel.setPreferredSize(new Dimension(520, 500));
		bottomConfigPanel.setPreferredSize(new Dimension(520, 50));

		jtfConfigName.setPreferredSize(new Dimension(150, 20));
		jtfConfigName.setText(selectedComponent.getGroupName());
		topConfigPanel.add(nameConfigLabel);
		topConfigPanel.add(jtfConfigName);

		bottomConfigPanel.add(cancelConfigButton);
		bottomConfigPanel.add(applyConfigButton);

		bodyConfigPanel.removeAll();
		bodyConfigPanel.validate();

		

		if (selectedComponent.isAnEntity()) {
			configFrame = new JFrame("Paramètres de l'entité");

			attributePanel.setPanelState(true);
			attributePanel.setAttributeList(shapePanel.getComponentMap().get(selectedComponent).getListAttribute());
			
			bodyConfigPanel.add(attributePanel);
		} else {
			configFrame = new JFrame("Paramètres de l'association");

			attributePanel.setPanelState(false);
			attributePanel.setAttributeList(shapePanel.getComponentMap().get(selectedComponent).getListAttribute());
			
			cardinalityPanel.setCardinalityList(
					((Association) shapePanel.getComponentMap().get(selectedComponent)).getCardinalityList());

			associationTabbedPane.addTab("Attributs", null, attributePanel, "Modifier les attributs particuliers");
			associationTabbedPane.addTab("Cardinalités", null, cardinalityPanel, "Modifier les cardinalités");

			bodyConfigPanel.add(associationTabbedPane);
		}

		bodyConfigPanel.revalidate();
		bodyConfigPanel.repaint();

		Container contentPane = configFrame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		contentPane.add(topConfigPanel, BorderLayout.PAGE_START);
		contentPane.add(bodyConfigPanel, BorderLayout.CENTER);
		contentPane.add(bottomConfigPanel, BorderLayout.PAGE_END);
		configFrame.addWindowListener(new WindowCloser());
		configFrame.setSize(520, 600);
		configFrame.setResizable(false);
		configFrame.setLocationRelativeTo(null);
//		configFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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

		if (shapePanel.canBeLinked(shapesToBeConnected)) {
			try {
				// Connecting nodes, Jgraph part
				mcdManager.connectNodes(shapePanel.getComponentMap().get(shapesToBeConnected.get(0)),
						shapePanel.getComponentMap().get(shapesToBeConnected.get(1)),
						new Cardinality("0", "1", shapesToBeConnected.get(1).getGroupName()));
				System.out.println("[GUIMacOS]  shape1 entity : " + shapesToBeConnected.get(0).isAnEntity()
						+ " ; shape2 entity : " + shapesToBeConnected.get(1).isAnEntity());

				// Connecting objects, graphical part
				// Depends on existing entries in the linkMap
				if (shapePanel.getLinkMap().containsKey(shapesToBeConnected.get(0))) {

					System.out.println("[GUIMacOS] linkTwoShapes()  Added a shape");
					shapePanel.getLinkMap().get(shapesToBeConnected.get(0)).add(shapesToBeConnected.get(1));

				} else {
					System.out.println("[GUIMacOS] linkTwoShapes()  Added a couple key-value");

					List<ShapeGroup> newLinkList = new ArrayList<ShapeGroup>();
					newLinkList.add(shapesToBeConnected.get(1));
					shapePanel.getLinkMap().put(shapesToBeConnected.get(0), newLinkList);
				}
			} catch (NullNodeException | ExistingEdgeException | InvalidNodeLinkException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println("[GUIMacOS]  Asso after MCDConnect : "
				+ ((Association) mcdManager.getNodeFromName(shapesToBeConnected.get(0).getGroupName())).toString());
	}

	/**
	 * Moves shapes from cursor coordinates
	 *
	 */
	public class ClickManager extends MouseAdapter {

		private int x;
		private int y;

		private List<ShapeGroup> shapesToDisconnect = new ArrayList<ShapeGroup>();
		private List<ShapeGroup> shapesToConnect = new ArrayList<ShapeGroup>();

		public void mousePressed(MouseEvent e) {
			x = e.getX();

			/*
			 * y coordinate is offset about 23 to 25 pixels, may include the titlebar of the
			 * frame
			 */
//			y = e.getY() - HEIGHT_DIFFERENCE;
			y = e.getY();
//			System.out.println("[GUIMacOS]  X : " + x + " Y : " + y);
//			System.out.println("shapePanel X : " + shapePanel.getX() + " shapePanel Y : " + shapePanel.getY());

			// Determining action to perform according to cursorState
			// Only determining when cusrsor is not disabled with "none" status
			if (!iconPanel.getCursorState().equals("none")) {

				// If entity button is selected, creating an entity at desired coordinates
//				if (iconPanel.getCursorState().equals("entity") && (x >= shapePanel.getX()) && (y >= shapePanel.getY())) {
				if (iconPanel.getCursorState().equals("entity") && (x > 0) && (y > 0)) {

					String newEntityName = "Entite" + uniqueSuffix;
					ArrayList<Attribute> entityAttributeList = new ArrayList<Attribute>();
					Node newNodeEntity = new Entity(newEntityName, entityAttributeList);
					mcdManager.addNode(newNodeEntity);

					shapePanel.getComponentMap().put(shapePanel.addShapeGroup(newEntityName, x, y, true),
							newNodeEntity);

					uniqueSuffix++;

					// If association button is selected, creating an association at desired
					// coordinates
//				} else if (iconPanel.getCursorState().equals("association") && (x >= shapePanel.getX()) && (y >= shapePanel.getY())) {
				} else if (iconPanel.getCursorState().equals("association") && (x > 0) && (y > 0)) {

					String newAssociationName = "Association" + uniqueSuffix;
					ArrayList<Attribute> associationAttributeList = new ArrayList<Attribute>();
					Node newNodeAssociation = new Association(newAssociationName, associationAttributeList);
					mcdManager.addNode(newNodeAssociation);
					shapePanel.getComponentMap().put(shapePanel.addShapeGroup(newAssociationName, x, y, false),
							newNodeAssociation);
					uniqueSuffix++;

				} else {

					// For all other states...

					selectedComponent = null;

					// Determining clicked component
					for (ShapeGroup component : shapePanel.getComponentMap().keySet()) {

						if (component.getMainShape().contains(x, y)) {
							selectedComponent = component;
//							System.out.println("[GUIMacOS]  pointing another component");
//							System.out.println("[GUIMacOS]  Component entity type ? " + selectedComponent.isAnEntity());
							jlaR1.setText(">>> " + selectedComponent.getGroupName() + " <<<");
							jlaXR1.setText("X : " + selectedComponent.getX());
							jlaYR1.setText("Y : " + selectedComponent.getY());
						}
					}

					if (selectedComponent != null) {

						switch (iconPanel.getCursorState()) {
						case "selection":
							// If a component is clicked, displays the configuration frame

							displayObjectConfiguration();
							iconPanel.setCursorState("none");
							break;

						case "link":
							// If the linking tool is selected, performs a linking

							if (shapesToConnect.size() < 2) {
								// Ensuring that the fisrt object in the list is an association
								if (selectedComponent.isAnEntity()) {
									shapesToConnect.add(selectedComponent);
								} else {
									shapesToConnect.add(0, selectedComponent);
								}
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
							break;

						case "deleteL":
							// If the deleting link tool is selected
							if (shapesToDisconnect.size() < 2) {
								// Ensuring that the fisrt object in the list is an association
								if (selectedComponent.isAnEntity()) {
									shapesToDisconnect.add(selectedComponent);
								} else {
									shapesToDisconnect.add(0, selectedComponent);
								}
								repaint();
							}
							// If two components are selected
							if (shapesToDisconnect.size() == 2) {
								boolean fisrtObjectType = shapesToDisconnect.get(0).isAnEntity();
								boolean secondObjectType = shapesToDisconnect.get(1).isAnEntity();

								// Braking links...
								if (fisrtObjectType != secondObjectType && shapePanel
										.existLinkBetween(shapesToDisconnect.get(0), shapesToDisconnect.get(1))) {
									try {
										mcdManager.disconnectNodes(
												shapePanel.getComponentMap().get(shapesToDisconnect.get(0)),
												shapePanel.getComponentMap().get(shapesToDisconnect.get(1)));
									} catch (NullNodeException | EdgesNotLinkedException e1) {
										e1.printStackTrace();
									}
									System.out.println("[GUIMacOS]  Asso after MCDdisconnet : " + ((Association) mcdManager
											.getNodeFromName(shapesToDisconnect.get(0).getGroupName())).toString());
									shapePanel.disconnectShapes(shapesToDisconnect);
									repaint();

									// Or says to the user that he's a clumsy smurf
								} else {
									JOptionPane.showMessageDialog(theFrame,
											"Vous avez sélectionné deux objets le même type !",
											"Aucun lien à supprimer", JOptionPane.WARNING_MESSAGE);
								}
								shapesToDisconnect.clear();
							}

							break;

						case "deleteC":
							try {
								mcdManager.removeNode(shapePanel.getComponentMap().get(selectedComponent));
							} catch (NullNodeException e1) {
								e1.printStackTrace();
							}
							
							shapePanel.removeShape(selectedComponent);
							repaint();
							
							break;

						}
					}
				}
			}
		}

		public void mouseDragged(MouseEvent e) {

			if (selectedComponent != null && iconPanel.getCursorState().equals("hand")) {

				int dx = e.getX() - x;
//				int dy = e.getY() - y - HEIGHT_DIFFERENCE;
				int dy = e.getY() - y;
				float newX = selectedComponent.getX() + dx;
				float newY = selectedComponent.getY() + dy;

				// Checking if component is at the border of the frame
				Dimension currentPanelSize = new Dimension(shapePanel.getSize());
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

	
	public class WindowCloser extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			configFrame.dispose();
			iconPanel.setCursorState("selection");
			iconPanel.repaint();
		}
	}
	
	public class ZoomAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			shapePanel.zoom();
			iconPanel.repaint();

		}
	}
	
	public class DezoomAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			shapePanel.dezoom(theFrame.getSize());
			iconPanel.repaint();
		}
	}

	public class MCDAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isDisplayingMCD) {
				isDisplayingMCD = true;
				Container contentPane = getContentPane();
				contentPane.removeAll();
				contentPane.validate();

				contentPane.add(headPanel, BorderLayout.NORTH);
				contentPane.add(jsp, BorderLayout.CENTER);

				contentPane.revalidate();
				contentPane.repaint();
			}

		}
	}

	public class MLDAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isDisplayingMCD) {
				isDisplayingMCD = false;
				
				MLDManaging mldManager = new MLDManaging();
				try {
					mldManager.newMld(mcdManager.getMCD());
				} catch (ClassNotFoundException e2) {
					e2.printStackTrace();
				} catch (IOException e2) {
					e2.printStackTrace();
				}

				try {
					MLDPanel mldPanel = new MLDPanel(mldManager.getMLD());
					JTextArea sqlArea = new JTextArea();
					sqlArea.setEditable(false);
					sqlArea.setText(SQLCreation.SQLPrevisualiser(mldManager.getMLD()));

					Container contentPane = getContentPane();

					JScrollPane jspMLD = new JScrollPane(mldPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
							JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					jspMLD.setBorder(BorderFactory.createTitledBorder("Schéma relationnel"));
					jspMLD.setPreferredSize(new Dimension(400,0));
					
					JScrollPane jspSQL = new JScrollPane(sqlArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
							JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					jspSQL.setBorder(BorderFactory.createTitledBorder("Prévisualisation SQL"));

					JSplitPane documentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jspMLD, jspSQL);

					contentPane.removeAll();
					contentPane.validate();

					contentPane.add(headPanel, BorderLayout.NORTH);
					contentPane.add(documentSplitPane, BorderLayout.CENTER);

					contentPane.revalidate();
					contentPane.repaint();

				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}

			}

		}
	}


	public class ApplyConfigAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean nameExist = false;
			boolean isDefaultName = false;

			// Checking if the new name given by the user doesn't already exist
			for (Node node : shapePanel.getComponentMap().values()) {
				if (node.getName().equals(jtfConfigName.getText())
						&& node != shapePanel.getComponentMap().get(selectedComponent)) {
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
				attributePanel.updateAttributeList();

				mcdManager.getNodeFromName(selectedComponent.getGroupName()).setListAttribute(attributePanel.getAttributeList());

				// If it's an entity, updating its references in cardinalities
				if (selectedComponent.isAnEntity()) {
					mcdManager.updateEntityNameInCardinalities(selectedComponent.getGroupName(),
							jtfConfigName.getText());

				} else { // Otherwise, custom updating for association
					cardinalityPanel.updateCardinalityList();
					((Association) mcdManager.getNodeFromName(selectedComponent.getGroupName()))
							.setCardinalityList(cardinalityPanel.getCardinalityList());
					((Association) shapePanel.getComponentMap().get(selectedComponent))
							.setCardinalityList(cardinalityPanel.getCardinalityList());
				}

				mcdManager.getNodeFromName(selectedComponent.getGroupName()).setName(jtfConfigName.getText());

				// Updating datas in graphical componentMap
				shapePanel.getComponentMap().get(selectedComponent).setName(jtfConfigName.getText());
				shapePanel.getComponentMap().get(selectedComponent).setListAttribute(attributePanel.getAttributeList());
				selectedComponent.setGroupName(jtfConfigName.getText());

				configFrame.dispose();
				iconPanel.setCursorState("selection");
				repaint();
				iconPanel.repaint();
			}
		}
	}

	public class CancelConfigAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			configFrame.dispose();
			iconPanel.setCursorState("selection");
			iconPanel.repaint();

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
	
	class NewAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			int rep = JOptionPane.showConfirmDialog(theFrame,"Voulez-vous créer un nouveau schéma conceptuel ?\nLe schéma actuel sera effacé.", "Nouveau schéma conceptuel",
					JOptionPane.CANCEL_OPTION);
					if(rep == JOptionPane.YES_OPTION) {
						shapePanel.clear();
						mcdManager = new MCDManaging();
						uniqueSuffix = 0;
					}
		}
	}

	class OpenAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			FileNameExtensionFilter extensionFilterXML = new FileNameExtensionFilter("Fichiers xml", "xml");

			jfc.setDialogType(JFileChooser.OPEN_DIALOG);
			jfc.setDialogTitle("Ouvrir un fichier .xml");
			jfc.setFileFilter(extensionFilterXML);
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int returnVal = jfc.showOpenDialog(theFrame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();
				workingPath = file.getPath();
				theFrame.setTitle(workingPath + " - MoCoDo Better");
				Map<String, ArrayList<Float>> coordinatesMap = new HashMap<String, ArrayList<Float>>();
				loadCoordinates(file, coordinatesMap);
				Loading loader = new Loading(file.getAbsolutePath());
				shapePanel.clear();
				mcdManager = loader.getMcd();

				setShapePanel(coordinatesMap, loader);
				loadLinks(loader);
//				mcdManager.getMCD().setMCDGraph((Pseudograph<Node, DefaultEdge>) loader.getMcdManager().getMCD().getMCDGraph());
//
//			}
				System.out.println(loader.getMcd().toString());
			}

		}

		@SuppressWarnings("unchecked")
		private void loadCoordinates(File file, Map<String, ArrayList<Float>> coordinatesMap) {
			try {
				BufferedReader br = Files.newBufferedReader(Paths.get(file.getAbsolutePath()));
				String name = null;
				String str;
				ArrayList<Float> graphicalValuestmp = new ArrayList<Float>();

				while (!(str = br.readLine()).equals("</Associations>")) {

					if (str.equals("</Table>")) {
						ArrayList<Float> graphicalValues = (ArrayList<Float>) graphicalValuestmp.clone();
						coordinatesMap.put(name, graphicalValues);

						graphicalValuestmp.removeAll(graphicalValuestmp);
						System.out.println(coordinatesMap.get(name).size());

					}
					if (str.equals("<Name>")) {
						str = br.readLine();
						name = str;
					}
					if (str.equals("<Graphical>")) {
						str = br.readLine();
						String[] graph = str.split(",");

						graphicalValuestmp.add(Float.parseFloat(graph[0]));
						graphicalValuestmp.add(Float.parseFloat(graph[1]));
					}
				}
				br.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void setShapePanel(Map<String, ArrayList<Float>> coordinatesMap, Loading loader) {
			AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(
					loader.getMcd().getMCD().getMCDGraph());
			while (iterator.hasNext()) {
				Node currentNode = iterator.next();
				System.out.println(currentNode.getName());

				Float x = coordinatesMap.get(currentNode.getName()).get(0);
				Float y = coordinatesMap.get(currentNode.getName()).get(1);
				if (currentNode instanceof Entity) {
					shapePanel.getComponentMap().put(shapePanel.addShapeGroup(currentNode.getName(), x, y, true),
							currentNode);
				} else if (currentNode instanceof Association) {
					shapePanel.getComponentMap().put(shapePanel.addShapeGroup(currentNode.getName(), x, y, false),
							currentNode);
				}

			}
		}

		@SuppressWarnings("unchecked")
		private void loadLinks(Loading loader) {
			AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(
					loader.getMcd().getMCD().getMCDGraph());
			ArrayList<ShapeGroup> cardinality = new ArrayList<ShapeGroup>();
			ShapeGroup asso = null;
			while (iterator.hasNext()) {
				Node currentNode = iterator.next();
				if (currentNode instanceof Association) {
					for (Cardinality card : ((Association) currentNode).getCardinalityList()) {
						for (ShapeGroup shape : shapePanel.getComponentMap().keySet()) {
							if (shape.getGroupName().equals(card.getNomEntity())) {
								cardinality.add(shape);
							}
						}
					}
					for (ShapeGroup assoshape : shapePanel.getComponentMap().keySet()) {
						if (assoshape.getGroupName().equals(currentNode.getName())) {
							asso = assoshape;
							shapePanel.getLinkMap().put(asso, (ArrayList<ShapeGroup>) cardinality.clone());
						}
					}
					cardinality.clear();
				}
			}
		}
	}

	class ActionSave implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			// Preparing the saving by filling
			Map<String, ArrayList<Float>> coordinatesMap = new HashMap<String, ArrayList<Float>>();
			for (ShapeGroup shape : shapePanel.getComponentMap().keySet()) {
				ArrayList<Float> position = new ArrayList<Float>();
				position.add(shape.getX());
				position.add(shape.getY());
				position.add(shape.getWidth());
				position.add(shape.getHeight());

				coordinatesMap.put(shape.getGroupName(), position);
			}
			jfc.setDialogType(JFileChooser.SAVE_DIALOG);
			jfc.setFileFilter(new FileNameExtensionFilter("Fichiers xml", "xml"));
			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			jfc.setAcceptAllFileFilterUsed(false);
			
			// Save as
			if(workingPath == null) {
				
				jfc.setDialogTitle("Enregistrer sous");
				
				int returnVal = jfc.showSaveDialog(theFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					workingPath = file.getPath();
					theFrame.setTitle(workingPath + " - MoCoDo Better");
					try {
						new Saving(workingPath,
								mcdManager.getMCD(), coordinatesMap, true);
					} catch (SaveWasInteruptedException e1) {
						e1.printStackTrace();
					} catch (FileAlreadyExistException e1) {
						e1.printStackTrace();
					}
				}
					
					
			}else {	// Save
				jfc.setDialogTitle("Enregistrer");
				int returnVal = jfc.showSaveDialog(theFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jfc.getSelectedFile();
					workingPath = file.getPath();
					theFrame.setTitle(workingPath + " - MoCoDo Better");
					
					try {
						new Saving(workingPath,
								mcdManager.getMCD(), coordinatesMap, false);
						
					} catch (SaveWasInteruptedException e1) {
						e1.printStackTrace();
					} catch (FileAlreadyExistException e1) {
						e1.printStackTrace();
					}
				}
				
			}
			
			
		}
	}

	class ExportAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			FileNameExtensionFilter extensionFilterSQL = new FileNameExtensionFilter("Fichiers sql", "sql");

			jfc.setDialogType(JFileChooser.SAVE_DIALOG);
			jfc.setDialogTitle("Export SQL");
			jfc.setFileFilter(new FileNameExtensionFilter("Fichiers sql", "sql"));
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			int returnVal = jfc.showSaveDialog(GUIMacOS.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = jfc.getSelectedFile();

				MLDManaging mldManager = new MLDManaging();
//				System.out.println("name : " + file.getName() + "\n Path : " + file.toPath());

				try {
					SQLCreation.SQLConverter(mldManager.getMLD(), file.toPath());
				} catch (SQLTranscriptionException e1) {
					e1.printStackTrace();
				}
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
		new GUIMacOS();
	}
}

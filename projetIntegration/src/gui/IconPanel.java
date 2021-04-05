package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * IconPanel, which inherits from JPanel, manages its iconGroups.
 * IconGroup emulates real button.
 * 
 * @author Yann Barrachina
 *
 */
public class IconPanel extends JPanel {
	private JPanel mainPanel;
	
	private ClickManager clickManager = new ClickManager();

	private List<IconGroup> componentList = new ArrayList<IconGroup>();
	
	private String cursorState = new String("selection");
	
	private Graphics2D g2d;

	public IconPanel() {
		mainPanel = this;
		

		String buttonName[] = { "selection", "hand", "entity", "association", "link", "deleteC", "deleteL" };
		String buttonTitle[] = { "Sélectionner", "Déplacer", "Entité", "Association", "Relation", "Effacer objet",
				"Effacer lien" };
		String pressedImages[] = { "cursorPressed", "openHandPressed", "newEntityPressed", "newAssoPressed", "newLinkPressed", "deleteObjectPressed", "deleteLinkPressed" };
		String standardImages[] = { "cursor", "openHand", "newEntity", "newAsso", "newLink", "deleteObject", "deleteLink" };

		int mainPanelWidth = 0;
		for (int index = 0; index < 7; index++) {
			mainPanelWidth += getFontMetrics(getFont()).stringWidth(" " + buttonTitle[index] + " ");
		}
		
		int xPosition = mainPanel.getX();
		int yPosition = mainPanel.getY();
		for (int index = 0; index < 7; index++) {

			IconGroup iconGroup = new IconGroup(buttonName[index], buttonTitle[index], xPosition, yPosition,
					getFontMetrics(getFont()).stringWidth(" " + buttonTitle[index] + " "), 60,
					new ImageIcon("images/" + pressedImages[index] + ".png").getImage(),
					new ImageIcon("images/" + standardImages[index] + ".png").getImage());
			componentList.add(iconGroup);

			xPosition += iconGroup.getWidth();
		}
		
		componentList.get(0).setSelected(true);

		mainPanel.addMouseListener(clickManager);
		mainPanel.setPreferredSize(new Dimension(mainPanelWidth, 60));
	}

	public void paint(Graphics g) {
		super.paint(g);
		g2d = (Graphics2D) g;
		
		// Parameters
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		
		int spaceMetrics = getFontMetrics(getFont()).stringWidth(" ");
		for (IconGroup iconGroup : componentList) {
			double imageXPosition = iconGroup.getSelectionShape().getBounds2D().getCenterX()
					- iconGroup.getPressedImage().getWidth(null) / 2;

			// Drawing selection rectangle, for selected button only
			if (iconGroup.isPressed() || iconGroup.isSelected()) {
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.fill(iconGroup.getSelectionShape());
			}
			
			// Drawing icon, according to buttonState, emulating a real button, because it's absolutely not the case
			if (iconGroup.isPressed()) {
				g2d.drawImage(iconGroup.getPressedImage(), (int) imageXPosition, (int) iconGroup.getY() + 2, null);
			} else {
				g2d.drawImage(iconGroup.getStandardImage(), (int) imageXPosition, (int) iconGroup.getY() + 2, null);
			}

			g2d.setColor(Color.BLACK);
			g2d.drawString(iconGroup.getIconTitle(), iconGroup.getX() + spaceMetrics, iconGroup.getY() + 45);

		}

	}
	
	public class ClickManager extends MouseAdapter {

		private int x;
		private int y;
	
		public void mousePressed(MouseEvent e) {
			x = e.getX();
			y = e.getY();
			
			for(IconGroup iconButton : componentList) {
				if(iconButton.getSelectionShape().contains(x, y)) {
					setPressed(iconButton);
				}
			}
			repaint();
		}
		
		public void mouseReleased(MouseEvent e) {
			for(IconGroup iconButton : componentList) {
				if(iconButton.isPressed()) {
					setCursorState(iconButton.getGroupName());
					setSelected();
				}
			}
		}
	}	

	/**
	 * @return the componentList
	 */
	public List<IconGroup> getComponentList() {
		return componentList;
	}

	/**
	 * Setting the right iconGroup at pressed state, unselecting the former selected
	 * iconGroup. The pressed state of an iconGroup lets it's selectionShape to be
	 * drawn and it's pressedIcon to be shown. Repaints IconPanel when called.
	 * 
	 * @param pressedIconGroup the pressed iconGroup by the user
	 */
	public void setPressed(IconGroup pressedIconGroup) {

		for (IconGroup iconGroup : componentList) {
			if (iconGroup == pressedIconGroup) {
				iconGroup.setPressed(true);
			} else if (iconGroup.isSelected()) {
				iconGroup.setSelected(false);
			}
		}

		repaint();
	}

	public void setSelected() {
		for (IconGroup iconGroup : componentList) {
			if (iconGroup.isPressed()) {
				iconGroup.setSelected(true);
			}
		}
		repaint();
	}

	/**
	 * @return the cursorState
	 */
	public String getCursorState() {
		return cursorState;
	}

	/**
	 * @param cursorState the cursorState to set
	 */
	public void setCursorState(String cursorState) {
		this.cursorState = cursorState;
	}
}

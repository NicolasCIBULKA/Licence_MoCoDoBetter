package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import data.Association;
import data.Attribute;
import data.Cardinality;
import data.Node;

/**
 * ShapePanel contains all the graphical objects to paint and manage their
 * update (for now).
 * 
 * @author Yann Barrachina
 *
 */
public class ShapePanel extends JPanel {
	private JPanel mainPanel;

	private static final int ORIGINAL_WIDTH = 3000;
	private static final int ORIGINAL_HEIGHT = 1500;

	private static final Dimension SHAPE_PANEL_SIZE = new Dimension(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);

	private Font mainFont;

	// LinkedHashMap to prevent an unsorted iteration for the mousePressed listener
	private Map<ShapeGroup, Node> componentMap = new LinkedHashMap<ShapeGroup, Node>();
	private Map<ShapeGroup, List<ShapeGroup>> linkMap = new HashMap<ShapeGroup, List<ShapeGroup>>();
	private List<Line2D.Float> alLines = new ArrayList<Line2D.Float>();
//	private Map<ShapeGroup, ArrayList<Line2D.Float>> mapRelation = new HashMap<ShapeGroup, ArrayList<Line2D.Float>>();

	private Color entityHeadColor = new Color(192, 223, 139);
	private Color entitySplitColor = new Color(148, 191, 89);
	private Color entityBodyColor = new Color(232, 243, 210);
	private Color entityBoundColor = new Color(140, 185, 78);

	// Former colors
//	private Color entityHeadColor = new Color(0, 150, 150);
//	private Color entitySplitColor = new Color(0, 100, 100);
//	private Color entityBodyColor = new Color(60, 190, 190);
//	private Color entityBoundColor = new Color(0, 75, 75);

	private Color associationHeadColor = new Color(233, 186, 217);
	private Color associationSplitColor = new Color(216, 146, 187);
	private Color associationBodyColor = new Color(248, 225, 238);
	private Color associationBoundColor = new Color(209, 126, 173);

	// Former colors
//	private Color associationHeadColor = new Color(160, 30, 150);
//	private Color associationSplitColor = new Color(140, 50, 140);
//	private Color associationBodyColor = new Color(210, 110, 200);
//	private Color associationBoundColor = new Color(100, 50, 100);

	private Graphics2D g2d;

	public ShapePanel() {
//		shapePanel = new JPanel();
		mainPanel = this;
		mainPanel.setDoubleBuffered(true);
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setPreferredSize(SHAPE_PANEL_SIZE);
		mainFont = new Font(Font.DIALOG, Font.PLAIN, 15);
		mainPanel.setFont(mainFont);
//		shapePanel.setSize(SHAPE_PANEL_SIZE);

//		mainPanel.add(jsp, BorderLayout.NORTH);

		// TODO : colorChooser (if still exist...)
	}

	public void paint(Graphics g) {
		super.paint(g);
		g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//		g2d.setFont(mainFont);

		// Painting entities, associations, links and cardinalities
		System.out.println("[ShapePanel]  ---- " + componentMap.size() + " component(s)");

		drawLinesAndCardinalities();

		for (ShapeGroup component : componentMap.keySet()) {

			maxComponentDimensionUpdate(component);

			if (component.isAnEntity()) {
				drawEntity(component);

			} else {
				drawAssociation(component);

			}
		}
	}

	/**
	 * Creates a new component to display and repaints.
	 * 
	 * @param x          the X coordinate of the new component
	 * @param y          the Y coordinate of the new component
	 * @param entityType defines if the component is an entity or an association
	 */
	public ShapeGroup addShapeGroup(String newName, float x, float y, boolean entityType) {
		ShapeGroup component = new ShapeGroup(newName, x, y, entityType);
		repaint();
		return component;
	}

	public void addLine(float startX, float startY, float endX, float endY) {
		Line2D.Float line = new Line2D.Float(startX, startY, endX, endY);
		alLines.add(line);
	}

	private void drawEntity(ShapeGroup component) {

		// Body and head
		g2d.setColor(entityBodyColor);
		g2d.fill(component.getMainShape());

		g2d.setColor(entityHeadColor);
		g2d.fill(component.getHeadShape());

		// Split line between body and head
		BasicStroke headStroke = new BasicStroke(3);
		g2d.setColor(entitySplitColor);
		g2d.setStroke(headStroke);
		g2d.draw(new Rectangle2D.Double(component.getHeadShape().getBounds2D().getX() - 1,
				component.getHeadShape().getBounds2D().getY() - 1,
				component.getHeadShape().getBounds2D().getWidth() + 1,
				component.getHeadShape().getBounds2D().getHeight() + 1));

		// Entity bound
		BasicStroke boundStroke = new BasicStroke(3);
		g2d.setColor(entityBoundColor);
		g2d.setStroke(boundStroke);
		g2d.draw(new Rectangle2D.Double(component.getMainShape().getBounds2D().getX() - 1,
				component.getMainShape().getBounds2D().getY() - 1,
				component.getMainShape().getBounds2D().getWidth() + 1,
				component.getMainShape().getBounds2D().getHeight() + 1));

		// Drawing text
		// Title
		int titleWidth = getFontMetrics(getFont()).stringWidth(component.getGroupName());
		float titleTab = (component.getWidth() - titleWidth) / 2;
		g2d.setColor(Color.black);
		g2d.drawString(component.getGroupName(), component.getX() + titleTab, component.getY() + 30.0f);

		// Attributes
		ArrayList<Attribute> attributeList = componentMap.get(component).getListAttribute();
		int attributeQuantity = attributeList.size();

		for (int index = 0; index < attributeQuantity; index++) {

			g2d.drawString(attributeList.get(index).getName(), component.getX() + 10.0f,
					component.getY() + 70.0f + (index * 20.0f));

			// Underlines the Primary Key attribute
			if (attributeList.get(index).isPrimaryKey()) {

				int x = (int) (component.getX() + 10.0f);
				int y = (int) (component.getY() + 70.0f + (index * 20.0f));
				int attributeWidth = getFontMetrics(getFont()).stringWidth(attributeList.get(index).getName());
				g2d.drawLine(x, y + 2, x + attributeWidth, y + 2);
			}
		}

	}

	private void drawAssociation(ShapeGroup component) {

		// Body and head
		g2d.setColor(associationBodyColor);
		g2d.fill(component.getMainShape());

		g2d.setColor(associationHeadColor);
		g2d.fill(component.getHeadShape());

		// Split line between body and head
		BasicStroke headStroke = new BasicStroke(3);
		g2d.setColor(associationSplitColor);
		g2d.setStroke(headStroke);
		g2d.draw(new RoundRectangle2D.Double(component.getHeadShape().getBounds2D().getX() - 1,
				component.getHeadShape().getBounds2D().getY() - 1,
				component.getHeadShape().getBounds2D().getWidth() + 1,
				component.getHeadShape().getBounds2D().getHeight() + 1, 20.0f, 20.0f));

		// Association bound
		BasicStroke boundStroke = new BasicStroke(3);
		g2d.setColor(associationBoundColor);
		g2d.setStroke(boundStroke);
		g2d.draw(new RoundRectangle2D.Double(component.getMainShape().getBounds2D().getX() - 1,
				component.getMainShape().getBounds2D().getY() - 1,
				component.getMainShape().getBounds2D().getWidth() + 1,
				component.getMainShape().getBounds2D().getHeight() + 1, 20.0f, 20.0f));

		// Drawing text
		// Title
		int titleWidth = getFontMetrics(getFont()).stringWidth(component.getGroupName());
		float titleTab = (component.getWidth() - titleWidth) / 2;
		g2d.setColor(Color.black);
		g2d.drawString(component.getGroupName(), component.getX() + titleTab, component.getY() + 30.0f);

		// Attributes
		ArrayList<Attribute> attributeList = componentMap.get(component).getListAttribute();
		int attributeQuantity = attributeList.size();

		for (int index = 0; index < attributeQuantity; index++) {

			g2d.drawString(attributeList.get(index).getName(), component.getX() + 10.0f,
					component.getY() + 70.0f + (index * 20.0f));

			// Underlines the Primary Key attribute
			if (attributeList.get(index).isPrimaryKey()) {

				int x = (int) (component.getX() + 10.0f);
				int y = (int) (component.getY() + 70.0f + (index * 20.0f));
				int attributeWidth = getFontMetrics(getFont()).stringWidth(attributeList.get(index).getName());
				g2d.drawLine(x, y + 2, x + attributeWidth, y + 2);
			}
		}
	}

	// TODO : case of two links between one A/E couple
	// TODO : case of vertical links
	private void drawLinesAndCardinalities() {

		for (ShapeGroup shape : linkMap.keySet()) {
			System.out.println("[ShapePanel]  Cardinality : " + ((Association) componentMap.get(shape)).toString());
			float associationCenterX = (float) shape.getMainShape().getBounds2D().getCenterX();
			float associationCenterY = (float) shape.getMainShape().getBounds2D().getCenterY();

			for (ShapeGroup linkedShape : linkMap.get(shape)) {
				System.out.println("[ShapePanel]  Entity : " + linkedShape.getGroupName());

				float entityCenterX = (float) linkedShape.getMainShape().getBounds2D().getCenterX();
				float entityCenterY = (float) linkedShape.getMainShape().getBounds2D().getCenterY();

				g2d.setColor(Color.BLACK);
				g2d.draw(new Line2D.Float(associationCenterX, associationCenterY, entityCenterX, entityCenterY));

				// Here we calculate the most revlevent position to display cardinality
				// Have to find first the intersection between entity perimeter and link line

				// if the slope is infinite, x coordinates are aligned
				if (associationCenterX == entityCenterX) {
					float intersectionY;
					g2d.setColor(Color.GREEN);

					// If association is under entity
					if (associationCenterY > entityCenterY) {
						intersectionY = entityCenterY + linkedShape.getHeight() / 2;
						g2d.drawOval((int) (entityCenterX - 2.5), (int) (intersectionY - 2.5), 5, 5);
					} else {
						intersectionY = entityCenterY - linkedShape.getHeight() / 2;
						g2d.drawOval((int) (entityCenterX - 2.5), (int) (intersectionY - 2.5), 5, 5);
					}

				} else { // Else it calculates as usual

					// line's equation : y = ax + b ; finding "a" and "b"
					float a = (associationCenterY - entityCenterY) / (associationCenterX - entityCenterX);
					float b = entityCenterY - (a * entityCenterX);

//					g2d.drawString("a = " + a, 10, 20);
//					g2d.drawString("b = " + b, 10, 40);

					float x = entityCenterX;
					float y = entityCenterY;
					float halfWidth = linkedShape.getWidth() / 2;
					float halfHeight = linkedShape.getHeight() / 2;

					float associationX = associationCenterX;
					float associationY = associationCenterY;

					// These are the four intersection candidates
					// Vertical ones
					float vX = entityCenterX - halfWidth;
					float vY = a * (vX) + b;

					float wX = entityCenterX + halfWidth;
					float wY = a * (wX) + b;

					// Horizontal ones
					float hY = entityCenterY - halfHeight;
					float hX = (hY - b) / a;

					float gY = entityCenterY + halfHeight;
					float gX = (gY - b) / a;

					// Creating points to simplify distance calculation
					Point2D v = new Point2D.Float();
					v.setLocation(vX, vY);
					Point2D w = new Point2D.Float();
					w.setLocation(wX, wY);
					Point2D h = new Point2D.Float();
					h.setLocation(hX, hY);
					Point2D g = new Point2D.Float();
					g.setLocation(gX, gY);

					// This part determines which point must be drawn

					/**
					 * Note : No need to verify second vertical or horizontal point, if one is in
					 * bounds so the other is mathematically inside too.
					 * 
					 * 2nd vertical : (y - halfHeight <= w.getY()) && (w.getY() <= y + halfHeight)
					 * 2nd horizontal : (x - halfWidth <= g.getX()) && (g.getX() <= x + halfWidth)
					 */
//					g2d.setColor(Color.RED);

					double distance = 0;
					Point2D validPoint = new Point2D.Float();

					if ((y - halfHeight <= v.getY()) && (v.getY() <= y + halfHeight)) {

						if (v.distance(associationX, associationY) < w.distance(associationX, associationY)) {
//							g2d.drawString("p1 = " + vX + " ; " + vY, 10, 80);

//							g2d.drawOval((int) (v.getX() - 2.5), (int) (v.getY() - 2.5), 5, 5);
							distance = v.distance(entityCenterX, entityCenterY);
							validPoint = v;

						} else {
//							g2d.drawString("p2 = " + wX + " ; " + wY, 10, 100);

//							g2d.drawOval((int) (w.getX() - 2.5), (int) (w.getY() - 2.5), 5, 5);

							distance = w.distance(entityCenterX, entityCenterY);
							validPoint = w;
						}

					} else if ((x - halfWidth <= h.getX()) && (h.getX() <= x + halfWidth)) {

						if (h.distanceSq(associationX, associationY) < g.distanceSq(associationX, associationY)) {
//							g2d.drawString("p3 = " + hX + " ; " + hY, 10, 160);

//							g2d.drawOval((int) (h.getX() - 2.5), (int) (h.getY() - 2.5), 5, 5);

							distance = h.distance(entityCenterX, entityCenterY);
							validPoint = h;
						} else {
//							g2d.drawString("p4 = " + gX + " ; " + gY, 10, 180);

//							g2d.drawOval((int) (g.getX() - 2.5), (int) (g.getY() - 2.5), 5, 5);

							distance = g.distance(entityCenterX, entityCenterY);
							validPoint = g;
						}
					}

					distance += 15;

					double angle = Math.atan(a);
					double newAngle;

					float endX = entityCenterX + (float) (distance * Math.cos(angle));
					float endY = entityCenterY + (float) (distance * Math.sin(angle));

					// These coordinates are dynamically updated according to cardinality string
					// size
					float newEndX;
					float newEndY;

					double isOnSegmentX = endX - associationX / validPoint.getX() - validPoint.getX();
					double isOnSegmentY = endY - associationY / validPoint.getY() - validPoint.getY();
//
//					g2d.drawString("isOnSegmentX =  " + isOnSegmentX, 200, 60);
//					g2d.drawString("isOnSegmentY =  " + isOnSegmentY, 200, 80);
//					g2d.drawString("angle in radian = " + angle, 200, 40);

					String values = "";
					for (Cardinality card : ((Association) componentMap.get(shape)).getCardinalityList()) {
						if (card.getNomEntity().equals(linkedShape.getGroupName())) {
							values += card.getLowValue() + "," + card.getHighValue();
						}
					}

					// If we are above π/2 and under -π/2
					if (isOnSegmentY > 14 || isOnSegmentY < -16 || isOnSegmentX > 14) {
						if (a > 0) {
							newAngle = Math.atan(a) - Math.PI / 11;
							newEndY = entityCenterY + (float) (-distance * Math.sin(newAngle));
							newEndX = entityCenterX + (float) (-distance * Math.cos(newAngle))
									- getFontMetrics(getFont()).stringWidth(values);
						} else {
							newAngle = Math.atan(a) + Math.PI / 11;
							newEndY = entityCenterY + (float) (-distance * Math.sin(newAngle)) + 10.0f;
							newEndX = entityCenterX + (float) (-distance * Math.cos(newAngle))
									- getFontMetrics(getFont()).stringWidth(values);
						}
						endX = entityCenterX + (float) (-distance * Math.cos(angle));
						endY = entityCenterY + (float) (-distance * Math.sin(angle));

//						g2d.setColor(Color.YELLOW);
//						g2d.draw(new Line2D.Float(entityCenterX, entityCenterY, endX, endY));
					} else {
						if (a > 0) {
							newAngle = Math.atan(a) - Math.PI / 11;
							newEndY = entityCenterY + (float) (distance * Math.sin(newAngle)) + 10.0f;

						} else {
							newAngle = Math.atan(a) + Math.PI / 11;
							newEndY = entityCenterY + (float) (distance * Math.sin(newAngle));
						}
//						g2d.setColor(Color.GREEN);
//						g2d.draw(new Line2D.Float(entityCenterX, entityCenterY, endX, endY));

						newEndX = entityCenterX + (float) (distance * Math.cos(newAngle));

					}
//					g2d.setColor(Color.PINK);
//					g2d.draw(new Line2D.Float(entityCenterX, entityCenterY, newEndX, newEndY));

					g2d.setColor(Color.BLACK);
					g2d.drawString(values, newEndX + 2, newEndY);

				}

			}

		}
	}

	/**
	 * Calculates and update the max width of the entity or association to be drawn
	 * from attribute names
	 * 
	 * @param component the component whose width must be updated
	 */
	private void maxComponentDimensionUpdate(ShapeGroup component) {
		ArrayList<Attribute> attributeList = componentMap.get(component).getListAttribute();

		// Calculating width
		int maxTextWidth = getFontMetrics(getFont()).stringWidth(component.getGroupName());
		System.out.println("[ShapePanel]  title font metrics : " + maxTextWidth);
		for (Attribute attribute : attributeList) {
			int nameWidth = getFontMetrics(getFont()).stringWidth(attribute.getName());
			if (nameWidth > maxTextWidth) {
				maxTextWidth = nameWidth;
			}
		}

		component.setWidth(maxTextWidth + 20.0f);

		// And now the height
		if (attributeList.size() == 1) {
			component.setHeight(50.0f + 27.0f);
		} else {
			component.setHeight(50.0f + attributeList.size() * 21.0f);
		}

	}

	public void zoom() {
		// The Java upper limit size for fonts is 20, so it's relevent to stop zooming
		// at max font size
		if (mainFont.getSize() < 20) {
			int newWidth = mainPanel.getWidth() + 200;
			int newHeight = mainPanel.getHeight() + 100;

			float percentWidth = (float) newWidth / mainPanel.getWidth() * 100;
			float percentHeight = (float) newHeight / mainPanel.getHeight() * 100;
			System.out.println("[Zoom] propWidth = " + percentWidth + "% ; propHeight = " + percentHeight + "%");

//			int newFontSize = (int) (mainFont.getSize2D() / 100 * percentWidth);
			int newFontSize = mainFont.getSize() + 1;
//			float mf = mainFont.getSize2D() / 100 * percentWidth;
			mainFont = new Font(Font.DIALOG, Font.PLAIN, (int) newFontSize);
			mainPanel.setFont(mainFont);

			System.out.println("[Zoom] Font size = " + mainFont.getSize2D());
//			System.out.println("[Zoom] Font size not int = " + mf);

			for (ShapeGroup shape : componentMap.keySet()) {
				float x = shape.getX() / 100 * percentWidth;
				float y = shape.getY() / 100 * percentHeight;
				// TODO : màj des dimensions des carrés
//				double w = shape.getMainShape().getWidth() / 100 * percentWidth;
//				double h = shape.getMainShape().getHeight() / 100 * percentHeight;

				// Only need to update coordinates, the ShapePanel has an auto method which
				// recalculate all the time the correct dimensions for shapeGroups from the font size
				shape.setGroupAbscissa(x);
				shape.setGroupOrdinate(y);

//				((Rectangle2D)shape).setRect(x, y, w, h);

			}
			Dimension newD = new Dimension(newWidth, newHeight);
//			mainPanel.removeAll();
//			mainPanel.validate();
			mainPanel.setPreferredSize(newD);
//			mainPanel.revalidate();
			mainPanel.repaint();

		}

	}

	public void dezoom(Dimension frameCurrentSize) {

		// Minimum dezoom allowed on this software, preventing illegible content
		if (mainFont.getSize() > 11) {
			int newWidth = mainPanel.getWidth() - 200;
			int newHeight = mainPanel.getHeight() - 100;

			System.out.println("[Dezoom]  frameWidth = " + frameCurrentSize.getWidth() + "px ; frameHeight = " + frameCurrentSize.getHeight() + "px");
			System.out.println("[Dezoom]  futurWidth = " + newWidth + "px ; futurHeight = " + newHeight + "px");
			if (newWidth >= frameCurrentSize.getWidth() && newHeight >= frameCurrentSize.getHeight()) {

				float percentWidth = (float) newWidth / mainPanel.getWidth() * 100;
				float percentHeight = (float) newHeight / mainPanel.getHeight() * 100;
				System.out.println("[Dezoom]  propWidth = " + percentWidth + "% ; propHeight = " + percentHeight + "%");

//				int newFontSize = (int) (testFont.getSize2D() / 100 * percentWidth);
				int newFontSize = mainFont.getSize() - 1;
//				float nfs = testFont.getSize2D() / 100 * percentWidth;
				mainFont = new Font(Font.DIALOG, Font.PLAIN, (int) newFontSize);
				mainPanel.setFont(mainFont);
				System.out.println("[Dezoom] (144) Font size = " + mainFont.getSize2D());
//				System.out.println("[Dezoom] (145) Font size not int = " + nfs);

				for (ShapeGroup shape : componentMap.keySet()) {
					float x = shape.getX() / 100 * percentWidth;
					float y = shape.getY() / 100 * percentHeight;
//					double w = shape.getBounds2D().getWidth() / 100 * percentWidth;
//					double h = shape.getBounds2D().getHeight() / 100 * percentHeight;

					shape.setGroupAbscissa(x);
					shape.setGroupOrdinate(y);

//					((Rectangle2D) shape).setRect(x, y, w, h);

				}
				Dimension newD = new Dimension(newWidth, newHeight);
//				mainPanel.removeAll();
//				mainPanel.validate();
				mainPanel.setPreferredSize(newD);
//				mainPanel.revalidate();
				mainPanel.repaint();
			}
		}

	}

//	public boolean associationDoubleLinkedWith(ShapeGroup association) {
//		boolean result;
//		
//		
//		
//		return result;
//	}
	/**
	 * @return the componentMap
	 */
	public Map<ShapeGroup, Node> getComponentMap() {
		return componentMap;
	}

	/**
	 * @return the linkMap
	 */
	public Map<ShapeGroup, List<ShapeGroup>> getLinkMap() {
		return linkMap;
	}

	/**
	 * @return the alLines
	 */
	public List<Line2D.Float> getAlLines() {
		return alLines;
	}

	/**
	 * @return the mainPanel
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}

	/**
	 * @param mainPanel the mainPanel to set
	 */
	public void setMainPanel(JPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	/**
	 * @return the entityHeadColor
	 */
	public Color getEntityHeadColor() {
		return entityHeadColor;
	}

	/**
	 * @param entityHeadColor the entityHeadColor to set
	 */
	public void setEntityHeadColor(Color entityHeadColor) {
		this.entityHeadColor = entityHeadColor;
	}

	/**
	 * @return the entityBodyColor
	 */
	public Color getEntityBodyColor() {
		return entityBodyColor;
	}

	/**
	 * @param mainFont the mainFont of the shapePanel to set
	 */
	public void setMainFont(Font mainFont) {
		this.mainFont = mainFont;
	}

	/**
	 * @param entityBodyColor the entityBodyColor to set
	 */
	public void setEntityBodyColor(Color entityBodyColor) {
		this.entityBodyColor = entityBodyColor;
	}

	/**
	 * @return the associationHeadColor
	 */
	public Color getAssociationHeadColor() {
		return associationHeadColor;
	}

	/**
	 * @param associationHeadColor the associationHeadColor to set
	 */
	public void setAssociationHeadColor(Color associationHeadColor) {
		this.associationHeadColor = associationHeadColor;
	}

	/**
	 * @return the associationBodyColor
	 */
	public Color getAssociationBodyColor() {
		return associationBodyColor;
	}

	/**
	 * @param associationBodyColor the associationBodyColor to set
	 */
	public void setAssociationBodyColor(Color associationBodyColor) {
		this.associationBodyColor = associationBodyColor;
	}
}

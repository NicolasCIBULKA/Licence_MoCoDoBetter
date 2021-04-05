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
 * ShapePanel contains all the graphical objects to paint and manages their
 * update and painting.
 * 
 * @author Yann Barrachina
 *
 */
public class ShapePanel extends JPanel {
	private JPanel mainPanel;

	private static final int ORIGINAL_WIDTH = 3000;
	private static final int ORIGINAL_HEIGHT = 1500;
	private static final int LINE_GAP = 20;

	private static final Dimension SHAPE_PANEL_SIZE = new Dimension(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);

	private Font mainFont;

	// LinkedHashMap to prevent an unsorted iteration for the mousePressed listener
	private Map<ShapeGroup, Node> componentMap = new LinkedHashMap<ShapeGroup, Node>();
	private Map<ShapeGroup, List<ShapeGroup>> linkMap = new HashMap<ShapeGroup, List<ShapeGroup>>();

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
		mainPanel = this;
		mainPanel.setDoubleBuffered(true);
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setPreferredSize(SHAPE_PANEL_SIZE);
		mainFont = new Font(Font.DIALOG, Font.PLAIN, 15);
		mainPanel.setFont(mainFont);
//		mainPanel.setBackground(Color.WHITE);

//		mainPanel.add(jsp, BorderLayout.NORTH);

		// TODO : [ShapePanel] colorChooser (if still exist...)
	}

	/**
	 * Painting method of ShapePanel Calls successively the drawers for lines,
	 * cardinalities, entities and associations. Also calls the redimensioning
	 * method for updating components dimensions according to their content.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Painting entities, associations, links and cardinalities
		drawLinks();

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
	 * @param newName    the name of this new component
	 * @param x          the X coordinate of the new component
	 * @param y          the Y coordinate of the new component
	 * @param entityType defines if the component is an entity or an association
	 */
	public ShapeGroup addShapeGroup(String newName, float x, float y, boolean entityType) {
		ShapeGroup component = new ShapeGroup(newName, x, y, entityType);
		repaint();
		return component;
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

	/**
	 * Scanning the linkMap and calling drawing methods according to links
	 * configurations. Determining if lines or arcs must be drawn, based on linkMap
	 * content.
	 */
	private void drawLinks() {

		for (ShapeGroup shape : linkMap.keySet()) {
			float associationCenterX = (float) shape.getMainShape().getBounds2D().getCenterX();
			float associationCenterY = (float) shape.getMainShape().getBounds2D().getCenterY();

			List<ShapeGroup> testList = new ArrayList<ShapeGroup>();
			testList = linkMap.get(shape);


			// Case of two links between one Association-Entity couple
			if (testList.size() == 2 && testList.get(0) == testList.get(1)) {

				ShapeGroup linkedShape = linkMap.get(shape).get(0);

				float entityCenterX = (float) linkedShape.getMainShape().getBounds2D().getCenterX();
				float entityCenterY = (float) linkedShape.getMainShape().getBounds2D().getCenterY();

				drawArcsAndCardinalities(shape, linkedShape, associationCenterX, associationCenterY, entityCenterX,
						entityCenterY);

//				drawArcsAndCardinalities(1, shape, linkedShape, associationCenterX, associationCenterY, entityCenterX,
//						entityCenterY);
//				drawArcsAndCardinalities(2, shape, linkedShape, associationCenterX, associationCenterY, entityCenterX,
//						entityCenterY);
			} else {
				// Standard drawing
				for (ShapeGroup linkedShape : linkMap.get(shape)) {

					float entityCenterX = (float) linkedShape.getMainShape().getBounds2D().getCenterX();
					float entityCenterY = (float) linkedShape.getMainShape().getBounds2D().getCenterY();

					drawLinesAndCardinalities(shape, linkedShape, associationCenterX, associationCenterY, entityCenterX,
							entityCenterY);

				}
			}
		}
	}

	/**
	 * Drawing lines and calling drawCardinality mathod
	 * 
	 * @param shape              the association
	 * @param linkedShape        the entity
	 * @param associationCenterX x coordinate of association's center
	 * @param associationCenterY y coordinate of association's center
	 * @param entityCenterX      x coordinate of entity's center
	 * @param entityCenterY      y coordinate of entity's center
	 */
	private void drawLinesAndCardinalities(ShapeGroup shape, ShapeGroup linkedShape, float associationCenterX,
			float associationCenterY, float entityCenterX, float entityCenterY) {
		// TODO : [lines] case of vertical links
		g2d.setColor(Color.GRAY);
		g2d.draw(new Line2D.Float(associationCenterX, associationCenterY, entityCenterX, entityCenterY));

		// Retrieving cardinality values for this linkedShape
		String cardinalityValue = "";
		for (Cardinality card : ((Association) componentMap.get(shape)).getCardinalityList()) {
			if (card.getNomEntity().equals(linkedShape.getGroupName())) {
				cardinalityValue += card.getLowValue() + "," + card.getHighValue();
			}
		}

		drawCardinality(linkedShape, cardinalityValue, Math.PI / 11, associationCenterX, associationCenterY,
				entityCenterX, entityCenterY, true, true);
	}

	/**
	 * Drawing lines and cardinalities in special case of two links between one
	 * association and one entity. In this case, lines become arcs.
	 * 
	 * @param shape              the association
	 * @param linkedShape        the entity
	 * @param associationCenterX x coordinate of association's center
	 * @param associationCenterY y coordinate of association's center
	 * @param entityCenterX      x coordinate of entity's center
	 * @param entityCenterY      y coordinate of entity's center
	 */
	private void drawArcsAndCardinalities(ShapeGroup shape, ShapeGroup linkedShape, float associationCenterX,
			float associationCenterY, float entityCenterX, float entityCenterY) {

//		g2d.setColor(Color.BLUE);
//		g2d.draw(new Line2D.Float(associationCenterX, associationCenterY, entityCenterX, entityCenterY));

		List<Cardinality> cardList = new ArrayList<Cardinality>();
		cardList = ((Association) componentMap.get(shape)).getCardinalityList();

		String value1 = cardList.get(0).getLowValue() + "," + cardList.get(0).getHighValue();
		String value2 = cardList.get(1).getLowValue() + "," + cardList.get(1).getHighValue();

		// TODO : [drawArcs] retirer les "= 20"
		float stringX1 = 0;
		float stringY1 = 0;
		float stringX2 = 0;
		float stringY2 = 0;

		int x1 = (int) associationCenterX;
		int y1 = (int) associationCenterY;
		int x2 = (int) associationCenterX;
		int y2 = (int) associationCenterY;

		int width1 = (int) (associationCenterX - entityCenterX);
		int height1 = (int) (associationCenterY - entityCenterY);
		int width2 = (int) (associationCenterX - entityCenterX);
		int height2 = (int) (associationCenterY - entityCenterY);

		int startAngle1;
		int arcAngle1;
		int startAngle2;
		int arcAngle2;
//		.println("(1) x = " + x1 + " y = " + y1 + " width = " + width1 + " height = " + height1);

		// TODO : problème avec la partie jaune, à voir
		// If the association and the entity are close to be vertically or horizontally
		// aligned
		// And if they do not form a diagonal with the coordinate system
		if ((Math.abs(width1) < 100 && Math.abs(height1) >= 100) && Math.abs(width1) != Math.abs(height1)) {
			x1 += LINE_GAP;
			x2 -= LINE_GAP;

			int endX1 = (int) entityCenterX + LINE_GAP;
			int endY1 = (int) entityCenterY;
			int endX2 = (int) entityCenterX - LINE_GAP;
			int endY2 = (int) entityCenterY;

			g2d.setColor(Color.GRAY);
			g2d.draw(new Line2D.Float(x1, y1, endX1, endY1));
			g2d.draw(new Line2D.Float(x2, y2, endX2, endY2));

			drawCardinality(linkedShape, value1, Math.PI / 11, x1, y1, endX1, endY1, true, true);
			drawCardinality(linkedShape, value2, Math.PI / 11, x2, y2, endX2, endY2, true, true);

		} else if ((Math.abs(width1) >= 100 && Math.abs(height1) < 100) && Math.abs(width1) != Math.abs(height1)) {

			y1 += LINE_GAP;
			y2 -= LINE_GAP;

			int endX1 = (int) entityCenterX;
			int endY1 = (int) entityCenterY + LINE_GAP;
			int endX2 = (int) entityCenterX;
			int endY2 = (int) entityCenterY - LINE_GAP;

			g2d.setColor(Color.GRAY);
			g2d.draw(new Line2D.Float(x1, y1, endX1, endY1));
			g2d.draw(new Line2D.Float(x2, y2, endX2, endY2));

			drawCardinality(linkedShape, value1, Math.PI / 11, x1, y1, endX1, endY1, true, true);
			drawCardinality(linkedShape, value2, Math.PI / 11, x2, y2, endX2, endY2, true, true);

		} else { // Standard calculation

			// Computing coordinates for first arc
			// Taking advantage of the if-else conditions here to calculate cardinalities
			// positions
			width1 *= 2;

			if (width1 < 0) {
				width1 = Math.abs(width1);

				if (height1 < 0) {
					// Bas Droite
					height1 = Math.abs(height1);

					y1 -= height1;
					startAngle1 = 180;
					arcAngle1 = 90;

					stringX1 = entityCenterX + 1;
					stringY1 = entityCenterY - linkedShape.getHeight() / 2 - 4;
					stringX2 = entityCenterX - linkedShape.getWidth() / 2
							- getFontMetrics(getFont()).stringWidth(value2) - 3;
					stringY2 = entityCenterY + mainFont.getSize() + 1;
				} else {
					// Haut Droite
					y1 = (int) entityCenterY;
					startAngle1 = 180;
					arcAngle1 = -90;

					stringX1 = entityCenterX + 1;
					stringY1 = entityCenterY + linkedShape.getHeight() / 2 + mainFont.getSize() + 1;
					stringX2 = entityCenterX - linkedShape.getWidth() / 2
							- getFontMetrics(getFont()).stringWidth(value2) - 3;
					stringY2 = entityCenterY - 1;
				}

			} else {
				if (height1 < 0) {
					// Bas Gauche
					height1 = Math.abs(height1);
					x1 -= width1;
					y1 -= height1;
					startAngle1 = 0;
					arcAngle1 = -90;

					stringX1 = entityCenterX - getFontMetrics(getFont()).stringWidth(value2) - 1;
					stringY1 = entityCenterY - linkedShape.getHeight() / 2 - 4;
					stringX2 = entityCenterX + linkedShape.getWidth() / 2 + 1;
					stringY2 = entityCenterY + mainFont.getSize() + 1;
				} else {
					// Haut Gauche
					x1 -= width1;
					y1 = (int) entityCenterY;
					startAngle1 = 0;
					arcAngle1 = 90;

					stringX1 = entityCenterX - getFontMetrics(getFont()).stringWidth(value2) - 1;
					stringY1 = entityCenterY + linkedShape.getHeight() / 2 + mainFont.getSize() + 1;
					stringX2 = entityCenterX + linkedShape.getWidth() / 2 + 1;
					stringY2 = entityCenterY - 1;
				}

			}
			height1 *= 2;

//			.println("Newx = " + x1 + " Newy = " + y1 + " Newwidth = " + width1 + " Newheight = " + height1);

//			.println("x = " + x2 + " y = " + y2 + " width = " + width2 + " height = " + height2);

			// Computing coordinates for second arc
			height2 *= 2;

			if (width2 < 0) {
				width2 = Math.abs(width2);

				if (height2 < 0) {

					height2 = Math.abs(height2);
					x2 -= width2;
					startAngle2 = 90;
					arcAngle2 = -90;
				} else {
					x2 -= width2;
					y2 -= height2;
					startAngle2 = 270;
					arcAngle2 = 90;
				}

			} else {
				if (height2 < 0) {
					height2 = Math.abs(height2);
					x2 -= width2;
					startAngle2 = 90;
					arcAngle2 = 90;
				} else {
					x2 -= width2;
					y2 -= height2;
					startAngle2 = 270;
					arcAngle2 = -90;
				}

			}
			width2 *= 2;

			g2d.setColor(Color.GRAY);
			g2d.drawArc(x1, y1, width1, height1, startAngle1, arcAngle1);
			g2d.drawArc(x2, y2, width2, height2, startAngle2, arcAngle2);
		}

//		g2d.setColor(Color.RED);
//		g2d.drawRect(x2, y2, width2, height2);
//
//		g2d.setColor(Color.GREEN);
//		g2d.drawRect(x1, y1, width1, height1);
//		

		g2d.setColor(Color.BLACK);
		g2d.drawString(value1, stringX1, stringY1);
		g2d.drawString(value2, stringX2, stringY2);

	}

	private void drawCardinality(ShapeGroup linkedShape, String cardinalityValues, double targetAngle,
			float associationCenterX, float associationCenterY, float entityCenterX, float entityCenterY,
			Boolean onlyRightCardinality, Boolean onlyLeftCardinality) {
		// Here we calculate the most relevent position to display cardinality
		// Have to find first the intersection between entity perimeter and link line

		// if the slope is infinite, x coordinates are aligned
		if (associationCenterX == entityCenterX) {
			float intersectionY;
			g2d.setColor(Color.BLACK);

			// If association is under entity
			if (associationCenterY > entityCenterY) {
				intersectionY = entityCenterY + linkedShape.getHeight() / 2;
//				g2d.drawOval((int) (entityCenterX - 2.5), (int) (intersectionY - 2.5), 5, 5);
				g2d.drawString(cardinalityValues, entityCenterX + 12, (int) (intersectionY + 22));
			} else {
				intersectionY = entityCenterY - linkedShape.getHeight() / 2;
//				g2d.drawOval((int) (entityCenterX - 2.5), (int) (intersectionY - 2.5), 5, 5);
				g2d.drawString(cardinalityValues, entityCenterX + 13, (int) (intersectionY - 12));
			}

		} else { // Else it calculates as usual

			// line's equation : y = ax + b ; finding "a" and "b"
			float a = (associationCenterY - entityCenterY) / (associationCenterX - entityCenterX);
			float b = entityCenterY - (a * entityCenterX);

//			g2d.drawString("a = " + a, 10, 20);
//			g2d.drawString("b = " + b, 10, 40);

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

			// This part determines which point must be selected

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
//					g2d.drawString("p1 = " + vX + " ; " + vY, 10, 80);
//
//					g2d.drawOval((int) (v.getX() - 2.5), (int) (v.getY() - 2.5), 5, 5);
					distance = v.distance(entityCenterX, entityCenterY);
					validPoint = v;

				} else {
//					g2d.drawString("p2 = " + wX + " ; " + wY, 10, 100);
//
//					g2d.drawOval((int) (w.getX() - 2.5), (int) (w.getY() - 2.5), 5, 5);

					distance = w.distance(entityCenterX, entityCenterY);
					validPoint = w;
				}

			} else if ((x - halfWidth <= h.getX()) && (h.getX() <= x + halfWidth)) {

				if (h.distanceSq(associationX, associationY) < g.distanceSq(associationX, associationY)) {
//					g2d.drawString("p3 = " + hX + " ; " + hY, 10, 160);
//
//					g2d.drawOval((int) (h.getX() - 2.5), (int) (h.getY() - 2.5), 5, 5);

					distance = h.distance(entityCenterX, entityCenterY);
					validPoint = h;
				} else {
//					g2d.drawString("p4 = " + gX + " ; " + gY, 10, 180);
//
//					g2d.drawOval((int) (g.getX() - 2.5), (int) (g.getY() - 2.5), 5, 5);

					distance = g.distance(entityCenterX, entityCenterY);
					validPoint = g;
				}
			}

			distance += 15;

			double angle = Math.atan(a);
			double newAngle;

			// This part calculates the relevant coordinates to display cardinality value
			// It is done according to the orientation of the association-entity couple

			float endX = entityCenterX + (float) (distance * Math.cos(angle));
			float endY = entityCenterY + (float) (distance * Math.sin(angle));

			// These coordinates are dynamically updated according to cardinality string
			// size
			float newEndX;
			float newEndY;

			double isOnSegmentX = endX - associationX / validPoint.getX() - validPoint.getX();
			double isOnSegmentY = endY - associationY / validPoint.getY() - validPoint.getY();

//			Boolean draw = false;
			//
//			g2d.drawString("isOnSegmentX =  " + isOnSegmentX, 200, 60);
//			g2d.drawString("isOnSegmentY =  " + isOnSegmentY, 200, 80);
//			g2d.drawString("angle in degrees = " + Math.toDegrees(angle), 200, 40);

			// Considering the A/E couple on a trigonometric circle
			// If we are above π/2 and under -π/2 (if entity is on the right)
			if (isOnSegmentY > 14 || isOnSegmentY < -16 || isOnSegmentX > 14) {
//
//				if (onlyLeftCardinality && !onlyRightCardinality) {
//					draw = true;
//				}

				// Upper right corner
				if (a > 0) {
					newAngle = Math.atan(a) - targetAngle;
					newEndY = entityCenterY + (float) (-distance * Math.sin(newAngle));
					newEndX = entityCenterX + (float) (-distance * Math.cos(newAngle))
							- getFontMetrics(getFont()).stringWidth(cardinalityValues);
				} else {
					// Lower right corner
					newAngle = Math.atan(a) + targetAngle;
					newEndY = entityCenterY + (float) (-distance * Math.sin(newAngle)) + 10.0f;
					newEndX = entityCenterX + (float) (-distance * Math.cos(newAngle))
							- getFontMetrics(getFont()).stringWidth(cardinalityValues);
				}
				endX = entityCenterX + (float) (-distance * Math.cos(angle));
				endY = entityCenterY + (float) (-distance * Math.sin(angle));

//				g2d.setColor(Color.YELLOW);
//				g2d.draw(new Line2D.Float(entityCenterX, entityCenterY, endX, endY));
			} else {

//				if (onlyRightCardinality && !onlyLeftCardinality) {
//					draw = true;
//				}

				// Upper left corner
				if (a > 0) {
					newAngle = Math.atan(a) - targetAngle;
					newEndY = entityCenterY + (float) (distance * Math.sin(newAngle)) + 10.0f;

				} else {
					// Lower left corner
					newAngle = Math.atan(a) + targetAngle;
					newEndY = entityCenterY + (float) (distance * Math.sin(newAngle));
				}
//				g2d.setColor(Color.GREEN);
//				g2d.draw(new Line2D.Float(entityCenterX, entityCenterY, endX, endY));

				newEndX = entityCenterX + (float) (distance * Math.cos(newAngle));

			}
//			if ((onlyLeftCardinality && onlyRightCardinality) || draw) {
//				g2d.setColor(Color.PINK);
//				g2d.draw(new Line2D.Float(entityCenterX, entityCenterY, newEndX, newEndY));
//				g2d.setColor(entityBoundColor);
			g2d.setColor(Color.BLACK);
			g2d.drawString(cardinalityValues, newEndX + 2, newEndY);
//				g2d.drawString("newAngle in degrees = " + Math.toDegrees(newAngle), 200, 100);
//				.println("newEndX = " + newEndX);
//			}

		}
	}

	public Boolean canBeLinked(List<ShapeGroup> shapesToBeLinked) {
		// TODO : [canBeLinked] retourner des nombres pour signifier à l'utilisateur
		// pourquoi il ne
		// peut pas lier les objets ?
		Boolean result = true;
		List<ShapeGroup> testList = new ArrayList<ShapeGroup>();
		testList = linkMap.get(shapesToBeLinked.get(0));

		if (testList != null) {

			if (testList.size() == 2) {

				if ((testList.get(0) == testList.get(1)) || testList.contains(shapesToBeLinked.get(1))) {
					result = false;
				}

			} else if (testList.size() >= 3) {

				if (testList.contains(shapesToBeLinked.get(1))) {
					result = false;
				}
			}
		}

		return result;
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

//			int newFontSize = (int) (mainFont.getSize2D() / 100 * percentWidth);
			int newFontSize = mainFont.getSize() + 1;
//			float mf = mainFont.getSize2D() / 100 * percentWidth;
			mainFont = new Font(Font.DIALOG, Font.PLAIN, (int) newFontSize);
			mainPanel.setFont(mainFont);

//			.println("[Zoom] Font size not int = " + mf);

			for (ShapeGroup shape : componentMap.keySet()) {
				float x = shape.getX() / 100 * percentWidth;
				float y = shape.getY() / 100 * percentHeight;
//				double w = shape.getMainShape().getWidth() / 100 * percentWidth;
//				double h = shape.getMainShape().getHeight() / 100 * percentHeight;

				// Only need to update coordinates, the ShapePanel has an auto method which
				// recalculate all the time the correct dimensions for shapeGroups from the font
				// size
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

			if (newWidth >= frameCurrentSize.getWidth() && newHeight >= frameCurrentSize.getHeight()) {

				float percentWidth = (float) newWidth / mainPanel.getWidth() * 100;
				float percentHeight = (float) newHeight / mainPanel.getHeight() * 100;

//				int newFontSize = (int) (testFont.getSize2D() / 100 * percentWidth);
				int newFontSize = mainFont.getSize() - 1;
//				float nfs = testFont.getSize2D() / 100 * percentWidth;
				mainFont = new Font(Font.DIALOG, Font.PLAIN, (int) newFontSize);
				mainPanel.setFont(mainFont);
//				.println("[Dezoom] (145) Font size not int = " + nfs);

				for (ShapeGroup shape : componentMap.keySet()) {
					float x = shape.getX() / 100 * percentWidth;
					float y = shape.getY() / 100 * percentHeight;
//					double w = shape.getBounds2D().getWidth() / 100 * percentWidth;
//					double h = shape.getBounds2D().getHeight() / 100 * percentHeight;

					// Only need to update coordinates, the ShapePanel has an auto method which
					// recalculate all the time the correct dimensions for shapeGroups from the font
					// size
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

	/**
	 * Erase the drawing by deleting all components of componentMap and linkMap
	 */
	public void clear() {
		componentMap.clear();
		linkMap.clear();
		mainPanel.repaint();
	}

	public boolean isDoubleLinkedAssociation(ShapeGroup association) {
		boolean result = false;

		if (linkMap.get(association).size() == 2) {
			if (linkMap.get(association).get(0) == linkMap.get(association).get(1)) {
				result = true;
			}
		}

		return result;

	}

	/**
	 * Checks if a link between an association and an entity exist.
	 * 
	 * @param association to check the linkage
	 * @param entity      to check the linkage with the association
	 * @return
	 */
	public boolean existLinkBetween(ShapeGroup association, ShapeGroup entity) {
		boolean result = false;

		if (linkMap.containsKey(association)) {
			if (linkMap.get(association).contains(entity)) {
				result = true;
			}
		}

		return result;
	}

	public void disconnectShapes(List<ShapeGroup> shapeList) {

		if (linkMap.containsKey(shapeList.get(0))) {
			if (existLinkBetween(shapeList.get(0), shapeList.get(1))) {

				if (isDoubleLinkedAssociation(shapeList.get(0)) || linkMap.get(shapeList.get(0)).size() == 1) {
					linkMap.remove(shapeList.get(0));
					((Association) componentMap.get(shapeList.get(0))).getCardinalityList().clear();

				} else {
					// Removing entity from linkMap
					linkMap.get(shapeList.get(0)).remove(shapeList.get(1));

					ArrayList<Cardinality> newCardinalityList = new ArrayList<Cardinality>();
					for (Cardinality cardinality : ((Association) componentMap.get(shapeList.get(0)))
							.getCardinalityList()) {
						if (!cardinality.getNomEntity().equals(shapeList.get(1).getGroupName())) {
							newCardinalityList.add(cardinality);
						}
					}

//					int index = 0, range = 0;
//					for (Cardinality cardinality : ((Association) componentMap.get(shapeList.get(0)))
//							.getCardinalityList()) {
//						.println("--- index : " + index + " range : " + range);
//						.println("--- cardName : " + cardinality.getNomEntity() + " shapeList : "
//								+ shapeList.get(1).getGroupName());
//						if (cardinality.getNomEntity().equals(shapeList.get(1).getGroupName())) {
//							index = range;
//							.println("--- Cardinality founded in " + shapeList.get(0).getGroupName() + " for "
//									+ cardinality.getNomEntity());
//						}
//						range++;
//					}

//					((Association) componentMap.get(shapeList.get(0))).getCardinalityList().remove(index);
					((Association) componentMap.get(shapeList.get(0))).setCardinalityList(newCardinalityList);
				}

			}
		}

	}

	public void removeShape(ShapeGroup shape) {
		if (shape.isAnEntity()) {

			// for each association linked to the entity to be removed
			for (ShapeGroup keyShape : linkMap.keySet()) {

				if (linkMap.get(keyShape).contains(shape)) {

					List<ShapeGroup> shapeList = new ArrayList<ShapeGroup>();
					shapeList.add(keyShape);
					shapeList.add(shape);
					disconnectShapes(shapeList);
				}
			}

		} else {
			linkMap.remove(shape);
		}
		componentMap.remove(shape);
	}

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

	public String linkMapToString() {
		String intial = "{";

		StringBuffer stringBuilder = new StringBuffer(intial);
		for (ShapeGroup shape : linkMap.keySet()) {
			stringBuilder.append(shape.getGroupName() + "=");
			if (linkMap.get(shape) != null) {
				for (ShapeGroup linkedShape : linkMap.get(shape)) {
					stringBuilder.append(linkedShape.getGroupName() + ",");
				}
			}
			stringBuilder.append(" ; ");
		}
		stringBuilder.append("}");
		String result = stringBuilder.toString();
		return result;
	}
}

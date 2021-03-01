package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import data.Node;
import data.Attribute;

/**
 * ShapePanel contains all the graphical objects to paint and manage their
 * update (for now).
 * 
 * @author Yann Barrachina
 *
 */
public class ShapePanel extends JPanel {
	private JPanel mainPanel;
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

	private Color associationHeadColor = new Color(233,	186, 217);
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

		// TODO : colorChooser (if still exist...)
	}

	public void paint(Graphics g) {
		super.paint(g);
		g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Painting lines which links objects
		System.out.println("[ShapePanel]  ---- " + linkMap.size() + " link(s)");
		g2d.setColor(Color.BLACK);
		for (ShapeGroup shape : linkMap.keySet()) {
			float firstObjectCenterX = (float) shape.getMainShape().getBounds2D().getCenterX();
			float firstObjectCenterY = (float) shape.getMainShape().getBounds2D().getCenterY();
			System.out.println("[ShapePanel]  first : x = " + firstObjectCenterX + " Y = " + firstObjectCenterY);
			for (ShapeGroup linkedShape : linkMap.get(shape)) {
				System.out.println("[ShapePanel] I'm drawing !");
				float secondObjectCenterX = (float) linkedShape.getMainShape().getBounds2D().getCenterX();
				float seconfObjectCenterY = (float) linkedShape.getMainShape().getBounds2D().getCenterY();
				System.out.println("[ShapePanel]  second : x = " + secondObjectCenterX + " Y = " + seconfObjectCenterY);
				g2d.draw(new Line2D.Float(firstObjectCenterX, firstObjectCenterY, secondObjectCenterX,
						seconfObjectCenterY));
			}

		}

		// Painting entities and associations
		System.out.println("[ShapePanel]  ---- " + componentMap.size() + " component(s)");
		
		for (ShapeGroup component : componentMap.keySet()) {
//			float nameWidth = g.getFontMetrics().stringWidth(componentMap.get(component).getName()) ;
//			if( nameWidth > component.getWidth()) {
//				component.setWidth(nameWidth);
//			}
			maxComponentDimensionUpdate(component);
			
			if (component.isAnEntity()) {
				drawEntity(component);

			} else {
				drawAssociation(component);

			}

		}

//		for (Shape shape : al) {
//
//			String shapeTypeTable[] = shape.getClass().getCanonicalName().split("\\.");
//			String shapeType = shapeTypeTable[3] + shapeTypeTable[4];
//
//			switch (shapeType) {
//
//			case "Rectangle2DFloat":
//
//				// TODO : paintRectangle() ;
//
//				Graphics2D g2d = (Graphics2D) g;
//
//				
//
//				float dash1[] = { 10.0f };
//				BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
//						0.0f);
//
//				g2d.setStroke(dashed);
//
//				g2d.draw(new Rectangle2D.Double(shape.getBounds2D().getX() - 1, shape.getBounds2D().getY() - 1,
//						shape.getBounds2D().getWidth() + 1, shape.getBounds2D().getHeight() + 1));
//
//				g2d.setColor(new Color(100, 100, 200));
//				g2d.fill(shape);
//				shape.setRect(0,0,0,0) ;
//
//				break;
//
//			case "Line2DFloat":
//
//				// TODO : paintLine() ;
//
//				
//
//				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//				g2d.setColor(new Color(200, 100, 100));
//
//				g2d.draw(shape);
//
//				break;
//
//			default:
//				System.err.println("Unknown shape type : " + shapeType);
//			}
//
//		}

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

	public void drawEntity(ShapeGroup component) {
		
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
		
		for(int index = 0 ; index < attributeQuantity ; index++) {
			
			g2d.drawString(attributeList.get(index).getName(), component.getX() + 10.0f, component.getY() + 70.0f + (index*20.0f));
			
			// Underlines the Primary Key attribute
			if(attributeList.get(index).isPrimaryKey()) {
				
				int x = (int) (component.getX() + 10.0f);
				int y = (int) (component.getY() + 70.0f + (index*20.0f));
				int attributeWidth = getFontMetrics(getFont()).stringWidth(attributeList.get(index).getName());
				g2d.drawLine(x, y + 2, x + attributeWidth, y + 2);
			}
		}

	}

	public void drawAssociation(ShapeGroup component) {

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
		
		for(int index = 0 ; index < attributeQuantity ; index++) {
			
			g2d.drawString(attributeList.get(index).getName(), component.getX() + 10.0f, component.getY() + 70.0f + (index*20.0f));
			
			// Underlines the Primary Key attribute
			if(attributeList.get(index).isPrimaryKey()) {
				
				int x = (int) (component.getX() + 10.0f);
				int y = (int) (component.getY() + 70.0f + (index*20.0f));
				int attributeWidth = getFontMetrics(getFont()).stringWidth(attributeList.get(index).getName());
				g2d.drawLine(x, y + 2, x + attributeWidth, y + 2);
			}
		}
	}

	public void drawLines(List<ShapeGroup> linkedComponents, ShapeGroup mainComponent) {
		for (ShapeGroup shape : linkedComponents) {
			float firstObjectCenterX = (float) shape.getMainShape().getBounds2D().getCenterX();
			float firstObjectCenterY = (float) shape.getMainShape().getBounds2D().getCenterX();

			float secondObjectCenterX = (float) mainComponent.getMainShape().getBounds2D().getCenterX();
			float seconfObjectCenterY = (float) mainComponent.getMainShape().getBounds2D().getCenterX();
			g2d.setColor(Color.BLACK);
			g2d.draw(
					new Line2D.Float(firstObjectCenterX, firstObjectCenterY, secondObjectCenterX, seconfObjectCenterY));
		}
	}
	
	/**
	 * Calculates the max width of the entity or association to be drawn from attribute names
	 * 
	 * @param component the component to calcul the width
	 */
	private void maxComponentDimensionUpdate(ShapeGroup component){
		ArrayList<Attribute> attributeList = componentMap.get(component).getListAttribute();
		
		// Calculating width
		int maxTextWidth = getFontMetrics(getFont()).stringWidth(component.getGroupName());
		System.out.println("[ShapePanel]  title font metrics : " + maxTextWidth);
		for(Attribute attribute : attributeList) {
			int nameWidth = getFontMetrics(getFont()).stringWidth(attribute.getName());
			if( nameWidth > maxTextWidth) {
				maxTextWidth = nameWidth;
			}
		}
		
		component.setWidth(maxTextWidth + 20.0f);
		
		// And now the height
		if(attributeList.size() == 1) {
			component.setHeight(50.0f + 27.0f);
		}else {
			component.setHeight(50.0f + attributeList.size() * 21.0f);
		}
		
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

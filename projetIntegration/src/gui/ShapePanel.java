package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * ShapePanel contains all the graphical objects to paint and manage their
 * update (for now).
 * 
 * @author Yann Barrachina
 *
 */
public class ShapePanel extends JPanel {
	private JPanel mainPanel;

	private ArrayList<ShapeGroup> alComponents = new ArrayList<ShapeGroup>();

	private Color entityHeadColor = new Color(0, 150, 150);
	private Color entitySplitColor = new Color(0, 100, 100);
	private Color entityBodyColor = new Color(60, 190, 190);
	private Color entityBoundColor = new Color(0, 75, 75);

	private Color associationHeadColor = new Color(160, 30, 150);
	private Color associationSplitColor = new Color(140, 50, 140);
	private Color associationBodyColor = new Color(210, 110, 200);
	private Color associationBoundColor = new Color(100, 50, 100);

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

		System.out.println("---- " + alComponents.size() + " component(s)");
		for (ShapeGroup component : alComponents) {
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
	 * Creates a new component to display and appends it to the end of the
	 * ArrayList.
	 * 
	 * @param x          the X coordinate of the new component
	 * @param y          the Y coordinate of the new component
	 * @param entityType defines if the component is an entity or an association
	 */
	public void addShape(float x, float y, boolean entityType, String name) {
		ShapeGroup component = new ShapeGroup(x, y, entityType, name);
		alComponents.add(0, component);
	}

	public void drawEntity(ShapeGroup component) {

		g2d.setColor(entityBodyColor);
		g2d.fill(component.getMainShape());

		g2d.setColor(entityHeadColor);
		g2d.fill(component.getHeadShape());

		BasicStroke headStroke = new BasicStroke(3);
		g2d.setColor(entitySplitColor);
		g2d.setStroke(headStroke);
		g2d.draw(new Rectangle2D.Double(component.getHeadShape().getBounds2D().getX() - 1,
				component.getHeadShape().getBounds2D().getY() - 1,
				component.getHeadShape().getBounds2D().getWidth() + 1,
				component.getHeadShape().getBounds2D().getHeight() + 1));

		BasicStroke boundStroke = new BasicStroke(3);
		g2d.setColor(entityBoundColor);
		g2d.setStroke(boundStroke);
		g2d.draw(new Rectangle2D.Double(component.getMainShape().getBounds2D().getX() - 1,
				component.getMainShape().getBounds2D().getY() - 1,
				component.getMainShape().getBounds2D().getWidth() + 1,
				component.getMainShape().getBounds2D().getHeight() + 1));
		
		g2d.setColor(Color.black);
		g2d.drawString(component.getGroupName(), component.getX()+70.0f, component.getY()+30.0f);

	}

	public void drawAssociation(ShapeGroup component) {

		g2d.setColor(associationBodyColor);
		g2d.fill(component.getMainShape());

		g2d.setColor(associationHeadColor);
		g2d.fill(component.getHeadShape());

		BasicStroke headStroke = new BasicStroke(3);
		g2d.setColor(associationSplitColor);
		g2d.setStroke(headStroke);
		g2d.draw(new RoundRectangle2D.Double(component.getHeadShape().getBounds2D().getX() - 1,
				component.getHeadShape().getBounds2D().getY() - 1,
				component.getHeadShape().getBounds2D().getWidth() + 1,
				component.getHeadShape().getBounds2D().getHeight() + 1, 20.0f, 20.0f));

		BasicStroke boundStroke = new BasicStroke(3);
		g2d.setColor(associationBoundColor);
		g2d.setStroke(boundStroke);
		g2d.draw(new RoundRectangle2D.Double(component.getMainShape().getBounds2D().getX() - 1,
				component.getMainShape().getBounds2D().getY() - 1,
				component.getMainShape().getBounds2D().getWidth() + 1,
				component.getMainShape().getBounds2D().getHeight() + 1, 20.0f, 20.0f));

		g2d.setColor(Color.black);
		g2d.drawString(component.getGroupName(), component.getX()+60.0f, component.getY()+30.0f);
	}

	/**
	 * @return the alComponents
	 */
	public ArrayList<ShapeGroup> getAlComponents() {
		return alComponents;
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

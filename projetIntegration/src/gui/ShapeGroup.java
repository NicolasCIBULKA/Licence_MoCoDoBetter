package gui;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class ShapeGroup {
	private Shape mainShape;
	private Shape headShape;
	private String groupName;
	private ArrayList<String> alEntries = new ArrayList<String>();

	private float x;
	private float y;
	private float width = 80.0f;
	private float height = 100.0f;

	private boolean isAnEntity;

	public ShapeGroup(String groupName, float x, float y, boolean entityType) {
		this.groupName = groupName;
		this.x = x;
		this.y = y;
		this.isAnEntity = entityType;

		if (isAnEntity) {

			mainShape = new Rectangle2D.Float(x, y, width, height);
			headShape = new Rectangle2D.Float(x, y, width, 50.0f);

		} else {

			mainShape = new RoundRectangle2D.Float(x, y, width, height, 20.0f, 20.0f);
			headShape = new RoundRectangle2D.Float(x, y, width, 50.0f, 20.0f, 20.0f);
		}

	}

	// TODO : update height operations, only suitable for empty entities
	public void setGroupAbscissa(float newX) {
		setX(newX);

		if (isAnEntity) {

			((Rectangle2D) mainShape).setRect(newX, y, width, height);
			((Rectangle2D) headShape).setRect(newX, y, width, 50.0f);

		} else {

			((RoundRectangle2D) mainShape).setRoundRect(newX, y, width, height, 20.0f, 20.0f);
			((RoundRectangle2D) headShape).setRoundRect(newX, y, width, 50.0f, 20.0f, 20.0f);

		}

	}

	public void setGroupOrdinate(float newY) {
		setY(newY);

		if (isAnEntity) {

			((Rectangle2D) mainShape).setRect(x, newY, width, height);
			((Rectangle2D) headShape).setRect(x, newY, width, 50.0f);

		} else {

			((RoundRectangle2D) mainShape).setRoundRect(x, newY, width, height, 20.0f, 20.0f);
			((RoundRectangle2D) headShape).setRoundRect(x, newY, width, 50.0f, 20.0f, 20.0f);

		}
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
		if (isAnEntity) {

			((Rectangle2D) mainShape).setRect(x, y, width, height);
			((Rectangle2D) headShape).setRect(x, y, width, 50.0f);

		} else {

			((RoundRectangle2D) mainShape).setRoundRect(x, y, width, height, 20.0f, 20.0f);
			((RoundRectangle2D) headShape).setRoundRect(x, y, width, 50.0f, 20.0f, 20.0f);

		}
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
		
		if (isAnEntity) {

			((Rectangle2D) mainShape).setRect(x, y, width, height);
			((Rectangle2D) headShape).setRect(x, y, width, 50.0f);

		} else {

			((RoundRectangle2D) mainShape).setRoundRect(x, y, width, height, 20.0f, 20.0f);
			((RoundRectangle2D) headShape).setRoundRect(x, y, width, 50.0f, 20.0f, 20.0f);

		}
	}

	/**
	 * @return the isAnEntity
	 */
	public boolean isAnEntity() {
		return isAnEntity;
	}

	/**
	 * @param isAnEntity the isAnEntity to set
	 */
	public void setAnEntity(boolean isAnEntity) {
		this.isAnEntity = isAnEntity;
	}

	/**
	 * @return the mainShape
	 */
	public Shape getMainShape() {
		return mainShape;
	}

	/**
	 * @return the bodyShape
	 */
	public Shape getHeadShape() {
		return headShape;
	}

	/**
	 * @return the alEntries
	 */
	public ArrayList<String> getAlEntries() {
		return alEntries;
	}

}

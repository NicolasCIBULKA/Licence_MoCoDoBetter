package gui;

import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

/**
 * Emulating a button with an icon, a description text at its base and a
 * sourrounding shape when selected
 * 
 * @author Yann Barrachina
 *
 */
public class IconGroup {
	private Shape selectionShape;
	private String groupName;
	private String iconTitle;

	private float x;
	private float y;
	private float width;
	private float height;

	private boolean isSelected;
	private boolean isPressed;

	private Image pressedImage;
	private Image standardImage;

	public IconGroup(String groupName, String iconTitle, float x, float y, float width, float height,
			Image pressedImage, Image standardImage) {
		this.groupName = groupName;
		this.iconTitle = iconTitle;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.pressedImage = pressedImage;
		this.standardImage = standardImage;

		selectionShape = new RoundRectangle2D.Float(x, y, width, height, 10.0f, 10.0f);
	}

	/**
	 * @return the iconTitle
	 */
	public String getIconTitle() {
		return iconTitle;
	}

	/**
	 * @param iconTitle the iconTitle to set
	 */
	public void setIconTitle(String iconTitle) {
		this.iconTitle = iconTitle;
	}

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		isPressed = false;
	}

	/**
	 * @return the isPressed
	 */
	public boolean isPressed() {
		return isPressed;
	}

	/**
	 * @param isPressed the isPressed to set
	 */
	public void setPressed(boolean isPressed) {
		this.isPressed = isPressed;
		isSelected = false;
	}

	/**
	 * @return the selectionShape
	 */
	public Shape getSelectionShape() {
		return selectionShape;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @return the pressedImage
	 */
	public Image getPressedImage() {
		return pressedImage;
	}

	/**
	 * @return the standardImage
	 */
	public Image getStandardImage() {
		return standardImage;
	}
}

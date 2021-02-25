package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import data.Attribute;

public class AttributePanel extends JPanel {
	private JPanel mainPanel;

	private JButton addButton = new JButton("+");
	private JButton deleteButton = new JButton("-");
	
	private JTable mainTable;
	private DefaultTableModel model;
	private JScrollPane jsp;
	private Dimension d = new Dimension(500, 400);
	

	private int index = 0;

	private ArrayList<Attribute> attributeList;

	public AttributePanel() {
		mainPanel = this;
		
		String columnHeader[] = { "Nom", "Type", "is Null", "Primary Key", "is Unique" };
		Object[][] data = {};
		model = new DefaultTableModel(data,columnHeader);
		initActions();

//		for (String field : columnHeader) {
//			model.addColumn(field);
//		}

		JPanel jpTable = new JPanel();
		JPanel buttonPanel = new JPanel();
		mainTable = new JTable(model){
			@Override
			public Class<? extends Object> getColumnClass(int column) {
				switch(column) {
				case 0:
					return String.class;
				case 1:
					return JComboBox.class;
				case 2:
					return Boolean.class;
				case 3:
					return Boolean.class;
				case 4:
					return Boolean.class;
				default:
					return null;	
				}
				
			}
		};

		TableColumn typeColumn = mainTable.getColumnModel().getColumn(1);
		
		String[] attributeTypeList = new String[] {"int", "varchar"};
		JComboBox<String> attributeTypeChooser = new JComboBox<String>(attributeTypeList);
		typeColumn.setCellEditor(new DefaultCellEditor(attributeTypeChooser));
//		isNullColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
//		isUniqueColumn.setCellEditor(new DefaultCellEditor(new JCheckBox()));
		
		
		jsp = new JScrollPane(mainTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(d);
		jpTable.add(jsp);
		mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jpTable.setBorder(BorderFactory.createTitledBorder("Attributs"));
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(jpTable, BorderLayout.CENTER);

		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);

		mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
	}

	public void initActions() {
		addButton.addActionListener(new AddAttributeAction());
		deleteButton.addActionListener(new DeleteAttributeAction());
	}

	/**
	 * @return the addButton
	 */
	public JButton getAddButton() {
		return addButton;
	}

	/**
	 * @return the deleteButton
	 */
	public JButton getDeleteButton() {
		return deleteButton;
	}

	/**
	 * @return the attributeList
	 */
	public ArrayList<Attribute> getAttributeList() {
		return attributeList;
	}

	/**
	 * @param attributeList the attributeList to set
	 */
	public void setAttributeList(ArrayList<Attribute> attributeList) {
		this.attributeList = attributeList;
		model.setRowCount(0);
		for (Attribute attribute : attributeList) {
			model.addRow(attribute.toVector());
		}
		// Notifying table that datas are uploaded into the table's model
		model.fireTableDataChanged();
	}

	/**
	 * Creates then adds a blank attribute to the attribute list and the mainTable.
	 */
	public class AddAttributeAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String newName = "" + index++;
			Attribute newAttribute;
//			Vector<Object> attributeVector = new Vector<Object>();
//			attributeVector.add(newName);
//			attributeVector.add("int");
//			attributeVector.add(false);
			if (attributeList.size() == 0) {
				newAttribute = new Attribute(newName, "int", false, true, false);
//				attributeVector.add(true);
			} else {
				newAttribute = new Attribute(newName, "int", false, false, false);
//				attributeVector.add(false);
			}

			attributeList.add(newAttribute);
			
//			attributeVector.add(new JCheckBox("", false));
			model.addRow(newAttribute.toVector());
//			model.addRow(attributeVector);
			model.fireTableDataChanged();
			repaint();
		}
	}

	/**
	 * Deleting selected attribute from attribute list and mainTable.
	 */
	public class DeleteAttributeAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int selectedRow = mainTable.getSelectedRow();
			attributeList.remove(selectedRow);
			model.removeRow(selectedRow);
			model.fireTableDataChanged();
			repaint();
		}
	}
}

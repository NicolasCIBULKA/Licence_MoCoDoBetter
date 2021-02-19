package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import data.Attribute;

public class AttributePanel extends JPanel{
	private JPanel mainPanel;
	
	private JButton addButton = new JButton("+");
	private JButton deleteButton = new JButton("-");
	
	private JTable mainTable ;
	private DefaultTableModel model = new DefaultTableModel();
	private JScrollPane jsp ;
	private Dimension d = new Dimension(350,400) ;
	private String columnHeader[] = { "Nom", "Type", "is Null", "Primary Key", "is Unique"};
	
	private List<Attribute> attributeList;
	
	public AttributePanel(List<Attribute> attributeList) {
		mainPanel = this;
		this.attributeList = attributeList;
		
		initActions();
		
		for(String field : columnHeader) {
	    	model.addColumn(field);
	    }
		
		JPanel jpTable = new JPanel();
		JPanel buttonPanel = new JPanel();
		mainTable = new JTable(model) ;
		for(Attribute attribute : attributeList) {
			model.addRow(attribute.toVector());
		}
		// Notifying table that datas are uploaded into the table's model
		model.fireTableDataChanged();
		
		jsp = new JScrollPane(mainTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) ;
	   	jsp.setPreferredSize(d);
	   	jpTable.add(jsp) ;
	   	mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	   	jpTable.setBorder(BorderFactory.createTitledBorder("Attributs"));
	   	mainPanel.setLayout(new BorderLayout());
	   	mainPanel.add(jpTable,BorderLayout.CENTER);
	   	
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
	 * Creates then adds a blank attribute to the attribute list and the mainTable.
	 */
	public class AddAttributeAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Attribute newAttribute = new Attribute("", "", false, false, false);
			attributeList.add(newAttribute);
			model.addRow(newAttribute.toVector());
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

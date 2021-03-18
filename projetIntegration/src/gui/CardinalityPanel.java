package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import data.Cardinality;

/**
 * CardinalityPanel is a JPanel containing the dynamic JTable which reprensents
 * all cardinalities of an association
 * 
 * @author Yann Barrachina
 *
 */
public class CardinalityPanel extends JPanel {
	private JPanel mainPanel;

	private JTable mainTable;
	private DefaultTableModel model;
	private JScrollPane jsp;
	private Dimension d = new Dimension(500, 400);

	private ArrayList<Cardinality> cardinalityList;

	public CardinalityPanel() {
		mainPanel = this;
		cardinalityList = new ArrayList<Cardinality>();

		String columnHeader[] = { "Entité", "Valeur basse", "Valeur haute" };
		Object[][] data = {};
		model = new DefaultTableModel(data, columnHeader);

		JPanel jpTable = new JPanel();
		mainTable = new JTable(model) {
			@Override
			public Class<? extends Object> getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return JComboBox.class;
				case 2:
					return JComboBox.class;
				default:
					return null;
				}

			}
			
			// First column is made uneditable to ensure that the user will not change the entity's name
			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 0) {
					return false;
				} else {
					return true;
				}
			}
		};

		TableColumn typeColumn = mainTable.getColumnModel().getColumn(1);

		String[] cardinalityLowValueList = new String[] { "0", "1", "N" };
		JComboBox<String> cardinalityLowValueChooser = new JComboBox<String>(cardinalityLowValueList);
		typeColumn.setCellEditor(new DefaultCellEditor(cardinalityLowValueChooser));

		typeColumn = mainTable.getColumnModel().getColumn(2);
		String[] cardinalityHighValueList = new String[] { "0", "1", "N" };
		JComboBox<String> cardinalityHighValueChooser = new JComboBox<String>(cardinalityHighValueList);
		typeColumn.setCellEditor(new DefaultCellEditor(cardinalityHighValueChooser));

		jsp = new JScrollPane(mainTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(d);
		jpTable.add(jsp);
		mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jpTable.setBorder(BorderFactory.createTitledBorder("Cardinalités"));
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(jpTable, BorderLayout.CENTER);

	}

	/**
	 * @return the cardinalityList
	 */
	public ArrayList<Cardinality> getCardinalityList() {
		return cardinalityList;
	}

	/**
	 * Updates the cardinalityList by reading all the rows. All user's modifications
	 * are registered into the list of cardinalities by retrieving the rows datas.
	 */
	public void updateCardinalityList() {
		int rowCount = model.getRowCount();
		ArrayList<Cardinality> newCardinalityList = new ArrayList<Cardinality>();

		for (int index = 0; index < rowCount; index++) {
			newCardinalityList.add(new Cardinality((String) model.getValueAt(index, 1),
					(String) model.getValueAt(index, 2), (String) model.getValueAt(index, 0)));
		}

		cardinalityList = newCardinalityList;
	}

	/**
	 * @param cardinalityList the cardinalityList to set
	 */
	public void setCardinalityList(ArrayList<Cardinality> cardinalityList) {
		this.cardinalityList = cardinalityList;

		model.setRowCount(0);
		for (Cardinality cardinality : cardinalityList) {
			
			Vector<String> attributeVector = new Vector<String>();
			attributeVector.add(cardinality.getNomEntity());
			attributeVector.add(cardinality.getLowValue());
			attributeVector.add(cardinality.getHighValue());

			model.addRow(attributeVector);
		}
		// Notifying table that datas are uploaded into the table's model
		model.fireTableDataChanged();
	}

}
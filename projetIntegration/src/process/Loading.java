package process;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.AbstractGraphIterator;
import org.jgrapht.traverse.BreadthFirstIterator;

import java.util.Set;

import javax.swing.JOptionPane;

import data.Association;
import data.Attribute;
import data.Cardinality;
import data.Entity;
import data.Node;
import exceptions.EdgesNotLinkedException;
import exceptions.ExistingEdgeException;
import exceptions.InvalidNodeLinkException;
import exceptions.NotTheRightFileFormatException;
import exceptions.NullNodeException;
/**
 * This class load an xml file 
 * @author Etienne Coutenceau
 *
 */
public class Loading {

	private MCDManaging mcd;
	private ArrayList<Node> listNode;
	private HashMap<String,ArrayList<Cardinality>> listCard;

	public Loading(String path) throws NotTheRightFileFormatException {
		this.mcd = new MCDManaging();
		this.listNode = new ArrayList<Node>();
		this.listCard = new HashMap<String,ArrayList<Cardinality>>();
		extract(path);
		addNodes();
		addCardinality();
	}
	/**
	 * Add the extracted Nodes in the MCD
	 */
	private void addNodes() {
		for(Node node : listNode) {
			mcd.addNode(node);
		}
	}
	/**
	 * Loop through the hashMap listCard and it's cardinality to add each connections
	 */
	private void addCardinality() {
		try {
			Set<Entry<String, ArrayList<Cardinality>>> tempNode = listCard.entrySet();
			for (Entry<String, ArrayList<Cardinality>> set : tempNode) {
				ArrayList<Cardinality> tempList = set.getValue();
				for(Cardinality set2 : tempList) {
					mcd.connectNodes(mcd.getNodeFromName(set.getKey()),mcd.getNodeFromName(set2.getNomEntity()) ,set2);
				}
			}
		} catch (NullNodeException e) {
			e.printStackTrace();
		} catch (ExistingEdgeException e) {
			e.printStackTrace();
		} catch (InvalidNodeLinkException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Initiate the buffered Reader for extracting the MCD
	 * @param path
	 * @throws NotTheRightFileFormatException 
	 */
	private void extract(String path) throws NotTheRightFileFormatException {
		try {
			BufferedReader br = Files.newBufferedReader(Paths.get(path));
			String str = br.readLine();
			for(int i=0; i<12;i++) {
				str = br.readLine();

			}
			if(str.equals("<MCD>")) {
				extractEntity(br);
				extractAssociation(br);
				extractCardinality(br);
			}
			else {
				throw new NotTheRightFileFormatException("This file is not Mocodo Better Compatible");
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	/**
	 * Read the xml file and extract the entities
	 * @param br
	 */
	private void extractEntity(BufferedReader br) {
		try {
			String name = null;
			ArrayList<Attribute> att = new ArrayList<Attribute>();
			String str;
			while (!(str = br.readLine()).equals("</Entities>") ) {

				if (str.equals("</Table>")) {
					ArrayList<Attribute> att2 = (ArrayList<Attribute>) att.clone();

					Entity a = new Entity(name, att2);
					listNode.add(a);
					att.clear();
				}
				if (str.equals("<Name>")) {

					str = br.readLine();
					name = str;

				}
				if (str.equals("<Attribut>")) {
					str = br.readLine();
					String[] listatt = str.split(",");
					Attribute tmpAtt = new Attribute(listatt[0], listatt[1], Boolean.parseBoolean(listatt[2]),
							Boolean.parseBoolean(listatt[3]), Boolean.parseBoolean(listatt[4]));
					att.add(tmpAtt);
					tmpAtt = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Read the xml file and extract the associations
	 * @param br
	 */
	private void extractAssociation(BufferedReader br) {
		try {
			String name = null;
			ArrayList<Attribute> att = new ArrayList<Attribute>();
			ArrayList<Cardinality> card = new ArrayList<Cardinality>();

			String str;
			while (!(str = br.readLine()).equals("<Associations>") );
			while (!(str = br.readLine()).equals("</Associations>") ) {

				if (str.equals("</Table>")) {
					ArrayList<Attribute> att2 = (ArrayList<Attribute>) att.clone();
					ArrayList<Cardinality> card2 = (ArrayList<Cardinality>) card.clone();
					Association a = new Association(name, att2, card2);
					listNode.add(a);
					att.clear();
				}
				if (str.equals("<Name>")) {

					str = br.readLine();
					name = str;
				}
				if (str.equals("<Attribut>")) {
					str = br.readLine();
					String[] listatt = str.split(",");
					Attribute tmpAtt = new Attribute(listatt[0], listatt[1], Boolean.parseBoolean(listatt[2]),
							Boolean.parseBoolean(listatt[3]), Boolean.parseBoolean(listatt[4]));
					att.add(tmpAtt);
					tmpAtt = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Read the xml file and extract the cardinality and the association name
	 * @param br
	 */
	private void extractCardinality(BufferedReader br) {
		try {
			String str;
			ArrayList<Cardinality> card = new ArrayList<Cardinality>();
			while (!(str = br.readLine()).equals("</Cardinalities>")) {
				if (str.equals("<Cardinality>")) {
					str = br.readLine();
					String[] tmpStr = str.split(",");
					if(!listCard.containsKey(tmpStr[0])) {
						card = new ArrayList<Cardinality>();
						listCard.put(tmpStr[0], (ArrayList<Cardinality>) card.clone());
					}
					Cardinality help =new Cardinality(tmpStr[2],tmpStr[3],tmpStr[1]);
					listCard.get(tmpStr[0]).add(help);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @return the mcd
	 */
	public MCDManaging getMcdManager() {
		return mcd;
	}

	/**
	 * @return the mcd
	 */
	public MCDManaging getMcd() {
		return mcd;
	}

	/**
	 * @return the listNode
	 */
	public ArrayList<Node> getListNode() {
		return listNode;
	}

	/**
	 * @return the listCard
	 */
	public HashMap<String, ArrayList<Cardinality>> getListCard() {
		return (HashMap<String, ArrayList<Cardinality>>) listCard;
	}
}

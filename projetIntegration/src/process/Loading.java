package process;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import data.Association;
import data.Attribute;
import data.Cardinality;
import data.Entity;
import data.Node;
import exceptions.ExistingEdgeException;
import exceptions.InvalidNodeLinkException;
import exceptions.NullNodeException;

public class Loading {

	private MCDManaging mcd;
	private HashMap<String, Node> listNode;
	private HashMap<String, Cardinality> listCard;

	public Loading(String path) {
		this.mcd = new MCDManaging();
		this.listNode = new HashMap<String, Node>();
		this.listCard = new HashMap<String, Cardinality>();
		extract(path);
		addNodes();
		addCardinality();
	}

	private void addNodes() {
		for (Entry<String, Node> set : listNode.entrySet()) {
			mcd.addNode(set.getValue());
		}
	}

	private void addCardinality() {
		try {
			for (Entry<String, Cardinality> set : listCard.entrySet()) {
				mcd.connectNodes(listNode.get(set.getKey()), listNode.get(set.getValue().getNomEntity()),
						set.getValue());
			}
		} catch (NullNodeException e) {
			e.printStackTrace();
		} catch (ExistingEdgeException e) {
			e.printStackTrace();
		} catch (InvalidNodeLinkException e) {
			e.printStackTrace();
		}
	}

	private void extract(String path) {
		try {
			BufferedReader br = Files.newBufferedReader(Paths.get("path"));
			extractEntity(br);
			extractAssociation(br);
			extractCardinality(br);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void extractEntity(BufferedReader br) {
		try {
			String name = null;
			ArrayList<Attribute> att = new ArrayList<Attribute>();
			String str;
			while ((str = br.readLine()) != "</Entities>") {
				if (str.equals("</Table>")) {
					Entity a = new Entity(name, att);
					listNode.put(name, a);
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

	private void extractAssociation(BufferedReader br) {
		try {
			String name = null;
			ArrayList<Attribute> att = new ArrayList<Attribute>();
			String str;
			while ((str = br.readLine()) != "</Associations>") {
				if (str.equals("</Table>")) {
					Association a = new Association(name, att, null);
					listNode.put(name, a);
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

	private void extractCardinality(BufferedReader br) {
		try {
			String str;
			while (!(str = br.readLine()).equals("</Cardinalities>")) {
				if (str.equals("<Cardinality>")) {
					str = br.readLine();
					String[] tmpStr = str.split(",");
					listCard.put(tmpStr[0], new Cardinality(tmpStr[2], tmpStr[3], tmpStr[1]));
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
	public HashMap<String, Node> getListNode() {
		return listNode;
	}

	/**
	 * @return the listCard
	 */
	public HashMap<String, Cardinality> getListCard() {
		return listCard;
	}
}

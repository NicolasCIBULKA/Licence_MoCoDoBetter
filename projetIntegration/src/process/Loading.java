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

import data.Association;
import data.Attribute;
import data.Cardinality;
import data.Entity;
import data.Node;
import exceptions.EdgesNotLinkedException;
import exceptions.ExistingEdgeException;
import exceptions.InvalidNodeLinkException;
import exceptions.NullNodeException;
/**
 * 
 * @author Etienne Coutenceau
 *
 */
public class Loading {

	private MCDManaging mcd;
	private HashMap<String, Node> listNode;
	private HashMap<String,ArrayList<Cardinality>> listCard;

	public Loading(String path) {
		this.mcd = new MCDManaging();
		this.listNode = new HashMap<String, Node>();
		this.listCard = new HashMap<String,ArrayList<Cardinality>>();
		extract(path);
		addNodes();
		addCardinality();
	}

	private void addNodes() {
		for (HashMap.Entry<String, Node> set : listNode.entrySet()) {
			mcd.addNode(set.getValue());
		}
	}

	private void addCardinality() {
		try {
			Set<Entry<String, ArrayList<Cardinality>>> tempNode = listCard.entrySet();
			for (Entry<String, ArrayList<Cardinality>> set : tempNode) {
				ArrayList<Cardinality> tempList = set.getValue();
				for(Cardinality set2 : tempList) {
					Association asso = (Association) mcd.getNodeFromName(set.getKey());
					Entity entity = (Entity) mcd.getNodeFromName(set2.getNomEntity());
					mcd.connectNodes(asso,entity ,set2);

				
				}
				
				
			}
			Set<Entry<String, ArrayList<Cardinality>>> tempNode2 = listCard.entrySet();
			for (Entry<String, ArrayList<Cardinality>> set : tempNode2) {
				
				ArrayList<Cardinality> tempList = set.getValue();
				for(Cardinality set2 : tempList) {
					System.out.println("\n\n\n\n3");
					System.out.println("\n"+set2.toString()+"\n");
					AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCD().getMCDGraph());
					while (iterator.hasNext()) {
						Node currentNode = iterator.next();
						if (currentNode instanceof Association) {
							System.out.println("la taille de la liste de card : "+((Association) currentNode).getCardinalityList().size());
							System.out.println("L'association : "+currentNode.getName());
							for (Cardinality card : ((Association) currentNode).getCardinalityList()) {
								System.out.println("\n passage : "+card.toString()+"\n");

								if(!card.getNomEntity().equalsIgnoreCase(set2.getNomEntity())) {
									try {
										mcd.disconnectNodes(mcd.getNodeFromName(currentNode.getName()), mcd.getNodeFromName(set2.getNomEntity()));
									} catch (EdgesNotLinkedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					}
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

	private void extract(String path) {
		try {
			BufferedReader br = Files.newBufferedReader(Paths.get(path));
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
			while (!(str = br.readLine()).equals("</Entities>") ) {

				if (str.equals("</Table>")) {
					ArrayList<Attribute> att2 = (ArrayList<Attribute>) att.clone();

					Entity a = new Entity(name, att2);
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
			ArrayList<Cardinality> card = new ArrayList<Cardinality>();

			String str;
			while (!(str = br.readLine()).equals("<Associations>") );
			while (!(str = br.readLine()).equals("</Associations>") ) {

				if (str.equals("</Table>")) {
					ArrayList<Attribute> att2 = (ArrayList<Attribute>) att.clone();
					Association a = new Association(name, att2, card);
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
	public HashMap<String, Node> getListNode() {
		return listNode;
	}

	/**
	 * @return the listCard
	 */
	public HashMap<String, ArrayList<Cardinality>> getListCard() {
		return (HashMap<String, ArrayList<Cardinality>>) listCard;
	}
}

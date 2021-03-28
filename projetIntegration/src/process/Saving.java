package process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.AbstractGraphIterator;
import org.jgrapht.traverse.BreadthFirstIterator;

import data.Association;
import data.Attribute;
import data.Cardinality;
import data.Entity;
import data.MCD;
import data.Node;
import exceptions.FileAlreadyExistException;
import exceptions.SaveWasInteruptedException;
/**
 * This class will generate a xml file with all the information related to the MCD and the graphical placement
 * 
 * @author Etienne Coutenceau
 *
 */
public class Saving {

	// keeping in memory the list of entity and association to treat them separatly
	private ArrayList<Entity> listEntity;
	private ArrayList<Association> listAssociation;
	private Map<String, ArrayList<Float>> coordinatesMap;

	// constructor for both "save" and "save as"
	public Saving(String path, MCD mcd, Map<String, ArrayList<Float>> coordinatesMap, boolean saveAs)
			throws SaveWasInteruptedException, FileAlreadyExistException {
		listEntity = new ArrayList<Entity>();
		listAssociation = new ArrayList<Association>();
		this.coordinatesMap = coordinatesMap;
		storeMCD(mcd);
		if (creaFile(path, saveAs)) {
			writeEntity(path);
			writeAssociation(path);
			writeCard(path);
		}
	}

	/**
	 * This method will store in ArrayList the MCD and separating association and entities
	 * @param mcd
	 */
	private void storeMCD(MCD mcd) {
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while (iterator.hasNext()) {
			Node currentNode = iterator.next();
			if (currentNode instanceof Entity) {
				listEntity.add((Entity) currentNode);
			} else if (currentNode instanceof Association) {
				listAssociation.add((Association) currentNode);
			}

		}
		
	}


	/**
	 * I create a new file if it doesn't exist if already exist depending on save as
	 * or save action it will either overwrite or throws an exception
	 * 
	 * @param path
	 * @param saveAs the boolean to save as if the constructor have a boolean in
	 *               argument then it means it's a save as request
	 * @return
	 * @throws SaveWasInteruptedException
	 * @throws FileAlreadyExistException
	 */
	private boolean creaFile(String path, Boolean saveAs) throws SaveWasInteruptedException, FileAlreadyExistException {
		try {
			File initfile = new File(path + ".xml");
			if (initfile.createNewFile()) {
				System.out.println("File created: " + initfile.getName());
			} else {
				if (saveAs) {
					System.out.println("File already exists.");
					throw new FileAlreadyExistException("This file already exist");
				}
			}
		} catch (IOException e) {
			throw new SaveWasInteruptedException("The file couldn't be opened");
		}
		return true;
	}

	/**
	 * This method will start the xml file and write the information relative to each entities
	 * @param path
	 * @throws SaveWasInteruptedException
	 * 
	 */
	private void writeEntity(String path)
			throws SaveWasInteruptedException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path + ".xml"));
			
			writer.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>\n");
			writer.write("<!DOCTYPE MCD [\n"
					+ "<!ELEMENT MCD (Entities+, Associations+, Cardinalities+)>\n"
					+ "<!ELEMENT Entities (Table*)>\n"
					+ "<!ELEMENT Associations (Table*)>\n"
					+ "<!ELEMENT Table (Name+, Graphical+, Attribut*)>\n"
					+ "<!ELEMENT Name (#PCDATA)>\n"
					+ "<!ELEMENT Graphical (#PCDATA)>\n"
					+ "<!ELEMENT Attribut (#PCDATA)>\n"
					+ "<!ELEMENT Cardinalities (Cardinality*)>\n"
					+ "<!ELEMENT Cardinality (#PCDATA)>\n"
					+"]>\n");
			writer.write("<MCD>\n");
			writer.write("<Entities>\n");
			if (!listEntity.isEmpty()) {
				for (Node entity : listEntity) {
					writer.write("<Table>\n");
					writer.write("<Name>\n");
					writer.write(entity.getName() + "\n");
					writer.write("</Name>\n");
					writer.write("<Graphical>\n");
					writer.write(coordinatesMap.get(entity.getName()).get(0) + ","
							+ coordinatesMap.get(entity.getName()).get(1) + ","
							+ coordinatesMap.get(entity.getName()).get(2) + ","
							+ coordinatesMap.get(entity.getName()).get(3) + "\n");
					writer.write("</Graphical>\n");
					for (Attribute attribut : entity.getListAttribute()) {
						writer.write("<Attribut>\n");
						writer.write(attribut.getName() + "," + attribut.getType() + "," + attribut.isNullable() + ","
								+ attribut.isPrimaryKey() + "," + attribut.isUnique() + "\n");
						writer.write("</Attribut>\n");
					}
					writer.write("</Table>\n");
				}

			} else {
				System.out.println("\n\n\n empty entity\n\n");
			}
			writer.write("</Entities>\n");
			writer.close();
		} catch (IOException e) {
			throw new SaveWasInteruptedException("Can't write the entity part of the file");
		}
	}

	/**
	 * This method will write the information relative to each association
	 * @param path
	 * @throws SaveWasInteruptedException
	 */
	private void writeAssociation(String path)
			throws SaveWasInteruptedException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path + ".xml", true));
			writer.write("<Associations>\n");
			// null pointer probablement une lecture � vide
			if (!listAssociation.isEmpty()) {
				for (Node association : listAssociation) {
					writer.write("<Table>\n");
					writer.write("<Name>\n");
					writer.write(association.getName() + "\n");
					writer.write("</Name>\n");
					writer.write("<Graphical>\n");
					writer.write(coordinatesMap.get(association.getName()).get(0) + ","
							+ coordinatesMap.get(association.getName()).get(1) + ","
							+ coordinatesMap.get(association.getName()).get(2) + ","
							+ coordinatesMap.get(association.getName()).get(3) + "\n");
					writer.write("</Graphical>\n");
					for (Attribute attribut : association.getListAttribute()) {
						writer.write("<Attribut>\n");
						writer.write(attribut.getName() + "," + attribut.getType() + "," + attribut.isNullable() + ","
								+ attribut.isPrimaryKey() + "," + attribut.isUnique() + "\n");
						writer.write("</Attribut>\n");
					}
					writer.write("</Table>\n");
				}
			} else {
				System.out.println("\n\n\n empty association\n\n");
			}
			writer.write("</Associations>\n");
			writer.close();
		} catch (IOException e) {
			throw new SaveWasInteruptedException("Can't write the association part of the file");
		}
	}

	/**
	 * Finally this method will write the cardinality information and will close the xml document
	 * @param path
	 * @throws SaveWasInteruptedException
	 */
	private void writeCard(String path) throws SaveWasInteruptedException {
		try {
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(path + ".xml", true));
			writer.write("<Cardinalities>\n");
			// null pointer probablement une lecture � vide
			if (!listAssociation.isEmpty()) {
				for (Association association : listAssociation) {
					
					if (!association.getCardinalityList().isEmpty()) {
						for (Cardinality card : association.getCardinalityList()) {
							writer.write("<Cardinality>\n");
							writer.write(association.getName() + "," + card.getNomEntity() + "," + card.getLowValue()
									+ "," + card.getHighValue() + "\n");
							writer.write("</Cardinality>\n");
						}
					} else {
						System.out.println("\n\n\n empty cardinatlity\n\n");
					}
					
				}
			} else {
				System.out.println("\n\n\n empty association\n\n");

			}
			writer.write("</Cardinalities>\n");
			writer.write("</MCD>\n");
			writer.close();
		} catch (IOException e) {
			throw new SaveWasInteruptedException("Can't write the cardinality part of the file");
		}
	}
}

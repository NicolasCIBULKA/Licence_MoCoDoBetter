package process;

import data.Association;
import data.Attribute;
import data.Cardinality;
import data.Entity;
import data.MCD;
import data.Node;
import exceptions.FileAlreadyExistException;
import exceptions.SaveWasInteruptedException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.AbstractGraphIterator;
import org.jgrapht.traverse.BreadthFirstIterator;

public class Saving {
	private String path;
	private ArrayList<Entity> listEntity;
	private ArrayList<Association> listAssociation;

	public Saving(String path, MCD mcd) throws SaveWasInteruptedException, FileAlreadyExistException {
		listEntity = new ArrayList<Entity>();
		listAssociation = new ArrayList<Association>();
		storeMCD(mcd);
		
		this.path=path;
		if(creaFile(path)) {
			writeEntity();
			writeAssociation();
			writeCard();
		}
	}
	
	private void storeMCD(MCD mcd) {
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			for(Node connectedNode : connectedNodes) {
				System.out.println("\n\n\n"+connectedNode.toString() +"\n\n");
				if(connectedNode instanceof Entity) {
					listEntity.add((Entity) connectedNode);
				}
				else if(connectedNode instanceof Association) {
					listAssociation.add((Association) connectedNode);
				}
				
			}	
		}
	}
	
	private boolean creaFile(String path) throws SaveWasInteruptedException, FileAlreadyExistException {
		try {
		      File initfile = new File(path+".stdd");
		      if (initfile.createNewFile()) {
		        System.out.println("File created: " + initfile.getName());
		      } else {
		        System.out.println("File already exists.");
		        throw new FileAlreadyExistException("This file already exist");
		      }
		    } catch (IOException e) {
		      throw new SaveWasInteruptedException("The file couldn't be opened");
		    }
		return true;
	}
	
	private void writeEntity() throws SaveWasInteruptedException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path+".stdd",true));
			writer.write("<Entities>\n");
			//null pointer probablement une lecture à vide
			if(!listEntity.isEmpty()) {
				for(Node entity : listEntity) {
					writer.write("<Entity>\n");
					writer.write("<Name>\n");
					writer.write(entity.getName()+"\n");
					writer.write("</Name>\n");
					for(Attribute attribut : entity.getListAttribute()) {
						writer.write("<Attribut>\n");
						writer.write(attribut.getName()+","+attribut.getType()+","
					+attribut.isNullable()+","+attribut.isPrimaryKey()+","+attribut.isUnique()+"\n");
						writer.write("</Attribut>\n");
					}
					writer.write("</Entity>\n");
				}
				
			}
			else {
				System.out.println("\n\n\n empty entity\n\n");
			}
			writer.write("</Entities>\n");
			writer.close();
		}catch(IOException e) {
			throw new SaveWasInteruptedException("Can't write the entity part of the file");
		}
	}
	
	private void writeAssociation() throws SaveWasInteruptedException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path+".stdd",true));
			writer.write("<Associations>\n");
			//null pointer probablement une lecture à vide
			if(!listAssociation.isEmpty()) {
				for(Node association : listAssociation) {
					writer.write("<Entity>\n");
					writer.write("<Name>\n");
					writer.write(association.getName()+"\n");
					writer.write("</Name>\n");
					for(Attribute attribut : association.getListAttribute()) {
						writer.write("<Attribut>\n");
						writer.write(attribut.getName()+","+attribut.getType()+","
					+attribut.isNullable()+","+attribut.isPrimaryKey()+","+attribut.isUnique()+"\n");
						writer.write("</Attribut>\n");
					}
					writer.write("</Entity>\n");
				}
			}
			else {
				System.out.println("\n\n\n empty association\n\n");
			}
			writer.write("</Associations>\n");
			writer.close();
		}catch(IOException e) {
			throw new SaveWasInteruptedException("Can't write the association part of the file");
		}
	}
	private void writeCard() throws SaveWasInteruptedException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path+".stdd",true));
			writer.write("<Cardinalities>\n");
			//null pointer probablement une lecture à vide
			if(!listAssociation.isEmpty()) {
				for(Association association : listAssociation) {
					writer.write("<Cardinality>\n");
					if(!association.getCardinalityList().isEmpty()) {
						for(Cardinality card : association.getCardinalityList()) {
							writer.write(association.getName()+","+card.getNomEntity()+","+card.getLowValue()+","+
						card.getHighValue()+"\n");
						}
					}
					else {
						System.out.println("\n\n\n empty cardinatlity\n\n");
					}
					writer.write("</Cardinality>\n");
				}
			}
			else {
				System.out.println("\n\n\n empty association\n\n");

			}
			writer.write("</Cardinalities>\n");
			writer.close();
		}catch(IOException e) {
			throw new SaveWasInteruptedException("Can't write the cardinality part of the file");
		}
	}
}

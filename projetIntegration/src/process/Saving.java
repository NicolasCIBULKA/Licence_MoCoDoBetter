package process;

import data.Association;
import data.Attribute;
import data.Cardinality;
import data.Entity;
import data.MCD;
import data.Node;
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

	public Saving(String path, MCD mcd) throws SaveWasInteruptedException {
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
				if(connectedNode instanceof Entity) {
					listEntity.add((Entity) connectedNode);
				}
				else if(connectedNode instanceof Association) {
					listAssociation.add((Association) connectedNode);
				}
			}	
		}
	}
	
	private boolean creaFile(String path) throws SaveWasInteruptedException {
		try {
		      File initfile = new File(path+".stdd");
		      if (initfile.createNewFile()) {
		        System.out.println("File created: " + initfile.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      throw new SaveWasInteruptedException("The file couldn't be opened");
		    }
		return true;
	}
	
	private void writeEntity() throws SaveWasInteruptedException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path+".stdd"));
			writer.write("<Entities>");
			for(Node entity : listEntity) {
				writer.write("<Entity>");
				writer.write("<Name>");
				writer.write(entity.getName());
				writer.write("</Name>");
				for(Attribute attribut : entity.getListAttribute()) {
					writer.write("<Attribut>");
					writer.write(attribut.getName()+","+attribut.getType()+","
				+attribut.isNullable()+","+attribut.isPrimaryKey()+","+attribut.isUnique());
					writer.write("</Attribut>");
				}
				writer.write("</Entity>");
			}
			writer.write("</Entities>");
			writer.close();
		}catch(IOException e) {
			throw new SaveWasInteruptedException("Can't write the entity part of the file");
		}
	}
	
	private void writeAssociation() throws SaveWasInteruptedException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path+".stdd"));
			writer.write("<Associations>");
			for(Node association : listAssociation) {
				writer.write("<Entity>");
				writer.write("<Name>");
				writer.write(association.getName());
				writer.write("</Name>");
				for(Attribute attribut : association.getListAttribute()) {
					writer.write("<Attribut>");
					writer.write(attribut.getName()+","+attribut.getType()+","
				+attribut.isNullable()+","+attribut.isPrimaryKey()+","+attribut.isUnique());
					writer.write("</Attribut>");
				}
				writer.write("</Entity>");
			}
			writer.write("</Associations>");
			writer.close();
		}catch(IOException e) {
			throw new SaveWasInteruptedException("Can't write the association part of the file");
		}
	}
	private void writeCard() throws SaveWasInteruptedException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path+".stdd"));
			writer.write("<Cardinalities>");
			for(Association association : listAssociation) {
				writer.write("<Cardinality>");
				for (Map.Entry<String, Cardinality> card : association.getCardinalityMap().entrySet()) { 
					writer.write(association.getName()+","+card.getKey()+","+card.getValue().getLowValue()+","+
				card.getValue().getHighValue());
				}
				writer.write("</Cardinality>");
			}
			writer.write("</Cardinalities>");
			writer.close();
		}catch(IOException e) {
			throw new SaveWasInteruptedException("Can't write the cardinality part of the file");
		}
	}
}

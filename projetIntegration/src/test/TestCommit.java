package test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.AbstractGraphIterator;
import org.jgrapht.traverse.BreadthFirstIterator;

import data.*;
import exceptions.ExistingEdgeException;
import exceptions.InvalidNodeLinkException;
import exceptions.NullNodeException;
import process.MCDManaging;
import process.MLDManaging;
import process.SQLCreation;

public class TestCommit {
	
	public static void main(String[] args) throws InvalidNodeLinkException {
		
		MCDManaging manager = new MCDManaging();
		MLDManaging m = new MLDManaging();
		MLD mld = new MLD();

		
		
		// Entities
		Attribute at1 = new Attribute("Attribute 1","String",false,true,false);
		Attribute at2 = new Attribute("Attribute 2","int",false,false,false);
		Attribute at3 = new Attribute("Attribute 3","String",false,false,false);
		Attribute at4 = new Attribute("Attribute 4","int",false,true,false);
		ArrayList<Attribute> liste1 = new ArrayList<Attribute>();
		liste1.add(at1);
		liste1.add(at2);
		ArrayList<Attribute> liste2 = new ArrayList<Attribute>();
		liste2.add(at3);
		liste2.add(at4);
		
		Entity e1 = new Entity("Entité 1", liste1);
		
		
		Entity e2 = new Entity("Entité 2", liste2);
		Cardinality ce1_e2= new Cardinality("0","N",e1.getName());
		Cardinality ce2_e1= new Cardinality("2","N",e2.getName());
		ArrayList< Cardinality> card = new ArrayList< Cardinality>();
		card.add(ce1_e2);
		card.add(ce2_e1);
		
		
		Association a1 = new Association("Association", new ArrayList<Attribute>(), card);
	
		manager.addNode(e1);
		manager.addNode(e2);
		manager.addNode(a1);
		//m.addkeytoAssociation(manager);
		//System.out.println(mld.getEntityList().isEmpty());
		try {
			manager.connectNodes(e1, a1,ce1_e2);
			manager.connectNodes(e2, a1,ce2_e1);
		} catch (NullNodeException | ExistingEdgeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		

		
	}
}

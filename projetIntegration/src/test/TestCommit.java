package test;

import java.util.ArrayList;
import java.util.HashMap;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import data.*;
import exceptions.ExistingEdgeException;
import exceptions.InvalidNodeLinkException;
import exceptions.NullNodeException;
import process.MCDManaging;

public class TestCommit {
	
	public static void main(String[] args) {
		
		MCDManaging manager = new MCDManaging();
		
		
		// Entities
		Entity e1 = new Entity("Entité 1", new ArrayList<Attribute>());
		Entity e2 = new Entity("Entité 2", new ArrayList<Attribute>());
		Association a1 = new Association("Association", new ArrayList<Attribute>(), new ArrayList<Cardinality>());
	
		manager.addNode(e1);
		manager.addNode(e2);
		manager.addNode(a1);
		try {
			manager.connectNodes(e1, a1);
			manager.connectNodes(e2, a1);
			manager.connectNodes(e2, e1);
		} catch (NullNodeException | ExistingEdgeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidNodeLinkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(manager.isCorrectlyBuild());
	}
}

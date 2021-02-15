package process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.AbstractGraphIterator;
import org.jgrapht.traverse.BreadthFirstIterator;

import data.*;
import exceptions.*;

public class MLDManaging {

	private MLD mld;
	private MCDManaging mcdM;


	public MLDManaging() {
		this.mcdM = new MCDManaging();
		this.mld = new MLD();
	}

/**
	public void  addEntity(MCDManaging mcdM) {
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcdM.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			if (currentNode instanceof Entity) {
				mld.getEntityList().add((Entity) currentNode);
			}
		}
		for(int i = 0 ; i < mld.getEntityList().size(); i++) {
			
			Entity n=mld.getEntityList().get(i);
			System.out.println(n.getName());
		}
		System.out.println(mld.getEntityList().isEmpty());
		
	}*/
	
	public ArrayList<Entity>  ListOfAllEntities(MCDManaging mcdM) {
		ArrayList<Entity> listEntity= new ArrayList<Entity>();
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcdM.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			if (currentNode instanceof Entity) {
				listEntity.add((Entity) currentNode);
			}
		}
		return listEntity;
		
		
	}
	
	
	public ArrayList<Attribute> ListOfPk(Entity e1){
		ArrayList<Attribute> listepk = new ArrayList<Attribute>();
		ArrayList<Attribute> listeAttribute = new ArrayList<Attribute>();
		listeAttribute=e1.getListAttribute();
		for (Attribute attpk : listeAttribute) {
			if(attpk.isPrimaryKey()) {
				listepk.add(attpk);
				System.out.println(attpk.getName()+",");
			}
		}
		return listepk;

	}
	
	//This function add key put foreing keys
	public ArrayList<Entity>  addAssociationToMLD(MCDManaging mcdM) {
		ArrayList<Entity> ListOfAllEntities = new ArrayList<Entity>();
		boolean addfk=true;
		String nameE;
		String high;
		List<String> listCard = new ArrayList<String>();
		List<String> listName = new ArrayList<String>();
		Association association;
		List<Cardinality> Card;
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcdM.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcdM.getMCDGraph(), currentNode);
			//Verify the type of Cardinality
			if (currentNode instanceof Association) {
				System.out.println("yaya:"+currentNode.getName());
				association=(Association)currentNode;
				Card=association.getCardinalityList();
				ListIterator<Cardinality> lItr = Card.listIterator();
				while (lItr.hasNext()) {
				    high=lItr.next().getHighValue();
				    nameE=lItr.next().getNomEntity();
				    listCard.add(high);
				    listName.add(nameE);
				}
				for(String str:listCard) {
					if(str.equals("N")) {
						
					}
					else {
						addfk=false;
						break;
					}
				}
				if(addfk==true) {
					//ArrayList<Attribute> newAttribute=new ArrayList<MLDAttribute>();
					for(int i = 0 ; i < connectedNodes.size(); i++) {
						ArrayList<Attribute> liste = new ArrayList<Attribute>();
						Node n=connectedNodes.get(i);
						System.out.println("neud actuel:"+n.getName());
						liste=n.getListAttribute();
						//put the pk first
						for (Attribute attpk : liste) {
							if(attpk.isPrimaryKey()) {
								MLDAttribute mldA = new MLDAttribute(attpk.getName(),
										attpk.getType(), attpk.isNullable(), attpk.isPrimaryKey(),
										attpk.isUnique(), true, n, attpk);	
								//newAttribute.add(mldA);
							}
						}
					}
					//Entity e = new Entity(currentNode.getName(),newAttribute);
					//ListOfAllEntities.add(e);
				}
				else {
					System.out.println("on ajoute pas");
				}
			}
			
		}
		return ListOfAllEntities;
	}
	
	
	public void test(MCDManaging mcdM) {
		ArrayList<Entity> ListOfAllEntities = addAssociationToMLD(mcdM);
		for(int i = 0 ; i < ListOfAllEntities.size(); i++) {
			Node n=ListOfAllEntities.get(i);
			ArrayList<Attribute> liste = new ArrayList<Attribute>();
			System.out.println("neud actuel:"+n.getName());
			liste=n.getListAttribute();
			for (Attribute attpk : liste) {
				//System.out.println("narggh:"+attpk.isForeignKey());
				}
			
		}
	}
	
	
	
	
	
	
	
	
	
	

	
	
/**	
	public void  test(MCDManaging mcdM) {
		String n="";
		ArrayList<Attribute> liste = new ArrayList<Attribute>();
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcdM.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			liste=currentNode.getListAttribute();
			System.out.print("\n"+currentNode.getName()+":");
			//put the pk first
			for (Attribute attpk : liste) {
				if(attpk.isPrimaryKey()) {
					System.out.print(attpk.getName()+",");
				}
			}
			for (Attribute att : liste) {
				if(!att.isPrimaryKey()) {
					System.out.print(att.getName()+",");
				}
			}
		}
	}*/
	
/**	public String  test2(MCDManaging mcdM) {
		String n="";
		String low="";
		String high="";
		HashMap<String, Cardinality> b;
		Association a= new Association(n, null, null);
		ArrayList<Attribute> liste = new ArrayList<Attribute>();
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcdM.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			liste=currentNode.getListAttribute();
			if (currentNode instanceof Entity) {
				System.out.println("Entité");
			}
			else if (currentNode instanceof Association) {
				System.out.println("Association");
				a=(Association)currentNode;
				b=a.getCardinalityMap();
				System.out.println("Initial Mappings are: " + b);
				System.out.println("Is the map empty? " + b.isEmpty());
				for( Map.Entry<String, Cardinality> entry : b.entrySet() ){
				    System.out.println( entry.getKey() + " => " + entry.getValue() );
				    low=(entry.getValue()).getLowValue();
				    high=(entry.getValue()).getHighValue();
				    System.out.println( high + " => " + low );
				}
			}
			else {
				System.out.println("r");
			}
		}
		return n;
	}*/
	
	
	
	
	




}

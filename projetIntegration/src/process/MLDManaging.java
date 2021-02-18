package process;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.AbstractGraphIterator;
import org.jgrapht.traverse.BreadthFirstIterator;

import data.*;

public class MLDManaging {

	private MLD mld;
	private MCDManaging mcdM;
	private ArrayList<Entity> entityListToMld;
	private MCD mcd;


	public MLDManaging() {
		this.mcdM = new MCDManaging();
		this.entityListToMld = new ArrayList<Entity>();
		this.mcd = new MCD();
	}
	
	public MLDManaging(ArrayList<Entity> entityListToMld) {
		this.entityListToMld = entityListToMld;
	}
	
	
	//This function add key put foreing keys
	public ArrayList<Entity>  addAssociationToMLD(MCDManaging mcdM) {
		MCD mcd=mcdM.getMCDGraph();
		UndirectedGraph<Node, DefaultEdge> graph=mcd.getMCDGraph();
		ArrayList<Entity> ListOfAllEntities = new ArrayList<Entity>();
		boolean addfk=true;
		MLDAttribute mldA;
		String nameE;
		String high;
		List<String> listCard = new ArrayList<String>();
		List<String> listName = new ArrayList<String>();
		Association association;
		List<Cardinality> Card;
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			//Verify the type of Cardinality
			if (currentNode instanceof Association) {
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
					if(str.equals("N")) {}
					else {addfk=false;break;}
				}if(addfk==true) {
					ArrayList<Attribute> newAttribute=new ArrayList<Attribute>();
					for(int i = 0 ; i < connectedNodes.size(); i++) {
						ArrayList<Attribute> liste = new ArrayList<Attribute>();
						Node n=connectedNodes.get(i);
						liste=n.getListAttribute();
						//put the pk first
						for (Attribute attpk : liste) {
							if(attpk.isPrimaryKey()) {
								mldA = new MLDAttribute(attpk.getName(),
										attpk.getType(), attpk.isNullable(), attpk.isPrimaryKey(),
										attpk.isUnique(), true, n, attpk);	
								newAttribute.add(mldA);
							}
						}
					}
					Entity e = new Entity(currentNode.getName(),newAttribute);
					ListOfAllEntities.add(e);
				}
				else {
					System.out.println("on ajoute pas");
				}
			}
			
		}
		return ListOfAllEntities;
	}
	

	
	public ArrayList<Entity>  addEntitiesToMLD(MCDManaging mcdM) {
		MCD mcd=mcdM.getMCDGraph();
		UndirectedGraph<Node, DefaultEdge> graph=mcd.getMCDGraph();
		ArrayList<Attribute> liste;
		String high1,high2,low1,low2;
		Entity e1,e2;
		ArrayList<Entity> ListOfAllEntities = new ArrayList<Entity>();
		Association association;
		List<Cardinality> Card;
		int nb_neighbor;
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			nb_neighbor=connectedNodes.size();
			//Verify the type of Cardinality
			if ((currentNode instanceof Association)&&(nb_neighbor==2)) {
				association=(Association)currentNode;
				Card=association.getCardinalityList();
				e1=(Entity) connectedNodes.get(0);e2=(Entity) connectedNodes.get(1);
				high1=Card.get(0).getHighValue();high2=Card.get(1).getHighValue();
				low1=Card.get(0).getLowValue();low2=Card.get(1).getLowValue();
				//Case where one of max card is "N" and the other one is something else
				if(((high1.equals("N"))&&(!high2.equals("N")))||(high2.equals("N"))&&(!high1.equals("N"))) {
					if((high1.equals("N"))&&(!high2.equals("N"))) {
						System.out.println(e2.getName());
						liste = new ArrayList<Attribute>();
						System.out.println("size début:"+e2.getListAttribute().size());
						liste=e1.getListAttribute();
						for (Attribute attpk1 : liste) {
							if(attpk1.isPrimaryKey()) {
								MLDAttribute mldA = new MLDAttribute(attpk1.getName(),
										attpk1.getType(), attpk1.isNullable(), attpk1.isPrimaryKey(),
										attpk1.isUnique(), true, e1, attpk1);	
								e1.getListAttribute().add(mldA);
								System.out.println("size début:"+e1.getListAttribute().size());
								ListOfAllEntities.add(e2);
								ListOfAllEntities.add(e1);
							}
						}
					}
					else if ((high2.equals("N"))&&(!high1.equals("N"))){
						liste = new ArrayList<Attribute>();
						System.out.println("size début:"+e1.getListAttribute().size());
						liste=e2.getListAttribute();
						for (Attribute attpk : liste) {
							if(attpk.isPrimaryKey()) {
								MLDAttribute mldA = new MLDAttribute(attpk.getName(),
										attpk.getType(), attpk.isNullable(), attpk.isPrimaryKey(),
										attpk.isUnique(), true, e2, attpk);	
								e1.getListAttribute().add(mldA);
								System.out.println("size début:"+e1.getListAttribute().size());
								ListOfAllEntities.add(e2);
								ListOfAllEntities.add(e1);
							}
						}
					}
				}
				//Case where we have (0,1) and (1,1)
				else if((high1.equals("1"))&&(low1.equals("0"))&&(high2.equals("1"))&&(low2.equals("1"))){
					liste = new ArrayList<Attribute>();
					System.out.println("size début:"+e1.getListAttribute().size());
					liste=e2.getListAttribute();
					for (Attribute attpk : liste) {
						if(attpk.isPrimaryKey()) {
							MLDAttribute mldA = new MLDAttribute(attpk.getName(),
									attpk.getType(), attpk.isNullable(), attpk.isPrimaryKey(),
									attpk.isUnique(), true, e2, attpk);	
							e1.getListAttribute().add(mldA);
							System.out.println("size début:"+e1.getListAttribute().size());
							ListOfAllEntities.add(e2);
							ListOfAllEntities.add(e1);
						}
					}
				}
				else if((high2.equals("1"))&&(low2.equals("0"))&&(high1.equals("1"))&&(low1.equals("1"))){
					System.out.println(e2.getName());
					liste = new ArrayList<Attribute>();
					System.out.println("size début:"+e2.getListAttribute().size());
					liste=e1.getListAttribute();
					for (Attribute attpk1 : liste) {
						if(attpk1.isPrimaryKey()) {
							MLDAttribute mldA = new MLDAttribute(attpk1.getName(),
									attpk1.getType(), attpk1.isNullable(), attpk1.isPrimaryKey(),
									attpk1.isUnique(), true, e1, attpk1);	
							e1.getListAttribute().add(mldA);
							System.out.println("size début:"+e1.getListAttribute().size());
							ListOfAllEntities.add(e2);
							ListOfAllEntities.add(e1);
						}
					}
				}
				else {
					ListOfAllEntities.add(e2);
					ListOfAllEntities.add(e1);
				}
			}
			else if ((currentNode instanceof Association)&&(nb_neighbor==3)) {
				//ajout d'entité dans le cas avec 3 association
			}
			else {
				//Imposible
			}
			
		}
		return ListOfAllEntities;
	}
	
	
	public ArrayList<Entity> ListForMld(MCDManaging mcdM){
		MLD data = new MLD();
		ArrayList<Entity> ListOfAllAssociation = new ArrayList<Entity>();
		ArrayList<Entity> ListOf2Entities = new ArrayList<Entity>();
		ListOf2Entities=addEntitiesToMLD(mcdM);
		ListOfAllAssociation=addAssociationToMLD(mcdM);
		entityListToMld.addAll(ListOfAllAssociation);
		entityListToMld.addAll(ListOf2Entities);
		data.setEntityList(entityListToMld);
		return entityListToMld;
		
	}
	
	
	public MLD newMld(MCDManaging mcdM){
		MLD mld = new MLD();
		System.out.println(mld.getEntityList().size());
		ArrayList<Entity> AllEntities = new ArrayList<Entity>();
		AllEntities =ListForMld(mcdM);
		mld = new MLD(AllEntities);
		System.out.println(mld.getEntityList().size());
		return mld;
	}
}

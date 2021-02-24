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
	//private ArrayList<Entity> entityListToMld;
	private MCD mcd;


	public MLDManaging() {
		this.mcdM = new MCDManaging();
		this.mld = new MLD();
		//this.entityListToMld = new ArrayList<Entity>();
		this.mcd = new MCD();
	}

	
	/**	
	public MLDManaging(ArrayList<Entity> entityListToMld) {
		this.entityListToMld = entityListToMld;
	}*/
	
	
	public MLDManaging(MLD mld) {
		this.mld = mld;
	}

	
	
	
	
	

//Method		
	
	
	public MCD  newtoMCD(MCD mcd) {
		ArrayList<Attribute> liste,liste1;
		String high1 = null;String low1 = null;String high2= null;String low2= null;
		//MCD mcd=mcdM.getMCD();
		//UndirectedGraph<Node, DefaultEdge> graph=mcd.getMCDGraph();
		//ArrayList<Entity> ListOfAllEntities = new ArrayList<Entity>();
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			if ((currentNode instanceof Entity)) {
				Entity actualEntity=(Entity)currentNode;
				for (Node actualNode : connectedNodes) {
					Association actualAssociation=(Association)actualNode;
					List<Node> connectedToAssociation = Graphs.neighborListOf(mcd.getMCDGraph(), actualAssociation);			
					if(connectedToAssociation.size()==3) {/*ne rien faire*/}
					else if(connectedToAssociation.size()==2) {
						Entity e2 = null;
						for (Node j:connectedToAssociation) {
							Entity k=(Entity)j;
							if(!actualEntity.equals(k)) {
								e2=k;
							}
						}
						ArrayList<Cardinality> listCard = actualAssociation.getCardinalityList();
						for (int i = 0;i<listCard.size();i++) {
							if(listCard.get(i).getNomEntity().equals(actualEntity.getName())) {
								high1=listCard.get(i).getHighValue();
								low1=listCard.get(i).getLowValue();
							}
							else {
								high2=listCard.get(i).getHighValue();
								low2=listCard.get(i).getLowValue();
							}
						}
						if ((high2.equals("N"))&&(!high1.equals("N"))){
							liste = new ArrayList<Attribute>();
							liste1 = new ArrayList<Attribute>();
							liste=e2.getListAttribute();
							liste1=actualEntity.getListAttribute();
							for (Attribute attpk : liste) {
								if(attpk.isPrimaryKey()) {
									boolean newAtt = true;
									MLDAttribute mldA = new MLDAttribute(attpk.getName(),
											attpk.getType(), attpk.isNullable(), false,
											attpk.isUnique(), true, e2, attpk);	
									for(Attribute l :liste1) {
										if(l.getName().equals(mldA.getName())) {newAtt=false;break;}
									}
									if(newAtt==true){currentNode.getListAttribute().add(mldA);}
								}
							}
						}
						else if((high1.equals("1"))&&(low1.equals("0"))&&(high2.equals("1"))&&(low2.equals("1"))){
							liste = new ArrayList<Attribute>();
							liste1 = new ArrayList<Attribute>();
							liste=e2.getListAttribute();
							liste1=actualEntity.getListAttribute();
							for (Attribute attpk : liste) {
								if(attpk.isPrimaryKey()) {
									boolean newAtt = true;
									MLDAttribute mldA = new MLDAttribute(attpk.getName(),
											attpk.getType(), attpk.isNullable(), false,
											attpk.isUnique(), true, e2, attpk);	
									for(Attribute l :liste1) {
										if(l.getName().equals(mldA.getName())) {newAtt=false;break;}
									}
									if(newAtt==true){currentNode.getListAttribute().add(mldA);}
								}
							}
						}
					}
				}
			}
		}
		return mcd;
	}
	
	
	
	public MCD  newAssociation(MCD mcd) {
		MLDAttribute mldA;
		UndirectedGraph<Node, DefaultEdge> graph=mcd.getMCDGraph();
		String high;
		Entity e1,e2;
		ArrayList<Entity> ListOfAllEntities = new ArrayList<Entity>();
		Association association;
		List<Cardinality> Card;
		List<String> listCard = new ArrayList<String>();
		int nb_neighbor;
		boolean addfk=true;
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			nb_neighbor=connectedNodes.size();
			//Verify the type of Cardinality
			if ((currentNode instanceof Association)&&(nb_neighbor==3)) {
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
							currentNode.getListAttribute().add(mldA);
						}
					}
				}
				Entity e = new Entity(currentNode.getName(),newAttribute);
				ListOfAllEntities.add(e);
			}
			else if ((currentNode instanceof Association)&&(nb_neighbor==2)) {
				association=(Association)currentNode;
				Card=association.getCardinalityList();
				ListIterator<Cardinality> lItr = Card.listIterator();
				int it=0;
				String h1=null,l1=null,h2=null,l2=null;
				for(Cardinality c:Card) {
					high=c.getHighValue();
					listCard.add(high);
					if (it==0) {
						h1=c.getHighValue();
						l1=c.getLowValue();
					}
					else if (it==1) {
						h2=c.getHighValue();
						l2=c.getLowValue();
					}
					it++;
				}
				for(String str:listCard) {
					if(str.equals("N")) {addfk=true;}
					else {addfk=false;break;}
				}if ((addfk==true)||((h1.equals("1"))&&(h2.equals("1"))&&(l1.equals("0"))&&(l2.equals("0")))) {
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
								currentNode.getListAttribute().add(mldA);
							}
						}
					}
					Entity e = new Entity(currentNode.getName(),newAttribute);
					ListOfAllEntities.add(e);
				}
				//else {System.out.println("on ajoute pas");}
			}
		}
		return mcd;
	}
	
	
	
	public MCD newMCD(MCDManaging mcdM) {
		MCD mcd=mcdM.getMCD();
		MCD firstMCD=newtoMCD(mcd);
		MCD secondMCD=newtoMCD(firstMCD);
		MCD finalMCD=newAssociation(secondMCD);
		return mcd;
	}
	

	
	
	
	
	
	
	public ArrayList<Entity>  addEntityToMLD(MCDManaging mcdM) {
		MCD mcd=newMCD(mcdM);
		ArrayList<Entity> entityListToMld=new ArrayList<Entity>();
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			if ((currentNode instanceof Entity)) {
				entityListToMld.add((Entity) currentNode);
			}
		}
		//System.out.println("Fin entite: "+entityListToMld.size());
		return entityListToMld;
	}
	
	
	public ArrayList<Entity>  addAssociationToMLD(MCDManaging mcdM) {
		MCD mcd=newMCD(mcdM);
		ArrayList<Entity> entityListToMld=new ArrayList<Entity>();
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			if ((currentNode instanceof Association)) {
				
				ArrayList<Attribute> newAttribute=new ArrayList<Attribute>();
				
				
				ArrayList<Attribute> curentNodeAttribute=currentNode.getListAttribute();
				for(int i=0;i<curentNodeAttribute.size();i++) {
					MLDAttribute cat = new MLDAttribute(null, null, false, false, false, false, null, null);
					
					if (curentNodeAttribute.get(i) instanceof MLDAttribute) {
						newAttribute.add(curentNodeAttribute.get(i));
					}
					else {
						newAttribute.add(curentNodeAttribute.get(i));
					}

				}
				
				Entity newEntity=new Entity(currentNode.getName(), newAttribute);
				entityListToMld.add(newEntity);
			}
		}
		System.out.println("Fin asso: "+entityListToMld.size());
		return entityListToMld;
		
	}
	
	
	
	
	public ArrayList<Entity> ListForMld(MCDManaging mcdM){
		MLD data = new MLD();
		ArrayList<Entity> entityListToMld=new ArrayList<Entity>();
		ArrayList<Entity> ListOfAllAssociation = new ArrayList<Entity>();
		ArrayList<Entity> ListOf2Entities = new ArrayList<Entity>();
		ListOf2Entities=addEntityToMLD(mcdM);
		ListOfAllAssociation=addAssociationToMLD(mcdM);
		//System.out.println("Debut: "+entityListToMld.size());
		entityListToMld.addAll(ListOf2Entities);
		//System.out.println("Après entité:"+entityListToMld.size());
		entityListToMld.addAll(ListOfAllAssociation);
		//System.out.println("Après association:"+entityListToMld.size());
		data.setEntityList(entityListToMld);
		return entityListToMld;
		
	}
	
	
	public MLD newMld(MCDManaging mcdM){
		//System.out.println(mld.getEntityList().size());
		ArrayList<Entity> AllEntities = new ArrayList<Entity>();
		AllEntities =ListForMld(mcdM);
		mld = new MLD(AllEntities);
		//System.out.println(mld.getEntityList().size());
		return mld;
	}
	
	
	public MLD getMLD(){
		return mld;
	}
	
	


	public ArrayList<Entity>  addAssociation(MCDManaging mcdM) {
		MLDAttribute mldA;
		MCD mcd=mcdM.getMCD();
		UndirectedGraph<Node, DefaultEdge> graph=mcd.getMCDGraph();
		String high;
		Entity e1,e2;
		ArrayList<Entity> ListOfAllEntities = new ArrayList<Entity>();
		Association association;
		List<Cardinality> Card;
		List<String> listCard = new ArrayList<String>();
		int nb_neighbor;
		boolean addfk=true;
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			nb_neighbor=connectedNodes.size();
			//Verify the type of Cardinality
			if ((currentNode instanceof Association)&&(nb_neighbor==3)) {
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
			else if ((currentNode instanceof Association)&&(nb_neighbor==2)) {
				association=(Association)currentNode;
				Card=association.getCardinalityList();
				ListIterator<Cardinality> lItr = Card.listIterator();
				while (lItr.hasNext()) {
				    high=lItr.next().getHighValue();
				    listCard.add(high);
				}
				for(String str:listCard) {
					if(str.equals("N")) {addfk=true;}
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
				else {System.out.println("on ajoute pas");}
			}
		}
		return ListOfAllEntities;
	}
	
	
	public ArrayList<Entity>  addEntities(MCDManaging mcdM) {
		ArrayList<Attribute> liste;
		String high1 = null;
		String low1 = null;
		String high2= null;
		String low2= null;
		MCD mcd=mcdM.getMCD();
		UndirectedGraph<Node, DefaultEdge> graph=mcd.getMCDGraph();
		ArrayList<Entity> ListOfAllEntities = new ArrayList<Entity>();
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			if ((currentNode instanceof Entity)) {
				//System.out.println("---------");
				Entity actualEntity=(Entity)currentNode;
				System.out.println("------Entité actuel:"+actualEntity.getName()+"-----");
				for (Node actualNode : connectedNodes) {
					Association actualAssociation=(Association)actualNode;
					//System.out.println("asso actuel:"+actualAssociation.getName());
					List<Node> connectedToAssociation = Graphs.neighborListOf(mcd.getMCDGraph(), actualAssociation);			
					//System.out.println(connectedToAssociation.size());
					if(connectedToAssociation.size()==3) {/*ne rien faire*/}
					else if(connectedToAssociation.size()==2) {
						Entity e2 = null;
						for (Node j:connectedToAssociation) {
							Entity k=(Entity)j;
							if(!actualEntity.equals(k)) {
								e2=k;
								System.out.println(e2.getName());
								
							}
						}
						//System.out.println("asso actuel:"+actualAssociation.getName());
						//ArrayList<Cardinality> 
						ArrayList<Cardinality> listCard = actualAssociation.getCardinalityList();
						for (int i = 0;i<listCard.size();i++) {
							if(listCard.get(i).getNomEntity().equals(actualEntity.getName())) {
								high1=listCard.get(i).getHighValue();
								low1=listCard.get(i).getLowValue();
								
							}
							else {
								high2=listCard.get(i).getHighValue();
								low2=listCard.get(i).getLowValue();
							}
						}
						System.out.println(high1);
						System.out.println(low1);
						System.out.println(high2);
						System.out.println(e2.getName());
						
						
						if ((high2.equals("N"))&&(!high1.equals("N"))){
							
							liste = new ArrayList<Attribute>();
							System.out.println("size début:"+actualEntity.getListAttribute().size());
							liste=e2.getListAttribute();
							for (Attribute attpk : liste) {
								if(attpk.isPrimaryKey()) {
									MLDAttribute mldA = new MLDAttribute(attpk.getName(),
											attpk.getType(), attpk.isNullable(), false,
											attpk.isUnique(), true, e2, attpk);	
									actualEntity.getListAttribute().add(mldA);
									System.out.println("size début:"+actualEntity.getListAttribute().size());
								}
							}
						}
						else if((high1.equals("1"))&&(low1.equals("0"))&&(high2.equals("1"))&&(low2.equals("1"))){
							System.out.println("Cas 3");
							liste = new ArrayList<Attribute>();
							System.out.println("size début:"+actualEntity.getListAttribute().size());
							liste=e2.getListAttribute();
							for (Attribute attpk : liste) {
								if(attpk.isPrimaryKey()) {
									MLDAttribute mldA = new MLDAttribute(attpk.getName(),
											attpk.getType(), attpk.isNullable(), false,
											attpk.isUnique(), true, e2, attpk);	
									actualEntity.getListAttribute().add(mldA);
									System.out.println("size début:"+actualEntity.getListAttribute().size());
								}
							}
						}
					}
				}
			}
		}
		return ListOfAllEntities;
	}
	
	
	
	
/**
	
	
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
	
	public void  testduprograme(MCDManaging mcdM) {
		MCD mcd= newMCD(mcdM);
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			System.out.println("**********"+currentNode.getName()+"*********");
			
			
			ArrayList<Attribute> att=currentNode.getListAttribute();
			
			
			for(Attribute i:att) {
				MLDAttribute dog = new MLDAttribute(null, null, false, false, false, false, null, null);
				if (i instanceof MLDAttribute) {
					dog = (MLDAttribute) i;
					System.out.println("--"+dog.getName());
					//System.out.println("--"+dog.isForeignKey());
				}
				else {
					System.out.println(dog.getName());
				}
			}
			
			
			
			for(int i=0;i<att.size();i++) {
				MLDAttribute cat = new MLDAttribute(null, null, false, false, false, false, null, null);
				
				if (att.get(i) instanceof MLDAttribute) {
					System.out.println("--"+att.get(i).getName());
					System.out.println("--"+((MLDAttribute) att.get(i)).isForeignKey());
				}
				else {
					System.out.println(att.get(i).getName());
				}
			}
			
			
		}
	}
	*/
}
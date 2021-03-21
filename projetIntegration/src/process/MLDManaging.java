package process;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.AbstractGraphIterator;
import org.jgrapht.traverse.BreadthFirstIterator;

import data.Association;
import data.Attribute;
import data.Cardinality;
import data.Entity;
import data.MCD;
import data.MLD;
import data.MLDAttribute;
import data.Node;

public class MLDManaging {

	
	
	private MLD mld;

	public MLDManaging() {
		this.mld = new MLD();
	}
	
	public MLDManaging(MLD mld) {
		this.mld = mld;
	}

		
	
	/*
	 * With this method we're going to add foreign keys in the Entity that are supposed to receive some
	 * There is different case where we can add foreign keys which are:
	 * 	[*,n]/[*,1]
	 * 	[0,1]/[1,1]
	 */
	public MCD  newtoMCD(MCD mcd) {
		ArrayList<Attribute> liste,liste1;
		String high1 = null;String low1 = null;String high2= null;String low2= null;
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			if ((currentNode instanceof Entity)) {
				Entity actualEntity=(Entity)currentNode;
				for (Node actualNode : connectedNodes) {
					Association actualAssociation=(Association)actualNode;
					List<Node> connectedToAssociation = Graphs.neighborListOf(mcd.getMCDGraph(), actualAssociation);			
					if(connectedToAssociation.size()==3) {}
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
						if(e2!=null) {
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
										ArrayList<Attribute> att= liste1;
										for(int i=0;i<att.size();i++) {
											if (att.get(i) instanceof MLDAttribute) {
												if((att.get(i).getName().equals(mldA.getName()))
													&&	(att.get(i).getType().equals(mldA.getType()))
													&&	(((MLDAttribute) att.get(i)).isForeignKey()==(mldA.isForeignKey()))
													&&	(((MLDAttribute) att.get(i)).getReferenceNode().equals(mldA.getReferenceNode()))
													&&	(((MLDAttribute) att.get(i)).getReferenceAttribute().equals(mldA.getReferenceAttribute()))
												) {
													newAtt=false;break;
												}
											}
											else {
												if(att.get(i).getName().equals(mldA.getName())) {
													newAtt=false;break;
												}
											}
											
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
										ArrayList<Attribute> att= liste1;
										for(int i=0;i<att.size();i++) {
											if (att.get(i) instanceof MLDAttribute) {
												if((att.get(i).getName().equals(mldA.getName()))
													&&	(att.get(i).getType().equals(mldA.getType()))
													&&	(((MLDAttribute) att.get(i)).isForeignKey()==(mldA.isForeignKey()))
													&&	(((MLDAttribute) att.get(i)).getReferenceNode().equals(mldA.getReferenceNode()))
													&&	(((MLDAttribute) att.get(i)).getReferenceAttribute().equals(mldA.getReferenceAttribute()))
												) {
													newAtt=false;break;
												}
											}
											else {
												if(att.get(i).getName().equals(mldA.getName())) {
													newAtt=false;break;
												}
											}
											
										}
										if(newAtt==true){currentNode.getListAttribute().add(mldA);}
									}
								}
							}
							
						}	
					}
				}
			}
		}
		return mcd;
	}
	
	
	/*
	 *Here we treat the case where the association is [1,1]/[1,1]
	 * In this case we ask to the user how he would like to treat it
	 */
	public MCD Adding11(MCD mcd) {
		Association association;
		List<Cardinality> Card;
		int nb_neighbor;
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			nb_neighbor=connectedNodes.size();
			if ((currentNode instanceof Association)&&(nb_neighbor==2)) {
				association=(Association)currentNode;
				Card=association.getCardinalityList();
				String h1=null,l1=null,h2=null,l2=null;
				h1=Card.get(0).getHighValue();
				l1=Card.get(0).getLowValue();
				h2=Card.get(1).getHighValue();
				l2=Card.get(1).getLowValue();

				Entity e1=(Entity) connectedNodes.get(0);
				Entity e2=(Entity) connectedNodes.get(1);
				
				if(e1!=e2) {
					if (((h1.equals("1"))&&(h2.equals("1"))&&(l1.equals("1"))&&(l2.equals("1")))) {
						String firstEntity=e1.getName();
						String secondEntity=e2.getName();
						String twoOfThem="Les deux";
						String[] choices = {firstEntity , secondEntity,twoOfThem};
					    String input = (String) JOptionPane.showInputDialog(null, "Choose now...",
					        "Quelle Entité a le fonctionnement le plus important? ", JOptionPane.QUESTION_MESSAGE, null,choices,
					        choices[1]); // Initial choice
					    System.out.println(input);
					    if (input==firstEntity) {
					    	ArrayList<Attribute> liste = new ArrayList<Attribute>();
					    	ArrayList<Attribute> liste1 = new ArrayList<Attribute>();
							liste=e1.getListAttribute();
							liste1=e2.getListAttribute();
							for (Attribute attpk : liste) {
								if(attpk.isPrimaryKey()) {
									boolean newAtt = true;
									MLDAttribute mldA = new MLDAttribute(attpk.getName(),
											attpk.getType(), attpk.isNullable(), false,
											attpk.isUnique(), true, e1, attpk);
									ArrayList<Attribute> att= liste1;
									for(int i=0;i<att.size();i++) {
										if (att.get(i) instanceof MLDAttribute) {
											if((att.get(i).getName().equals(mldA.getName()))
												&&	(att.get(i).getType().equals(mldA.getType()))
												&&	(((MLDAttribute) att.get(i)).isForeignKey()==(mldA.isForeignKey()))
												&&	(((MLDAttribute) att.get(i)).getReferenceNode().equals(mldA.getReferenceNode()))
												&&	(((MLDAttribute) att.get(i)).getReferenceAttribute().equals(mldA.getReferenceAttribute()))
											) {
												newAtt=false;break;
											}
										}
										else {
											if(att.get(i).getName().equals(mldA.getName())) {
												newAtt=false;break;
											}
										}
										
									}
									if(newAtt==true){e2.getListAttribute().add(mldA);}
								}
							}
						}
					    
					    else   if (input==secondEntity) {
					    	ArrayList<Attribute> liste = new ArrayList<Attribute>();
					    	ArrayList<Attribute> liste1 = new ArrayList<Attribute>();
							liste=e2.getListAttribute();
							liste1=e1.getListAttribute();
							for (Attribute attpk : liste) {
								if(attpk.isPrimaryKey()) {
									boolean newAtt = true;
									MLDAttribute mldA = new MLDAttribute(attpk.getName(),
											attpk.getType(), attpk.isNullable(), false,
											attpk.isUnique(), true, e2, attpk);
									ArrayList<Attribute> att= liste1;
									for(int i=0;i<att.size();i++) {
										if (att.get(i) instanceof MLDAttribute) {
											if((att.get(i).getName().equals(mldA.getName()))
												&&	(att.get(i).getType().equals(mldA.getType()))
												&&	(((MLDAttribute) att.get(i)).isForeignKey()==(mldA.isForeignKey()))
												&&	(((MLDAttribute) att.get(i)).getReferenceNode().equals(mldA.getReferenceNode()))
												&&	(((MLDAttribute) att.get(i)).getReferenceAttribute().equals(mldA.getReferenceAttribute()))
											) {
												newAtt=false;break;
											}
											//System.out.println("--"+att.get(i).getName());
											//System.out.println("--"+((MLDAttribute) att.get(i)).isForeignKey());
										}
										else {
											if(att.get(i).getName().equals(mldA.getName())) {
												newAtt=false;break;
											}
										}
										
									}
									if(newAtt==true){e1.getListAttribute().add(mldA);}
								}
							}
					    }
					    else   if (input==twoOfThem) {
					    	ArrayList<Entity> ListOfAllEntities = new ArrayList<Entity>();
					    	ArrayList<Attribute> newAttribute=new ArrayList<Attribute>();
							for(int i = 0 ; i < connectedNodes.size(); i++) {
								ArrayList<Attribute> liste = new ArrayList<Attribute>();
								Node n=connectedNodes.get(i);
								liste=n.getListAttribute();
								ArrayList<Attribute> liste1=currentNode.getListAttribute();
								for (Attribute attpk : liste) {
									if(attpk.isPrimaryKey()) {
										MLDAttribute mldA = new MLDAttribute(attpk.getName(),
												attpk.getType(), attpk.isNullable(), attpk.isPrimaryKey(),
												attpk.isUnique(), true, n, attpk);	
										boolean newAtt = true;
										for(Attribute l :liste1) {
											if(l.getName().equals(mldA.getName())) {newAtt=false;break;}
										}
										if(newAtt==true){currentNode.getListAttribute().add(mldA);}
									}
								}
							}
							Entity e = new Entity(currentNode.getName(),newAttribute);
							ListOfAllEntities.add(e);
					    }
					    
					}
				}
			
			}
		}
		return mcd;
	}
	
	
	

	
	
	/*
	 * With this method we're going to add foreign keys in the Associations that are supposed to receive some
	 * There is different case where we can add foreign keys which are:
	 * 	Association that are connected to 3 entities
	 * 	[*,N]/[*,N]
	 * [0,1]/[0,1]
	 */
	public MCD  newAssociation(MCD mcd) {
		MLDAttribute mldA;
		String high;
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
					ArrayList<Attribute> liste1=currentNode.getListAttribute();
					//put the pk first
					for (Attribute attpk : liste) {
						if(attpk.isPrimaryKey()) {
							mldA = new MLDAttribute(attpk.getName(),
									attpk.getType(), attpk.isNullable(), attpk.isPrimaryKey(),
									attpk.isUnique(), true, n, attpk);
							boolean newAtt = true;
							for(Attribute l :liste1) {
								if(l.getName().equals(mldA.getName())) {newAtt=false;break;}
							}
							if(newAtt==true){currentNode.getListAttribute().add(mldA);}
						}
					}
				}
				Entity e = new Entity(currentNode.getName(),newAttribute);
				ListOfAllEntities.add(e);
			}
			else if ((currentNode instanceof Association)&&(nb_neighbor==2)) {
				association=(Association)currentNode;
				Card=association.getCardinalityList();
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
				}
				Entity e1=(Entity) connectedNodes.get(0);
				Entity e2=(Entity) connectedNodes.get(1);
				if(e1!=e2) {
					if (((addfk==true)||((h1.equals("1"))&&(h2.equals("1"))&&(l1.equals("0"))&&(l2.equals("0"))))
							&&((connectedNodes.get(0)!=null)&&(connectedNodes.get(1)!=null)&&(h2!=null)&&(l2!=null))) {
						ArrayList<Attribute> newAttribute=new ArrayList<Attribute>();
						for(int i = 0 ; i < connectedNodes.size(); i++) {
							ArrayList<Attribute> liste = new ArrayList<Attribute>();
							Node n=connectedNodes.get(i);
							liste=n.getListAttribute();
							ArrayList<Attribute> liste1=currentNode.getListAttribute();
							for (Attribute attpk : liste) {
								if(attpk.isPrimaryKey()) {
									mldA = new MLDAttribute(attpk.getName(),
											attpk.getType(), attpk.isNullable(), attpk.isPrimaryKey(),
											attpk.isUnique(), true, n, attpk);	
									boolean newAtt = true;
									for(Attribute l :liste1) {
										if(l.getName().equals(mldA.getName())) {newAtt=false;break;}
									}
									if(newAtt==true){currentNode.getListAttribute().add(mldA);}
								}
							}
						}
						Entity e = new Entity(currentNode.getName(),newAttribute);
						ListOfAllEntities.add(e);
					}
				}
			}
		}
		return mcd;
	}
	
	
	
	
	/*
	 * With this function treat the case of reflexive association
	 */
	public MCD  reAssociation(MCD mcd) {
		boolean newAtt = true;
		MLDAttribute mldA = null;
		MLDAttribute mldA2 = null;
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			List<Node> connectedNodes = Graphs.neighborListOf(mcd.getMCDGraph(), currentNode);
			int nb_neighbor=connectedNodes.size();
			if ((currentNode instanceof Association)&&(nb_neighbor==2)) {
				Association actualAssociation=(Association) currentNode;
				Entity e1=(Entity) connectedNodes.get(0);
				Entity e2=(Entity) connectedNodes.get(1);
				if(e1==e2) {
					ArrayList <Cardinality> ListCard=actualAssociation.getCardinalityList();
					String h1,h2;
					//String l1,l2;
					h1=ListCard.get(0).getHighValue();h2=ListCard.get(1).getHighValue();
					//l1=ListCard.get(0).getLowValue();l2=ListCard.get(1).getLowValue();
					if((h1.equals("N"))&&(h2.equals("N"))) {
						ArrayList<Attribute> liste = new ArrayList<Attribute>();
						liste=e1.getListAttribute();
						for (Attribute attpk : liste) {
							newAtt = true;
							if(attpk.isPrimaryKey()) {
								mldA = new MLDAttribute(attpk.getName()+"_1",
										attpk.getType(), attpk.isNullable(), true,
										attpk.isUnique(), true, e1, attpk);
								mldA2 = new MLDAttribute(attpk.getName()+"_2",
										attpk.getType(), attpk.isNullable(), true,
										attpk.isUnique(), true, e1, attpk);
								ArrayList<Attribute> att= currentNode.getListAttribute();
								for(int i=0;i<att.size();i++) {
									if (att.get(i) instanceof MLDAttribute) {
										if((att.get(i).getName().equals(mldA.getName()))
											&&	(att.get(i).getType().equals(mldA.getType()))
											&&	(((MLDAttribute) att.get(i)).isForeignKey()==(mldA.isForeignKey()))
											&&	(((MLDAttribute) att.get(i)).getReferenceNode().equals(mldA.getReferenceNode()))
											&&	(((MLDAttribute) att.get(i)).getReferenceAttribute().equals(mldA.getReferenceAttribute()))
										) {
											newAtt=false;break;
										}
									}
									else {
										if(att.get(i).getName().equals(mldA.getName())) {
											newAtt=false;break;
										}
									}
								}
								if(newAtt==true){currentNode.getListAttribute().add(mldA);currentNode.getListAttribute().add(mldA2);}
							}
						}	
					}
					else if((((h1.equals("N"))&&(!h2.equals("N")))||((!h1.equals("N"))&&(h2.equals("N"))))||
							(((h1.equals("1"))&&(h2.equals("1"))))
							) {
						
						ArrayList<Attribute> liste1 = new ArrayList<Attribute>();
						liste1=e1.getListAttribute();
						for (Attribute attpk : liste1) {
							newAtt = true;
							if(attpk.isPrimaryKey()) {
								String newNameAtt=currentNode.getName();
								mldA = new MLDAttribute(newNameAtt,
										attpk.getType(), attpk.isNullable(), false,
										attpk.isUnique(), true, e1, attpk);
								ArrayList<Attribute> att= currentNode.getListAttribute();
								for(int i=0;i<att.size();i++) {
									if (att.get(i) instanceof MLDAttribute) {
										if((att.get(i).getName().equals(mldA.getName()))
											&&	(att.get(i).getType().equals(mldA.getType()))
											&&	(((MLDAttribute) att.get(i)).isForeignKey()==(mldA.isForeignKey()))
											&&	(((MLDAttribute) att.get(i)).getReferenceNode().equals(mldA.getReferenceNode()))
											&&	(((MLDAttribute) att.get(i)).getReferenceAttribute().equals(mldA.getReferenceAttribute()))
										) {
											newAtt=false;break;
										}
									}
									else {
										if(att.get(i).getName().equals(mldA.getName())) {
											newAtt=false;break;
										}
									}
									
								}
							}
						}	
						if(newAtt==true){e2.getListAttribute().add(mldA);}
					}
				}
			}
		}
		return mcd;
	}
	
	
	
	/*
	 * With this function we take an initial mcd that we modify through different function
	 */
	public MCD newMCD(MCD mcd) {
		MCD firstMCD=newtoMCD(mcd);
		MCD reMCD=reAssociation(firstMCD);
		MCD add11=Adding11(reMCD);
		MCD secondMCD=newtoMCD(add11);
		MCD finalMCD=newAssociation(secondMCD);
		return finalMCD;
	}
	

	
	
	
	
	/*
	 * Here we take all the Enties of the mcd that we modify
	 */
	public ArrayList<Entity>  addEntityToMLD(MCD mcd) {
		ArrayList<Entity> entityListToMld=new ArrayList<Entity>();
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			if ((currentNode instanceof Entity)) {
				if (!(currentNode.getListAttribute().isEmpty())) {
				
				entityListToMld.add((Entity) currentNode);
				}
			}
		}
		return entityListToMld;
	}	
	/*
	 * Here we take all the Association that are not empty from the modified mcd , convert them into Entities that 
	 * we add to the mld
	 */
	public ArrayList<Entity>  addAssociationToMLD(MCD mcd) {
		ArrayList<Entity> entityListToMld=new ArrayList<Entity>();
		AbstractGraphIterator<Node, DefaultEdge> iterator = new BreadthFirstIterator<>(mcd.getMCDGraph());
		while(iterator.hasNext()) {
			Node currentNode = iterator.next();
			if ((currentNode instanceof Association)) {
				ArrayList<Attribute> newAttribute=new ArrayList<Attribute>();
				ArrayList<Attribute> curentNodeAttribute=currentNode.getListAttribute();
				for(int i=0;i<curentNodeAttribute.size();i++) {
					
					if (curentNodeAttribute.get(i) instanceof MLDAttribute) {
						newAttribute.add(curentNodeAttribute.get(i));
					}
					else {
						newAttribute.add(curentNodeAttribute.get(i));
					}
				}
				Entity newEntity=new Entity(currentNode.getName(), newAttribute);
				if(newAttribute.isEmpty()==false) {
					entityListToMld.add(newEntity);
				}
				
			
			}
		}
		return entityListToMld;
		
	}

	
	
	
	
	
	//Here we join the two mld that we creat before
	public ArrayList<Entity> ListForMld(MCD mcd){
		MLD data = new MLD();
		newMCD(mcd);
		ArrayList<Entity> entityListToMld=new ArrayList<Entity>();
		ArrayList<Entity> ListOfAllAssociation = new ArrayList<Entity>();
		ArrayList<Entity> ListOf2Entities = new ArrayList<Entity>();
		ListOf2Entities=addEntityToMLD(mcd);
		ListOfAllAssociation=addAssociationToMLD(mcd);
		entityListToMld.addAll(ListOf2Entities);
		entityListToMld.addAll(ListOfAllAssociation);
		data.setEntityList(entityListToMld);
		return entityListToMld;
		
	}
	
	

	/*
	 * This this the method we call to convert the mcd to an mld
	 */	
	public void newMld(MCD mcd) throws  ClassNotFoundException, IOException{
		//Here we decided to modify a clone of the initial mcd, because we don't wan't the inital mcd to be modify
		MCD clonedEmp = mcd.deepClone();
		ArrayList<Entity> AllEntities = new ArrayList<Entity>();
		AllEntities =ListForMld(clonedEmp);
		mld = new MLD(AllEntities);
	}
	
	public MLD getMLD(){
		return mld;
	}
	



}

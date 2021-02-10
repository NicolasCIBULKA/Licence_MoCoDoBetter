package process;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import data.Attribute;
import data.Entity;
import data.MLD;
import data.MLDAttribute;
import exceptions.SQLTranscriptionException;

public class SQLCreation {

	/**
	 * Method that create a SQL Script from a MLD object
	 * @param mld
	 * @param filename
	 * @param path
	 * @throws SQLTranscriptionException 
	 */
	public static void SQLConverter(MLD mld, String filename, Path path) throws SQLTranscriptionException {
		// create the file
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()+"/"+filename+".sql"));
			// print some comments 
			writer.write("# This is an autogenerate SQL file for PostgreSQL created by BetterThanMoCoDo");
			writer.write("# To use it, you just have to copy/paste the code below in your Postgres DB manager to create your database");
			
			/*
			 *  organise the entity list to have the "natural" entities first, and then those 
			 *  
			 *	that have been created from associations
			*/
			List<Entity> entities = mld.getEntityList();
			entities = organiseMLDEntities(entities);
			
			// moving through all the entities of the MLD
			for(Entity entity : entities) {
				// list of the primary key and foreign keys
				List<Attribute> pklist = new ArrayList<Attribute>();
				List<MLDAttribute> fklist = new ArrayList<MLDAttribute>();
				// creating the table
				writer.write("\n\nCREATE TABLE "+ entity.getName()+"(");
				
				// creating the attributes
				for(Attribute attribute : entity.getListAttribute()) {
					// possible parameter for the attribute
					String isnull = " NOT NULL";
					String isunique = " UNIQUE";
					// setting the parameters
					if(attribute.isNullable()){
						isnull = " ";
					}
					if(!attribute.isUnique()) {
						isunique = " ";
					}
					if(attribute.isPrimaryKey()) {
						pklist.add(attribute);
					}
					if(attribute instanceof MLDAttribute && (((MLDAttribute) attribute).isForeignKey())) {
						fklist.add((MLDAttribute) attribute);
					}
					// adding the attribute to the file
					writer.write("\t"+attribute.getName()+ " " + attribute.getType() + isnull + isunique +",");
					
				}
				// create the constraints
				// creating constraint for Primary Key
				String pkString = "( ";
				pkString+=pklist.get(0).getName().toString() + " ";
				for(int i = 1; i < pklist.size(); i++) {
					pkString+=", "+pklist.get(i).getName().toString() + " ";
				}
				pkString+=")";
				if(!fklist.isEmpty()) {
					pkString+=",";
				}
				writer.write("CONSTRAINT "+entity.getName()+"_pk PRIMARY KEY "+ pkString);
				
				// Creating constraints for Foreign Keys
				for (int i = 0; i < fklist.size(); i++) {
					String coma =",";
					if(i == fklist.size()-1) {
						coma = "";
					}
					writer.write("CONSTRAINT " + entity.getName()+"_FK_"+fklist.get(i).getReferenceAttribute().getName() + 
					" FOREIGN KEY ("+fklist.get(i).getName()+") REFERENCE "+fklist.get(i).getReferenceNode().getName()
					+" ("+fklist.get(i).getReferenceAttribute().getName()+")"+coma);
					
				}
				
				// end of the table
				writer.write(");");
			}
			// creating the part of script to erase the DB
			writer.write("# This part of the code is useful to reset the database by destroying all the tables");
			writer.write("# Do not take this part of code if you want to install the DB\n\n");
			writer.close();
			for(Entity entity : entities) {
				writer.write("DROP TABLE IF EXISTS "+ entity.getName());
			}
			
		} catch (IOException e) {
			throw new SQLTranscriptionException("Error - Impossible to load the file using given path");
			
		}
		
		
		
	}
	
	// Organise all the Entities of the MLD to have all the entities of MCF first
	// and after all the Entities created from associations
	public static List<Entity> organiseMLDEntities(List<Entity> entities){
		List<Entity> finalList = new ArrayList<Entity>();
		List<Entity> tempList = new ArrayList<Entity>();
		for(Entity entity : entities) {
			List<Attribute> attributeList= entity.getListAttribute();
			boolean MLDAttributeNotFound = true;
			int i = 0;
			while(i < attributeList.size() && MLDAttributeNotFound) {
				Attribute attribute = attributeList.get(i);
				if(attribute instanceof MLDAttribute) {
					MLDAttributeNotFound = false;
					tempList.add(entity);
				}
			}
			if(MLDAttributeNotFound = true) {
				finalList.add(entity);
			}
		}
		for(Entity entity : tempList) {
			finalList.add(entity);
		}
		return finalList;
	}
	
}

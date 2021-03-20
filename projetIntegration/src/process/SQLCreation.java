package process;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
			writer.write("-- This is an autogenerate SQL file for PostgreSQL created by BetterThanMoCoDo\n");
			writer.write("-- To use it, you just have to copy/paste the code below in your Postgres DB manager to create your database\n");
			
			/*
			 *  organise the entity list to have the "natural" entities first, and then those 
			 *  
			 *	that have been created from associations
			*/
			List<String> bufferConstraint = new ArrayList<String>();
			List<Entity> entities = mld.getEntityList();
			//entities = organiseMLDEntities(entities);
			System.out.println("test");
			
			// moving through all the entities of the MLD
			for (Entity entity : entities) {
				System.out.println(" -- "+entity.getName());
				// list of the primary key and foreign keys
				List<Attribute> pklist = new ArrayList<Attribute>();
				List<MLDAttribute> fklist = new ArrayList<MLDAttribute>();
				// creating the table
				writer.write("\n\nCREATE TABLE " + entity.getName() + "(\n");
				System.out.println(entity.getListAttribute().size());
				// creating the attributes
				String coma = ",";
				int index = 0;
				for (Attribute attribute : entity.getListAttribute()) {
					System.out.println(attribute.getName() +" "+ attribute.isPrimaryKey() );
					System.out.println(attribute.getName());
					// possible parameter for the attribute
					String isnull = " NOT NULL";
					String isunique = " UNIQUE";
					// setting the parameters
					if (attribute.isNullable()) {
						isnull = "";
					}
					if (!attribute.isUnique()) {
						isunique = "";
					}
					if (attribute.isPrimaryKey()) {
						//System.out.println("pk" + attribute.getType());
						pklist.add(attribute);
					}
					if (attribute instanceof MLDAttribute && (((MLDAttribute) attribute).isForeignKey())) {
						fklist.add((MLDAttribute) attribute);
					}
					if(index == entity.getListAttribute().size() - 1) {
						coma = "";
					}
					index++;
					// adding the attribute to the file
					writer.write("\t" + attribute.getName() + " " + attribute.getType() + isnull + isunique + coma + "\n");
					
				}
				System.out.println("pklist " + pklist.size());
				// create the constraints
				// creating constraint for Primary Key
				String pkString = "( ";
				if(pklist.size() > 0) {
				pkString += pklist.get(0).getName().toString() + " ";
				}
				for (int i = 1; i < pklist.size(); i++) {
					pkString += ", " + pklist.get(i).getName().toString() + " ";
				}
				pkString += ");";
				
				String strConstraint = "\nALTER TABLE "+entity.getName()+" ADD CONSTRAINT " + entity.getName() + "_pk PRIMARY KEY " + pkString ;
				bufferConstraint.add(strConstraint);
				//writer.write("\tCONSTRAINT " + entity.getName() + "_pk PRIMARY KEY " + pkString);
				
				// Creating constraints for Foreign Keys
				for (int i = 0; i < fklist.size(); i++) {
					
					String bufconst = "\nALTER TABLE "+entity.getName() +" ADD CONSTRAINT " + entity.getName() + "_FK_"
							+ fklist.get(i).getReferenceAttribute().getName() + " FOREIGN KEY ("
							+ fklist.get(i).getName() + ") REFERENCES " + fklist.get(i).getReferenceNode().getName()
							+ " (" + fklist.get(i).getReferenceAttribute().getName() + ");" ;
					bufferConstraint.add(bufconst);
					/*writer.write("\n ALTER TABLE "+entity.getName()+" ADD CONSTRAINT " + entity.getName() + "_FK_"
							+ fklist.get(i).getReferenceAttribute().getName() + " FOREIGN KEY ("
							+ fklist.get(i).getName() + ") REFERENCE " + fklist.get(i).getReferenceNode().getName()
							+ " (" + fklist.get(i).getReferenceAttribute().getName() + ")" + coma);*/

				}

				// end of the table
				writer.write("\n);\n");
			}
			// writing the constraints
			writer.write("\n\n-- Constraints for the database, you have to take this part when you create the database\n\n");
			
			for(String constraint : bufferConstraint) {
				writer.write(constraint);
			}
			
			// creating the part of script to erase the DB
			writer.write("\n\n\n-- This part of the code is useful to reset the database by destroying all the tables\n");
			writer.write("-- Do not take this part of code if you want to install the DB\n\n");
			
			for (Entity entity : entities) {
				writer.write("DROP TABLE IF EXISTS " + entity.getName() + "\n");
			}
			writer.close();

		} catch (IOException e) {
			throw new SQLTranscriptionException("Error - Impossible to load the file using given path\n");

		}

	}

	// Organise all the Entities of the MLD to have all the entities of MCF first
	// and after all the Entities created from associations
	public static List<Entity> organiseMLDEntities(List<Entity> entities) {

		List<Entity> finalList = new Vector<Entity>();
		List<Entity> tempList = new ArrayList<Entity>();
		for (Entity entity : entities) {
			List<Attribute> attributeList = entity.getListAttribute();
			boolean MLDAttributeNotFound = true;
			int i = 0;
			while (i < attributeList.size() && MLDAttributeNotFound) {
				Attribute attribute = attributeList.get(i);
				if (attribute instanceof MLDAttribute) {
					MLDAttributeNotFound = false;
					tempList.add(entity);
				}
				i++;
			}
			if (MLDAttributeNotFound == true) {
				finalList.add(entity);
			}
		}
		System.out.println("templist " + tempList.size());

		for (Entity entity : tempList) {
			System.out.println(entity.getName());
			finalList.add(entity);
		}
		return finalList;
	}
	
	
	public static String SQLPrevisualiser(MLD mld) {
			String prevString = "";
			// print some comments 
			prevString+= "-- This is an autogenerate SQL file for PostgreSQL created by BetterThanMoCoDo\n";
			prevString+=("-- To use it, you just have to copy/paste the code below in your Postgres DB manager to create your database\n");
			
			/*
			 *  organise the entity list to have the "natural" entities first, and then those 
			 *  
			 *	that have been created from associations
			*/
			List<String> bufferConstraint = new ArrayList<String>();
			List<Entity> entities = mld.getEntityList();
			//entities = organiseMLDEntities(entities);
			System.out.println("test");
			
			// moving through all the entities of the MLD
			for (Entity entity : entities) {
				System.out.println(" -- "+entity.getName());
				// list of the primary key and foreign keys
				List<Attribute> pklist = new ArrayList<Attribute>();
				List<MLDAttribute> fklist = new ArrayList<MLDAttribute>();
				// creating the table
				prevString+=("\n\nCREATE TABLE " + entity.getName() + "(\n");
				System.out.println(entity.getListAttribute().size());
				// creating the attributes
				String coma = ",";
				int index = 0;
				for (Attribute attribute : entity.getListAttribute()) {
					System.out.println(attribute.getName() +" "+ attribute.isPrimaryKey() );
					System.out.println(attribute.getName());
					// possible parameter for the attribute
					String isnull = " NOT NULL";
					String isunique = " UNIQUE";
					// setting the parameters
					if (attribute.isNullable()) {
						isnull = "";
					}
					if (!attribute.isUnique()) {
						isunique = "";
					}
					if (attribute.isPrimaryKey()) {
						//System.out.println("pk" + attribute.getType());
						pklist.add(attribute);
					}
					if (attribute instanceof MLDAttribute && (((MLDAttribute) attribute).isForeignKey())) {
						fklist.add((MLDAttribute) attribute);
					}
					if(index == entity.getListAttribute().size() - 1) {
						coma = "";
					}
					index++;
					// adding the attribute to the file
					prevString+=("\t" + attribute.getName() + " " + attribute.getType() + isnull + isunique + coma + "\n");
					
				}
				System.out.println("pklist " + pklist.size());
				// create the constraints
				// creating constraint for Primary Key
				String pkString = "( ";
				if(pklist.size() > 0) {
				pkString += pklist.get(0).getName().toString() + " ";
				}
				for (int i = 1; i < pklist.size(); i++) {
					pkString += ", " + pklist.get(i).getName().toString() + " ";
				}
				pkString += ");";
				
				String strConstraint = "\nALTER TABLE "+entity.getName()+" ADD CONSTRAINT " + entity.getName() + "_pk PRIMARY KEY " + pkString ;
				bufferConstraint.add(strConstraint);
				//writer.write("\tCONSTRAINT " + entity.getName() + "_pk PRIMARY KEY " + pkString);
				
				// Creating constraints for Foreign Keys
				for (int i = 0; i < fklist.size(); i++) {
					
					String bufconst = "\nALTER TABLE "+entity.getName() +" ADD CONSTRAINT " + entity.getName() + "_FK_"
							+ fklist.get(i).getReferenceAttribute().getName() + " FOREIGN KEY ("
							+ fklist.get(i).getName() + ") REFERENCES " + fklist.get(i).getReferenceNode().getName()
							+ " (" + fklist.get(i).getReferenceAttribute().getName() + ");" ;
					bufferConstraint.add(bufconst);
					/*writer.write("\n ALTER TABLE "+entity.getName()+" ADD CONSTRAINT " + entity.getName() + "_FK_"
							+ fklist.get(i).getReferenceAttribute().getName() + " FOREIGN KEY ("
							+ fklist.get(i).getName() + ") REFERENCE " + fklist.get(i).getReferenceNode().getName()
							+ " (" + fklist.get(i).getReferenceAttribute().getName() + ")" + coma);*/

				}

				// end of the table
				prevString+=("\n);\n");
			}
			// writing the constraints
			prevString+=("\n\n-- Constraints for the database, you have to take this part when you create the database\n\n");
			
			for(String constraint : bufferConstraint) {
				prevString+=(constraint);
			}
			
			// creating the part of script to erase the DB
			prevString+=("\n\n\n-- This part of the code is useful to reset the database by destroying all the tables\n");
			prevString+=("-- Do not take this part of code if you want to install the DB\n\n");
			
			for (Entity entity : entities) {
				prevString+=("DROP TABLE IF EXISTS " + entity.getName() + "\n");
			}
		return prevString;
	}

}

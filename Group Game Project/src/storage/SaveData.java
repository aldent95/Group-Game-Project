/**
 * This object contains all data that needs saved.  If there is a superclass then this
 * contains a reference to another instance of SaveData which has the data for the superclass.
 *
 * @author Andrew Buntain - 300024338
 *
 */
package storage;

import java.util.HashMap;

public class SaveData {

	// An id number
	private String id = null;

	// Name of the class being saved
	private String className = null;

	// Data for the superclass (in another instance of this class)
	private SaveData superData = null;

	// List of the fields of the class being saved, and the value of those fields
	private HashMap <String, Object> fields = null;

	// Fields that refer to other objects and create cycles in the graph
	private HashMap <String, Object> references = null;


	// Constructor
	public SaveData(String className){
		this.className = className;
		this.fields = new HashMap <String,Object>();
		this.references = new HashMap <String,Object>();
	}

	/*
	 * All other methods are just getters, setters and adders to the Hashmaps
	 */

	public String getClassName() {
		return className;
	}

	public void setId(String id){
		this.id = id;
	}

	public boolean hasId(){
		return id != null;
	}

	public String GetId(){
		return id;
	}

	public SaveData getSuperData() {
		return superData;
	}

	public void setSuperData(SaveData parent){
		superData = parent;
	}

	public boolean hasSuperData(){
		return superData != null;
	}

	public void addField(String name, Object field){
		fields.put(name, field);
	}

	public Object getField(String fieldName){
		return fields.get(fieldName);
	}

	public HashMap <String, Object> getFields(){
		return fields;
	}

	public void setFields(HashMap <String, Object> fields){
		this.fields = fields;
	}

	public boolean hasFields(){
		return !fields.isEmpty();
	}

	public void addReference(String name, Object field){
		references.put(name, field);
	}

	public Object getReference(String referenceName){
		return references.get(referenceName);
	}

	public HashMap <String, Object> getReferences(){
		return references;
	}

	public void setReferences(HashMap <String, Object> references){
		this.references = references;
	}

	public boolean hasReferences(){
		return !references.isEmpty();
	}

}





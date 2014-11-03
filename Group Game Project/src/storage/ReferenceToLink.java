/**
 *  Helper class for resolving references that crate cycles in the graph (for loading).
 *
 * @author Andrew Buntain - 300024338
 *
 */
package storage;

public class ReferenceToLink {

	private Linkable referee;			// The object that makes a reference
	private String fieldName;			// The name of the field that refers to another object
	private String id;					// The unique id that will be used to find the reference in the index

	public ReferenceToLink(Linkable referee, String fieldName, String id){
		this.referee = referee;
		this.fieldName = fieldName;
		this.id = id;
	}

	Linkable getReferee() {
		return referee;
	}

	String getFieldName() {
		return fieldName;
	}

	String getId() {
		return id;
	}
}

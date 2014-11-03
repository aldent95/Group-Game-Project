/**
 * Helper class for resolving references that crate cycles in the graph (for saving).
 *
 * @author Andrew Buntain - 300024338
 *
 */
package storage;

import nu.xom.Element;

public class ReferenceToCreate {

	private Object reference;			// The object being referred to.
	private Element treeLocation; 		// Where in the xml tree this reference is made

	public ReferenceToCreate(Object reference, Element treeLocation){
		this.reference = reference;
		this.treeLocation = treeLocation;
	}
	Object getReference() {
		return reference;
	}
	void setReference(Object reference) {
		this.reference = reference;
	}
	Element getTreeLocation() {
		return treeLocation;
	}
	void setTreeLocation(Element treeLocation) {
		this.treeLocation = treeLocation;
	}

}

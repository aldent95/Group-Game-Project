/**
 * This interface is for classes that contain references to other objects which may create cycles in the graph.
 * The method is to link the object referred after all classes are instantiated.
 *
 * @author Andrew Buntain - 300024338
 *
 */
package storage;

public interface Linkable {
	public void linkReference(String name, Object refersTo);
}

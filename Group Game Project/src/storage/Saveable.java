/**
 * For classes that can be loaded.  These classes produce a "SaveData" object that contains all data needing saved,
 * with references marked(if they create cycles in the graph).
 * @author Andrew Buntain - 300024338
 *
 */
package storage;

public interface Saveable {
	public SaveData getSaveData();
}

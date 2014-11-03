/**
 * Class for saving game state to XML.
 *
 * @author Andrew Buntain - 300024338
 *
 */
package storage;

import gameLogic.GameLogic;
import gameLogic.Tile;
import gameLogic.Wall;

import java.awt.Point;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

public class Saver {

	int walls = 0;

	public static final String DEFAULT_SAVE_FILENAME = "save.xml";
	public static final String ENCODING = "UTF-8";

	// Unique ID for saved objects
	private int uniqueId = 0;

	// Stores unique ID numbers for objects
	private HashMap<Object,Integer> index = new HashMap<Object,Integer>();

	// Stores Objects that make references to other objects
	private ArrayList<ReferenceToCreate> hanging = new ArrayList<ReferenceToCreate>();

	// Map width and height
	private int mapWidth,mapHeight;

	// Find unique ID from the Hashmap
	// Had to implement this to use .equals() comparison because the hashmap was not returning what I wanted.
	private int findUniqueIDof(Object o){
		Iterator<Entry<Object, Integer>> entries = index.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<Object, Integer> entry = (Entry<Object,Integer>) entries.next();
			Object key = entry.getKey();
			Integer value = entry.getValue();
			if(o.equals(key))
			{
				return value;
			}
		}
		return -1;
	}

	// Go through all the objects with unresolved references, and fill in the spots with index numbers
	// identifying the objects referred to.
	private void linkReferences(){
		//System.out.println("Linking " + hanging.size() + " hanging references");
		for (int i=0; i < hanging.size(); i++){
			int numericIndex = findUniqueIDof(hanging.get(i).getReference());
			Element leaf = hanging.get(i).getTreeLocation();
			leaf.addAttribute(new Attribute("type","java.lang.Integer"));
			leaf.appendChild(Integer.toString(numericIndex));
		}
	}

	// If an object is refers to another (creating a cycle in the graph) this remembers the place that the
	// reference was made, so that we can come back later and write in an index number identifying the object.
	private Element saveReferences(String name, Object o){
		Element node = new Element(name);
		node.addAttribute(new Attribute("type","java.util.HashMap"));
		HashMap<String, Object> h = (HashMap<String, Object>)o;
		Iterator<Entry<String, Object>> entries = h.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, Object> entry = (Entry<String, Object>) entries.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value == null) {
				value = new String("NULL");
			}
			Element e = new Element(key);
			node.appendChild(e);

			if (!"NULL".equals(value)){
				ReferenceToCreate ref = new ReferenceToCreate(value, e);
				hanging.add(ref);
				//System.out.println("Remembered Reference: " + key + " = " + value );
			} else {
				e.addAttribute(new Attribute("type","NULL"));
				e.appendChild("NULL");
			}
		}
		return node;
	}

	// Produce a root element of the XML tree.  This is done recursively.
	private Element save(String name, Object o){

		// Set up the node
		Element node = new Element(name);
		if (o==null){
			node.addAttribute(new Attribute("type","NULL"));
			node.appendChild("NULL");
			return node;
		}
		node.addAttribute(new Attribute("type",o.getClass().getName()));

		// Increment unique ID
		uniqueId++;

		// Add object to index so we can get it's uniqueId later if another object refers to it
		index.put(o, uniqueId);

		// Add id attribute to XML element
		node.addAttribute(new Attribute("id",Integer.toString(uniqueId)));

		// Treat appropriately based on type
		if (o instanceof Saveable) {
			node.addAttribute(new Attribute("saveable","True"));
			Saveable s = (Saveable)o;
			node.appendChild(save("fields",s.getSaveData().getFields()));
			node.appendChild(saveReferences("references",s.getSaveData().getReferences()));
			node.appendChild(save("superData",s.getSaveData().getSuperData()));
		}
		else if (o instanceof SaveData) {
			SaveData s = (SaveData)o;
			node.appendChild(save("fields",s.getFields()));
			node.appendChild(saveReferences("references",s.getReferences()));
			node.appendChild(save("superData",s.getSuperData()));
		}
		else if (o instanceof Tile[][]) {
			Tile[][] tiles = (Tile[][])o;
			for (int x = 0; x < mapWidth; x++){
				for (int y = 0; y < mapHeight; y++){
					Element e = save("room",tiles[x][y]);
					e.addAttribute(new Attribute("x",Integer.toString(x)));
					e.addAttribute(new Attribute("y",Integer.toString(y)));
					node.appendChild(e);
				}
			}
		}
		else if (o instanceof HashMap) {
			HashMap<String, Object> h = (HashMap<String, Object>)o;
			Iterator<Entry<String, Object>> entries = h.entrySet().iterator();
			while (entries.hasNext()) {
				Entry<String, Object> entry = (Entry<String, Object>) entries.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				node.appendChild(save(key,value));
			}
		}
		else if (o instanceof List) {
			List l = (List)o;
			Iterator i = l.listIterator();
			while (i.hasNext()) {
				node.appendChild(save("listitem",i.next()));
			}
		}
		else if (o instanceof Point){
			Point p = (Point)o;
			node.appendChild(save("x",p.x));
			node.appendChild(save("y",p.y));
		}
		else if (o instanceof String) {
			node.appendChild((String)o);
		}
		else if (o instanceof Integer) {
			node.appendChild(Integer.toString((Integer)o));
		}
		else if (o instanceof Double){
			node.appendChild(Double.toString((Double)o));
		}
		else if (o instanceof Boolean){
			node.appendChild(Boolean.toString((Boolean)o));
		}
		else {
			node.appendChild("UNSAVEABLE_OBJECT: " + o.toString());
		}
		return node;
	}

	// Write the XML out to a file
	private void serialiseDoc(Element e, String saveFileName){
		Document doc = new Document(e);
		try {
			FileOutputStream outFile = new FileOutputStream(saveFileName);
			BufferedOutputStream outBuffer = new BufferedOutputStream(outFile);
			Serializer serializer = new Serializer(outBuffer, ENCODING);
			serializer.setIndent(4);
			//serializer.setMaxLength(64);
			serializer.write(doc);
		} catch(Exception ex) {}
	}

	// Ensure that this class cannot be instantiated except by it's own static method
	private Saver(){
	}

	// Nice easy static method for other people to call
	public static void saveGameLogic(GameLogic g, String saveFileName){
		System.out.println("Saving...");
		Saver s = new Saver();

		s.mapWidth = g.getMapWidth();
		s.mapHeight = g.getMapHeight();

		Element root = s.save("game", g);
		s.linkReferences();
		s.serialiseDoc(root,saveFileName);
	}

	// Save any object for testing
	public static void saveTestObject(Object o, String saveFileName){
		Saver s = new Saver();
		Element root = s.save("test", o);
		s.linkReferences();
		s.serialiseDoc(root,saveFileName);
	}

}

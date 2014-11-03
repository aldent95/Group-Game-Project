/**
 *
 * Class for loading game state from XML.
 *
 * @author Andrew Buntain - 300024338
 *
 */
package storage;


import render.CreateGameWorld;
import gameLogic.GameLogic;
import gameLogic.IAll;
import gameLogic.IContainer;
import gameLogic.IContainerTreasure;
import gameLogic.IKey;
import gameLogic.IKeyTreasure;
import gameLogic.ITreasure;
import gameLogic.Item;
import gameLogic.Player;
import gameLogic.Tile;
import gameLogic.Wall;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

public class Loader {

	public static final boolean DUMMY_LOAD = false;
	public static final boolean DEBUG = false;

	// Mapping of ID numbers to objects
	// This will be used to find the ojects that need to be linked as references
	HashMap<String,Object> index = new HashMap<String,Object>();

	// List of objects that make references (which need to be resolved using the index HashMap)
	List<ReferenceToLink> pending = new ArrayList<ReferenceToLink>();

	private int mapWidth, mapHeight;
	private int depth = -1;

	// Add an object to the index (so the XML can refer to it by it's unique Id number.
	private void addToIndex(String id, Object o){
		if (!index.containsKey(id)){
			index.put(id, o);
		}
	}

	// Add an object to the list of those that refer to other objects (which will need to be populated once
	// all objects are instantiated.
	private void addToPending(Linkable referee, SaveData s){
		for (int i = 0; i < s.getReferences().size(); i++) {
			Iterator<Entry<String, Object>> entries = s.getReferences().entrySet().iterator();
			while (entries.hasNext()) {
				Entry<String, Object> entry = (Entry<String, Object>) entries.next();
				String key = entry.getKey();
				Object value = entry.getValue();
				if (value != null) {
					pending.add(new ReferenceToLink(referee,key, value.toString()));
				}
			}
		}
	}

	// Go through all references and populate them with instantiated objects (once all objects are instantiated).
	private void linkReferences(){
		for (int i = 0; i < pending.size(); i++){
			ReferenceToLink p = pending.get(i);
			p.getReferee().linkReference(p.getFieldName(),index.get(p.getId()));
		}
	}

	// Recurse through XML tree, loading all objects.
	@SuppressWarnings("unchecked")
	private Object load(Element e){
		depth++;
		padPrint(depth, "Loading: " + e.getLocalName() + ", Type: " + e.getAttributeValue("type") );

		Object result = null;

		if ("NULL".equals(e.getAttributeValue("type")) ){
			depth--;
			result = null;
		}
		else if (e.getLocalName().equals("references")) {
			HashMap<String,Object> h = new HashMap<String,Object>();
			Elements children = e.getChildElements();
			for (int i = 0; i < children.size(); i++) {
				Element child = children.get(i);
				if (child.getValue().equals("NULL")) {
					h.put(child.getLocalName(), null);
				}
				else {
					int ref = (int)load(child);
					h.put(child.getLocalName(), ref);
				}
			}
			padPrint(depth,"finished: " + e.getLocalName());
			depth--;
			result = h;
		}
		else if ("True".equals(e.getAttributeValue("saveable")) || "storage.SaveData".equals(e.getAttributeValue("type"))) {
			// Load SaveData
			SaveData s = new SaveData(e.getAttributeValue("type"));
			s.setId(e.getAttributeValue("id"));
			s.setFields((HashMap<String,Object>)load(e.getFirstChildElement("fields")));
			s.setReferences((HashMap<String,Object>)load(e.getFirstChildElement("references")));
			s.setSuperData((SaveData)load(e.getFirstChildElement("superData")));
			padPrint(depth,"finished: " + e.getLocalName());
			depth--;

			// Instantiate object to appropriate type
			if (e.getAttributeValue("type").equals(GameLogic.class.getName())) {
				result = new GameLogic((SaveData)s);
			}
			else if (e.getAttributeValue("type").equals(Player.class.getName())) {
				result = new Player((SaveData)s);
			}
			else if (e.getAttributeValue("type").equals(Tile.class.getName())) {
				result = new Tile((SaveData)s);
			}
			else if (e.getAttributeValue("type").equals(Wall.class.getName())) {
				result = new Wall((SaveData)s);
			}
			else if (e.getAttributeValue("type").equals(Item.class.getName())) {
				result = new Item((SaveData)s);
			}
			else if (e.getAttributeValue("type").equals(IAll.class.getName())) {
				result = new IAll((SaveData)s);
			}
			else if (e.getAttributeValue("type").equals(IContainer.class.getName())) {
				result = new IContainer((SaveData)s);
			}
			else if (e.getAttributeValue("type").equals(IContainerTreasure.class.getName())) {
				result = new IContainerTreasure((SaveData)s);
			}
			else if (e.getAttributeValue("type").equals(IKey.class.getName())) {
				result = new IKey((SaveData)s);
			}
			else if (e.getAttributeValue("type").equals(IKeyTreasure.class.getName())) {
				result = new IKeyTreasure((SaveData)s);
			}
			else if (e.getAttributeValue("type").equals(ITreasure.class.getName())) {
				result = new ITreasure((SaveData)s);
			}
			else {
				result = s;
			}

			// Add object to pending resolutions, if it references other objects.
			if (s.hasReferences()) {
				addToPending((Linkable)result, s);
			}
		}
		else if ("[[LgameLogic.Tile;".equals(e.getAttributeValue("type"))){
			Tile[][] tiles = new Tile[mapWidth][mapHeight];
			Elements children = e.getChildElements();
			for (int i = 0; i < children.size(); i++) {
				Element child = children.get(i);
				int x = Integer.parseInt(child.getAttribute("x").getValue());
				int y = Integer.parseInt(child.getAttribute("y").getValue());
				tiles[x][y] = (Tile)load(child);
			}
			padPrint(depth,"finished: " + e.getLocalName());
			depth--;
			result = tiles;
		}
		else if ("java.util.HashMap".equals(e.getAttributeValue("type"))){
			HashMap<String,Object> h = new HashMap<String,Object>();
			Elements children = e.getChildElements();
			for (int i = 0; i < children.size(); i++){
				Element child = children.get(i);
				h.put(child.getLocalName(), load(child));
			}
			padPrint(depth,"finished: " + e.getLocalName());
			depth--;
			result = h;
		}
		else if ("java.util.ArrayList".equals(e.getAttributeValue("type"))){
			ArrayList l = new ArrayList();
			Elements children = e.getChildElements();
			for (int i = 0; i < children.size(); i++){
				Element child = children.get(i);
				l.add(load(child));
			}
			padPrint(depth,"finished: " + e.getLocalName());
			depth--;
			result = l;
		}
		else if ("java.awt.Point".equals(e.getAttributeValue("type"))){
			depth--;
			result = new Point(
					(int)load(e.getFirstChildElement("x")),
					(int)load(e.getFirstChildElement("y"))
					);
		}
		else if ("java.lang.Double".equals(e.getAttributeValue("type"))){
			depth--;
			result = new Double(e.getValue());
		}
		else if (e.getAttributeValue("type").equals(Integer.class.getName())){
			depth--;
			result = new Integer(e.getValue());
		}
		else if ("java.lang.String".equals(e.getAttributeValue("type"))){
			depth--;
			result = new String(e.getValue());
		}
		else if ("java.lang.Boolean".equals(e.getAttributeValue("type"))){
			depth--;
			result = new Boolean(e.getValue());
		}

		// Add to index and record id so hanging references can be resolved later
		addToIndex(e.getAttributeValue("id"), result);
		return result;
	}


	// Fancy indented output
	private void padPrint(int n, String s){
//		if (DEBUG) {
//			for (int i = 0; i < n; i++) {
//				System.out.print("  ");
//			}
//			System.out.println(s);
//		}
	}

	// Read file from disk.
	private Document readFile(String saveFileName) throws FileNotFoundException, ValidityException, ParsingException, IOException {
		Builder builder = new Builder();
	    InputStream in;
		in = new FileInputStream(new File(saveFileName));
		return builder.build(in);
	}

	// Ensure that this class cannot be instantiated except by it's own static method
	private Loader(){
	}

	// Nice easy static method for other people to call
	public static GameLogic loadGameLogic(String saveFileName){

		System.out.println("Loading...");

		GameLogic g = null;

		if (DUMMY_LOAD) {

			System.out.println("Dummy load specified...");
			g = CreateGameWorld.load();

		} else {
			try {
				Loader l = new Loader();
				Document doc = l.readFile(saveFileName);
				Element root = doc.getRootElement();

				// A bit of a hack
				l.mapWidth = Integer.parseInt(root.getFirstChildElement("fields").getFirstChildElement("mapWidth").getValue());
				l.mapHeight = Integer.parseInt(root.getFirstChildElement("fields").getFirstChildElement("mapHeight").getValue());

				g = (GameLogic)l.load(root);
				l.linkReferences();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (ValidityException e) {
				e.printStackTrace();
			}
			catch (ParsingException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			if (g == null) {
				System.out.println("Loading problem.  Loading failsafe data...");
				g = CreateGameWorld.load();

			}
		}
		return g;
	}

	// Load any object (for testing)
	public static Object loadTestObject(String LoadFileName){
		Loader l = new Loader();
		Document doc;
		Object o = null;
		try {
			doc = l.readFile(LoadFileName);
			Element root = doc.getRootElement();
			o = l.load(root);
			l.linkReferences();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (ValidityException e) {
			e.printStackTrace();
		}
		catch (ParsingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return o;
	}

}


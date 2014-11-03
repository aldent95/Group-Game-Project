package render;

import gameLogic.Entity;
import gameLogic.GameLogic;
import gameLogic.IKey;
import gameLogic.Item;
import gameLogic.Tile;
import gameLogic.Wall;

import java.awt.Point;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import storage.Saver;

public class CreateGameWorld {

	/**
	 * Creates an initial XML save file that contains the Game World
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		GameLogic gameLogic = load();
		Saver.saveGameLogic(gameLogic, Saver.DEFAULT_SAVE_FILENAME);
	}

	public static void writeOut(){
		GameLogic gameLogic = load();
		Saver.saveGameLogic(gameLogic, Saver.DEFAULT_SAVE_FILENAME);
	}
	/**
	 * A hard coded way to load the game world
	 * @return
	 */
	public static  GameLogic load() {

		GameLogic gameLogic = new GameLogic();
		//Create all the tiles first, we need these first as objects may reference them i.e. keys
		createTile("00","00",gameLogic);
		createTile("01","00",gameLogic);
		createTile("02","00",gameLogic);

		createTile("00","01",gameLogic);
		createTile("01","01",gameLogic);
		createTile("02","01",gameLogic);


		//Then create all the objects in each tile
		createObject("00","00",gameLogic);
		createObject("01","00",gameLogic);
		createObject("02","00",gameLogic);

		createObject("00","01",gameLogic);
		createObject("01","01",gameLogic);
		createObject("02","01",gameLogic);


		//Link the tiles
		gameLogic.getRoom(new Point(0,0)).getWall("East").setLeadsTo(gameLogic.getRoom(new Point(1,0)));
		gameLogic.getRoom(new Point(1,0)).getWall("West").setLeadsTo(gameLogic.getRoom(new Point(0,0)));

		gameLogic.getRoom(new Point(2,0)).getWall("West").setLeadsTo(gameLogic.getRoom(new Point(1,0)));
		gameLogic.getRoom(new Point(1,0)).getWall("East").setLeadsTo(gameLogic.getRoom(new Point(2,0)));


		gameLogic.getRoom(new Point(0,1)).getWall("East").setLeadsTo(gameLogic.getRoom(new Point(1,1)));
		gameLogic.getRoom(new Point(1,1)).getWall("West").setLeadsTo(gameLogic.getRoom(new Point(0,1)));

		gameLogic.getRoom(new Point(2,1)).getWall("West").setLeadsTo(gameLogic.getRoom(new Point(1,1)));
		gameLogic.getRoom(new Point(1,1)).getWall("East").setLeadsTo(gameLogic.getRoom(new Point(2,1)));

		gameLogic.getRoom(new Point(1,0)).getWall("South").setLeadsTo(gameLogic.getRoom(new Point(1,1)));
		gameLogic.getRoom(new Point(1,1)).getWall("North").setLeadsTo(gameLogic.getRoom(new Point(1,0)));

		return gameLogic;
	}

	/**
	 * creates a Tile with four walls
	 *
	 * @param x x position on the grid
	 * @param y y position on the grid
	 * @param gl GameLogic
	 */
	@SuppressWarnings("resource")
	/**
	 * Creates a Tile from file
	 * 
	 * @param x the x coordinate for the Tile
	 * @param y the y coordinate for the Tile
	 * @param gl the game the tiles are being added to
	 */
	public static void createTile(String x, String y, GameLogic gl){

		HashMap<String, Wall> theWalls = new HashMap<String, Wall>();

		InputStream east = CreateGameWorld.class.getResourceAsStream("/render/img/" + x + y + "/East/coords.txt");
		InputStream west = CreateGameWorld.class.getResourceAsStream("/render/img/" + x + y + "/West/coords.txt");
		InputStream north = CreateGameWorld.class.getResourceAsStream("/render/img/" + x + y + "/North/coords.txt");
		InputStream south = CreateGameWorld.class.getResourceAsStream("/render/img/" + x + y + "/South/coords.txt");

		Scanner scanner = null;

		scanner = new Scanner(east);
		if(scanner!=null){
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){
				String theLine = scanner.nextLine();

				String[] split = theLine.split(",");

				if(split[6].equals("wall")){
					theWalls.put("East", new Wall(split[3], split[4],"img/"+x+y+"/East/"+theLine.split(",")[0]+".png"));
					theWalls.get("East").setLocked(Boolean.valueOf(split[5]));
				}
			}
		}

		scanner = new Scanner(west);
		if(scanner!=null){
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){
				String theLine = scanner.nextLine();

				String[] split = theLine.split(",");

				if(split[6].equals("wall")){
					theWalls.put("West", new Wall(split[3], split[4],"img/"+x+y+"/West/"+theLine.split(",")[0]+".png"));
					theWalls.get("West").setLocked(Boolean.valueOf(split[5]));
				}

			}
		}



		scanner = new Scanner(north);
		if(scanner!=null){
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){

				String theLine = scanner.nextLine();

				String[] split = theLine.split(",");

				if(split[6].equals("wall")){
					theWalls.put("North", new Wall(split[3], split[4],"img/"+x+y+"/North/"+theLine.split(",")[0]+".png"));
					theWalls.get("North").setLocked(Boolean.valueOf(split[5]));
				}

			}
		}

		scanner = new Scanner(south);
		if(scanner!=null){
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){

				String theLine = scanner.nextLine();

				String[] split = theLine.split(",");

				if(split[6].equals("wall")){
					theWalls.put("South", new Wall(split[3], split[4],"img/"+x+y+"/South/"+theLine.split(",")[0]+".png"));
					theWalls.get("South").setLocked(Boolean.valueOf(split[5]));
				}

			}
		}

		Tile theTile = new Tile(800, 800, theWalls, "Tile " + x + y, "Tile Description");
		gl.addRoom(Integer.valueOf(x), Integer.valueOf(y), theTile);

	}

	/**
	 * This creates then stores objects on the correct tile
	 *
	 * @param x x position of tile where object resides
	 * @param y y position of tile where object resides
	 * @param gl GameLogic that stores the games
	 */
	@SuppressWarnings("resource")
	/**
	 * Creates the objects of the game from a file
	 * 
	 * @param x x-coordinate of the tile
	 * @param y y-coordinate of the tile
	 * @param gl GameLogic that stores the game
	 */
	public static void createObject(String x, String y, GameLogic gl){

		ArrayList<Entity> theObjects = new ArrayList<Entity>();

		InputStream east = CreateGameWorld.class.getResourceAsStream("/render/img/" + x + y + "/East/coords.txt");
		InputStream west = CreateGameWorld.class.getResourceAsStream("/render/img/" + x + y + "/West/coords.txt");
		InputStream north = CreateGameWorld.class.getResourceAsStream("/render/img/" + x + y + "/North/coords.txt");
		InputStream south = CreateGameWorld.class.getResourceAsStream("/render/img/" + x + y + "/South/coords.txt");
		
		Scanner scanner = null;

		scanner = new Scanner(east);
		if(scanner!=null){
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){
				String theLine = scanner.nextLine();

				String[] split = theLine.split(",");

				if (split[6].equals("item")){
					theObjects.add(new Item(split[3], split[4], new Point(800, Integer.valueOf(split[1])), "img/" + x + y + "/East/" + split[0] + ".png", Integer.valueOf(split[2]), Boolean.valueOf(split[5])));
				}
				else if (split[6].equals("key")){
					IKey tempKey = new IKey(split[3], split[4], new Point(800, Integer.valueOf(split[1])), "img/" + x + y + "/East/" + split[0] + ".png", Integer.valueOf(split[2]),  Boolean.valueOf(split[5]));
					tempKey.setOpens( gl.getRoom(new Point(Integer.valueOf(split[7]),Integer.valueOf(split[8]))).getWall(split[9]));
					theObjects.add(tempKey);
				}
			}
		}

		scanner = new Scanner(west);
		if(scanner!=null){
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){
				String theLine = scanner.nextLine();

				String[] split = theLine.split(",");

				if (split[6].equals("item")){
					theObjects.add(new Item(split[3], split[4], new Point(0, Integer.valueOf(split[1])), "img/" + x + y + "/West/" + split[0] + ".png", Integer.valueOf(split[2]),  Boolean.valueOf(split[5])));
				}
				else if (split[6].equals("key")){
					IKey tempKey = new IKey(split[3], split[4], new Point(0, Integer.valueOf(split[1])), "img/" + x + y + "/West/" + split[0] + ".png", Integer.valueOf(split[2]),  Boolean.valueOf(split[5]));
					tempKey.setOpens( gl.getRoom(new Point(Integer.valueOf(split[7]),Integer.valueOf(split[8]))).getWall(split[9]));
					theObjects.add(tempKey);
				}


			}
		}

		scanner = new Scanner(north);
		if(scanner!=null){
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){

				String theLine = scanner.nextLine();

				String[] split = theLine.split(",");

				if (split[6].equals("item")){
					theObjects.add(new Item(split[3], split[4], new Point(Integer.valueOf(split[1]),1), "img/" + x + y + "/North/" + split[0] + ".png", Integer.valueOf(split[2]),  Boolean.valueOf(split[5])));
				}
				else if (split[6].equals("key")){
					IKey tempKey = new IKey(split[3], split[4], new Point(Integer.valueOf(split[1]),1), "img/" + x + y + "/North/" + split[0] + ".png", Integer.valueOf(split[2]),  Boolean.valueOf(split[5]));
					tempKey.setOpens( gl.getRoom(new Point(Integer.valueOf(split[7]),Integer.valueOf(split[8]))).getWall(split[9]));
					theObjects.add(tempKey);
				}

			}
		}


		scanner = new Scanner(south);
		if(scanner!=null){
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()){

				String theLine = scanner.nextLine();

				String[] split = theLine.split(",");

				if (split[6].equals("item")){
					theObjects.add(new Item(split[3], split[4], new Point(Integer.valueOf(split[1]),800), "img/" + x + y + "/South/" + split[0] + ".png", Integer.valueOf(split[2]),  Boolean.valueOf(split[5])));
				}
				else if (split[6].equals("key")){
					IKey tempKey = new IKey(split[3], split[4], new Point(Integer.valueOf(split[1]),800), "img/" + x + y + "/South/" + split[0] + ".png", Integer.valueOf(split[2]),  Boolean.valueOf(split[5]));
					tempKey.setOpens( gl.getRoom(new Point(Integer.valueOf(split[7]),Integer.valueOf(split[8]))).getWall(split[9]));
					theObjects.add(tempKey);
				}

			}
		}

		gl.getRoom(new Point(Integer.valueOf(x),Integer.valueOf(y))).setObjects(theObjects);
	}

}

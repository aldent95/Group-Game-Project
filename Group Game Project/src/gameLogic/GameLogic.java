package gameLogic;

import gameLogic.interfaces.Drawable;
import gameLogic.interfaces.roomFinder;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import networking.TransmitTile;
import storage.SaveData;
import storage.Saveable;

/**
 * Main class for GameLogic
 *
 * @author Alex Dent
 *
 */
public class GameLogic implements Serializable, Saveable, roomFinder {
	private static final long serialVersionUID = -6604061543416080017L;
	private ArrayList<Player> players = new ArrayList<Player>();
	private Tile[][] rooms;
	private Player player = null; // Used to know which player is the current
									// player for

	// a client.

	/**
	 * Used to get the current player (Used by client side game logic)
	 *
	 * @return Player
	 */
	public Player getPlayer() {
		return player;
	}

	private int mapWidth, mapHeight;

	/**
	 * Setup method for server side game logic
	 *
	 * @param x
	 *            dimension of the map
	 * @param y
	 *            dimension of the map
	 */
	public GameLogic() {
		mapWidth = 10;
		mapHeight = 10;
		rooms = new Tile[mapWidth][mapHeight];

	}

	/**
	 * Constructor used only for testing
	 *
	 * @param x
	 * @param y
	 * @param testing
	 */
	protected GameLogic(int x, int y, boolean testing) {
		mapWidth = x;
		mapHeight = y;
		rooms = new Tile[mapWidth][mapHeight];
	}

	public Tile getRoom(Point p) {
		return rooms[p.x][p.y];
	}

	/**
	 * Generates the arraylist of all the current entities the player can see
	 *
	 * @param width
	 *            - width of the grapihcs area your drawing in
	 * @return ArrayList<Drawable>
	 */
	public ArrayList<Drawable> getView(int width) {
		ArrayList<Drawable> temp = new ArrayList<Drawable>();

		int x = player.getPos().x;
		int y = player.getPos().y;
		Tile start = rooms[x][y];
		String dir = player.getDir();
		int i = 0;
		Tile tempTile = start;

		while (i < 2) {
			if (players != null) {
				int j = 1;
				for (Player p : players) {
					if (tempTile.equals(getRoom(p.getPos()))
							&& !player.equals(p) && p.isConnected()) {
						if (tempTile.equals(start)) {
							switch (dir) {
							case ("North"):

								if (p.getDir().equals("South")) {
									p.setViewX(100 * j);
									p.setViewY(150);
									temp.add((Drawable) p);
									j++;
								}
								break;
							case ("South"):

								if (p.getDir().equals("North")) {
									p.setViewX(100 * j);
									p.setViewY(150);
									temp.add((Drawable) p);
									j++;
								}
								break;
							case ("East"):

								if (p.getDir().equals("West")) {
									p.setViewX(100 * j);
									p.setViewY(150);
									temp.add((Drawable) p);
									j++;
								}
								break;
							case ("West"):

								if (p.getDir().equals("East")) {
									p.setViewX(100 * j);
									p.setViewY(150);
									temp.add((Drawable) p);
									j++;
								}
								break;
							}
						}
						else{
							p.setViewX(100 * j);
							p.setViewY(150);
							temp.add((Drawable) p);
							j++;
						}
					}
				}
			}
			if (tempTile.equals(start))
				temp.addAll(tempTile.getView(dir, width, true));
			else
				temp.addAll(tempTile.getView(dir, width, false));
			if (start.getWalls().get(dir).isSolid() == false) {
				switch (dir) {
				case "North":
					tempTile = rooms[x][y - 1];
					break;
				case "South":
					tempTile = rooms[x][y + 1];
					break;
				case "East":
					tempTile = rooms[x + 1][y];
					break;
				case "West":
					tempTile = rooms[x - 1][y];
					break;
				}
			} else
				break;
			i++;
		}
		// Collections.reverse(temp);
		return temp;
	}

	private Player createPlayer(String name, String description, Point point,
			int height) {
		String filename = "img/Player/" + (players.size() % 4) + ".png";
		Player pTemp = new Player(name, description, point, filename, height,
				this);
		return pTemp;
	}

	private Player getPlayerByName(String name) {
		for (Player p : players) {
			if (p.getName().equals(name)) {
				return (p);
			}
		}
		return (null);
	}

	/**
	 * Create a new player
	 *
	 * @param name
	 * @param description
	 * @param startingPosition
	 *            stating point
	 * @param image
	 *            string
	 * @return the new player object
	 */
	public Player newPlayer(String name, String description,
			Point startingPosition, int height) {
		Player newPlayer = getPlayerByName(name);
		if (newPlayer == null) {
			newPlayer = createPlayer(name, description, startingPosition,
					height);
		} else if (newPlayer.isConnected()) {
			return (newPlayer(name + "1", description, startingPosition, height));
		} else if (!newPlayer.isConnected()) {
			newPlayer.connect();
		}

		if (!players.contains(newPlayer)) {
			players.add(newPlayer);
		}
		player = newPlayer; // BAD HACK! this object is serilized and sent with
							// the player field like this. grr
		return (newPlayer);
	}

	/**
	 * Used to add a new room to the map
	 *
	 * @param x
	 *            - xcoords for the room
	 * @param y
	 *            - ycoords for the room
	 * @param r
	 *            - The new room
	 */
	public boolean addRoom(int x, int y, Tile r) {
		if (r != null & r instanceof Tile) {
			// No overwrite protection
			rooms[x][y] = r;
			return true;
		}
		return false;
	}

	/**
	 * Gets the width of the map
	 *
	 * @return int
	 */
	public int getMapWidth() {
		return mapWidth;
	}

	/**
	 * Gets the height of the map
	 *
	 * @return int
	 */
	public int getMapHeight() {
		return mapHeight;
	}

	/**
	 * Used to update a player. Server passes the player to update to the client
	 * via this method. Client then updates is local copys of the players
	 *
	 * @param p
	 *            - Player
	 */
	public void updatePlayer(Player p, boolean disconnect) {
		boolean updated = false;
		if (!disconnect) {
			for (int i = 0; i < players.size(); i++) {
				if (p.getName().equals(players.get(i).getName())) {
					players.set(i, p);
					updated = true;
				}
			}

			if (!updated)
				players.add(p);
			if (player.getName().equals(p.getName())) {
				player = p;
			}
		} else {
			player.disconnect();
		}
	}

	/**
	 * Used to update a player. Server passes the player to update to the client
	 * via this method. Client then updates is local copys of the players
	 *
	 * @param p
	 *            - Player
	 */
	public void updatePlayer(Player p) {
		updatePlayer(p, false);
	}

	/**
	 * Used to update a room. Server passes the room to update to the client via
	 * this method. Client then updates is local copys of the rooms
	 *
	 * @param t
	 *            - Tile
	 */
	public void updateRoom(TransmitTile t) {
		rooms[t.getPosition().x][t.getPosition().y] = t.getTile();
	}

	@Override
	public SaveData getSaveData() {
		SaveData s = new SaveData(this.getClass().getName());
		s.addField("players", players);
		s.addField("rooms", rooms);
		s.addField("mapWidth", mapWidth);
		s.addField("mapHeight", mapHeight);
		return s;
	}

	@SuppressWarnings("unchecked")
	public GameLogic(SaveData s) {
		players = (ArrayList<Player>) s.getField("players");
		mapWidth = (int) s.getField("mapWidth");
		mapHeight = (int) s.getField("mapHeight");
		rooms = (Tile[][]) s.getField("rooms");
	}

	public boolean equals(GameLogic g) {
		if (!player.equals(g.player))
			return false;
		if (mapWidth != g.getMapWidth())
			return false;
		if (mapHeight != g.getMapHeight())
			return false;
		if (!players.equals(g.players))
			return false;
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				if (rooms[x][y] != null
						&& !rooms[x][y].equals(g.getRoom(new Point(x, y))))
					return false;
			}
		}
		return true;
	}

	public boolean checkInteg() {
		boolean correct = false;
		if (rooms == null)
			return false;
		for (int i = 0; i < mapWidth; i++) {
			if (rooms[i][i] != null)
				correct = true;
		}
		if (players == null)
			return false;
		return correct;
	}

	public void addPlayer(Player p) {
		players.add(p);
	}
}

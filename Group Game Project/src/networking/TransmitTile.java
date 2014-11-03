package networking;

import gameLogic.Tile;

import java.awt.Point;
import java.io.Serializable;
/**
 * This is a helper class to transmit a tile update accross the network.
 * @author Michael Pearson
 */
@SuppressWarnings("serial")
public class TransmitTile implements Serializable{
	private Tile tile;
	private Point position;
	public TransmitTile(Tile tile,Point point) {
		this.tile = tile;
		this.position = point;
	}
	public Point getPosition()
	{
		return(position);
	}
	public Tile getTile()
	{
		return(tile);
	}
}

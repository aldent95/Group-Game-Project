package gui;

import gameLogic.Entity;
import gameLogic.Item;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import render.CreateGameWorld;

/***
 *
 * This Class is responsible for handling the Inventory Canvas for the game
 *
 * @author Andrew Booker 300260693, Glen Peek 300283786
 *
 */
public class InventoryCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	//inventory boxes
	InventoryBox slotOne;
	InventoryBox slotTwo;
	InventoryBox slotThree;
	InventoryBox slotFour;
	InventoryBox slotFive;
	InventoryBox slotSix;
	InventoryBox previewSlot;

	private InventoryBox selectedItem = null;

	ArrayList<InventoryBox> inventoryBoxes;


	private Frame frame;


	private ArrayList<Item> inventory;
	/**
	 *
	 * replaces an item in a inventory slot
	 * @param e - the enity you want to replace
	 * @param j	- the number of the inventory box
	 */
	private void replaceItem(Entity e, int j) {
		InventoryBox a = inventoryBoxes.get(j);
		a.setItem(e);
		inventoryBoxes.remove(j);
		inventoryBoxes.add(j,a);
	}
	/**
	 * Sets the inventory to null
	 *
	 */
	private void EmptyInven() {
		slotOne.setItem(null);
		slotTwo.setItem(null);
		slotThree.setItem(null);
		slotFour.setItem(null);
		slotFive.setItem(null);
		slotSix.setItem(null);
	}

	/**
	 * Sets up the inventory boxes
	 *
	 */
	private void init() {
		inventoryBoxes= new ArrayList<InventoryBox>();

		slotOne = new InventoryBox(1,5,5);
		inventoryBoxes.add(0,slotOne);

		slotTwo = new InventoryBox(2,115,5);
		inventoryBoxes.add(1,slotTwo);

		slotThree = new InventoryBox(3,225,5);
		inventoryBoxes.add(2,slotThree);

		slotFour = new InventoryBox(4,335,5);
		inventoryBoxes.add(3,slotFour);

		slotFive = new InventoryBox(5,445,5);
		inventoryBoxes.add(4,slotFive);

		slotSix = new InventoryBox(6,555,5);
		inventoryBoxes.add(5,slotSix);

		// this is the preview Item slot
		previewSlot = new InventoryBox(7, 675, 5);
		inventoryBoxes.add(6, previewSlot);

		// this sets the default selected item in the inventory
		this.selectedItem = slotOne;
	}

	public InventoryCanvas(Frame frame) {
		this.frame = frame;
		init();
	}

	public void refreshInven(){
		this.inventory = frame.getGame().getPlayer().getInventory();
		EmptyInven();
		// for every item in a players inventory it draws it in the lowest spot
		for (Entity e: inventory){
			if(inventoryBoxes.get(0).getItem() == null){
				replaceItem(e,0);
			}
			else if(inventoryBoxes.get(1).getItem()==null){
				replaceItem(e, 1);
			}
			else if(inventoryBoxes.get(2).getItem()==null){
				replaceItem(e, 2);
			}
			else if(inventoryBoxes.get(3).getItem()==null){
				replaceItem(e, 3);
			}
			else if(inventoryBoxes.get(4).getItem()==null){
				replaceItem(e,4);
			}
			else if(inventoryBoxes.get(5).getItem()==null){
				replaceItem(e, 5);
			}else {
				frame.addToNotificationText("Inventory full");

			}
		}
		this.repaint();
	}

	public ArrayList<InventoryBox> getInventoryBoxes() {
		return inventoryBoxes;
	}


	/**
	 * @see java.awt.Canvas#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics arg0) {
		for (InventoryBox k : inventoryBoxes) {
			k.draw(arg0, selectedItem);
		}
		arg0.setColor(Color.BLACK);
	}


	/**
	 * This is passed x and y coords from a mouse click and checks to see if any of the inventroy boxes are being clicked
	 *
	 * @param xPoint - the x mouse point
	 * @param yPoint - the y mouse point
	 */
	public void checkBoundingBox(int xPoint, int yPoint) {
		for(InventoryBox k : inventoryBoxes){
			InventoryBox a = k.boundingBox(xPoint,yPoint);
			if(a!=null){
				if(a.getNum()==7){ // this is the preview box
					// do nothing
				}else{
					this.selectedItem = a;
				}
			}
		}
	}


	public InventoryBox getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(InventoryBox selectedItem) {
		this.selectedItem = selectedItem;
	}

	/***
	 *
	 * This Class handles a inventory item bounding box and drawing it
	 *
	 * @author Andrew Booker 300260693, Glen Peek 300283786
	 *
	 */
	public class InventoryBox extends Rectangle{
		private static final long serialVersionUID = 1L;
		private int num;
		private Entity  item=null;
		boolean isSelected;



		/**
		 * This creates A inventory box
		 * @param i the number of the inventory slot
		 * @param x the x coordinate to draw the Inventory Box
		 * @param y the y coordinate to draw the Inventory Box
		 */
		public InventoryBox(int i, int x, int y){
			this.x = x;
			this.y = y;
			this.width = 100;
			this.height = 150;
			this.setNum(i);
			setBounds(x, y, width, height);

		}
		/**
		 *
		 * draws the inventory box at the proper place
		 *
		 * @param arg0 - the graphics
		 * @param selectedItem - the currently slected item
		 */
		public void draw(Graphics arg0, InventoryBox selectedItem) {

			if(num == 7){

				arg0.setColor(Color.RED);
				arg0.fillRect(x, y, width, height);
				arg0.setColor(Color.BLACK);
				arg0.drawString("Preview Slot", x+10, (this.y)+(this.height/2));
			}else{
				arg0.fillRect(x, y, width, height);
			}

			//draws image of item stored at this inventory slot
			if(item!=null){
				ImageIcon i = new ImageIcon(CreateGameWorld.class.getResource(item.getImage()));
				arg0.drawImage(i.getImage(), this.x, this.y, this.getBounds().width, this.getBounds().height, null, null);
			}

			if (selectedItem != null) {
				if (selectedItem.getNum() == this.num) {
					arg0.setColor(Color.RED);
					arg0.drawRect(x, y, width, height);
					arg0.drawRect(x-1, y-1, width+2, height+2);
					arg0.drawRect(x-2, y-2, width+4, height+4);
					arg0.setColor(Color.BLACK);
				}
			}
		}

		/**
		 * This is passed x and y coords from a mouse click and checks to see if any of the inventroy boxes are being clicked
		 *
		 * @param xPoint - the x mouse point
		 * @param yPoint - the y mouse point
		 */
		public InventoryBox boundingBox(int xPoint, int yPoint){

			if(this.contains(xPoint, yPoint)){
				this.isSelected = true;
				return this;
			}
			return null;


		}
		public Entity getItem() {
			return item;
		}
		public void setItem(Entity item) {
			this.item = item;
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}




	}


}




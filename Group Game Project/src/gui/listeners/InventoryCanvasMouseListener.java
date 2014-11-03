package gui.listeners;

import gui.InventoryCanvas;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * This class moniters the mouse input for the inventory canvas
 *
 * @author Andrew Booker 300260963
 *
 */
public class InventoryCanvasMouseListener implements MouseListener {

	private InventoryCanvas inventoryCanvas;

	public InventoryCanvasMouseListener(InventoryCanvas i){
		this.inventoryCanvas = i;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		inventoryCanvas.requestFocus();
		inventoryCanvas.checkBoundingBox(e.getX(),e.getY());
		inventoryCanvas.refreshInven();
		inventoryCanvas.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}

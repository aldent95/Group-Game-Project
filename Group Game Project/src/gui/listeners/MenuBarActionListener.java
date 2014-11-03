package gui.listeners;

import gui.Frame;
import gui.StartClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


/***
 *
 * This is the listen responsible for the menu bar items listed in the frame
 *
 * @author Andrew Booker
 *
 */
public class MenuBarActionListener implements ActionListener {

	private Frame frame;

	public MenuBarActionListener(Frame frame){
		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Exit": exitGame();break;
		case "About": showAbout(); break;
		case "Help": showHelp();break;
		}
		}

	/**
	 *
	 * Handles what happens when you  press help on the menu
	 *
	 *
	 */
	private void showHelp() {
		JOptionPane.showMessageDialog(frame, "NO");
	}
	/**
	 *
	 * Handles what happens when you select about on the menu
	 *
	 *
	 */
	private void showAbout() {
		JOptionPane.showMessageDialog(frame, "This game was made by Andrew Booker, Michael Pearson, Alex Dent, Andrew Buntain and Glen Peek");

	}
/**
 *
 * Handles what happens when you select exit game on the menu bar
 *
 *
 */
	private void exitGame() {

			boolean confirmBox = JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to shut down?", "Confirm Shutdown.",
					JOptionPane.YES_NO_OPTION, 0, new ImageIcon("")) == JOptionPane.NO_OPTION;

			if (!confirmBox) {
				frame.getInputChannel().disconnect();
				frame.dispose();
			}

	}
}

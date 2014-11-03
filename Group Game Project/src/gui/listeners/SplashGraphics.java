package gui.listeners;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


/***
 * This is responsible for the graphics on the splash page panel When the game starts
 *  
 * @author Andrew Booker, Glen Peek *
 */
public class SplashGraphics extends Canvas {
	private static final long serialVersionUID = 1L;

	ImageIcon i = new ImageIcon(getClass().getResource("/render/img/logos/title.png"));
	Image pic = i.getImage();

	private ImageObserver observer;	

	@Override
	public void paint(Graphics arg0) {
		arg0.drawImage(pic, 0,0, observer);
	}

	public static BufferedImage loadImage(String fileName) {
		try {
			BufferedImage img;
			File f = new File(fileName);
			img = ImageIO.read(f);
			return img;

		} catch (IOException e) {
			System.out.println("Image couldn't be loaded!");
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}


}

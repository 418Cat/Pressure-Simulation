package mainPkg;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Window {
	
	JFrame frame;
	Graphics g;
	int gridSize[];
	int cellSize[];
	
	Window(int frameWidth, int frameHeight, int gridSizeX, int gridSizeY) {
		
		frame = new JFrame("Pressure simulation");
		frame.setSize(frameWidth, frameHeight);
		frame.setLocation(500,  1500);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		frame.setVisible(true);
		
		g = frame.getGraphics();
		
		gridSize = new int[] {gridSizeX, gridSizeY};
		cellSize = new int[] {frameWidth/gridSizeX, frameHeight/gridSizeY};
		
	}
	
	public void refreshCell(int[] cellCoords, Color color) {
		g.setColor(color);
		g.fillRect(cellSize[0]*cellCoords[0], cellSize[1]*cellCoords[1], cellSize[0], cellSize[1]);
	};
	
}

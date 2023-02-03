package mainPkg;

import java.awt.Color;

public class Simulation {
	
	Cell[][] cellGrid;
	Cell[][] bufferGrid;
	
	int[] gridSize;
	
	Window simWin;
	float refreshMs;
	
	Simulation(int gridSizeX, int gridSizeY, int simHz, Window win) {
		
		cellGrid = new Cell[gridSizeX][gridSizeY];
		
		gridSize = new int[] {gridSizeX, gridSizeY};
		
		simWin = win;
		
		try {
			Thread.sleep(100);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		if((float)1000/(float)simHz < 1) {
			System.err.println("refresh rate too high, defaulted to 60hz");
			refreshMs = ((float)1000/(float)60);
		} else {
			refreshMs = (float)1000/(float)simHz;
		}
		
		for(int x = 0; x < gridSizeX; x++) {
			for(int y = 0; y < gridSizeY; y++) {
				cellGrid[x][y] = new Cell(x, y);
			}
		}
		
		cellGrid[125][125].addX(1000000);
		cellGrid[125][125].addY(1000000);
		
	}
	
	
	
	private boolean cellHasLeftNeighbour(int[] cellCoords) {return(cellCoords[0] > 0);}
	private boolean cellHasRightNeighbour(int[] cellCoords) {return(cellCoords[0] < gridSize[0]-1);}
	
	private boolean cellHasTopNeighbour(int[] cellCoords) {return(cellCoords[1] > 0);}
	private boolean cellHasBottomNeighbour(int[] cellCoords) {return(cellCoords[1] < gridSize[1]-1);}
	
	private int[][] getXNeighbours(int[] cellCoords) {
		
		if(cellHasLeftNeighbour(cellCoords) && cellHasRightNeighbour(cellCoords)) {
			return(new int[][] {new int[] {cellCoords[0]-1, cellCoords[1]}, new int[] {cellCoords[0]+1, cellCoords[1]}});
		}
		
		if(cellHasLeftNeighbour(cellCoords)) {
			return(new int[][] {new int[] {cellCoords[0]-1, cellCoords[1]}});
		}
		
		if(cellHasRightNeighbour(cellCoords)) {
			return(new int[][] {new int[] {cellCoords[0]+1, cellCoords[1]}});
		}
		
		return(new int[][] {});
	}
	
	
	private int[][] getYNeighbours(int[] cellCoords) {
		
		if(cellHasTopNeighbour(cellCoords) && cellHasBottomNeighbour(cellCoords)) {
			return(new int[][] {new int[] {cellCoords[0], cellCoords[1]-1}, new int[] {cellCoords[0], cellCoords[1]+1}});
		}
		
		if(cellHasTopNeighbour(cellCoords)) {
			return(new int[][] {new int[] {cellCoords[0], cellCoords[1]-1}});
		}
		
		if(cellHasBottomNeighbour(cellCoords)) {
			return(new int[][] {new int[] {cellCoords[0], cellCoords[1]+1}});
		}
		
		return(new int[][] {});
	}
	
	private Cell computeCell(int[] cellCoords, boolean draw) {
		
		Cell bufferCell = cellGrid[cellCoords[0]][cellCoords[1]];
		if(draw) {
			simWin.refreshCell(cellCoords, new Color(0, 0, 0));
		}
		
		int[][][] cellNeighbours = {
				getXNeighbours(cellCoords),
				getYNeighbours(cellCoords)
		};
		
		bufferCell.divideX(cellNeighbours[0].length + cellNeighbours[1].length +1);
		bufferCell.divideY(cellNeighbours[0].length + cellNeighbours[1].length +1);
		
		for(int[] neighbX : cellNeighbours[0]) {
			bufferCell.addX(cellGrid[neighbX[0]][neighbX[1]].getSpeed()[0]/(getXNeighbours(neighbX).length + getYNeighbours(neighbX).length + 1));
			bufferCell.addY(cellGrid[neighbX[0]][neighbX[1]].getSpeed()[1]/(getXNeighbours(neighbX).length + getYNeighbours(neighbX).length + 1));
		}
		
		for(int[] neighbY : cellNeighbours[1]) {
			bufferCell.addX(cellGrid[neighbY[0]][neighbY[1]].getSpeed()[0]/(getXNeighbours(neighbY).length + getYNeighbours(neighbY).length + 1));
			bufferCell.addY(cellGrid[neighbY[0]][neighbY[1]].getSpeed()[1]/(getXNeighbours(neighbY).length + getYNeighbours(neighbY).length + 1));
		}
		
		if(draw) {
			int red = bufferCell.getIntensity();
			if(red > 255) {red = 255;}
			if(red < 0) {red = 0;}
			simWin.refreshCell(cellCoords, new Color(red, 0, 0));
		}
		
		return(bufferCell);		
	}
	
	private void computeGrid(boolean draw) {
		bufferGrid = cellGrid;
		
		for(Cell[] cellRow : bufferGrid) {
			
			for(Cell tmpCell : cellRow) {
				
				bufferGrid[tmpCell.getCoords()[0]][tmpCell.getCoords()[1]] = computeCell(tmpCell.getCoords(), draw);
				
			}
			
		}
		
		cellGrid = bufferGrid;
		
	}
	
	public void simulate(boolean draw) {
		while(true) {
			//if(new Date().getTime()%refreshMs == 0) {
				computeGrid(draw);
			//}
		}
	}
}




class Cell {
	
	private int[] XYSpeed;
	
	private int[] gridCoords;
	
	
	Cell(int xCoords, int yCoords, int[] initialSpeed) {
		this.gridCoords = new int[] {xCoords, xCoords};
		this.XYSpeed = initialSpeed;
	}
	
	Cell(int Xcoords, int YCoords) {
		this.gridCoords = new int[] {Xcoords, YCoords};
		XYSpeed = new int[] {0, 0};
	}
	
	
	public void addX(int xSpeed) {
		XYSpeed[0] += xSpeed;
	}
	
	public void addY(int ySpeed) {
		XYSpeed[1] += ySpeed;
	}
	
	public void divideX(int div) {
		XYSpeed[0] /= div;
	}
	
	public void divideY(int div) {
		XYSpeed[1] /= div;
	}
	
	
	/*
	 * GET
	 */
	public int[] getSpeed() {
		return(XYSpeed);
	}
	
	public int[] getCoords() {
		return(gridCoords);
	}
	
	public int getIntensity() {
		return(XYSpeed[0] + XYSpeed[1]);
	}
	
}





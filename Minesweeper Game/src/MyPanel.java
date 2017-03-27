import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Date;
//import java.sql.Date;
import java.util.Random;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 10;
	private static final int TOTAL_ROWS = 11;   //Last row has only one cell
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;	
	
	public  Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int[][] mines = new int [TOTAL_COLUMNS][TOTAL_ROWS];
	public int[][] neightbours = new int [TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean[][] isUncovered = new boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean[][] flagged = new boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean happy = true;
	public boolean victory = false;
	public boolean lost = false;
	public int[][] flag = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	
	public int smileyX=150, smileyY=30;
	
	
	public int timerX=150, timerY=30;
	
	public int seconds=0;
	public Date startDate = new Date();
	
	
	Random random = new Random();
	int squareNeightbors;
	
	
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //Top row
			colorArray[x][0] = Color.LIGHT_GRAY;
		//	colorArray[x][0] = Color.BLACK;
		}
		for (int y = 0; y < TOTAL_ROWS; y++) {   //Left column
			colorArray[0][y] = Color.LIGHT_GRAY;
		}
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   //The rest of the grid
			for (int y = 1; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
			}
			
		}
	
	
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   //Generate random mines with 20% of probability for each square.
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				if(random.nextInt(100) < 20){
					mines[x][y] = 1;                // If it is 1, it has a mine. If it is 0, it is empty.
				}
				else{
					mines[x][y] = 0;
					
				}
				isUncovered[x][y] = false;            // Initially, all mines will be covered.
				flagged[x][y] = false;
			}
			}
			
		for (int x = 1; x < TOTAL_COLUMNS; x++) {          
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				squareNeightbors = 0;
				for (int m = 1; m < TOTAL_COLUMNS; m++) {          
					for (int n = 1; n < TOTAL_ROWS - 1; n++) {
						if(!(x == m &&  y == n)){
							if(isNeightbor(x, y, m, n)){
								squareNeightbors++;
						
						}
			         }
                 }
					
					neightbours[x][y] = squareNeightbors;
			
				}
			}			
		}
	}
		
	


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//Draw the grid minus the bottom row (which has only one cell)
		//By default, the grid will be 10x10 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}

		//Draw an additional cell at the bottom left
		//g.drawRect(x1 + GRID_X, y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)), INNER_CELL_SIZE + 1, INNER_CELL_SIZE + 1);

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((x == 0) || (y != TOTAL_ROWS - 1)) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
				
				if(isUncovered[x][y] == true){
					if(mines[x][y] == 0){
						g.setColor(Color.GRAY);
						g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
					}
					else{
						g.setColor(Color.BLACK);
						for (int i = 0; i < TOTAL_COLUMNS; i++) {
							for (int j = 0; j < TOTAL_ROWS; j++) {
								if(mines[i][j]==1){
									isUncovered[i][j]=true;
									g.fillRect(x1 + GRID_X + (i * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (j * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
									happy = false;
									lost = true;	
									}
								}
							}
					    }
					
				}
//				else{
//					if(flagged[x][y]==false){
//						g.setColor(Color.RED);
//						g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
//						flagged[x][y]=true;									
//
//					}
//				}
				if(isUncovered[x][y] == true){
				
					if(mines[x][y] == 0 && neightbours[x][y] != 0){
						if(neightbours[x][y] == 1){
							g.setColor(Color.BLUE);
						}
						else if(neightbours[x][y] == 2){
							g.setColor(Color.GREEN);
						}
						else if(neightbours[x][y] == 3){
							g.setColor(Color.RED);
						}
						else if(neightbours[x][y] == 4){
							g.setColor(new Color(0,0,128));
						}
						else if(neightbours[x][y] == 5){
							g.setColor(new Color(178,34,34));
						}
						else if(neightbours[x][y] == 6){
							g.setColor(new Color(72,209,204));
						}
						else if(neightbours[x][y] == 7){
							g.setColor(Color.BLACK);
						}
						else if(neightbours[x][y] == 8){
							g.setColor(Color.DARK_GRAY);
						}
					     g.setFont(new Font("Tahoma", Font.BOLD, 25));
					     g.drawString(Integer.toString(neightbours[x][y]), x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 7, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 25);
					}
				}
			}
		}
		g.setColor(Color.YELLOW);
		g.fillOval(GRID_X+150, GRID_Y, INNER_CELL_SIZE,INNER_CELL_SIZE);
		g.setColor(Color.BLACK);
		g.fillOval(GRID_X+158, GRID_Y+5,INNER_CELL_SIZE/6,INNER_CELL_SIZE/6);
		g.fillOval(GRID_X+168, GRID_Y+5,INNER_CELL_SIZE/6,INNER_CELL_SIZE/6);
		// mouth is missing
		if(happy==true){
			//happy face
			g.fillRect(GRID_X+158, GRID_Y+16, 15, 3);
			g.fillRect(GRID_X+157, GRID_Y+14, 5, 4);
			g.fillRect(GRID_X+169, GRID_Y+14, 5, 4);
		} else {
			//sad face
			g.fillRect(GRID_X+158, GRID_Y+15, 15, 3);
			g.fillRect(GRID_X+157, GRID_Y+17, 5, 3);
			g.fillRect(GRID_X+169, GRID_Y+17, 5, 3);
		}
		// timer
		g.setColor(Color.BLACK);
		g.fillRect(GRID_X, GRID_Y, (INNER_CELL_SIZE+1)*2, INNER_CELL_SIZE+1);
		seconds =(int)((new Date().getTime()-startDate.getTime())/1000);
		if(seconds>100){
			seconds = 1000;
		}
		//System.out.println(seconds);
		g.setColor(Color.RED);
		g.setFont(new Font ("Tahoma", Font.PLAIN, 30));
		if(seconds<10){
		g.drawString("00"+Integer.toString(seconds), GRID_X+8, GRID_Y+INNER_CELL_SIZE);
			}else if(seconds<100){
				g.drawString("0"+Integer.toString(seconds), GRID_X+8, GRID_Y+INNER_CELL_SIZE);
			}
		// flags
//		g.setColor(Color.RED);
//		g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
		
		
		if(lost == true){
			g.setFont(new Font("Tahoma", Font.BOLD, 35));
			g.drawString("Game Over!", 70,height-5);
		}
	}
	

	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
	public static int getTotalColumns() {
		return TOTAL_COLUMNS;
	}
	public static int getTotalRows() {
		return TOTAL_ROWS;
	}
	public boolean isNeightbor(int squareX, int squareY, int neightborX, int neightborY ){
		if(squareX - neightborX < 2 && squareX - neightborX > -2 && squareY - neightborY < 2 && squareY - neightborY > -2 && mines[neightborX][neightborY] == 1){
			return true;
		}
		return false;
		
	}
	public void resetGame(){
		happy = true;
		victory = false;
		lost = false;
		startDate = new Date();
		
		for (int x = 1; x < TOTAL_COLUMNS; x++) {   //Generate random mines with 20% of probability for each square.
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				if(random.nextInt(100) < 20){
					mines[x][y] = 1;                // If it is 1, it has a mine. If it is 0, it is empty.
				}
				else{
					mines[x][y] = 0;
					
				}
				isUncovered[x][y] = false;            // Initially, all mines will be covered.
				flagged[x][y] = false;
			}
		}
		
			
		
			
		for (int x = 1; x < TOTAL_COLUMNS; x++) {          
			for (int y = 1; y < TOTAL_ROWS - 1; y++) {
				squareNeightbors = 0;
				for (int m = 1; m < TOTAL_COLUMNS; m++) {          
					for (int n = 1; n < TOTAL_ROWS - 1; n++) {
						if(!(x == m &&  y == n)){
							if(isNeightbor(x, y, m, n)){
								squareNeightbors++;
						
						}
			         }
                 }
					
					neightbours[x][y] = squareNeightbors;
			
				}
				}
			}
		
		
	}
	
	
}
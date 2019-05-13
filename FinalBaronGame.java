package application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;
/**
 * Class for the game itself that holds the rules of the game 
 * @author Erin Jagt & Micah Weiberg
 * @version 05/13/19
 *
 */
public class FinalBaronGame {
	/** A helper object to handle observer pattern behavior */
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/** Variable for size of board */
	private int size;
	/** Variable for game board */
	private ArrayList<LandTile> gameBoard;
	/** Variable for player 1's budget */
	private int p1Budget;
	/** Variable for player 2's budget */
	private int p2Budget;
	/** Variable for turn number */
	private int turn;
	/** Variable for passing */
	private int pass;
	/** Variable for if game done */
	private boolean done;
	
	/**
	 * Constructor for FinalBaronGame
	 * @param newSize the size of the game board o build
	 */
	FinalBaronGame(int newSize){
		size = newSize; 
		p1Budget = size*size*2;
		p2Budget = p1Budget;
		turn =1;
		pass=-1;
		done = false;
		reMakeBoard();
	}
	
	/**
	 * Method to change size (makes a new game)
	 * @param newSize the new size
	 */
	public void setSize(int newSize) {
		size = newSize; 
		p1Budget = size*size*2;
		p2Budget = p1Budget;
		turn =1;
		pass=-1;
		done = false;
		reMakeBoard();
		pcs.firePropertyChange("board", null, null);
	}
	
	/**
	 * Method to reset game
	 */
	public void resetGame() {
		p1Budget = size*size*2;
		p2Budget = p1Budget;
		turn =1;
		pass=-1;
		done = false;
		reMakeBoard();
		pcs.firePropertyChange("board", null, null);
	}
	
	/**
	 * Method to make a bid
	 * @param row Row of LandTile bidding on
	 * @param col Column of LandTile bidding on
	 * @throws Exception If Land can't be bid on
	 */
	public void makeBid(int position) throws Exception{
		try {
		if(turn%2==1) {
			int [] budgets = gameBoard.get(position).changeOwner(p1Budget,p2Budget,'1');
			p1Budget=budgets[0];
			p2Budget=budgets[1];
		}
		else {
			int [] budgets = gameBoard.get(position).changeOwner(p2Budget,p1Budget,'2');
			p1Budget=budgets[1];
			p2Budget=budgets[0];
		}
		turn++;
		}
		catch(Exception e) {
			throw e;
		}
		String name = "position "+position;
		pcs.firePropertyChange(name, null, null);
	}
	
	/**
	 * Method to handle if pass is clicked
	 */
	public void passClicked() {
		if (turn== pass+1) {
			done=true;
			try {
				findProfitAndPaid();
				pcs.firePropertyChange("board", null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			pass=turn;
			turn++;
			pcs.firePropertyChange("pass", null, null);
		}
	}
	
	/**
	 * Method to get the information that will be printed in the GUI
	 * @return
	 */
	public String getInfo() {
		String info="";
		if (done==true) {
			try {
				info = getWinner();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			if (turn%2==1)	
				info=info+"Player 1 Turn ";
			else
				info=info+"Player 2 Turn ";
			info=info+"\n"+"Player 1 Budget: "+ p1Budget +"\n"+ "Player 2 Budget: "+p2Budget;
			if(turn==pass+1)
				info=info+"\n"+"PASSING ENDS THE GAME";
			return info;
		}
		return info;
	}
	/**
	 * Method to find the profit each player made and amount each player was paid
	 * @return An array containing the profit and paid amount for each player 
	 * @throws Exception If there is no possible path
	 */
	private int [] findProfitAndPaid() throws Exception {
		int [] path = findPath(); //Array with the path
		int p1paid=0; //Amount player 1 will be paid
		int p2paid=0; //Amount player 2 will be paid
		for(int next=0;next<size*size;next++) {
			//If Land belongs to player 1
			if(gameBoard.get(path[next]).getType()=='1') {
				p1paid=p1paid+10*gameBoard.get(path[next]).getCost();
			}
			//If path belongs to player 2
			else if(gameBoard.get(path[next]).getType()=='2') {
				p2paid=p2paid+10*gameBoard.get(path[next]).getCost();
			}
			gameBoard.get(path[next]).setOnPath();
			if(path[next]==0)
				break;
		}
		int p1Profit= p1paid-(size*size*2-p1Budget);
		int p2Profit= p2paid-(size*size*2-p2Budget);
		int [] money= new int[4];
		money[0]=p1paid;
		money[1]=p2paid;
		money[2]=p1Profit;
		money[3]=p2Profit;
		return money;
	}
	
	/**
	 * Method to return winner
	 * @return 
	 * @throws Exception If no path exists
	 */
	private String getWinner() throws Exception {
		int [] money=findProfitAndPaid();
		int p1paid=money[0];
		int p2paid=money[1];
		int p1Profit=money[2];
		int p2Profit=money[3];
		String info=""; //String to hold info to print
		if (p1Profit<0&&p2Profit<0)
			info=info+"Both Players Lose!";
		else if (p1Profit>p2Profit)
			info=info+"Player 1 wins!";
		else if(p1Profit<p2Profit)
			info=info+"Player 2 wins!";
		else
			info=info+"Tie Game!";
		info=info+"\n"+"Player 1 Spent: "+(size*size*2-p1Budget)+" Player 2 Spent: "+(size*size*2-p2Budget);
		info=info+"\n"+"Player 1 was Paid: "+p1paid+" Player 2 was Paid: "+p2paid;
		info=info+"\n"+"Player 1 Profit: "+p1Profit+" Player 2 Profit: " +p2Profit;
		return info;
	}
	/**
	 * Method to find cheapest path
	 * @return The integer indexes of the cheapest path
	 * @throws Exception If no possible path exists
	 */
	private int[] findPath() throws Exception {
		int [][] pathTable = new int[size*size][3]; 
		PriorityQueue<LandItem> queue= new PriorityQueue<LandItem>();
		LandItem start=new LandItem(0,0);
		for(int i=0;i<size*size;i++)
			pathTable[i][2]=0;
		pathTable[0][0]=0;
		queue.add(start);
		while(!queue.isEmpty()) {
			LandItem next = queue.poll();
			pathTable[next.getIndex()][2]=1;
			//Check if we just dequeued the end position;
			if (next.getIndex()==size*size-1)
				break;
			//Check path going right
			if(next.getIndex()%size!=(size-1)&&gameBoard.get(next.getIndex()+1).getType()!='O'&&pathTable[next.getIndex()+1][2]!=1) {
				//First time encountering index
				if(pathTable[next.getIndex()+1][2]==0) {
					pathTable[next.getIndex()+1][0]=next.getPrice()+gameBoard.get(next.getIndex()+1).getCost();
					pathTable[next.getIndex()+1][2]=2;
					pathTable[next.getIndex()+1][1]=next.getIndex();
					LandItem toAdd = new LandItem(next.getIndex()+1, pathTable[next.getIndex()+1][0]);
					queue.add(toAdd);
				}
				//Second time, checking if cheaper path
				else if(pathTable[next.getIndex()+1][0]>next.getPrice()+gameBoard.get(next.getIndex()+1).getCost()) {
					LandItem toRemove = new LandItem(next.getIndex()+1, pathTable[next.getIndex()+1][2]);
					queue.remove(toRemove);
					pathTable[next.getIndex()+1][0]=next.getPrice()+gameBoard.get(next.getIndex()+1).getCost();
					pathTable[next.getIndex()+1][1]=next.getIndex();
					LandItem toAdd = new LandItem(next.getIndex()+1, pathTable[next.getIndex()+1][0]);
					queue.add(toAdd);
				}
			}
			//Check path going left
			if(next.getIndex()%size!=0&&gameBoard.get(next.getIndex()-1).getType()!='O'&&pathTable[next.getIndex()-1][2]!=1) {
				//First time encountering index
				if(pathTable[next.getIndex()-1][2]==0) {
					pathTable[next.getIndex()-1][0]=next.getPrice()+gameBoard.get(next.getIndex()-1).getCost();
					pathTable[next.getIndex()-1][2]=2;
					pathTable[next.getIndex()-1][1]=next.getIndex();
					LandItem toAdd = new LandItem(next.getIndex()-1, pathTable[next.getIndex()-1][0]);
					queue.add(toAdd);
				}
				//Second time, checking if cheaper path
				else if(pathTable[next.getIndex()-1][0]>next.getPrice()+gameBoard.get(next.getIndex()-1).getCost()) {
					LandItem toRemove = new LandItem(next.getIndex()-1, pathTable[next.getIndex()-1][2]);
					queue.remove(toRemove);
					pathTable[next.getIndex()-1][0]=next.getPrice()+gameBoard.get(next.getIndex()-1).getCost();
					pathTable[next.getIndex()-1][1]=next.getIndex();
					LandItem toAdd = new LandItem(next.getIndex()-1, pathTable[next.getIndex()-1][0]);
					queue.add(toAdd);
				}
			}
			//Check path going down
			if(next.getIndex()/size!=(size-1)&&gameBoard.get(next.getIndex()+size).getType()!='O'&&pathTable[next.getIndex()+size][2]!=1) {
				//First time encountering index
				if(pathTable[next.getIndex()+size][2]==0) {
					pathTable[next.getIndex()+size][0]=next.getPrice()+gameBoard.get(next.getIndex()+size).getCost();
					pathTable[next.getIndex()+size][2]=2;
					pathTable[next.getIndex()+size][1]=next.getIndex();
					LandItem toAdd = new LandItem(next.getIndex()+size, pathTable[next.getIndex()+size][0]);
					queue.add(toAdd);
				}
				//Second time, checking if cheaper path
				else if(pathTable[next.getIndex()+size][0]>next.getPrice()+gameBoard.get(next.getIndex()+size).getCost()) {
					LandItem toRemove = new LandItem(next.getIndex()+size, pathTable[next.getIndex()+size][2]);
					queue.remove(toRemove);
					pathTable[next.getIndex()+size][0]=next.getPrice()+gameBoard.get(next.getIndex()+size).getCost();
					pathTable[next.getIndex()+size][1]=next.getIndex();
					LandItem toAdd = new LandItem(next.getIndex()+size, pathTable[next.getIndex()+size][0]);
					queue.add(toAdd);
				}
			}
			//Check path going up
			if(next.getIndex()/size!=0&&gameBoard.get(next.getIndex()-size).getType()!='O'&&pathTable[next.getIndex()-size][2]!=1) {
				//First time encountering index
				if(pathTable[next.getIndex()-size][2]==0) {
					pathTable[next.getIndex()-size][0]=next.getPrice()+gameBoard.get(next.getIndex()-size).getCost();
					pathTable[next.getIndex()-size][2]=2;
					pathTable[next.getIndex()-size][1]=next.getIndex();
					LandItem toAdd = new LandItem(next.getIndex()-size, pathTable[next.getIndex()-size][0]);
					queue.add(toAdd);
				}
				//Second time, checking if cheaper path
				else if(pathTable[next.getIndex()-size][0]>next.getPrice()+gameBoard.get(next.getIndex()-size).getCost()) {
					LandItem toRemove = new LandItem(next.getIndex()-size, pathTable[next.getIndex()-size][2]);
					queue.remove(toRemove);
					pathTable[next.getIndex()-size][0]=next.getPrice()+gameBoard.get(next.getIndex()-size).getCost();
					pathTable[next.getIndex()-size][1]=next.getIndex();
					LandItem toAdd = new LandItem(next.getIndex()-size, pathTable[next.getIndex()-size][0]);
					queue.add(toAdd);
				}
			}
		}
		if (pathTable[size*size-1][2]!=1)
			throw new Exception("No possible paths");
		//Create an array of the path;
		int iterator=pathTable[size*size-1][1];
		int [] path = new int[size*size]; //Path cannot be longer than size^2 
		path[0]=size*size-1;
		int count=1;
		while(iterator!=0) {
			path[count]=iterator;
			count++;
			iterator=pathTable[iterator][1];
		}
		path[count]=0;
		return path;
	}

	/**
	 * A way for Observers to subscribe to this
	 * @param listener
	 */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * A way for Observers to unsubscribe
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
    /**
     * Getter for size variable
     * @return the size of game
     */
	public int getSize() {
		return size;
	}
	/**
	 * Method to get the land tile at specific location 
	 * @param i The position of land tile
	 * @return The land tile
	 */
	public LandTile getTileAt(int i) {
		return gameBoard.get(i);
	}
	/**
	 * Method to make or remake the game board
	 */
	private void reMakeBoard() {
		gameBoard = new ArrayList<LandTile>();
		for(int i=0;i<size*size;i++) {
				gameBoard.add(i, new LandTile('U'));
			}
		Random name = new Random();
		gameBoard.add(0, new LandTile('E'));
		gameBoard.add((size*size)-1, new LandTile('E'));
		for(int i=0; i<size; i++) {
			char newType = ' ';
			if (name.nextInt(2)==0)
				newType = 'C';
			else
				newType = 'O';
			int position = name.nextInt((size*size)-2)+1;
			while(gameBoard.get(position).getType()!='U'){
				position = name.nextInt((size*size)-2)+1;
			}
			gameBoard.remove(position);
			gameBoard.add(position, new LandTile(newType));
		}
		try {
			findPath();
		} catch (Exception e) {
			reMakeBoard();
		}
	}
}
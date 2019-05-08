package application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.TextField;

public class FinalBaronGame {
	/** A helper object to handle observer pattern behavior */
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	/** Variable for size of board */
	private int size;
	
	/** Variable for game board */
	private ArrayList<ArrayList<LandTile>> gameBoard;
	
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
	
	FinalBaronGame(int newSize){
		size = newSize; 
		p1Budget = size*size*2;
		p2Budget = p1Budget;
		turn =1;
		pass=-1;
		done = false;
		gameBoard = new ArrayList<ArrayList<LandTile>>();
		for(int i=0;i<size;i++) {
			gameBoard.add(i, new ArrayList<LandTile>());
			for(int j=0;j<size;j++) {
				gameBoard.get(i).add(j, new LandTile('U'));
			}
		}
		Random name = new Random(); 
		for(int i=0; i<size; i++) {
			char newType = ' ';
			if (name.nextInt(2)==0)
				newType = 'C';
			else
				newType = 'O';
			int position = name.nextInt((size*size)-2)+1;
			while(gameBoard.get(position/size).get(position%size).getType()!='U'){
				position = name.nextInt((size*size)-2)+1;
			}
			gameBoard.get(position/size).add(position%size, new LandTile(newType));
		}
		gameBoard.get(0).add(0, new LandTile('E'));
		gameBoard.get(size-1).add(size-1, new LandTile('E'));
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
		gameBoard = new ArrayList<ArrayList<LandTile>>();
		for(int i=0;i<size;i++) {
			gameBoard.add(i, new ArrayList<LandTile>());
			for(int j=0;j<size;j++) {
				gameBoard.get(i).add(j, new LandTile('U'));
			}
		}
		Random name = new Random(); 
		for(int i=0; i<size; i++) {
			char newType = ' ';
			if (name.nextInt(2)==0)
				newType = 'C';
			else
				newType = 'O';
			int position = name.nextInt((size*size)-2)+1;
			while(gameBoard.get(position/size).get(position%size).getType()!='U'){
				position = name.nextInt((size*size)-2)+1;
			}
			gameBoard.get(position/size).add(position%size, new LandTile(newType));
		}
		gameBoard.get(0).add(0, new LandTile('E'));
		gameBoard.get(size-1).add(size-1, new LandTile('E'));
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
		gameBoard = new ArrayList<ArrayList<LandTile>>();
		for(int i=0;i<size;i++) {
			gameBoard.add(i, new ArrayList<LandTile>());
			for(int j=0;j<size;j++) {
				gameBoard.get(i).add(j, new LandTile('U'));
			}
		}
		Random name = new Random(); 
		for(int i=0; i<size; i++) {
			char newType = ' ';
			if (name.nextInt(2)==0)
				newType = 'C';
			else
				newType = 'O';
			int position = name.nextInt((size*size)-2)+1;
			while(gameBoard.get(position/size).get(position%size).getType()!='U'){
				position = name.nextInt((size*size)-2)+1;
			}
			gameBoard.get(position/size).add(position%size, new LandTile(newType));
		}
		gameBoard.get(0).add(0, new LandTile('E'));
		gameBoard.get(size-1).add(size-1, new LandTile('E'));
		pcs.firePropertyChange("board", null, null);
	}
	
	/**
	 * Method to make a bid
	 * @param row Row of LandTile bidding on
	 * @param col Column of LandTile bidding on
	 * @throws Exception If Land can't be bid on
	 */
	public void makeBid(int row, int col) throws Exception{
		try {
		if(turn%2==1) {
			int [] budgets = gameBoard.get(row).get(col).changeOwner(p1Budget,p2Budget,'1');
			p1Budget=budgets[0];
			p2Budget=budgets[1];
		}
		else {
			int [] budgets = gameBoard.get(row).get(col).changeOwner(p2Budget,p1Budget,'2');
			p1Budget=budgets[1];
			p2Budget=budgets[0];
		}
		turn++;
		}
		catch(Exception e) {
			throw e;
		}
		String name = "position"+row+" "+col;
		pcs.firePropertyChange(name, null, null);
	}
	
	/**
	 * Method to handle if pass is clicked
	 */
	public void passClicked() {
		if (turn== pass+1)
			done=true;
		else {
			pass=turn;
			turn++;
		}
		pcs.firePropertyChange("pass", null, null);
	}

	public String getInfo() {
		if (done==true) {
			return getWinner();
		}
		else {
			String info="";
			if (turn%2==1)	
				info=info+"Player 1 Turn ";
			else
				info=info+"Player 2 Turn ";
			info=info+"\n"+"Player 1 Budget: "+ p1Budget +"\n"+ "Player 2 Budget: "+p2Budget;
			if(turn==pass+1)
				info=info+"\n"+"PASSING ENDS THE GAME";
			return info;
		}
	}

	/**
	 * Method to return winner
	 * @return 
	 */
	private String getWinner() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Don't forget to create a way for Observers to subscribe to this
	 * @param listener
	 */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * And Observers probably want to be able to unsubscribe as well
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

	public LandTile getTileAt(int i, int j) {
		return gameBoard.get(i).get(j);
	}
}

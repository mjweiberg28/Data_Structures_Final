package application;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.TextField;

public class FinalBaronGame {
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
			int row = name.nextInt(4);
			int col = 0;
			if(row==0)
				col = name.nextInt(3)+1;
			else if(row==3)
				col = name.nextInt(3)-1;
			else
				col = name.nextInt(4);
			gameBoard.get(row).add(col, new LandTile(newType));
		}
		gameBoard.get(0).add(0, new LandTile('E'));
		gameBoard.get(3).add(3, new LandTile('E'));
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
			int row = name.nextInt(4);
			int col = 0;
			if(row==0)
				col = name.nextInt(3)+1;
			else if(row==3)
				col = name.nextInt(3)-1;
			else
				col = name.nextInt(4);
			gameBoard.get(row).add(col, new LandTile(newType));
		}
		gameBoard.get(0).add(0, new LandTile('E'));
		gameBoard.get(3).add(3, new LandTile('E'));
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
			int row = name.nextInt(4);
			int col = 0;
			if(row==0)
				col = name.nextInt(3)+1;
			else if(row==3)
				col = name.nextInt(3)-1;
			else
				col = name.nextInt(4);
			gameBoard.get(row).add(col, new LandTile(newType));
		}
		gameBoard.get(0).add(0, new LandTile('E'));
		gameBoard.get(3).add(3, new LandTile('E'));
	}
	
	/**
	 * Method to make a bid
	 * @param row Row of LandTile bidding on
	 * @param col Column of LandTile bidding on
	 * @throws Exception If Land can't be bid on
	 */
	public void makeBid(int row, int col) throws Exception{
		if(turn%2==1)
			p1Budget = gameBoard.get(row).get(col).changeOwner(p1Budget,'1');
		else
			p2Budget = gameBoard.get(row).get(col).changeOwner(p2Budget,'2');
		turn++;
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
			info="\n"+"Player 1 Budget: "+ p1Budget +"\n"+ "Player 2 Budget: "+p2Budget;
			if(turn==pass+1)
				info="\n"+"PASSING ENDS THE GAME";
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
}

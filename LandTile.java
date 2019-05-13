package application;
/**
 * Class for the tiles used in the game
 * @author Erin Jagt
 * @version 05/13/19
 *
 */
public class LandTile {
	/** Variable for cost of land */
	private int cost;
	
	/** Variable for type of land (O=Off Limits, C = Company, E= end, U = unowned, ‘1’= player1 ‘2’ = player2) */
	private char type; 
	/** Variable to know if part of AI path*/
	private boolean onPath;
	/**
	 * Constructor 
	 * @param type The type of land
	 */
	LandTile(char newType){
		type=newType;
		onPath=false;
		if (type == 'O')
			cost = Integer.MAX_VALUE;
		else
			cost=0;
	}
	/**
	 * Method to change a tile to being on the path
	 */
	public void setOnPath() {
		onPath=true;
	}
	/**
	 * Getter for Type
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Method to get the cost
	 * @return The cost of the land
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 * Method to change the owner of the land
	 * @param playerBudget  budget of the player
	 * @param player which player who is buying the land
	 * @return the new budget of player
	 * @throws Exception If player can't buy land
	 */
	public int [] changeOwner(int playerBiddingBudget, int otherPlayerBudget,char player) throws Exception {
		if (type=='O')
			throw new Exception("This land is off limits. The cops are coming.");
		else if(type=='E')	
			throw new Exception("You cannot buy the starting or ending position.");
		else if(type=='C')
			throw new Exception("This land is used by the company, you thief.");
		else if((playerBiddingBudget==0)||(type!=player&&playerBiddingBudget<=cost))
			throw new Exception("You do not have enough money, you broke peasant.");
		else {
			if(type!='U'&&type!=player)
				otherPlayerBudget=otherPlayerBudget+cost;
			int [] budgets = new int[2];
			if(type!=player) {
				type=player;
				cost=cost+1;
				budgets[0]=playerBiddingBudget-cost;
			}
			else if(type==player) {
				budgets[0]=playerBiddingBudget-1;
				cost=cost+1;
			}
			budgets[1]=otherPlayerBudget;
			return budgets;
		}
	}
	
	/**
	 * Method to check if the tile is on the path or not
	 * @return True if a tile is on the path chosen, false otherwise
	 */
	public boolean isOnPath() {
		return onPath;
	}
}

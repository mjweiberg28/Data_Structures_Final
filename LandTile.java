package application;

public class LandTile {
	/** Variable for cost of land */
	private int cost;
	
	/** Variable for type of land (O=Off Limits, C = Company, E= end, U = unowned, �1�= player1 �2� = player2) */
	private char type; 

	/**
	 * Constructor 
	 * @param type The type of land
	 */
	LandTile(char newType){
		type=newType;
		if (type == 'O')
			cost = Integer.MAX_VALUE;
		else
			cost=0;
	}
	
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
	public int changeOwner(int playerBudget, char player) throws Exception {
		if (type=='O')
			throw new Exception("This land is off limits. The cops are coming.");
		else if(type=='E')	
			throw new Exception("You cannot buy the starting or ending position.");
		else if(type=='C')
			throw new Exception("This land is used by the company, you thief.");
		else if(playerBudget<cost)
			throw new Exception("You do not have enough money, you broke peasant.");
		else {
			type=player;
			cost=cost+1;
			return playerBudget-cost;
		}
	}
}

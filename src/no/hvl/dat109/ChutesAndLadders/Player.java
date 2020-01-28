package no.hvl.dat109.ChutesAndLadders;

public class Player {
	final public int playerID;

	private String name;

	private ChutesAndLadders game;

	private int currentTile = 0;

	private Dice myDice = new Dice();

	private boolean lockedAtStart = false;
	
	private GameMonitor gMonitor;
	
	public void setGMonitor(GameMonitor gm) {
		gMonitor = gm;
		gMonitor.MovePlayer(this, 0);
	}

	public Player(ChutesAndLadders game, String name, int id, GameMonitor gm) {
		this.name = name;
		this.game = game;
		this.playerID = id;
		gMonitor = gm;
		if(gMonitor != null) gMonitor.MovePlayer(this, 0);
	}

	/**
	 * Handles the logic for a player round, also calls Movesteps to move player
	 */
	public void DoRound() {
		myDice.ResetDice();

		int diceValue = 6;

		while(diceValue == 6 && game.isPlaying()) {
			diceValue = myDice.Throw();

			gMonitor.ConsolePrint(name + " rolled a " + diceValue);

			if(myDice.getSixCounter() == 3) {
				//do something
				currentTile = 0;
				lockedAtStart = true;
				myDice.ResetDice();
				gMonitor.ConsolePrint(name + " rolled 6 three times");
			} else {
				if(!lockedAtStart || diceValue ==6) {
					MoveSteps(diceValue);
					lockedAtStart = false;
				}
			}
		}
	}

	/**Moves player a number of steps
	 * @param steps - to move the player
	 */
	private void MoveSteps(int steps) {
		
		/////LOGIC FOR THE ANIMATION
		int goalTile = currentTile+steps;	

		//landing above 100 send you back
		if(goalTile > ChutesAndLadders.TILENUMBER) {
			goalTile = ChutesAndLadders.TILENUMBER - (goalTile-ChutesAndLadders.TILENUMBER);
		}
		/////LOGIC FOR THE ANIMATION
		do {
			//note: signum may return 0
			currentTile+= Integer.signum(goalTile-currentTile);
			gMonitor.MovePlayer(this,0.3);
		} while(currentTile != goalTile); 

		//DECLARE Victory! before portal
		if(game.getTileAt(currentTile).isGoal()) {
			game.PrintBoard();
			gMonitor.ConsolePrint(name + " moved to " + currentTile);
			game.Win(this);
			return;
		}

		//Portal Loop, land on one portal after another
		while(game.getTileAt(currentTile).isPortal()) {
			//PortalMove
			boolean isSnake = currentTile >game.getTileAt(currentTile).getPortalDestination();

			gMonitor.ConsolePrint(name + " hit a "+(isSnake?"Snake":"Ladder")+"!  ("+ currentTile+","+game.getTileAt(currentTile).getPortalDestination() +")");
			currentTile = game.getTileAt(currentTile).getPortalDestination();
			gMonitor.MovePlayer(this,1);
		}

		game.PrintBoard();

		gMonitor.ConsolePrint(name + " moved to " + currentTile);
		
		//DECLARE Victory! after portal
		if(game.getTileAt(currentTile).isGoal()) {
			game.Win(this);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCurrentTile() {
		return currentTile;
	}
}

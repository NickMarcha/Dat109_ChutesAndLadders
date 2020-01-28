package no.hvl.dat109.ChutesAndLadders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class ChutesAndLadders extends Thread{

	//Current Board
	private List<Tile> board;
	private List<Player> players;
	
	//Settings for random generation
	final static int TILENUMBER = 100;
	final static double PORTALCHANCE = 0.2;
	private Random rand = new Random();
	
	//just tracking current rounds, does nothing
	private int roundNumber = 0;
	
	final static boolean LOGTOCONSOLE = false;
	//The game loop variable
	private boolean playing = true;
	
	public boolean isPlaying() {
		return playing;
	}
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
	
	GameMonitor gMonitor;
	
	public ChutesAndLadders (GameMonitor mon) {
		gMonitor = mon;
	}
	
	public void run() {

		while(playing) {
			roundNumber++;
			players.forEach(p -> {if(playing) {
				p.DoRound();
				try {
					sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}});
		}
	}
	
	List<Player> templatePlayers = Arrays.asList(
			new Player(this, "Red",0,null),
			new Player(this, "Purple",1,null),
			new Player(this, "Yellow",2,null),
			new Player(this, "Green",3,null),
			new Player(this, "Blue",4,null)
		);
	
	/**
	 * Sets up board based on Ladders and Snakes from the image with a number of template players
	 */
	public void SetupFixed(int nrPlayers) {
		SetupFixedPlayers(nrPlayers);
		SetupFixedBoard();
	}
	/**
	 * Creates players from template
	 */
	private void SetupFixedPlayers(int nrPlayers) {
		players = new ArrayList<Player>();
		for(int i = 0; i < nrPlayers; i ++) {
			templatePlayers.get(i).setGMonitor(gMonitor);
			players.add(templatePlayers.get(i));
		}
	}
	
	/**
	 * Sets up board based on Ladders and Snakes from the image
	 */
	private void SetupFixedBoard() {
		//start
		
		board = new ArrayList<Tile>();
		board.add(new Tile(0,false,false,-1));
		
		HashMap<Integer,Integer> portals = new HashMap<Integer,Integer>(); 
		portals.put(1, 38);
		portals.put(4, 14);
		portals.put(8, 30);
		portals.put(21, 42);
		portals.put(28, 76);
		portals.put(32, 10);
		portals.put(36, 6);
		portals.put(48, 26);
		portals.put(50, 67);
		portals.put(62, 18);
		portals.put(71, 92);
		portals.put(80, 99);
		portals.put(88, 24);
		portals.put(95, 56);
		portals.put(97, 78);
		
		for(int i = 1; i < 100; i++) {
			
			if(portals.containsKey(i)) {
				//Portal
				int portalDestination = portals.get(i);
				board.add(new Tile(i,false,true,portalDestination));
			}else {
				//Regular Tile
				board.add(new Tile(i,false,false,-1));
			}
		}
		//goal
		board.add(new Tile(100,true,false,-1));
		
	}
	public void SetupRandom() {
		
		generateTiles();
		generatePlayers();
	}
	
	public void PrintBoard() {
		
		if(!LOGTOCONSOLE)return;
		
		String StartString = "";
		for(int j = 0; j < players.size(); j++) {
			if(players.get(j).getCurrentTile() == 0) {
				StartString += players.get(j).getName().substring(0,1);
			}
		}
		
		System.out.println("PlayersAtStart:" + StartString);
		
		//loops trough all tiles for print out
		for (int i = 1; i<= TILENUMBER; i++) {
			//gets current tile
			Tile currentTile = getTileAt(i);
			
			
			if(currentTile.isPortal()) {
				//portal
				if(currentTile.getPortalDestination()< 10) {
					System.out.print("[( "+currentTile.getPortalDestination()+")]");
				} else {
					System.out.print("[("+currentTile.getPortalDestination()+")]");
				}
			} else {
				//regular tiles & players
				
				
				boolean printedPlayer = false;
				String playersString = "";
				for(int j = 0; j < players.size(); j++) {
					if(i == players.get(j).getCurrentTile()) {
						playersString += players.get(j).getName().substring(0,1);
						printedPlayer = true;
					}
				}
				
				if(printedPlayer) {
					//prints first letter of players inside tile
					switch(playersString.length()) {
					case 1:
						System.out.print("[  "+playersString +" ]");
						break;
					case 2:
						System.out.print("[ "+playersString +" ]");
						break;
					case 3:
						System.out.print("[ "+playersString +"]");
						break;
					case 4:
						System.out.print("["+playersString +"]");
						break;
					}
					
				}else {
					//regular tile
					System.out.print("[****]");
				}
			}
			
			//makes board square
			if(i %Math.sqrt(TILENUMBER) == 0) {
				System.out.println();
			}
		}
	}
	
	private void generatePlayers() {
		players = new ArrayList<Player>();
		
		players.add(new Player(this, "Arne",0,gMonitor));
		players.add(new Player(this, "Bjarne",1,gMonitor));
		players.add(new Player(this, "Cato",2,gMonitor));
		players.add(new Player(this, "Fatima",3,gMonitor));
	}
	
	private void generateTiles() {
		board = new ArrayList<Tile>();
		//start
		board.add(new Tile(0,false,false,-1));
				
		
		
		for(int i = 1; i < TILENUMBER; i++) {
			
			if(rand.nextDouble() <PORTALCHANCE) {
				//Portal
				int portalDestination = (int) (1 +rand.nextDouble()*(TILENUMBER-2));
				board.add(new Tile(i,false,true,portalDestination));
			}else {
				//Regular Tile
				board.add(new Tile(i,false,false,-1));
			}
		}
		//goal
		board.add(new Tile(TILENUMBER,true,false,-1));
		
	}

	public Tile getTileAt(int tileNumber) {
		return board.get(tileNumber);
	}
	
	public void Win(Player p) {
		gMonitor.ConsolePrint(p.getName() +" Won the game in " + roundNumber + " rounds!");
		playing = false;
		//System.exit(0);
	}
}

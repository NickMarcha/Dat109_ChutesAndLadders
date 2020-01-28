package no.hvl.dat109.ChutesAndLadders;

public class Tile {

	private int tileNumber;
	public int getTileNumber() {
		return tileNumber;
	}

	public boolean isGoal() {
		return isGoal;
	}

	public boolean isPortal() {
		return isPortal;
	}

	public int getPortalDestination() {
		return portalDestination;
	}

	private boolean isGoal;
	private boolean isPortal;
	private int portalDestination;
	
	public Tile(int tileNumber, boolean isGoal, boolean isPortal, int portalDestination) {
		this.tileNumber = tileNumber;
		this.isGoal = isGoal;
		this.isPortal = isPortal;
		this.portalDestination = portalDestination;
	}

	@Override
	public String toString() {
		return "Tile [tileNumber=" + tileNumber + ", isGoal=" + isGoal + ", isPortal=" + isPortal
				+ ", portalDestination=" + portalDestination + "]";
	}
	
}

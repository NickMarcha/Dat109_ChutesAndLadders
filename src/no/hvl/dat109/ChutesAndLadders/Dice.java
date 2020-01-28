package no.hvl.dat109.ChutesAndLadders;

import java.util.Random;

public class Dice {
	private int value = 0;
	private int sixCounter = 0;
	
	/**Retrieves the number of sixes rolled since last reset
	 * @return number of sixes
	 */
	public int getSixCounter() {
		return sixCounter;
	}

	private Random rand = new Random();
	
	/**
	 * Sets sixcounter and dicevalue to zero
	 */
	public void ResetDice() {
		sixCounter = 0;
		value = 0;
	}
	
	/**Throws dice and increases six counter if a six was rolled
	 * @return diceValue
	 */
	public int Throw() {
		value = (int) Math.round((1+rand.nextDouble()*5));
		if(value == 6) sixCounter++;
		return value;
	}	
	/**Retrieves dice value without rolling dice
	 * @return diceValue
	 */
	public int getValue() {
		return value;
	}
}

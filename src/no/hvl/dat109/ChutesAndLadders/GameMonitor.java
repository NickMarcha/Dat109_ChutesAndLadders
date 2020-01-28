package no.hvl.dat109.ChutesAndLadders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GameMonitor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int B_WIDTH = 500;
	private final int B_HEIGHT = 600;


	private Image background;
	private List<Image> players = new ArrayList<Image>();

	int[] playerx = new int[]{0,0,0,0,0};
	int[] playery = new int[]{0,0,0,0,0};

	public GameMonitor() {
		initBoard();
	}

	private void initBoard() {

		setBackground(Color.white);
		setFocusable(true);

		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		loadImages();
		initGame();
	}

	private void loadImages() {

		ImageIcon iid = new ImageIcon(getScaledImage(new ImageIcon("res/img/board.png").getImage(),500,500));
		background = iid.getImage();

		ImageIcon ii0 = new ImageIcon(getScaledImage(new ImageIcon("res/img/bear_0.png").getImage(),35,40));
		players.add(ii0.getImage());

		ImageIcon ii1 = new ImageIcon(getScaledImage(new ImageIcon("res/img/bear_1.png").getImage(),35,40));
		players.add(ii1.getImage());

		ImageIcon ii2 = new ImageIcon(getScaledImage(new ImageIcon("res/img/bear_2.png").getImage(),35,40));
		players.add(ii2.getImage());

		ImageIcon ii3 = new ImageIcon(getScaledImage(new ImageIcon("res/img/bear_3.png").getImage(),35,40));
		players.add(ii3.getImage());

		ImageIcon ii4 = new ImageIcon(getScaledImage(new ImageIcon("res/img/bear_4.png").getImage(),35,40));
		players.add(ii4.getImage());
	}

	private Image getScaledImage(Image srcImg, int w, int h){
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}

	private void initGame() {
		ChutesAndLadders cl = new ChutesAndLadders(this);
		cl.SetupFixed(5);

		((Thread)cl).start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	public void MovePlayer(Player p, double time) {
		//doDrawing();

		int playerID = p.playerID;

		int newplayerx = 0;
		int newplayery = 0;

		if(p.getCurrentTile() != 0) {

			if((int) Math.floor(((double)p.getCurrentTile()-1) * 0.1) %2 == 0) {
				//rightgoing
				newplayerx = 20+ (((p.getCurrentTile()-1)%10) *46) +(-5+(playerID*2));
			}else {
				//leftgoing
				newplayerx = (480)-((((p.getCurrentTile()-1)%10)+1) *45)+(-5+(playerID*2));
			}

			newplayery =  435- (int) Math.floor(((double)p.getCurrentTile()-1) * 0.1)*45;
		}else {
			newplayerx = 20 + (playerID *6);
			newplayery = 520;
		}

		for(double i = 0; i < 1; i+= (time== 0)?1:1/(time*500)/Main.PLAYSPEED) {

			playerx[playerID] = lerp(playerx[playerID], newplayerx,i);
			playery[playerID] = lerp(playery[playerID], newplayery,i);
			repaint();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		playerx[playerID] = lerp(playerx[playerID],newplayerx,1);
		playery[playerID] = lerp(playery[playerID],newplayery,1);
		repaint();
	}

	private int lerp(int a, int b, double f)
	{
		return (int)(((double)a * (1.0 - f)) + ((double)b * f));
	}

	private List<String> ConsoleOutput = new ArrayList<String>();
	private List<Long> ConsoleTime = new ArrayList<Long>();
	//five seconds max on console output
	long maxTime = 5000;

	public void ConsolePrint(String msg) {
		ConsoleOutput.add(msg);
		ConsoleTime.add(System.currentTimeMillis());
	}

	private void doDrawing(Graphics g) {

		Font small = new Font("Helvetica", Font.BOLD, 14);

		g.setColor(Color.black);
		g.setFont(small);
		
		while(ConsoleOutput.size() > 5) {
			ConsoleOutput.remove(0);
			ConsoleTime.remove(0);
		}

		for(int i = Math.min(ConsoleOutput.size()-1, 5); i >= 0; i--){
			if(ConsoleTime.get(i) + maxTime < System.currentTimeMillis()) {
				ConsoleOutput.remove(i);
				ConsoleTime.remove(i);

			} else {
				int c = (int) Math.min(255,(System.currentTimeMillis()-ConsoleTime.get(i))*0.05);
				g.setColor(new Color(c, c, c));
				g.drawString(ConsoleOutput.get(i), 300, 596 - ((Math.min(ConsoleOutput.size()-1, 5)-i)*14));
			}
		}

		g.drawImage(background, 0, 0, this);

		for(int i = 0; i < players.size(); i++) {
			g.drawImage(players.get(i), playerx[i], playery[i], this);
		}
		Toolkit.getDefaultToolkit().sync();
	}
}
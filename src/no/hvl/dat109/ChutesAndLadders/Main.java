package no.hvl.dat109.ChutesAndLadders;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Main extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final double PLAYSPEED = 0.5;

	public Main() {
		initUI();
	}

	private void initUI() {
		add(new GameMonitor());
		setResizable(false);
		pack();
		setTitle("ChutesAndLadders");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			JFrame ex = new Main();
			ex.setVisible(true);
		});
	}
}
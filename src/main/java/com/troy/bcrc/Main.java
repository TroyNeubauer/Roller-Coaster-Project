package com.troy.bcrc;

import javax.swing.JFrame;

import com.troy.bcrc.ui.CoordinatePlane;

public class Main {

	private static final int UPDATES_PER_SECOND = 600;
	private static final long NANOS_PER_UPDATE = 1_000_000_000 / 60;

	public static void main(String[] args) {
		JFrame frame = new JFrame("Troy's Math");
		CoordinatePlane plane = new CoordinatePlane();
		frame.add(plane);

		frame.addMouseListener(plane);
		frame.addMouseWheelListener(plane);
		frame.addMouseMotionListener(plane);
		frame.addKeyListener(Keyboard.LISTENER);

		frame.setSize(1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		long nextUpdate = System.nanoTime() + NANOS_PER_UPDATE;
		long last = System.nanoTime();
		while (frame.isDisplayable()) {
			long end = MiscUtil.sleepUntil(nextUpdate);
			double delta = (end - last) / 1_000_000_000.0;
			plane.update(delta);

			nextUpdate += NANOS_PER_UPDATE;
			last = end;
		}
	}

}

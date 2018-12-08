package com.troy.bcrc;

import javax.swing.JFrame;

import com.troy.bcrc.ui.CoordinatePlane;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Troy's Math");
		CoordinatePlane plane = new CoordinatePlane();
		frame.add(plane);

		frame.addMouseListener(plane);
		frame.addMouseWheelListener(plane);

		frame.setSize(1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

}

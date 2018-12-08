package com.troy.bcrc;

import java.awt.event.*;

public class Keyboard implements KeyListener {
	private static boolean[] keys = new boolean[Short.MAX_VALUE];

	public static final Keyboard LISTENER = new Keyboard();

	private Keyboard() {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	public static boolean isKeyDown(int keyCode) {
		return keys[keyCode];
	}

}

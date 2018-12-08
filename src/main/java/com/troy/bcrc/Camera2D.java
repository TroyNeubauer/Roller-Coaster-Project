package com.troy.bcrc;

public class Camera2D {
	private double x, y, width, height;// Where width is 1/2 of the viewport so left = x - width

	public Camera2D(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public double getLeft() {
		return x - width;
	}

	public double getRight() {
		return x + width;
	}

	public double getTop() {
		return y + height;
	}

	public double getBottom() {
		return y - height;
	}

	public double getCenterX() {
		return x;
	}

	public double getCenterY() {
		return y;
	}

	public double totalWidth() {
		return 2.0 * width;
	}

	public double totalHeight() {
		return 2.0 * height;
	}

	public void zoom(double zoom) {// + means in - means out
		double mult = Math.pow(2, -zoom);
		width *= mult;
		height *= mult;
	}

	public void move(double realX, double realY) {
		x += realX;
		y += realY;
		System.out.println("x " + realX);
	}

}

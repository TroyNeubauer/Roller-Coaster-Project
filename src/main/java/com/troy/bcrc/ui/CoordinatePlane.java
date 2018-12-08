package com.troy.bcrc.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

import com.troy.bcrc.Maths;

public class CoordinatePlane extends JPanel implements MouseListener, MouseWheelListener {
	private double left, right, top, bottom;

	private static final int MIN_PX = 2;
	private static final double ZOOM_SENSITIVITY = 0.01;

	public CoordinatePlane() {
		this(-10, 10, 7, -7);
	}

	public CoordinatePlane(double left, double right, double top, double bottom) {
		setBackground(Color.WHITE);
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

	public int pixelCountX(double xDistance) {
		int pixelWidth = getWidth();
		double currentUnits = right - left;
		double pixelsPerUint = pixelWidth / currentUnits;
		if (Double.isFinite(pixelsPerUint)) {
			long value = Math.round(pixelsPerUint * xDistance);
			if (value > Integer.MAX_VALUE) {
				return Integer.MAX_VALUE;
			} else if (value < Integer.MIN_VALUE) {
				return Integer.MIN_VALUE;
			} else {
				return (int) value;
			}
		} else
			return 0;
	}

	public int pixelCountY(double yDistance) {
		int pixelHeight = getHeight();
		double currentUnits = top - bottom;
		double pixelsPerUint = pixelHeight / currentUnits;
		if (Double.isFinite(pixelsPerUint)) {
			long value = Math.round(pixelsPerUint * yDistance);
			if (value > Integer.MAX_VALUE) {
				return Integer.MAX_VALUE;
			} else if (value < Integer.MIN_VALUE) {
				return Integer.MIN_VALUE;
			} else {
				return (int) value;
			}
		} else
			return 0;
	}

	public int getPixelX(double x) {
		return (int) Math.round(Maths.map(x, left, right, 0, getWidth()));
	}

	public int getPixelY(double y) {
		return (int) Math.round(Maths.map(y, top, bottom, 0, getHeight()));
	}

	public double getRealX(int px) {
		return Maths.map(px, 0, getWidth(), left, right);
	}

	public double getRealY(int py) {
		return Maths.map(py, 0, getHeight(), top, bottom);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		drawLines(g);
		drawPoints(g);
		drawFunctions(g);

	}

	public void drawLines(Graphics g) {
		Color light = Color.LIGHT_GRAY, dark = Color.BLACK;

		int exp = doIterationX(-30);
		double step = Math.pow(10, exp);
		g.setColor(light);
		for (double x = left; x <= right; x += step) {
			int px = getPixelX(x);
			g.drawLine(px, 0, px, getHeight());
		}
		exp = doIterationX(exp);
		step = Math.pow(10, exp);
		g.setColor(dark);
		for (double x = left; x <= right; x += step) {
			int px = getPixelX(x);
			g.drawLine(px, 0, px, getHeight());
		}

		exp = doIterationY(-30);
		step = Math.pow(10, exp);
		g.setColor(light);
		for (double y = bottom; y <= top; y += step) {
			int py = getPixelY(y);
			g.drawLine(0, py, getWidth(), py);
		}
		exp = doIterationX(exp);
		step = Math.pow(10, exp);
		g.setColor(dark);
		for (double y = bottom; y <= top; y += step) {
			int py = getPixelY(y);
			g.drawLine(0, py, getWidth(), py);
		}
	}

	private int doIterationX(int exp) {
		double value;
		int pixels;
		do {
			value = Math.pow(10.0, exp);
			pixels = pixelCountX(value);
			exp++;
		} while (pixels < MIN_PX && exp < 30);
		return exp;
	}

	private int doIterationY(int exp) {
		double value;
		int pixels;
		do {
			value = Math.pow(10.0, exp);
			pixels = pixelCountY(value);
			exp++;
		} while (pixels < MIN_PX && exp < 30);
		return exp;
	}

	public void drawFunction(UnivariateFunction function, Color color, Graphics g, int xStep) {
		g.setColor(color);
		if (xStep == 0) {
			for (int x = 0; x < getWidth(); x++) {
				double rx = getRealX(x);
				double ry = function.value(rx);
				int py = getPixelY(ry);
				g.drawRect(x, py, 1, 1);

			}
		} else {
			int prevX = Integer.MIN_VALUE, prevY = 0;
			for (int x = -xStep; x <= getWidth() + xStep; x += xStep) {
				double rx = getRealX(x);
				double ry = function.value(rx);
				int py = getPixelY(ry);
				if (prevX != Integer.MAX_VALUE) {
					g.drawLine(prevX, prevY, x, py);
				}
				prevX = x;
				prevY = py;
			}
		}
	}

	private void zoom(int count) {
		System.out.println("before " + left);
		double cx = (left + right) / 2.0;
		double cy = (top + bottom) / 2.0;
		for (int i = 0; i < Math.abs(count); i++) {
			
			left += ZOOM_SENSITIVITY * (count < 0 ? -1 : +1) * Math.abs(cx - left);
			right += ZOOM_SENSITIVITY * (count < 0 ? -1 : +1) * Math.abs(cx - right);
			top += ZOOM_SENSITIVITY * (count < 0 ? -1 : +1) * Math.abs(cx - top);
			bottom += ZOOM_SENSITIVITY * (count < 0 ? -1 : +1) * Math.abs(cx - bottom);
		}
		System.out.println("after " + left);
	}

	private void drawPoints(Graphics g) {
	}

	private void drawFunctions(Graphics g) {
		PolynomialFunction function = new PolynomialFunction(new double[] { 2, 0, 6, 2, -1 });
		drawFunction(function, Color.GREEN, g, 1);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		zoom(1);
		System.out.println("press");
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		System.out.println(e.getWheelRotation());
	}
}

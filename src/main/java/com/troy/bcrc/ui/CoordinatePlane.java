package com.troy.bcrc.ui;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

import com.troy.bcrc.*;

public class CoordinatePlane extends JPanel implements MouseInputListener, MouseWheelListener, KeyListener {
	private Camera2D camera;

	private static final int MIN_PX = 2;
	private static final double ZOOM_SENSITIVITY = 0.01;
	private static final DecimalFormat df = new DecimalFormat("#.###");

	public CoordinatePlane() {
		this(0, 0, 8, 4.5);
	}

	public CoordinatePlane(double x, double y, double width, double height) {
		setBackground(Color.WHITE);
		this.camera = new Camera2D(x, y, width, height);
	}

	public int pixelCountX(double xDistance) {
		int pixelWidth = getWidth();
		double pixelsPerUint = pixelWidth / camera.totalWidth();
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
		double pixelsPerUint = pixelHeight / camera.totalHeight();
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
		return (int) Math.round(Maths.map(x, camera.getLeft(), camera.getRight(), 0, getWidth()));
	}

	public int getPixelY(double y) {
		return (int) Math.round(Maths.map(y, camera.getTop(), camera.getBottom(), 0, getHeight()));
	}

	public double getRealX(int px) {
		return Maths.map(px, 0, getWidth(), camera.getLeft(), camera.getRight());
	}

	public double getRealY(int py) {
		return Maths.map(py, 0, getHeight(), camera.getTop(), camera.getBottom());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		//drawLines(g);
		drawPoints(g);
		drawFunctions(g);

	}

	public void drawLines(Graphics g) {
		Color light = Color.LIGHT_GRAY, dark = Color.BLACK;

		int exp = doIterationX(-30);
		double step = Math.pow(10, exp);
		g.setColor(light);
		for (double x = Math.floor(camera.getLeft() / step); x <= camera.getRight(); x += step) {
			if (x > camera.getRight())
				break;
			int px = getPixelX(x);
			g.drawLine(px, 0, px, getHeight());
			drawLabelX(px, x, g);

		}

	}

	private void drawLabelX(int px, double label, Graphics g) {
		Color o = g.getColor();
		g.setColor(Color.BLACK);
		int py = getPixelY(camera.getCenterY());
		if (py < -10 || py > getHeight()) {
			py = 2;
		}
		g.drawString(df.format(label), px, py);
		g.setColor(o);
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

	private void drawPoints(Graphics g) {
	}

	private void drawFunctions(Graphics g) {
		PolynomialFunction function = new PolynomialFunction(new double[] { 2, 0, 6, 2, -1 });
		drawFunction(function, Color.GREEN, g, 1);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	double ox, oy;

	@Override
	public void mousePressed(MouseEvent e) {
		ox = getRealX(e.getX());
		oy = getRealY(e.getY());
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
		camera.zoom(e.getWheelRotation() * ZOOM_SENSITIVITY);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		double nx = getRealX(e.getX()), ny = getRealY(e.getY());
		camera.move(ox - nx, oy - ny);
		repaint();
		ox = nx;
		oy = ny;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println("typed");
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}

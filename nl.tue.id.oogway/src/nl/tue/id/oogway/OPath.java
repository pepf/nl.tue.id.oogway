/*
 This file is part of Oogway.
 
 Oogway is free software: you can redistribute it and/or modify
 it under the terms of the GNU Lesser General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Oogway is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with PTurtle.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.tue.id.oogway;

import java.util.Hashtable;
import processing.core.PApplet;
import static processing.core.PApplet.*;
import processing.core.PShape;

// TODO: Auto-generated Javadoc
/**
 * The Class Path.
 */
public class OPath {

	/** The applet. */
	PApplet applet;

	/** The closed. */
	boolean closed = false;

	private static Hashtable<String, PShape> shapes = new Hashtable<String, PShape>();

	private PShape shape = null;
	private float x = 0;
	private float y = 0;
	private float distance = 0;
	private float headingRad = 0;
	private int reflect = 1;

	/**
	 * Instantiates a new path.
	 * 
	 * @param applet
	 *            the applet
	 */
	public OPath(PApplet applet) {
		this.applet = applet;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Path [applet=" + applet + ", closed=" + closed + ", vertices="
				+ shape.getVertexCount() + "]";
	}

	/**
	 * Instantiates a new path.
	 * 
	 * @param applet
	 *            the applet
	 * @param path
	 *            the path
	 */
	public OPath(PApplet applet, String path) {

		this.applet = applet;
		if (!loadPath(path)) {
			System.err
					.println("Oogway was not able to open or parse the svg file. \r\n"
							+ "Oogway supports SVG files created with Inkscape and Adobe Illustrator. "
							+ "The SVG file should contain simply one path. No other shapes are supported.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public OPath clone() {
		OPath path = new OPath(applet);
		path.copy(this);
		return path;

	}

	/**
	 * Copy.
	 * 
	 * @param path
	 *            the path
	 */
	protected void copy(OPath path) {
		this.applet = path.applet;
		this.shape = path.shape;
		this.x = path.x;
		this.y = path.y;
		this.distance = path.distance;
		this.headingRad = path.headingRad;
		this.reflect = path.reflect;
	}

	/**
	 * Draw.
	 */
	public void draw() {
		boolean drawable = true;

		if (shape == null || distance < EPSILON)
			drawable = false;
		if (shape != null)
			if (shape.getVertexCount() < 2)
				drawable = false;

		if (!drawable) {
			applet.line(x, y, x + distance * cos(headingRad), y + distance
					* sin(headingRad));
			return;
		}

		float startx = shape.getVertexX(0);
		float starty = shape.getVertexY(0);

		float endx = shape.getVertexX(shape.getVertexCount() - 1);
		float endy = shape.getVertexY(shape.getVertexCount() - 1);

		float d = sqrt(pow(endx - startx, 2) + pow(endy - starty, 2));

		if (d < EPSILON) {
			System.err
					.println("Starting and ending points are too close in the path. ");
			return;
		}

		float s = distance / d;

		applet.pushMatrix();
		applet.translate(x, y);
		applet.rotate(headingRad);
		applet.scale(s, s * reflect);
		applet.rotate(-atan2(endy - starty, endx - startx));
		applet.translate(-startx, -starty);
		shape.draw(applet.g);
		if (applet.recorder != null)
			shape.draw(applet.recorder);
		applet.popMatrix();
	}

	/**
	 * Try svg file.
	 * 
	 * @param filename
	 *            the filename
	 * @return the string
	 */
	public boolean loadPath(String filename) {
		filename = filename.trim();

		PShape s = null;

		if (shapes.containsKey(filename)) {
			s = shapes.get(filename);
		} else {

			try {
				s = applet.loadShape(filename);
			} catch (Exception e) {
				return false;
			}

			if (s == null)
				return false;

			int count = 0;

			count = s.getVertexCount();
			while (count < 2) {
				if (s.getChildCount() == 0)
					break;
				for (int i = 0; i < s.getChildCount(); i++) {
					s = s.getChild(i);
					count = s.getVertexCount();
					if (count >= 2)
						break;
				}
			}

			if (count < 2)
				return false;

			shapes.put(filename, s);
		}

		this.shape = s;

		return true;
	}

	/**
	 * Transform.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param distance
	 *            the distance
	 * @param headingRad
	 *            the heading rad
	 * @param reflect
	 *            the reflect
	 */
	public void transform(float x, float y, float distance, float headingRad,
			int reflect) {
		this.x = x;
		this.y = y;
		this.distance = distance;
		this.headingRad = headingRad;
		this.reflect = reflect;

	}

}
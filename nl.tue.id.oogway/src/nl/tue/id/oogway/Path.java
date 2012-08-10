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

import java.util.Vector;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.xml.XMLElement;

// TODO: Auto-generated Javadoc
/**
 * The Class Path.
 */
public class Path {

	/** The applet. */
	PApplet applet;

	/** The closed. */
	boolean closed = false;

	/** The vertices. */
	Vector<float[]> vertices = new Vector<float[]>();

	/**
	 * Instantiates a new path.
	 * 
	 * @param applet
	 *            the applet
	 */
	public Path(PApplet applet) {
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
				+ vertices + "]";
	}

	/**
	 * Instantiates a new path.
	 * 
	 * @param applet
	 *            the applet
	 * @param path
	 *            the path
	 */
	public Path(PApplet applet, String path) {

		this.applet = applet;
		loadPath(path);
	}

	/**
	 * Clear.
	 */
	public void clear() {
		vertices.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Path clone() {
		Path path = new Path(applet);
		path.copy(this);
		return path;

	}

	/**
	 * Copy.
	 * 
	 * @param path
	 *            the path
	 */
	protected void copy(Path path) {
		this.applet = path.applet;
		this.clear();
		for (int i = 0; i < path.vertices.size(); i++) {
			float a[] = (float[]) path.vertices.get(i);
			float b[] = { a[0], a[1] };
			vertices.add(b);
		}
	}

	/**
	 * Draw.
	 */
	public void draw() {

		if (vertices.size() < 4)
			return;

		applet.beginShape();
		float start[] = (float[]) vertices.get(0);
		applet.vertex(start[0], start[1]);
		for (int i = 1; i < vertices.size(); i += 3) {
			float a[] = (float[]) vertices.get(i);
			float b[] = (float[]) vertices.get(i + 1);
			float e[] = (float[]) vertices.get(i + 2);

			applet.bezierVertex(a[0], a[1], b[0], b[1], e[0], e[1]);

		}

		if (closed)
			applet.endShape(PConstants.CLOSE);
		else
			applet.endShape();
	}

	/**
	 * Load path.
	 * 
	 * @param path
	 *            the path
	 */
	protected void loadPath(String path) {

		this.clear();

		StringBuffer pathChars = new StringBuffer();

		String pathDataBuffer = trySvgFile(path); // try whether "path" is a SVG
													// file;

		if (pathDataBuffer == null)
			pathDataBuffer = path;

		boolean lastSeperate = false;

		for (int i = 0; i < pathDataBuffer.length(); i++) {
			char c = pathDataBuffer.charAt(i);
			boolean seperate = false;

			if (c == 'M' || c == 'm' || c == 'L' || c == 'l' || c == 'H'
					|| c == 'h' || c == 'V' || c == 'v' || c == 'C' || c == 'c'
					|| c == 'S' || c == 's' || c == ',' || c == 'Z' || c == 'z') {
				seperate = true;
				if (i != 0)
					pathChars.append("|");
			}
			if (c == 'Z' || c == 'z')
				seperate = false;
			if (c == '-' && !lastSeperate) {
				pathChars.append("|");
			}
			if (c != ',')
				pathChars.append("" + pathDataBuffer.charAt(i));
			if (seperate && c != ',' && c != '-')
				pathChars.append("|");
			lastSeperate = seperate;
		}

		pathDataBuffer = pathChars.toString();

		String pathDataKeys[] = PApplet.split(pathDataBuffer, '|');

		float cp[] = { 0, 0 };

		for (int i = 0; i < pathDataKeys.length; i++) {
			char c = pathDataKeys[i].charAt(0);
			switch (c) {
			// M - move to (absolute)
			case 'M': {
				cp[0] = valueOf(pathDataKeys[i + 1]);
				cp[1] = valueOf(pathDataKeys[i + 2]);
				float s[] = { cp[0], cp[1] };
				i += 2;
				vertices.add(s);
			}
				break;
			// m - move to (relative)
			case 'm': {
				cp[0] = cp[0] + valueOf(pathDataKeys[i + 1]);
				cp[1] = cp[1] + valueOf(pathDataKeys[i + 2]);
				float s[] = { cp[0], cp[1] };
				i += 2;
				vertices.add(s);
			}
				break;
			// C - curve to (absolute)
			case 'C': {
				float curvePA[] = { valueOf(pathDataKeys[i + 1]),
						valueOf(pathDataKeys[i + 2]) };
				float curvePB[] = { valueOf(pathDataKeys[i + 3]),
						valueOf(pathDataKeys[i + 4]) };
				float endP[] = { valueOf(pathDataKeys[i + 5]),
						valueOf(pathDataKeys[i + 6]) };
				cp[0] = endP[0];
				cp[1] = endP[1];
				i += 6;
				vertices.add(curvePA);
				vertices.add(curvePB);
				vertices.add(endP);
			}
				break;
			// c - curve to (relative)
			case 'c': {
				float curvePA[] = { cp[0] + valueOf(pathDataKeys[i + 1]),
						cp[1] + valueOf(pathDataKeys[i + 2]) };
				float curvePB[] = { cp[0] + valueOf(pathDataKeys[i + 3]),
						cp[1] + valueOf(pathDataKeys[i + 4]) };
				float endP[] = { cp[0] + valueOf(pathDataKeys[i + 5]),
						cp[1] + valueOf(pathDataKeys[i + 6]) };
				cp[0] = endP[0];
				cp[1] = endP[1];
				i += 6;
				vertices.add(curvePA);
				vertices.add(curvePB);
				vertices.add(endP);
			}
				break;
			// S - curve to shorthand (absolute)
			case 'S': {
				float lastPoint[] = (float[]) vertices.get(vertices.size() - 1);
				float lastLastPoint[] = (float[]) vertices
						.get(vertices.size() - 2);
				float curvePA[] = { cp[0] + (lastPoint[0] - lastLastPoint[0]),
						cp[1] + (lastPoint[1] - lastLastPoint[1]) };
				float curvePB[] = { valueOf(pathDataKeys[i + 1]),
						valueOf(pathDataKeys[i + 2]) };
				float e[] = { valueOf(pathDataKeys[i + 3]),
						valueOf(pathDataKeys[i + 4]) };
				cp[0] = e[0];
				cp[1] = e[1];
				vertices.add(curvePA);
				vertices.add(curvePB);
				vertices.add(e);
				i += 4;
			}
				break;
			// s - curve to shorthand (relative)
			case 's': {
				float lastPoint[] = (float[]) vertices.get(vertices.size() - 1);
				float lastLastPoint[] = (float[]) vertices
						.get(vertices.size() - 2);
				float curvePA[] = { cp[0] + (lastPoint[0] - lastLastPoint[0]),
						cp[1] + (lastPoint[1] - lastLastPoint[1]) };
				float curvePB[] = { cp[0] + valueOf(pathDataKeys[i + 1]),
						cp[1] + valueOf(pathDataKeys[i + 2]) };
				float e[] = { cp[0] + valueOf(pathDataKeys[i + 3]),
						cp[1] + valueOf(pathDataKeys[i + 4]) };
				cp[0] = e[0];
				cp[1] = e[1];
				vertices.add(curvePA);
				vertices.add(curvePB);
				vertices.add(e);
				i += 4;
			}
				break;
			case 'Z':
				closed = true;
				break;
			case 'z':
				closed = true;
				break;
			}
		}
	}

	/**
	 * Move to.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void moveTo(float x, float y) {

		if (vertices.isEmpty())
			return;

		float start[] = (float[]) vertices.get(0);
		float offset[] = { x - start[0], y - start[1] };

		for (int i = 0; i < vertices.size(); i++) {
			float a[] = (float[]) vertices.get(i);

			a[0] = a[0] + offset[0];
			a[1] = a[1] + offset[1];
		}
	}

	/**
	 * Place along x.
	 */
	public void placeAlongX() {

		if (vertices.size() < 4)
			return;

		moveTo(0, 0);
		float end[] = (float[]) vertices.get(vertices.size() - 1);
		rotateRad(-PApplet.atan2(end[1], end[0])); // rotate to X axis
	}

	/**
	 * Reflect in x.
	 */
	public void reflectInX() {
		for (int i = 0; i < vertices.size(); i++) {
			float a[] = (float[]) vertices.get(i);
			a[1] = -a[1];
		}
	}

	/**
	 * Rotate rad.
	 * 
	 * @param rotRad
	 *            the rot rad
	 */
	protected void rotateRad(float rotRad) {
		for (int i = 0; i < vertices.size(); i++) {
			float a[] = (float[]) vertices.get(i);

			float x = a[0], y = a[1];
			float sin = PApplet.sin(rotRad), cos = PApplet.cos(rotRad);

			a[0] = x * cos - y * sin;
			a[1] = x * sin + y * cos;
		}
	}

	/**
	 * Scale to.
	 * 
	 * @param size
	 *            the size
	 */
	public void scaleTo(float size) {

		if (vertices.size() < 4)
			return;

		float start[] = (float[]) vertices.get(0);
		float end[] = (float[]) vertices.get(vertices.size() - 1);
		float distance = PApplet.sqrt(PApplet.pow(end[0] - start[0], 2)
				+ PApplet.pow(end[1] - start[1], 2));

		for (int i = 0; i < vertices.size(); i++) {
			float a[] = (float[]) vertices.get(i);

			a[0] = a[0] * size / distance;
			a[1] = a[1] * size / distance;
		}
	}

	/**
	 * Try svg file.
	 * 
	 * @param filename
	 *            the filename
	 * @return the string
	 */
	public String trySvgFile(String filename) {
		filename = filename.trim();

		if (filename.length() < 4)
			return null;

		if (filename.length() > 4) {
			if (!filename.substring(filename.length() - 4).equalsIgnoreCase(
					".svg")) {
				return null;
			}
		}

		XMLElement xml = null;
		try {
			xml = new XMLElement(applet, filename);
		} catch (Exception e) {
			PApplet.print(e);
			return null;
		}
		int n = xml.getChildCount();
		for (int i = 0; i < n; i++) {
			XMLElement kid = xml.getChild(i);
			String name = kid.getName().trim();
			if (name.equalsIgnoreCase("path")) {
				return kid.getString("d");
			}
		}
		return null;
	}

	// Converts a string to a float
	/**
	 * Value of.
	 * 
	 * @param s
	 *            the s
	 * @return the float
	 */
	private float valueOf(String s) {
		return Float.valueOf(s).floatValue();
	}

}
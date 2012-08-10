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

public class Path {

	Vector<float[]> points = new Vector<float[]>();
	PApplet applet;

	boolean closed = false;

	public Path(PApplet applet, String pathString) {

		this.applet = applet;
		StringBuffer pathChars = new StringBuffer();

		String pathDataBuffer = trySvgFile(pathString); // try whether "path" is a SVG
													// file;

		if (pathDataBuffer == null)
			pathDataBuffer = pathString;

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
				points.add(s);
			}
				break;
			// m - move to (relative)
			case 'm': {
				cp[0] = cp[0] + valueOf(pathDataKeys[i + 1]);
				cp[1] = cp[1] + valueOf(pathDataKeys[i + 2]);
				float s[] = { cp[0], cp[1] };
				i += 2;
				points.add(s);
			}
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
				points.add(curvePA);
				points.add(curvePB);
				points.add(endP);
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
				points.add(curvePA);
				points.add(curvePB);
				points.add(endP);
			}
				break;
			// S - curve to shorthand (absolute)
			case 'S': {
				float lastPoint[] = (float[]) points.get(points.size() - 1);
				float lastLastPoint[] = (float[]) points.get(points.size() - 2);
				float curvePA[] = { cp[0] + (lastPoint[0] - lastLastPoint[0]),
						cp[1] + (lastPoint[1] - lastLastPoint[1]) };
				float curvePB[] = { valueOf(pathDataKeys[i + 1]),
						valueOf(pathDataKeys[i + 2]) };
				float e[] = { valueOf(pathDataKeys[i + 3]),
						valueOf(pathDataKeys[i + 4]) };
				cp[0] = e[0];
				cp[1] = e[1];
				points.add(curvePA);
				points.add(curvePB);
				points.add(e);
				i += 4;
			}
				break;
			// s - curve to shorthand (relative)
			case 's': {
				float lastPoint[] = (float[]) points.get(points.size() - 1);
				float lastLastPoint[] = (float[]) points.get(points.size() - 2);
				float curvePA[] = { cp[0] + (lastPoint[0] - lastLastPoint[0]),
						cp[1] + (lastPoint[1] - lastLastPoint[1]) };
				float curvePB[] = { cp[0] + valueOf(pathDataKeys[i + 1]),
						cp[1] + valueOf(pathDataKeys[i + 2]) };
				float e[] = { cp[0] + valueOf(pathDataKeys[i + 3]),
						cp[1] + valueOf(pathDataKeys[i + 4]) };
				cp[0] = e[0];
				cp[1] = e[1];
				points.add(curvePA);
				points.add(curvePB);
				points.add(e);
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
	
	public Path(Path path) {
		this.applet = path.applet;
		for (int i = 0; i < path.points.size(); i++) {
			float a[] = (float[]) path.points.get(i);
			float b[] = {a[0], a[1]};
			points.add(b);
		}
	}


	public String trySvgFile(String filename) {
		filename = filename.trim();
		if (filename.length()>4){
			if(!filename.substring(filename.length()-4).equalsIgnoreCase(".svg")){
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

	public void moveTo(float x, float y) {
		float start[] = (float[]) points.get(0);
		float offset[] = { x - start[0], y - start[1] };

		for (int i = 0; i < points.size(); i++) {
			float a[] = (float[]) points.get(i);

			a[0] = a[0] + offset[0];
			a[1] = a[1] + offset[1];
		}
	}

	public void placeAlongX() {
		moveTo(0, 0);
		float end[] = (float[]) points.get(points.size() - 1);
		rotateRad(-PApplet.atan2(end[1], end[0])); // rotate to X axis
	}
	
	public void reflectInX(){
		for (int i = 0; i < points.size(); i++) {
			float a[] = (float[]) points.get(i);
			a[1] = -a[1];	
		}
	}

	public void scaleTo(float size) {
		float start[] = (float[]) points.get(0);
		float end[] = (float[]) points.get(points.size() - 1);
		float distance = PApplet.sqrt(PApplet.pow(end[0] - start[0], 2)
				+ PApplet.pow(end[1] - start[1], 2));

		for (int i = 0; i < points.size(); i++) {
			float a[] = (float[]) points.get(i);

			a[0] = a[0] * size / distance;
			a[1] = a[1] * size / distance;
		}
	}

	protected void rotateRad(float rotRad) {

		for (int i = 0; i < points.size(); i++) {
			float a[] = (float[]) points.get(i);

			float x = a[0], y = a[1];
			float sin = PApplet.sin(rotRad), cos = PApplet.cos(rotRad);

			a[0] = x * cos - y * sin;
			a[1] = x * sin + y * cos;
		}
	}

	public float getEndRotation() {

		float e[] = (float[]) points.get(points.size() - 1);
		float c[] = (float[]) points.get(points.size() - 4); // control point
		//float c[] = (float[]) points.get(points.size() - 2); // control point ?
		return PApplet.degrees(PApplet.atan2(e[1] - c[1], e[0] - c[0]));
	}

	public float[] getEnd() {
		return (float[]) points.get(points.size() - 1);
	}

	public float getStartRotation() {
		float c[] = (float[]) points.get(3); // control point 
		//float c[] = (float[]) points.get(1); // control point ? 
		float e[] = (float[]) points.get(0);
		return PApplet.degrees(PApplet.atan2(c[1] - e[1], c[0] - e[0]));
	}

	public void draw() {
		// draw the path
		applet.beginShape();
		float start[] = (float[]) points.get(0);
		applet.vertex(start[0], start[1]);
		for (int i = 1; i < points.size(); i += 3) {
			float a[] = (float[]) points.get(i);
			float b[] = (float[]) points.get(i + 1);
			float e[] = (float[]) points.get(i + 2);

			applet.bezierVertex(a[0], a[1], b[0], b[1], e[0], e[1]);

		}

		if (closed)
			applet.endShape(PConstants.CLOSE);
		else
			// p.beginShape();
			applet.endShape();
	}

	public Vector<float[]> getPoints() {
		return points;
	}

	// Converts a string to a float
	private float valueOf(String s) {
		return Float.valueOf(s).floatValue();
	}

}
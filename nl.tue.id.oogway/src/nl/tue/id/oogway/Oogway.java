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
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PShape;
import static processing.core.PApplet.*;

// TODO: Auto-generated Javadoc
/**
 * Oogway class, making ideas from the LOGO teaching language available in
 * Processing.
 * 
 * @author Jun Hu
 * @author Loe Feijs
 * 
 */
public class Oogway implements Cloneable, OConstants {

	/** The PApplet to render to. */
	private PApplet applet;

	/** The angle (in degrees) that the Oogway is heading. */
	private float heading = 0.0f;
	
	private float penSize = 1;

	/** If false, the Oogway moves but does not leave a trail. */
	private boolean isDown = true;

	/** The path. */
	private OPath path;

	/** Color of line drawn by Oogway (as a Processing color). */
	private int penColor;

	/** Reflection. */
	private int reflect = 1;

	/** The spline. */
	private OSpline spline;

	/** The states. */
	private Vector<Oogway> states = new Vector<Oogway>();

	/** The memories. */
	private Hashtable<String, Oogway> memories = new Hashtable<String, Oogway>();

	/** The trace. */
	private int trace = OLINE;
	/**
	 * x location on screen. Any change to this variable must be done using
	 * setPosition.
	 */
	private float xcor;

	/**
	 * y location on screen. Any change to this variable must be done using
	 * setPosition.
	 */
	private float ycor;

	private float[] dashPattern = null;

	private int oogwayShape = OARROW;
	private PShape oogwayShapeSVG = null;

	/**
	 * Standard constructor, creates a Oogway in the middle of the screen which
	 * draws in white.
	 * 
	 * @param applet
	 *            PApplet to render to.
	 */
	public Oogway(PApplet applet) {
		this.applet = applet;
		setPosition(xcor = applet.width / 2, applet.height / 2);
		penColor = applet.color(255, 255, 255);
		spline = new OSpline(applet);
		path = new OPath(applet);

	}

	/**
	 * Move Oogway backward.
	 * 
	 * @param distance
	 *            the distance
	 */
	public void backward(float distance) {
		forward(-distance);
	}

	/**
	 * Begin reflection.
	 */
	public void beginReflection() {
		reflect *= -1;
	}

	/**
	 * Begin spline.
	 */
	public void beginSpline() {
		beginSpline(xcor, ycor);
	}

	/**
	 * Begin spline.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void beginSpline(float x, float y) {
		spline.clear();
		spline.curveVertex(x, y);
		spline.curveVertex(xcor, ycor);
		trace = OSPLINE;
	}

	public void beginDash(float[] pattern) {
		dashPattern = pattern;
	}

	public void beginDash() {
		dashPattern = new float[] { 10, 5 };
	}

	public void endDash() {
		dashPattern = null;
	}

	/**
	 * Bk.
	 * 
	 * @param distance
	 *            the distance
	 */
	public void bk(float distance) {
		backward(distance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Oogway clone() {
		Oogway o = new Oogway(applet);
		o.copy(this);
		return o;
	}

	/**
	 * Copy.
	 * 
	 * @param o
	 *            the o
	 */
	protected void copy(Oogway o) {
		applet = o.applet;
		setPosition(o.xcor, o.ycor);
		penColor = o.penColor;
		penSize = o.penSize;
		isDown = o.isDown;
		heading = o.heading;
		reflect = o.reflect;
		trace = o.trace;
		oogwayShape = o.oogwayShape;
		oogwayShapeSVG = o.oogwayShapeSVG;
		path.copy(o.path);
		spline.copy(o.spline);

		/* copy dash pattern */
		dashPattern = null;
		if (o.dashPattern != null) {
			dashPattern = new float[o.dashPattern.length];
			for (int i = 0; i < o.dashPattern.length; i++)
				dashPattern[i] = o.dashPattern[i];
		}

		/* do not copy memories */
		/* do not copy states */

	}

	/**
	 * Get the distance between this Oogway and point (x,y).
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return distance in pixels.
	 */
	public float distance(float x, float y) {
		return sqrt(pow((x - xcor), 2) + pow((y - ycor), 2));
	}

	/**
	 * Down.
	 */
	public void down() {
		penDown();
	}

	/**
	 * Move the Oogway to a specified point. Used internally to allow
	 * HistoryOogway to override this to record lines.
	 * 
	 * @param x
	 *            location in x axis
	 * @param y
	 *            location in y axis
	 */
	private void drawLine(float x, float y) {



		if (isDown) {
			applet.pushStyle();
			applet.stroke(penColor);
			applet.strokeWeight(penSize);
			
			if (dashPattern == null)
				applet.line(xcor, ycor, x, y);
			else
				dashLine(xcor, ycor, x, y);
			
			applet.popStyle();
		}

	}

	/*
	 * Draw a dashed line with given set of dashes and gap lengths. x0 starting
	 * x-coordinate of line. y0 starting y-coordinate of line. x1 ending
	 * x-coordinate of line. y1 ending y-coordinate of line. spacing array
	 * giving lengths of dashes and gaps in pixels; an array with values {5, 3,
	 * 9, 4} will draw a line with a 5-pixel dash, 3-pixel gap, 9-pixel dash,
	 * and 4-pixel gap. if the array has an odd number of entries, the values
	 * are recycled, so an array of {5, 3, 2} will draw a line with a 5-pixel
	 * dash, 3-pixel gap, 2-pixel dash, 5-pixel gap, 3-pixel dash, and 2-pixel
	 * gap, then repeat.
	 */
	private void dashLine(float x0, float y0, float x1, float y1) {
		if (dashPattern == null) {
			applet.line(x0, y0, x1, y1);
			return;
		}
		float distance = dist(x0, y0, x1, y1);
		float[] xSpacing = new float[dashPattern.length];
		float[] ySpacing = new float[dashPattern.length];
		float drawn = 0.0f; // amount of distance drawn

		if (distance > 0) {
			int i;
			boolean drawLine = true; // alternate between dashes and gaps

			/*
			 * Figure out x and y distances for each of the spacing values I
			 * decided to trade memory for time; I'd rather allocate a few dozen
			 * bytes than have to do a calculation every time I draw.
			 */
			for (i = 0; i < dashPattern.length; i++) {
				xSpacing[i] = lerp(0, (x1 - x0), dashPattern[i] / distance);
				ySpacing[i] = lerp(0, (y1 - y0), dashPattern[i] / distance);
			}

			i = 0;
			while (drawn < distance) {
				/* Add distance "drawn" by this line or gap */
				drawn = drawn + mag(xSpacing[i], ySpacing[i]);

				if (drawLine) {
					if (drawn < distance)
						applet.line(x0, y0, x0 + xSpacing[i], y0 + ySpacing[i]);
					else
						applet.line(x0, y0, x1, y1);
				}
				x0 += xSpacing[i];
				y0 += ySpacing[i];
				i = (i + 1) % dashPattern.length; // cycle through array
				drawLine = !drawLine; // switch between dash and gap
			}
		}
	}

	/**
	 * Draw path.
	 * 
	 * @param distance
	 *            the distance
	 */
	private void drawPath(float distance) {

		float rotRad = radians(heading);

		path.transform(xcor, ycor, distance, rotRad, reflect);

		if (isDown) {
			applet.pushStyle();
			applet.noFill();
			applet.stroke(penColor);
			applet.strokeWeight(penSize);
			path.draw();
			applet.popStyle();
		}
	}

	/**
	 * Draw spline.
	 */
	private void drawSpline() {


		if (isDown) {
			applet.pushStyle();
			applet.noFill();
			applet.stroke(penColor);
			applet.strokeWeight(penSize);
			spline.draw();
			applet.popStyle();
		}


	}

	/**
	 * End reflection.
	 */
	public void endReflection() {
		reflect *= -1;
	}
	
	public boolean isReflecting(){
		return reflect == -1;
	}

	/**
	 * End spline.
	 */
	public void endSpline() {
		endSpline(xcor, ycor);
	}

	/**
	 * End spline.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void endSpline(float x, float y) {
		spline.curveVertex(x, y);
		drawSpline();
		spline.clear();
		trace = OLINE;
	}

	/**
	 * Fd.
	 * 
	 * @param distance
	 *            the distance
	 */
	public void fd(float distance) {
		forward(distance);
	}

	/**
	 * Move Oogway forward.
	 * 
	 * @param distance
	 *            the distance
	 */
	public void forward(float distance) {

		float x, y;
		float rotRad = radians(heading);
		x = xcor + (distance * cos(rotRad));
		y = ycor + (distance * sin(rotRad));

		switch (trace) {
		case OLINE:
			drawLine(x, y);
			break;
		case OSPLINE:
			spline.curveVertex(x, y);
			break;
		case OPATH:
			drawPath(distance);
			break;
		}

		setPosition(x, y);
	}

	/**
	 * Get the angle that the Oogway is facing.
	 * 
	 * @return angle in degrees.
	 */
	public float heading() {
		return heading;
	}

	/**
	 * Home.
	 */
	public void home() {
		setPosition(xcor = applet.width / 2, applet.height / 2);
		setHeading(0f);
	}

	/**
	 * Checks if is down.
	 * 
	 * @return true, if is down
	 */
	public boolean isDown() {
		return isDown;
	}

	/**
	 * Turn the Oogway left.
	 * 
	 * @param angle
	 *            the angle
	 */
	public void left(float angle) {
		heading -= angle * reflect;
	}

	/**
	 * Lt.
	 * 
	 * @param angle
	 *            the angle
	 */
	public void lt(float angle) {
		left(angle);
	}

	/**
	 * Begin path.
	 * 
	 * @param path
	 *            the path
	 */
	public void beginPath(String path) {
		this.path.loadPath(path);
		beginPath();
	}


	/**
	 * Begin path.
	 */
	public void beginPath() {
		trace = OPATH;
	}

	/**
	 * End path.
	 */
	public void endPath() {
		trace = OLINE;
	}

	/**
	 * Pd.
	 */
	public void pd() {
		penDown();
	}

	/**
	 * Pen color.
	 * 
	 * @return the int
	 */
	public int penColor() {
		return penColor;
	}
	
	public float penSize(){
		return penSize();
	}

	/**
	 * put the pen down (draw subsequent movements).
	 */
	public void penDown() {
		isDown = true;
	}

	/**
	 * take the pen up (do not draw subsequent movements).
	 */
	public void penUp() {
		isDown = false;
	}

	/**
	 * Pu.
	 */
	public void pu() {
		penUp();
	}

	/**
	 * popState.
	 */
	public void popState() {
		if (states.size() < 1)
			return;
		Oogway o = states.get(states.size() - 1);
		states.remove(states.size() - 1);
		copy(o);
	}

	/**
	 * Remember.
	 */
	public void pushState() {
		states.add(clone());
	}

	public void remember(String s) {
		memories.put(s, clone());
	}

	public void remember(char c) {
		remember(String.valueOf(c));
	}

	public void remember(int i) {
		remember(String.valueOf(i));
	}

	public void recall(String s) {
		if (memories.containsKey(s)) {
			copy(memories.get(s));
		}
	}

	public void recall(char c) {
		recall(String.valueOf(c));
	}

	public void recall(int i) {
		recall(String.valueOf(i));
	}

	/**
	 * Turn the Oogway right.
	 * 
	 * @param angle
	 *            heading in degrees.
	 */
	public void right(float angle) {
		heading += angle * reflect;
	}

	/**
	 * Rt.
	 * 
	 * @param angle
	 *            the angle
	 */
	public void rt(float angle) {
		right(angle);
	}

	/**
	 * Set the direction the Oogway is facing in to an absolute angle.
	 * 
	 * @param angle
	 *            the new heading
	 */
	public void setHeading(float angle) {
		heading = angle;
	}

	/**
	 * Set the color the Oogway draws with.
	 * 
	 * @param c
	 *            a color created with
	 *            {@link processing.core.PApplet#color(int, int, int)}.
	 */
	public void setPenColor(int c) {
		penColor = c;
	}

	/**
	 * Set the colour the Oogway draws with.
	 * 
	 * @param r
	 *            red value, 0-255.
	 * @param g
	 *            green value, 0-255.
	 * @param b
	 *            blue value, 0-255.
	 */
	public void setPenColor(int r, int g, int b) {
		penColor = applet.color(r, g, b);
	}
	
	public void setPenSize(float size){
		penSize = size;
	}

	/**
	 * Move the Oogway to an absolute location. <strong>This does not
	 * draw</strong>.
	 * 
	 * @param x
	 *            location in x axis.
	 * @param y
	 *            location in y axis.
	 */
	public void setPosition(float x, float y) {
		xcor = x;
		ycor = y;
	}

	/**
	 * Stamp.
	 */
	public void stamp(float width, float height) {
		applet.pushStyle();
		applet.pushMatrix();
		
		applet.stroke(penColor);
		applet.strokeWeight(penSize);		
		applet.translate(xcor, ycor);
		applet.rotate(0.5f * PI + radians(heading));
		switch (this.oogwayShape) {
		case OSVG:
			if(this.oogwayShapeSVG != null){
				applet.pushStyle();
				applet.noFill();
				applet.shape(this.oogwayShapeSVG, -width/2, -height/2, width, height);
				applet.popStyle();
				break;
			}
		default:
			applet.beginShape();
			applet.vertex(0, 0);
			applet.vertex(width / 2, height / 2);
			applet.vertex(0, -height / 2);
			applet.vertex(-width / 2, height / 2);
			applet.endShape(CLOSE);
			break;
		}
		
		applet.popMatrix();
		applet.popStyle();
	}

	public void stamp(float size) {
		stamp(size, size);
	}

	public void stamp() {
		stamp(20);
	}

	public void setStamp(int shape) {
		this.oogwayShape = shape;
	}

	public void setStamp(String svg) {
		this.oogwayShapeSVG = applet.loadShape(svg);
		this.oogwayShape = OSVG;
	}

	/**
	 * Convert the Oogway to a String representation.
	 * 
	 * @return "Oogway at 100,100"
	 */
	@Override
	public String toString() {
		return "Oogway at " + xcor + "," + ycor + " heading " + heading;
	}

	/**
	 * Towards.
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the float
	 */
	public float towards(float x, float y) {

		float rotRad = atan2(y - ycor, x - xcor);
		return degrees(rotRad);
	}

	/**
	 * Up.
	 */
	public void up() {
		penUp();
	}

	/**
	 * Xcor.
	 * 
	 * @return the float
	 */
	public float xcor() {
		return xcor;
	}

	/**
	 * Ycor.
	 * 
	 * @return the float
	 */
	public float ycor() {
		return ycor;
	}

}

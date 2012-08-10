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

/**
 * Oogway class, making ideas from the LOGO teaching language available in
 * Processing.
 * 
 * @author Jun Hu
 * @author Loe Feijs
 * 
 */
public class Oogway implements Cloneable {

	/** The PApplet to render to. */
	private PApplet applet;

	private Graphics g;
	/** The angle (in degrees) that the Oogway is heading. */
	private float heading = 0.0f;
	/** If false, the Oogway moves but does not leave a trail. */
	private boolean isDown = true;
	private Path path;

	/** Color of line drawn by Oogway (as a Processing color). */
	private int penColor;

	/** Reflection */
	private int reflect = 1;

	private Spline spline;
	private Vector<Oogway> states = new Vector<Oogway>();

	private Trace trace = Trace.LINE;
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

	/**
	 * Standard constructor, creates a Oogway in the middle of the screen which
	 * draws in white.
	 * 
	 * @param applet
	 *            PApplet to render to.
	 */
	public Oogway(PApplet applet) {
		this.applet = applet;
		g = new Graphics(applet);
		setPosition(xcor = applet.width / 2, applet.height / 2);
		penColor = applet.color(255, 255, 255);
		spline = new Spline(applet);
		path = new Path(applet);

	}

	/**
	 * Move Oogway backward.
	 * 
	 * @param amount
	 *            number of pixels to move by.
	 */
	public void backward(float distance) {
		forward(-distance);
	}

	public void beginReflection() {
		reflect = -1;
	}

	public void beginSpline() {
		beginSpline(xcor, ycor);
	}

	public void beginSpline(float x, float y) {
		spline.clear();
		spline.curveVertex(x, y);
		spline.curveVertex(xcor, ycor);
		trace = Trace.SPLINE;
	}

	public void bk(float distance) {
		backward(distance);
	}

	public Oogway clone() {
		Oogway o = new Oogway(applet);
		o.copy(this);
		return o;
	}


	protected void copy(Oogway o) {
		applet = o.applet;
		g = o.g;
		setPosition(o.xcor, o.ycor);
		penColor = o.penColor;
		isDown = o.isDown;
		heading = o.heading;
		reflect = o.reflect;
		trace = o.trace;
		path.copy(o.path);
		spline.copy(o.spline);
		states.clear();
		for(int i = 0; i<o.states.size(); i++)
			states.add(o.states.get(i));
	}
	

	/**
	 * Get the distance between this Oogway and point (x,y).
	 * 
	 * @param otherX
	 *            location in x axis.
	 * @param otherY
	 *            location in y axis.
	 * @return distance in pixels.
	 */
	public float distance(float x, float y) {
		return PApplet.sqrt(PApplet.pow((x - xcor), 2)
				+ PApplet.pow((x - ycor), 2));
	}

	public void down() {
		pendown();
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

		g.save();

		if (isDown) {
			applet.stroke(penColor);
			applet.line(xcor, ycor, x, y);
		}

		g.restore();
	}

	private void drawPath(float distance) {
		
		float rotRad = PApplet.radians(heading);

		Path p = path.clone();

		if (reflect == -1)
			p.reflectInX();
		p.scaleTo(distance);
		p.rotateRad(rotRad);


		p.moveTo(xcor, ycor);
	
		g.save();
		applet.noFill();

		if (isDown) {
			applet.stroke(penColor);
			p.draw();
		}

		g.restore();

	}

	private void drawSpline() {

		g.save();

		applet.noFill();

		if (isDown) {
			applet.stroke(penColor);
			spline.draw();
		}

		g.restore();

	}

	public void endReflection() {
		reflect = 1;
	}

	public void endSpline() {
		endSpline(xcor, ycor);
	}

	public void endSpline(float x, float y) {
		spline.curveVertex(x, y);
		drawSpline();
		spline.clear();
		trace = Trace.LINE;
	}

	public void fd(float distance) {
		forward(distance);
	}

	/**
	 * Move Oogway forward.
	 * 
	 * @param amount
	 *            number of pixels to move by.
	 */
	public void forward(float distance) {
		
		float x, y;
		float rotRad = PApplet.radians(heading);
		x = xcor + (distance * PApplet.cos(rotRad));
		y = ycor + (distance * PApplet.sin(rotRad));
		
		switch(trace){
		case LINE:
			drawLine(x,y);
			break;
		case SPLINE:
			spline.curveVertex(x, y);
			break;
		case PATH:
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

	public void home() {
		setPosition(xcor = applet.width / 2, applet.height / 2);
		setHeading(0f);
	}

	public boolean isDown() {
		return isDown;
	}

	/**
	 * Turn the Oogway left.
	 * 
	 * @param amount
	 *            angle in degrees.
	 */
	public void left(float angle) {
		heading -= angle * reflect;
	}

	public void lt(float angle) {
		left(angle);
	}

	public void beginPath(String path) {
		this.path.loadPath(path);
		beginPath();
	}

	public void beginPath(Path path) {
		this.path.copy(path);
		beginPath();
	}

	public void beginPath() {
		this.path.placeAlongX();
		trace = Trace.PATH;
	}

	/**
	 * 
	 */
	public void endPath() {
		path.clear();
		trace = Trace.LINE;
	}
	

	public void pd() {
		pendown();
	}

	public int penColor() {
		return penColor;
	}

	/**
	 * put the pen down (draw subsequent movements)
	 */
	public void pendown() {
		isDown = true;
	}

	/**
	 * take the pen up (do not draw subsequent movements)
	 */
	public void penup() {
		isDown = false;
	}

	public void pu() {
		penup();
	}

	public void recall() {
		if (states.size() < 1)
			return;
		Oogway o = states.get(states.size() - 1);
		states.remove(states.size() - 1);
		copy(o);
	}

	public void remember() {
		states.add(clone());
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

	public void rt(float angle) {
		right(angle);
	}

	/**
	 * Set the direction the Oogway is facing in to an absolute angle.
	 * 
	 * @param heading
	 *            heading in degrees.
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

	public void stamp() {
		g.save();
		applet.stroke(penColor);		
		applet.pushMatrix();
		applet.translate(xcor, ycor);
		applet.rotate(0.5f * PApplet.PI + PApplet.radians(heading));
		applet.line(0, 0, 10, 10);
		applet.line(10, 10, 0, -10);
		applet.line(0, -10, -10, 10);
		applet.line(-10, 10, 0, 0);
		applet.popMatrix();
		g.restore();
	}

	/**
	 * Convert the Oogway to a String representation
	 * 
	 * @return "Oogway at 100,100"
	 */
	@Override
	public String toString() {
		return "Oogway at " + xcor + "," + ycor + " heading " + heading;
	}

	public float towards(float x, float y) {

		float rotRad = PApplet.atan2(y - ycor, x - xcor);
		return PApplet.degrees(rotRad);
	}

	public void up() {
		penup();
	}

	public float xcor() {
		return xcor();
	}

	public float ycor() {
		return ycor();
	}

}

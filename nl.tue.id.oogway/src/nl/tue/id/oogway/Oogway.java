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
public class Oogway implements Cloneable{

	/** The PApplet to render to. */
	private PApplet applet;

	/** The angle (in degrees) that the Oogway is heading. */
	private float heading;
	/** If false, the Oogway moves but does not leave a trail. */
	private boolean isDown;
	/** Color of line drawn by Oogway (as a Processing color). */
	private int penColor;
	/** Reflection */
	private int reflect;


	private Vector<Oogway> states = new Vector<Oogway>();
	
	private Graphics g;

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
	
	private Spline spline;
	private Path path;


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
		isDown = true;
		heading = 0f;
		reflect = 1;
		spline = new Spline(applet);
		path = new Path(applet);
		
	}

	public void home() {
		setPosition(xcor = applet.width / 2, applet.height / 2);
		setHeading(0f);
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

	protected void copy(Oogway o) {
		applet = o.applet;
		g = o.g;
		setPosition(o.xcor, o.ycor);
		penColor = o.penColor;
		isDown = o.isDown;
		heading = o.heading;
		reflect = o.reflect;
		path.copy(o.path);
		spline.copy(o.spline);
	}
	
	public Oogway clone(){
		Oogway o = new Oogway(applet);
		o.copy(this);
		return o;
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

	/**
	 * put the pen down (draw subsequent movements)
	 */
	public void pendown() {
		isDown = true;
	}

	public void pd() {
		pendown();
	}

	public void pu() {
		penup();
	}

	public void up() {
		penup();
	}

	public void down() {
		pendown();
	}
	
	public float xcor(){
		return xcor();
	}
	
	public float ycor(){
		return ycor();
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

	private void drawPath(Path path) {
        g.save();
        
        applet.noFill();
        
		if (isDown) {
			applet.stroke(penColor);
			path.draw();
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
	
	public void endSpline(){
		drawSpline();
		spline.clear();
	}
	

	public void recall() {
		if (states.size() < 1)
			return;
		Oogway o = states.get(states.size() - 1);
		states.remove(states.size() - 1);
		copy(o);
	}

	/**
	 * Move Oogway forward.
	 * 
	 * @param amount
	 *            number of pixels to move by.
	 */
	public void forward(float distance) {
		float newX, newY;
		float rotRad = PApplet.radians(heading);
		newX = xcor + (distance * PApplet.cos(rotRad));
		newY = ycor + (distance * PApplet.sin(rotRad));
		drawLine(newX, newY);
		setPosition(newX, newX);
	}

	public void fd(float distance) {
		forward(distance);
	}

	public void bk(float distance) {
		backward(distance);
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
	 * Set the direction the Oogway is facing in to an absolute angle.
	 * 
	 * @param heading
	 *            heading in degrees.
	 */
	public void setHeading(float angle) {
		heading = angle;
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

	public void pathBackward(float distance, String s) {
		pathForward(-distance, s);
	}

	public void pathBackward(float distance, Path path) {
		pathForward(-distance, path);
	}

	public void pathForward(float distance, String s) {
		Path path = new Path(applet, s);
		pathForward(distance, path);
	}

	public void pathForward(float distance, Path path) {

		float rotRad = PApplet.radians(heading);

		path.placeAlongX();
		if (reflect == -1)
			path.reflectInX();
		path.scaleTo(distance);
		path.rotateRad(rotRad);

		path.moveTo(xcor, ycor);
		drawPath(path);
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
	
	public int penColor(){
		return penColor;
	}

	public void beginReflection() {
		reflect = -1;
	}

	public void endReflection() {
		reflect = -1;
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


	public void splineBackward(float distance) {
		splineForward(-distance);
	}

	public void splineForward(float distance) {
		float newX, newY;
		float rotRad = PApplet.radians(heading);
		newX = xcor + (distance * PApplet.cos(rotRad));
		newY = ycor + (distance * PApplet.sin(rotRad));

		spline.curveVertex(newX, newY);
		setPosition(newX, newY);
	}



	public void stamp() {
		applet.pushMatrix();
		applet.translate(xcor, ycor);
		applet.rotate(0.5f * PApplet.PI + PApplet.radians(heading));
		applet.line(0, 0, 10, 10);
		applet.line(10, 10, 0, -10);
		applet.line(0, -10, -10, 10);
		applet.line(-10, 10, 0, 0);
		applet.popMatrix();
	}

	/**
	 * Strafe the Oogway left.
	 * 
	 * @param distance
	 *            number of pixels to strafe Oogway.
	 */
	public void strafeLeft(int distance) {
		left(90);
		forward(distance);
		right(90);
	}

	/**
	 * Strafe the Oogway right.
	 * 
	 * @param distance
	 *            number of pixels to strafe Oogway.
	 */
	public void strafeRight(int distance) {
		right(90);
		forward(distance);
		left(90);
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

	/**
	 * take the pen up (do not draw subsequent movements)
	 */
	public void penup() {
		isDown = false;
	}

}

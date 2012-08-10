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
public class Oogway {
	
	/** The PApplet to render to. */
	private PApplet applet;
	private float fromx;
	private float fromy;
	/** The angle (in degrees) that the Oogway is heading. */
	private float heading;
	/** If false, the Oogway moves but does not leave a trail. */
	private boolean isDown;
	/** Color of line drawn by Oogway (as a Processing color). */
	private int penColor;
	/** Reflection */
	private int reflect;

	private Vector<float[]> splinePoints = new Vector<float[]>();
	private Vector<Oogway> states = new Vector<Oogway>();

	public float x; // a copy of xcor, for read only access;
	/**
	 * x location on screen. Any change to this variable must be done using
	 * moveTo.
	 */
	private float xcor;
	public float y; // a copy of ycor, for read only access;
	/**
	 * y location on screen. Any change to this variable must be done using
	 * moveTo.
	 */
	private float ycor;

	/**
	 * "Copy" constructor, creates an identical Oogway to the one passed in.
	 * 
	 * @param t
	 *            Oogway to copy.
	 */
	public Oogway(Oogway o) {
		copy(o);
	}

	/**
	 * Standard constructor, creates a Oogway in the middle of the screen which
	 * draws in white.
	 * 
	 * @param applet
	 *            PApplet to render to.
	 */
	public Oogway(PApplet applet) {
		this.applet = applet;
		moveTo(xcor = applet.width / 2, applet.height / 2);
		penColor = applet.color(255, 255, 255);
		isDown = true;
		heading = 0f;
		reflect = 1;
	}
	
	public void home(){
		moveTo(xcor = applet.width / 2, applet.height / 2);
		heading(0f);
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

	private Oogway copy(Oogway o) {
		applet = o.applet;
		moveTo(o.xcor, o.ycor);
		penColor = o.penColor;
		isDown = o.isDown;
		heading = o.heading;
		reflect = o.reflect;
		fromx = o.fromx;
		fromy = o.fromy;
		return this;
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
	
	public void pd(){
		pendown();
	}
	
	public void  pu(){
		penup();
	}
	
	public void up(){
		penup();
	}
	
	public void down(){
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
		drawSpline(x, y);

		if (isDown) {
			applet.stroke(penColor);
			applet.line(xcor, ycor, x, y);
		}

		moveTo(x, y);

	}

	private void drawPath(Path path) {
		boolean fill = applet.g.fill;
		int c = applet.g.fillColor;
		applet.noFill();

		drawSpline(path.getEnd()[0], path.getEnd()[1]);

		if (isDown) {
			applet.stroke(penColor);
			path.draw();
		}

		moveTo(path.getEnd()[0], path.getEnd()[1]);
		
		if (fill)
			applet.fill(c);
		else
			applet.noFill();
	}

	private void drawSpline(float x, float y) {
		
		if (splinePoints.size() < 3) // at least three vertexes are needed.
			return;
		
		boolean fill = applet.g.fill;
		int c = applet.g.fillColor;
		applet.noFill();

		addSplinePoint(x, y);

		applet.beginShape();
		for (int i = 0; i < splinePoints.size(); i++)
			applet.curveVertex(splinePoints.get(i)[0], splinePoints.get(i)[1]);
		applet.endShape();
        
		splinePoints.clear();
		
		if (fill)
			applet.fill(c);
		else
			applet.noFill();		

	}

	public void recall() {
		if(states.size()<1) return;
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
	}
	
	public void fd(float distance){
		forward(distance);
	}
	
	public void bk(float distance){
		backward(distance);
	}

	/**
	 * Get the angle that the Oogway is facing.
	 * 
	 * @return angle in degrees.
	 */
	/* FIXME: should this be float as well? */
	public float heading() {
		return heading;
	}

	/**
	 * Set the direction the Oogway is facing in to an absolute angle.
	 * 
	 * @param heading
	 *            heading in degrees.
	 */
	public void heading(float angle) {
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
	
	public void lt(float angle){
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
	private void moveTo(float x, float y) {
		fromx = xcor;
		fromy = ycor;
		xcor = x;
		ycor = y;
		this.x = xcor;
		this.y = ycor;
	}
	
	public void pathAlong(float distance, String s) {
		Path path = new Path(applet, s);
		pathAlong(distance, path);
	}

	public void pathAlong(float distance, Path path) {
		
		path = new Path(path); //keep the input path intact.

		path.placeAlongX();
		if (reflect == -1)
			path.reflectInX();
		float rotRad = PApplet.radians(heading - path.getStartRotation());
		path.rotateRad(rotRad);
		path.scaleTo(distance);

		path.moveTo(xcor, ycor);
		drawPath(path);
		heading = path.getEndRotation();

	}

	public void pathBackward(float distance, String s) {
		pathForward(-distance, s);
	}
	
	public void pathBackward(float distance, Path path){
		pathForward(-distance, path);
	}
	
	public void pathForward(float distance, String s) {
		Path path = new Path(applet, s);
		pathForward(distance, path);
	}

	public void pathForward(float distance, Path path) {
		
		path = new Path(path); //keep the input path intact.
		
		float rotRad = PApplet.radians(heading);

		path.placeAlongX();
		if (reflect == -1)
			path.reflectInX();
		path.scaleTo(distance);
		path.rotateRad(rotRad);

		path.moveTo(xcor, ycor);
		drawPath(path);
		heading = path.getEndRotation();
	}

	/**
	 * Set the color the Oogway draws with.
	 * 
	 * @param c
	 *            a color created with
	 *            {@link processing.core.PApplet#color(int, int, int)}.
	 */
	public void penColor(int c) {
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
	public void penColor(int r, int g, int b) {
		penColor = applet.color(r, g, b);
	}

	public void reflect() {
		reflect = -1 * reflect;
	}

	public void remember() {
		states.add(new Oogway(this));
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

	public void setPosition(float x, float y) {
		moveTo(x, y);
		drawSpline(x, y);
	}

	public void splineBackward(float distance) {
		splineForward(-distance);
	}

	public void splineForward(float distance) {
		float newX, newY;
		float rotRad = PApplet.radians(heading);
		newX = xcor + (distance * PApplet.cos(rotRad));
		newY = ycor + (distance * PApplet.sin(rotRad));

		addSplinePoint(newX, newY);
		moveTo(newX, newY);
	}

	private void addSplinePoint(float x, float y) {
		if (splinePoints.size() < 2) {
			float p[] = { fromx, fromy };
			splinePoints.add(p);
			float p2[] = { xcor, ycor };
			splinePoints.add(p2);
		}

		float p[] = { x, y };
		splinePoints.add(p);
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

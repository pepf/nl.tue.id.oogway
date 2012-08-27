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

import processing.core.PApplet;

// TODO: Auto-generated Javadoc
/**
 * The Class Graphics.
 */
public class Graphics {
	
	/** The applet. */
	PApplet applet;
	
	/** The stroke. */
	boolean fill, stroke;
	
	/** The stroke color. */
	int fillColor, strokeColor;
	
	/** The stroke weight. */
	float strokeWeight; 
	
	/**
	 * Instantiates a new graphics.
	 *
	 * @param applet the applet
	 */
	public Graphics(PApplet applet){
		this.applet = applet;
	}
	
	protected void copy(Graphics g){
		this.applet = g.applet;
		this.fill = g.fill;
		this.stroke = g.stroke;
		this.fillColor = g.fillColor;
		this.strokeColor = g.strokeColor;
		this.strokeWeight = g.strokeWeight;
	}
	
	/**
	 * Restore.
	 */
	public void restore(){
		if(fill) applet.fill(fillColor);
		else applet.noFill();
		if(stroke) applet.stroke(strokeColor);
		else applet.noStroke();
		
		applet.strokeWeight(strokeWeight);
	
	}
	
	/**
	 * Save.
	 */
	public void save(){
		fill = applet.g.fill;
		stroke = applet.g.stroke;
		fillColor = applet.g.fillColor;
		strokeColor = applet.g.strokeColor;
		strokeWeight = applet.g.strokeWeight;
		
	}

}

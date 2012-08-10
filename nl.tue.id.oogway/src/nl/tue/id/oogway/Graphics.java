package nl.tue.id.oogway;

import processing.core.PApplet;

public class Graphics {
	
	PApplet applet;
	boolean fill, stroke;
	int fillColor, strokeColor;
	float strokeWeight; 
	
	public Graphics(PApplet applet){
		this.applet = applet;
	}
	
	public void restore(){
		if(fill) applet.fill(fillColor);
		else applet.noFill();
		if(stroke) applet.stroke(strokeColor);
		else applet.noStroke();
		
		applet.strokeWeight(strokeWeight);
	
	}
	
	public void save(){
		fill = applet.g.fill;
		stroke = applet.g.stroke;
		fillColor = applet.g.fillColor;
		strokeColor = applet.g.strokeColor;
		strokeWeight = applet.g.strokeWeight;
		
	}

}

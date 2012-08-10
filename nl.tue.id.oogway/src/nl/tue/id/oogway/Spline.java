package nl.tue.id.oogway;

import processing.core.PApplet;
import java.util.Vector;

public class Spline implements Cloneable{
	
	private PApplet applet;
	private Vector<float[]> vertices = new Vector<float[]>();
	
	public Spline(PApplet applet){
		this.applet = applet;
	}
	
	public void clear(){
		vertices.clear();
	}
	
	public Spline clone(){
		Spline s = new Spline(applet);
        s.copy(this);
		return s;
	}
	
	protected void copy(Spline s){
		this.clear();
		for (int i = 0; i < s.vertices.size(); i++)
			curveVertex(s.vertices.get(i)[0], s.vertices.get(i)[1]);
	}
	
	public void curveVertex(float x, float y){
		float v[] = {x, y};
		vertices.add(v);
	}
	
	public void draw(){
		if(vertices.size()<4) return;
		applet.beginShape();
		for (int i = 0; i < vertices.size(); i++)
			applet.curveVertex(vertices.get(i)[0], vertices.get(i)[1]);
		applet.endShape();
	}
	
}

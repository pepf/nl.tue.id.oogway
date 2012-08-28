import processing.pdf.*;
import nl.tue.id.oogway.*;

int XSIZE=int(3.6*297);
int YSIZE=int(3.6*210);

Oogway o;
PFont font;

void setup(){
  size(XSIZE,YSIZE);
  o = new Oogway(this);
  noLoop();
  beginRecord(PDF, "CGG.pdf");
  o.setPenColor(0);
}

void draw(){
  background(255);
  tesselate(100);
  
  o.home();
  o.setPosition(200, o.ycor()+200);
  abc(200);

  endRecord();
}

void tesselate(float size){
  o.home();
  o.setPosition(o.xcor(), o.ycor()+50);
  float x = o.xcor();
  float y = o.ycor();
  o.beginReflection(); abc(size); o.endReflection();
  abc(size);
}
void abc(float size){
  o.pushState();
  
  float x = o.xcor();
  float y = o.ycor();
  o.left(30);
  o.beginPath("AB.svg"); o.forward(size); o.endPath();
  o.left(120);
  o.beginPath("AB.svg"); o.forward(size); o.endPath();

  float distance_CA = o.distance(x, y);
  float towards_A = o.towards(x,y);
  o.left(360 - abs(towards_A - o.heading()));
  o.beginPath("CM.svg"); o.forward(distance_CA/2); o.endPath();
  float mx = o.xcor();
  float my = o.ycor();
  
  o.setPosition(x, y);
  float distance_AM = o.distance(mx, my);
  float towards_M = o.towards(mx, my);
  o.left(abs(towards_M - o.heading()));
  o.beginPath("CM.svg"); o.forward(distance_AM); o.endPath();
  
  o.popState();
}


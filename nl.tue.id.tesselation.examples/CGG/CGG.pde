import processing.pdf.*;
import nl.tue.id.oogway.*;

int XSIZE=int(3.6*297);
int YSIZE=int(3.6*210);

Oogway o;

//latest A, B, C coordinates
float ax, ay, bx, by, cx, cy;

float ab = 100;
float degreeABC = 75;

void setup() {
  size(XSIZE, YSIZE);
  o = new Oogway(this);
  noLoop(); smooth();
  beginRecord(PDF, "CGG.pdf");
  o.setPenColor(0);
}

void draw() {
  background(255);
  o.setPosition(o.xcor()+80, o.ycor()+250);
  tesselate(0.7);

  o.home();
  o.setPosition(200, o.ycor()+250);
  abc(2.5);

  endRecord();
}

void tesselate(float scale) {
  o.pushState();
 
  float step = ab*scale*cos(radians(degreeABC/2))*2;
  for(int i=0; i<3; i++){
    pair(scale);
    float bx0 = bx, by0 = by;
    float ax0 = ax, ay0 = ay;
    for(int j=1; j<3; j++){
      o.setPosition(cx + step , cy);
      pair(scale);
    }
    o.setPosition(bx0, by0);
    for(int j=0; j<3; j++){
      pairReflected(scale);
      o.setPosition(cx + step , cy);
    }
    o.setPosition(ax0, ay0);
  }
  
  o.popState();
}

void abc(float scale) {
  o.pushState();
  
  //arbitrary line AB
  ax=o.xcor(); ay = o.ycor();
  o.left(degreeABC/2);
  o.beginPath("AB.svg");  o.forward(ab*scale); o.endPath();
  bx=o.xcor(); by = o.ycor();

  //glide reflection of AB until it connects to the position BC,
  //where the angle ABC (degreeABC) is arbitrary.
  o.left(180-degreeABC);
  o.beginReflection(); o.beginPath("AB.svg"); 
  o.forward(ab*scale); 
  o.endPath(); o.endReflection();
  cx = o.xcor(); cy = o.ycor();
  
  //close the figure by a C-line CA
  o.setHeading(o.towards(ax, ay));
  float am = o.distance(ax,ay)/2;
  o.beginPath("CM.svg"); o.forward(am); o.endPath();
  o.setPosition(ax, ay);
  o.setHeading(o.towards(cx,cy));
  o.beginPath("CM.svg"); o.forward(am); o.endPath();

  o.popState();
}

void pair(float scale){
  o.pushState();
  abc(scale);
  o.setPosition(cx, cy);
  o.left(180);
  abc(scale);
  o.popState();
}

void pairReflected(float scale) {
  o.pushState();
  o.left(180);
  o.beginReflection(); pair(scale); o.endReflection();
  o.popState();
}



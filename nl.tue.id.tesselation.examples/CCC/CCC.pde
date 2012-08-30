import processing.pdf.*;
import nl.tue.id.oogway.*;

int XSIZE=int(3.6*297);
int YSIZE=int(3.6*210);

Oogway o;

//latest A, B, C coordinates
float ax, ay, bx, by, cx, cy;

float ab = 100;
float ac = 80;
float degreeBAC= 75;

void setup() {
  size(XSIZE, YSIZE);
  o = new Oogway(this);
  noLoop(); smooth();
  beginRecord(PDF, "CCC.pdf");
  o.setPenColor(0);
}

void draw() {
  background(255);
  
  
//  o.setPosition(o.xcor()+80, o.ycor()+250);
 // tesselate(0.7);

  o.setPosition(200, 250);
  abc(4);
  


  endRecord();
}
/*
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
*/
void abc(float scale) {
  o.pushState();
  
  //Let AB be a C-line
  ax=o.xcor(); ay = o.ycor();
  o.right(90);
  o.beginPath("AMb.svg");  o.forward(ab*scale/2); o.endPath();
  o.penup(); o.forward(ab*scale/2); o.pendown();
  bx=o.xcor(); by = o.ycor();
  o.left(180);
  o.beginPath("AMb.svg");  o.forward(ab*scale/2); o.endPath();
  
   //Draw another C-line from A to C
  o.setPosition(ax, ay);
  o.right(180-degreeBAC);
  o.beginPath("AMc.svg");  o.forward(ac*scale/2); o.endPath();
  o.penup(); o.forward(ac*scale/2); o.pendown();
  cx=o.xcor(); cy = o.ycor();
  o.left(180);
  o.beginPath("AMc.svg");  o.forward(ac*scale/2); o.endPath();
  
  //Draw a third C-line from B to C
  o.setPosition(bx, by);
  float bm = o.distance(cx, cy)/2;
  o.setHeading(o.towards(cx, cy));
  o.beginPath("BMc.svg");  o.forward(bm); o.endPath();
  o.penup(); o.forward(bm); o.pendown();
  o.left(180);
  o.beginPath("BMc.svg");  o.forward(bm); o.endPath();
  
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




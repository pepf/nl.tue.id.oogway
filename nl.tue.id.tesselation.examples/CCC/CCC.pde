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
  o.setPenSize(2);
}

void draw() {
  background(255);
  
  o.setPosition(o.xcor()+270, 330);
  tesselate(1);

  o.setPosition(200, 300);
  abc(3);

  endRecord();
}

void tesselate(float scale) {
  o.pushState();
 
  for(int i=0; i<3; i++){
    o.setHeading(0);
    pair(scale);
    float ax0 = ax, ay0 = ay;
    o.setPosition(ax, ay);
    float headingAC = o.towards(cx, cy);
    float ac = o.distance(cx, cy);
    for(int j=0; j<2; j++){
      o.setPosition(bx, by);
      o.setHeading(headingAC);
      o.up(); o.forward(ac); o.down();
      o.setHeading(0);
      pair(scale);
    }
    o.setPosition(ax0, ay0);
  }
  
  o.popState();
}

void abc(float scale) {
  o.pushState();
  
  //Let AB be a C-line
  ax=o.xcor(); ay = o.ycor();
  o.right(90);
  o.beginPath("AMb.svg");  o.forward(ab*scale/2); o.endPath();
  o.up(); o.forward(ab*scale/2); o.down();
  bx=o.xcor(); by = o.ycor();
  o.left(180);
  o.beginPath("AMb.svg");  o.forward(ab*scale/2); o.endPath();
  
   //Draw another C-line from A to C
  o.setPosition(ax, ay);
  o.right(180-degreeBAC);
  o.beginPath("AMc.svg");  o.forward(ac*scale/2); o.endPath();
  o.up(); o.forward(ac*scale/2); o.down();
  cx=o.xcor(); cy = o.ycor();
  o.left(180);
  o.beginPath("AMc.svg");  o.forward(ac*scale/2); o.endPath();
  
  //Draw a third C-line from B to C
  o.setPosition(bx, by);
  float bm = o.distance(cx, cy)/2;
  o.setHeading(o.towards(cx, cy));
  o.beginPath("BMc.svg");  o.forward(bm); o.endPath();
  o.up(); o.forward(bm); o.down();
  o.left(180);
  o.beginPath("BMc.svg");  o.forward(bm); o.endPath();
  
  o.popState();
}

void pair(float scale){
  o.pushState();
  abc(scale);
  o.setPosition(bx, by);
  o.right(180);
  abc(scale);
  o.popState();
}




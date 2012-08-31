import processing.pdf.*;
import nl.tue.id.oogway.*;

int XSIZE=int(3.6*297);
int YSIZE=int(3.6*210);

Oogway o;
PFont font;

//latest A, B, C, D coordinates
float ax, ay, bx, by, cx, cy, dx, dy;
float ab = 100;
float ad = 100;
float degreeDAB = 80;

void setup() {
  size(XSIZE, YSIZE);
  o = new Oogway(this);
  noLoop(); smooth();
  beginRecord(PDF, "TTTT.pdf");
  o.setPenColor(0);
  o.setPenSize(2);
}

void draw() {
  background(255);
  o.setPosition(o.xcor(), o.ycor() + 300);
  tesselate(1);

  o.home();
  o.setPosition(200, o.ycor()+250);
  abcd(2);

  endRecord();
}

void tesselate(float scale) {
  o.pushState();
  for (int i = 0; i<3; i++) {
    abcd(scale);
    float x = dx, y = dy;   
    for (int j = 1; j<3; j++) {
      o.setPosition(bx, by);
      abcd(scale);
    }
    o.setPosition(x,y);
  }
  o.popState();
}

void abcd(float scale) {
  o.pushState();
  
  //arbitrary line AB
  ax = o.xcor(); ay = o.ycor();
  o.beginPath("AB.svg");  o.forward(ab*scale);  o.endPath();
  bx = o.xcor(); by = o.ycor();
  
  //shift AB to DC
  o.setPosition(ax, ay);
  o.left(degreeDAB);
  o.up(); o.forward(ad*scale); o.down();
  dx = o.xcor(); dy = o.ycor();
  o.right(degreeDAB);
  o.beginPath("AB.svg");  o.forward(ab*scale);  o.endPath();
  cx = o.xcor(); cy = o.ycor();
  
  //Another arbitrary line from A to D
  o.setPosition(ax, ay);
  o.left(degreeDAB);
  o.beginPath("AD.svg");  o.forward(ad*scale);  o.endPath();
  
  //shift AD to BC;
  o.setPosition(bx, by);
  o.beginPath("AD.svg");  o.forward(ad*scale);  o.endPath();

  o.popState();
}


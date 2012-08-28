import processing.pdf.*;
import nl.tue.id.oogway.*;

int XSIZE=int(3.6*297);
int YSIZE=int(3.6*210);

Oogway o;
PFont font;
float distance_MB;
float distance_CA;

void setup() {
  size(XSIZE, YSIZE);
  o = new Oogway(this);
  noLoop();
  beginRecord(PDF, "CGG.pdf");
  o.setPenColor(0);
}

void draw() {
  background(255);
  tesselate(100);

  o.home();
  o.setPosition(250, o.ycor()+250);
  abc(250);

  endRecord();
}

void tesselate(float size) {
  o.home();
  calc_CA_MB(100);
  o.setPosition(o.xcor()+ distance_MB, o.ycor()+50);
  float x = o.xcor();
  float y = o.ycor();

  for (int i=0; i<2; i++) {

    o.setPosition(x + distance_MB, y + distance_CA * i);  
    for (int j=0; j<2; j++) {
      abcRotated(100);
      abc(100);
      o.setPosition(o.xcor()+distance_MB*2, o.ycor());
    }

    o.setPosition(x, o.ycor() + distance_CA/2);
    for (int j=0; j<2; j++) {
      abcReflected(100);
      abcReflectedRotated(100);
      o.setPosition(o.xcor()+distance_MB*2, o.ycor());
    }
  }
}

void abc(float size) {
  // we assume Oogway starts at A, heading right.
  o.pushState();

  float ax = o.xcor();
  float ay = o.ycor();

  o.left(45);
  o.beginPath("AB.svg"); 
  o.forward(size); 
  o.endPath();

  float bx = o.xcor();
  float by = o.ycor();

  o.left(90);
  o.beginReflection();
  o.beginPath("AB.svg"); 
  o.forward(size); 
  o.endPath();
  o.endReflection();

  distance_CA = o.distance(ax, ay);
  o.setHeading(o.towards(ax, ay));
  o.beginPath("CM.svg"); 
  o.forward(distance_CA/2); 
  o.endPath();
  float mx = o.xcor();
  float my = o.ycor();

  distance_MB = o.distance(bx, by);

  o.setPosition(ax, ay);
  o.setHeading(o.towards(mx, my));
  o.beginPath("CM.svg"); 
  o.forward(distance_CA/2); 
  o.endPath();

  o.popState();
}

void abcReflected(float size) { //reflect against AC
  // we assume Oogway starts at A, heading right.
  o.pushState();
  o.left(180);
  o.beginReflection();
  abc(size);
  o.endReflection();
  o.popState();
}

void abcReflectedRotated(float size) {
  // we assume Oogway starts at A, heading right.
  o.pushState();
  o.left(180);
  o.beginReflection();
  abcRotated(size);
  o.endReflection();
  o.popState();
}

void abcRotated(float size) {
  o.pushState();
  o.setPosition(o.xcor(), o.ycor()-distance_CA);
  o.left(180);
  abc(size);
  o.popState();
}

void calc_CA_MB(float size) {
  o.pushState();
  o.penup();
  abc(size);
  o.popState();
}


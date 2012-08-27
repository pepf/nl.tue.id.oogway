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
  beginRecord(PDF, "TTTT.pdf");
  o.setPenColor(0);
  font = createFont("Comic Sans MS",32); 
}

void draw(){
  background(255);
  drawIntro();
  tesselate(100);
  
  o.home();
  o.setPosition(200, o.ycor()+200);
  abcd(200);

  drawDashlines();

  endRecord();
}

void tesselate(float size){
  o.home();
  o.setPosition(o.xcor(), o.ycor()+50);
  float x = o.xcor();
  float y = o.ycor();
  for (int i = 0; i<3; i++){
    for (int j = 0; j<3; j++){
      o.setPosition(x+i*size,y+j*size);
      abcd(size);
    }
  }
}
void abcd(float size){
  o.remember("A");
  o.beginPath("AB.svg"); o.forward(size); o.endPath();
  o.remember("B");
  o.left(90);
  o.beginPath("AD.svg"); o.forward(size); o.endPath();
  o.recall("A");
  o.left(90);
  o.beginPath("AD.svg"); o.forward(size); o.endPath();
  o.right(90);
  o.beginPath("AB.svg"); o.forward(size); o.endPath();
  o.right(135);
  o.penup();
  o.forward(sqrt(size*size*2)/2);
  o.pendown();
  o.left(45);
  o.backward(size/16);
  o.forward(size/8);
  o.stamp(size/32);
  o.recall("A");
}

void drawIntro(){
     textFont(font,32);
     stroke(0);
     fill(0);
     text("Nr.1, Basic Type TTTT",200,50);
     textFont(font,16);
     text("Shift the arbitrary line AB to DC, such that ABCD are the corners of a parallelogram "
    + "translation vector AD). Draw another arbitrary line from A to D and shift it into the position "
    + "BC (translation vector AB)."
         , 200, 100,700,200); 
     text("Network 4444,\r\n"
     + "1 positioning."
         , 650, 200,700,100); 
}

void drawDashlines(){
  textFont(font,16);
  fill(255,0,0);
  
  o.recall("A");
  o.setPenColor(0,0,255);
  ellipse(o.xcor(), o.ycor(), 10 , 10);
  text("A", o.xcor()+10, o.ycor());
  o.left(90);   
  o.beginDash(); o.forward(200); o.endDash();
  text("D", o.xcor()+10, o.ycor());
  ellipse(o.xcor(), o.ycor(), 10 , 10);
  
  o.recall("B"); 
  o.setPenColor(0,0,255);
  ellipse(o.xcor(), o.ycor(), 10 , 10);
  text("B", o.xcor()+10, o.ycor());
  o.left(90); 
  o.beginDash(); o.forward(200); o.endDash();
  text("C", o.xcor()+10, o.ycor());
  ellipse(o.xcor(), o.ycor(), 10 , 10);
}


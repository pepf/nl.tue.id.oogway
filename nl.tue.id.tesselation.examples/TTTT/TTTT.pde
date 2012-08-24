import processing.pdf.*;

import nl.tue.id.oogway.*;


Oogway o;

void setup(){
  size(600,600);
  o = new Oogway(this);
  noLoop();
   beginRecord(PDF, "TTTT_Nr1_FLOAT_PEGASUS.pdf");
   o.setPenColor(255,0,0);
}

void draw(){
  
  o.home();
  abcd();
  o.home();
  o.pu();
  o.forward(100);
  o.pd();
  abcd();
  o.home();
  o.right(90);
  o.pu();
  o.forward(100);
  o.pd();
  o.left(90);
  abcd();
  endRecord();
}

void abcd(){
  o.remember("A");
  o.beginPath("AB.svg");
  o.forward(100);
  o.endPath();
  o.left(90);
  o.beginPath("AC.svg");
  o.forward(100);
  o.endPath();
  o.recall("A");
  o.left(90);
  o.beginPath("AC.svg");
  o.forward(100);
  o.endPath();
  o.right(90);
  o.beginPath("AB.svg");
  o.forward(100);
  o.endPath();
}


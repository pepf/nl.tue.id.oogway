import processing.pdf.*;
import nl.tue.id.oogway.*;

int XSIZE=int(3.6*297);
int YSIZE=int(3.6*210);

Oogway o;


void setup(){
  size(XSIZE,YSIZE);
  o = new Oogway(this);
  noLoop();
   beginRecord(PDF, "TTTT.pdf");
   o.setPenColor(255,0,0);
}

void draw(){
  o.setPosition(200, o.ycor()+200);
  abcd(200);
  o.beginDash(new float[]{10,5});
  o.recall('A');
  o.left(90);
  o.forward(200);
  o.recall('B');
  o.left(90);
  o.forward(200);
  o.endDash();
  
  tesselate(100);
  endRecord();
}

void tesselate(float size){
  o.home();
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
  o.remember('A');
  o.beginPath("AB.svg"); o.forward(size); o.endPath();
  o.remember('B');
  o.left(90);
  o.beginPath("AD.svg"); o.forward(size); o.endPath();
  o.recall('A');
  o.left(90);
  o.beginPath("AD.svg"); o.forward(size); o.endPath();
  o.right(90);
  o.beginPath("AB.svg"); o.forward(size); o.endPath();
  o.recall('A');
}


